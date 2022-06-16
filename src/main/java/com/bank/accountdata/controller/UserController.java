package com.bank.accountdata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.accountdata.payload.UserRequest;
import com.bank.accountdata.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bank")
@Slf4j
public class UserController {

	@Autowired
	UserService userService;

	@ApiOperation(value = "Login API to get authentication token. Username and password are mandatory.")
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody UserRequest userRequest) {
		log.info("Login Service called.....");
		return userService.login(userRequest);
	}

	@ApiOperation(value = "Logout API to destroy authentication token. Username and password are mandatory.")
	@PostMapping("/logout")
	public ResponseEntity<String> logout(@RequestBody UserRequest userRequest) {
		log.info("Logout Service called.....");
		return userService.logout(userRequest);
	}

}
