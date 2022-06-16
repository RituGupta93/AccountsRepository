package com.bank.accountdata.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.bank.accountdata.dao.AccountSearchDao;
import com.bank.accountdata.entity.Account;
import com.bank.accountdata.entity.Statement;
import com.bank.accountdata.exception.AccountSearchException;
import com.bank.accountdata.payload.AccountSearchRequest;
import com.bank.accountdata.util.UserRoleUtil;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@InjectMocks
	AccountSearchService accountSearchService = new AccountSearchService();

	@Mock
	AccountSearchDao accountSearchDao;

	@Before
	public void setUp() {
		Statement statement1 = new Statement();
		statement1.setAmount("23");
		statement1.setDate("03.08.2022");
		Statement statement2 = new Statement();
		statement2.setAmount("25");
		statement2.setDate("04.08.2022");
		List<Statement> statements = new ArrayList<Statement>();
		statements.add(statement1);
		statements.add(statement2);
		Account account = new Account();
		account.setAccountId("2");
		account.setAccountNumber("12345");
		account.setAccountType("Savings");
		Optional<Account> optionalAccount = Optional.of(account);
		Mockito.when(accountSearchDao.getAccountBasedOnId(any())).thenReturn(optionalAccount);
		Mockito.when(accountSearchDao.getStatementBasedOnId(any())).thenReturn(statements);
	}

	@Test
	public void searchAccount_whenRoleIsUser_ThenAccountReturned() {
		try (MockedStatic<UserRoleUtil> utilities = Mockito.mockStatic(UserRoleUtil.class)) {
			utilities.when(UserRoleUtil::checkIfRoleIsUser).thenReturn(true);
			AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").build();
			Account account = accountSearchService.searchAccount(accountSearchRequest);
			Assert.assertNotNull(account);
			Assert.assertFalse(account.getStatements().isEmpty());
		}
	}

	@Test
	public void searchAccount_whenRoleIsAdminNoParametersPassed_ThenAccountReturned() {
		try (MockedStatic<UserRoleUtil> utilities = Mockito.mockStatic(UserRoleUtil.class)) {
			utilities.when(UserRoleUtil::checkIfRoleIsUser).thenReturn(false);
			utilities.when(UserRoleUtil::checkIfRoleIsAdmin).thenReturn(true);
			AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").build();
			Account account = accountSearchService.searchAccount(accountSearchRequest);
			Assert.assertNotNull(account);
			Assert.assertFalse(account.getStatements().isEmpty());
		}
	}

	@Test(expected = AccountSearchException.class)
	public void searchAccount_whenRoleIsOthersParametersPassed_ThenException() {
		try (MockedStatic<UserRoleUtil> utilities = Mockito.mockStatic(UserRoleUtil.class)) {
			utilities.when(UserRoleUtil::checkIfRoleIsUser).thenReturn(false);
			utilities.when(UserRoleUtil::checkIfRoleIsAdmin).thenReturn(false);
			AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").build();
			accountSearchService.searchAccount(accountSearchRequest);
		}
	}

	@Test
	public void searchAccount_whenRoleIsAdminParametersPassed_ThenAccountReturned() {
		try (MockedStatic<UserRoleUtil> utilities = Mockito.mockStatic(UserRoleUtil.class)) {
			utilities.when(UserRoleUtil::checkIfRoleIsUser).thenReturn(false);
			utilities.when(UserRoleUtil::checkIfRoleIsAdmin).thenReturn(true);
			AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").fromAmount("23")
					.toAmount("25").fromDate("03.08.2022").toDate("04.08.2022").build();
			Account account = accountSearchService.searchAccount(accountSearchRequest);
			Assert.assertNotNull(account);
			Assert.assertFalse(account.getStatements().isEmpty());
			assertEquals(2, account.getStatements().size());
		}
	}
}