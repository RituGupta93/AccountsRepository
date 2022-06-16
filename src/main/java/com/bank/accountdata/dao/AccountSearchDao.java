package com.bank.accountdata.dao;

import java.util.List;
import java.util.Optional;

import com.bank.accountdata.entity.Account;
import com.bank.accountdata.entity.Statement;

public interface AccountSearchDao {

	public Optional<Account> getAccountBasedOnId(String accountId);

	public List<Statement> getStatementBasedOnId(String accountId);

}
