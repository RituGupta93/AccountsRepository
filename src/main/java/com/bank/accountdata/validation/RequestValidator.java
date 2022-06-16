package com.bank.accountdata.validation;

import com.bank.accountdata.exception.AccountSearchException;
import com.bank.accountdata.payload.AccountSearchRequest;
import com.bank.accountdata.util.UserRoleUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Validate request based on Role
 * 
 * @author @RG
 *
 */
@Slf4j
public class RequestValidator {

	/**
	 * " Private Constructor to hide implicit
	 */
	private RequestValidator() {

	}

	public static void validateRequestBasedOnRole(AccountSearchRequest accountSearchRequest) {

		if (Boolean.TRUE.equals(UserRoleUtil.checkIfRoleIsUser())
				&& (accountSearchRequest.getFromAmount() != null || accountSearchRequest.getToAmount() != null
						|| accountSearchRequest.getFromDate() != null || accountSearchRequest.getToDate() != null)) {
			log.debug("User is not allowed to enter search parameters except accountID.");
			throw new AccountSearchException("User is not allowed to enter search parameters except accountID.");
		}
	}

	public static void validateRequestForAdmin(AccountSearchRequest request) {

		if ((request.getFromDate() != null && request.getToDate() == null)
				|| (request.getFromDate() == null && request.getToDate() != null)) {
			log.error("Please specify date range properly to search accounts.");
			throw new AccountSearchException("Please specify date range properly to search accounts.");
		} else if ((request.getFromAmount() != null && request.getToAmount() == null)
				|| (request.getFromAmount() == null && request.getToAmount() != null)) {
			log.error("Please specify amount range properly to search accounts.");
			throw new AccountSearchException("Please specify amount range properly to search accounts.");
		}

	}

}
