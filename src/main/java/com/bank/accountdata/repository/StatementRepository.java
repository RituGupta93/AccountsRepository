package com.bank.accountdata.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bank.accountdata.entity.Statement;

@Repository
public interface StatementRepository extends JpaRepository<Statement, String> {

	@Query(value = "SELECT * FROM Statement where account_id = ?1", nativeQuery = true)
	List<Statement> findStatementByAccountId(String accountId);

}
