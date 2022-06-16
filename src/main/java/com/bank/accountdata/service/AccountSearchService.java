package com.bank.accountdata.service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.accountdata.dao.AccountSearchDao;
import com.bank.accountdata.entity.Account;
import com.bank.accountdata.entity.Statement;
import com.bank.accountdata.exception.AccountSearchException;
import com.bank.accountdata.payload.AccountSearchRequest;
import com.bank.accountdata.util.UserRoleUtil;
import com.bank.accountdata.validation.RequestValidator;
import com.google.common.hash.Hashing;

import lombok.extern.slf4j.Slf4j;

/**
 * Service class to handle search functionality based on role
 * 
 * @author @RG
 *
 */
@Service
@Slf4j
public class AccountSearchService {

	private static final String DATE_FORMAT = "dd.MM.yyyy";
	@Autowired
	AccountSearchDao accountSearchDao;

	public Account searchAccount(AccountSearchRequest request) {
		log.info("Searching account ID in Db..");
		try {
			Optional<Account> accountOptional = accountSearchDao.getAccountBasedOnId(request.getAccountId());
			if (accountOptional.isPresent()) {
				Account account = accountOptional.get();
				List<Statement> statements = accountSearchDao.getStatementBasedOnId(request.getAccountId());
				List<Statement> filteredStatements = filterStatementsBasedOnRequestParameter(statements, request);
				account.setStatements(filteredStatements);
				account.setAccountNumber(Hashing.sha256()
						.hashString(accountOptional.get().getAccountNumber(), StandardCharsets.UTF_8).toString());
				return account;
			}
		} catch (DateTimeParseException e) {
			log.error("Error parsing date passed in request " + e.getMessage());
			throw new AccountSearchException(
					"Error parsing date passed in request. Please enter date in dd.MM.yyyy format.");
		}
		return null;
	}

	private List<Statement> filterStatementsBasedOnRequestParameter(List<Statement> statements,
			AccountSearchRequest request) {

		LocalDate currentDate = LocalDate.now();
		LocalDate dateMinus3Months = currentDate.minusMonths(3);

		// Send three months of bank statement for USER ROLE
		if (Boolean.TRUE.equals(UserRoleUtil.checkIfRoleIsUser())) {
			log.info("Get 3 months statement for USER");
			return get3MonthsStatements(statements, dateMinus3Months);
		} else if (Boolean.TRUE.equals(UserRoleUtil.checkIfRoleIsAdmin())) {
			return filterData(statements, request, dateMinus3Months);
		} else {
			throw new AccountSearchException("User Role is not authorized");
		}
	}

	private List<Statement> filterData(List<Statement> statements, AccountSearchRequest request,
			LocalDate dateMinus3Months) {
		RequestValidator.validateRequestForAdmin(request);
		// Check if only account ID is given in the request
		if (request.getFromDate() == null && request.getToDate() == null && request.getFromAmount() == null
				&& request.getToAmount() == null) {
			log.info("Get 3 months statement for ADMIN");
			return get3MonthsStatements(statements, dateMinus3Months);
		} else {
			// Filter statements based on parameters passed...
			List<Statement> filteredStatement = statements;
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
			if (request.getFromDate() != null && request.getToDate() != null) {
				log.info("Filtering data based on date range....");
				LocalDate fromDate = LocalDate.parse(request.getFromDate(), formatter);
				LocalDate toDate = LocalDate.parse(request.getToDate(), formatter);
				filteredStatement = statements.stream()
						.filter(statement -> formatDate(statement.getDate()).compareTo(fromDate) >= 0
								&& formatDate(statement.getDate()).compareTo(toDate) <= 0)
						.collect(Collectors.toList());
			}
			if (request.getFromAmount() != null && request.getToAmount() != null) {
				log.info("Filtering data based on amount range....");
				BigDecimal fromAmount = new BigDecimal(request.getFromAmount());
				BigDecimal toAmount = new BigDecimal(request.getToAmount());
				filteredStatement = filteredStatement.stream()
						.filter(statement -> new BigDecimal(statement.getAmount()).compareTo(fromAmount) >= 0
								&& new BigDecimal(statement.getAmount()).compareTo(toAmount) <= 0)
						.collect(Collectors.toList());
			}
			return filteredStatement;
		}
	}

	private List<Statement> get3MonthsStatements(List<Statement> statements, LocalDate dateMinus3Months) {
		return statements.stream().filter(statement -> formatDate(statement.getDate()).isAfter(dateMinus3Months))
				.collect(Collectors.toList());
	}

	private LocalDate formatDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return LocalDate.parse(date, formatter);
	}

}
