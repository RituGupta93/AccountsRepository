package com.bank.accountdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.bank.accountdata.payload.TokenResponse;
import com.bank.accountdata.payload.UserRequest;
import com.bank.accountdata.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	private static final String USER_TOKEN = "userToken";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	CacheManager cacheManager;

	/**
	 * Method to serve login request and generate token
	 * 
	 * @param userRequest
	 * @return token
	 */
	public ResponseEntity<Object> login(UserRequest userRequest) {
		String username = userRequest.getUsername();
		try {
			log.info("Authenticating user " + userRequest.getUsername());
			authenticate(username, userRequest.getPassword());
			// Check if user is already logged in...
			if (cacheManager.getCache(USER_TOKEN) != null && isTokenExpired(username)) {
				TokenResponse tokenResponse = TokenResponse.builder().token(generateToken(username)).build();
				return ResponseEntity.ok(tokenResponse);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You cannot login twice. Please logout to continue.");
			}
		} catch (ExpiredJwtException e) {
			TokenResponse tokenResponse = TokenResponse.builder().token(generateToken(username)).build();
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			log.error("Error while authenticating user");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	private boolean isTokenExpired(String username) {
		Cache cache = cacheManager.getCache(USER_TOKEN);
		if (cache != null && cache.get(username) == null) {
			return true;
		} else if (cache != null && cache.get(username) != null) {
			ValueWrapper token = cache.get(username);
			if (token != null && token.get() != null
					&& Boolean.FALSE.equals(jwtTokenUtil.isTokenExpired((String) token.get()))) {
				return false;
			}
		}
		return true;

	}

	private String generateToken(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		String token = jwtTokenUtil.generateToken(userDetails);
		Cache cache = cacheManager.getCache(USER_TOKEN);
		if (cache != null) {
			cache.put(username, token);
		}
		return token;
	}

	/**
	 * Method to logout user
	 * 
	 * @param userRequest
	 * @return message
	 */
	public ResponseEntity<String> logout(UserRequest userRequest) {
		String username = userRequest.getUsername();
		// Authenticate user first....
		authenticate(username, userRequest.getPassword());
		Cache cache = cacheManager.getCache(USER_TOKEN);
		try {
			if (cache != null && cache.get(username) != null) {
				ValueWrapper token = cache.get(username);
				if (token != null && token.get() != null
						&& Boolean.FALSE.equals(jwtTokenUtil.isTokenExpired((String) token.get()))) {
					log.info("Logging out user....");
					cache.evict(username);
					SecurityContextHolder.getContext().setAuthentication(null);
					return new ResponseEntity<>("User is logged out successfully", HttpStatus.OK);
				}
			}
		} catch (ExpiredJwtException e) {
			log.info("User session already expired");
			return new ResponseEntity<>("User session already expired", HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<>("User is not logged in", HttpStatus.BAD_REQUEST);
	}

	private void authenticate(String username, String password) throws AuthenticationException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException("User Disabled", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("Invalid Credentials", e);
		}
	}

}
