package com.bank.accountdata.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Util class to check user Role based on data stored in security context holder
 * 
 * @author @Rg
 *
 */
@Component
public class UserRoleUtil {

	private UserRoleUtil() {

	}

	public static Boolean checkIfRoleIsUser() {
		Boolean isUser = Boolean.FALSE;
		List<String> userRoles = getRoles();
		if (userRoles.contains("ROLE_USER")) {
			isUser = Boolean.TRUE;
		}
		return isUser;
	}

	public static Boolean checkIfRoleIsAdmin() {
		Boolean isAdmin = Boolean.FALSE;
		List<String> userRoles = getRoles();
		if (userRoles.contains("ROLE_ADMIN")) {
			isAdmin = Boolean.TRUE;
		}
		return isAdmin;
	}

	private static List<String> getRoles() {
		Collection<? extends GrantedAuthority> userNameAuthorities = SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();
		return userNameAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}
}
