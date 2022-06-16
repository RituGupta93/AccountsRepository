package com.bank.accountdata.exception;

/**
 * Custom Exception class
 * 
 * @author @RG
 *
 */
public class AccountSearchException extends RuntimeException {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	public AccountSearchException(String errorMessage) {
		super(errorMessage);
	}

}
