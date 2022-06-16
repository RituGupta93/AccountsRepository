package com.bank.accountdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.accountdata.entity.Account;
import com.bank.accountdata.payload.AccountSearchRequest;
import com.bank.accountdata.service.AccountSearchService;
import com.bank.accountdata.validation.RequestValidator;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bank/account")
@Slf4j
public class AccountSearchController {

	@Autowired
	AccountSearchService accountSearchService;

	@ApiOperation(value = "Authorization header is manadatory to access this API. Get access token from Login API and pass it in authorization header as bearer token."
			+ "Format is {Bearer ${tokenValue}}")
	@PostMapping("/search")
	public ResponseEntity<Object> searchAccounts(@RequestBody AccountSearchRequest request) {
		log.info("Starting account search....");
		if (request.getAccountId() == null) {
			log.error("Account ID is missing");
			return ResponseEntity.badRequest().body("Account ID is mandatory");
		} else {
			RequestValidator.validateRequestBasedOnRole(request);
			Account account = accountSearchService.searchAccount(request);
			if (account != null) {
				return ResponseEntity.ok(account);
			} else {
				return ResponseEntity.noContent().build();
			}
		}
	}

}