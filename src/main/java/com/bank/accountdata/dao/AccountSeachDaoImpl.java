package com.bank.accountdata.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bank.accountdata.entity.Account;
import com.bank.accountdata.entity.Statement;
import com.bank.accountdata.repository.AccountRepository;
import com.bank.accountdata.repository.StatementRepository;

@Component
public class AccountSeachDaoImpl implements AccountSearchDao {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	StatementRepository statementRepository;

	@Override
	public Optional<Account> getAccountBasedOnId(String accountId) {
		return accountRepository.findById(accountId);
	}

	@Override
	public List<Statement> getStatementBasedOnId(String accountId) {
		return statementRepository.findStatementByAccountId(accountId);
	}

}
