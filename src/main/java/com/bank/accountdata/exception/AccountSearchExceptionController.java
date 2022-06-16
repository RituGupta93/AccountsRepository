package com.bank.accountdata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class to handle custom exceptions
 * 
 * @author @RG
 *
 */
@ControllerAdvice
public class AccountSearchExceptionController {

	@ExceptionHandler(value = AccountSearchException.class)
	public ResponseEntity<Object> exception(AccountSearchException accountSearchException) {
		return new ResponseEntity<>(accountSearchException.getMessage(), HttpStatus.BAD_REQUEST);

	}

}
