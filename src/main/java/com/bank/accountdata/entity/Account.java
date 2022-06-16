package com.bank.accountdata.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Account")
@Getter
@Setter
public class Account {

	@Id
	@Column(name = "ID")
	private String accountId;

	@Column(name = "account_type")
	private String accountType;

	@Column(name = "account_number")
	private String accountNumber;

	@OneToMany
	private List<Statement> statements;

}
