package com.bank.accountdata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Statement")
@Getter
@Setter
public class Statement {

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "datefield")
	private String date;

	@Column(name = "amount")
	private String amount;

}
