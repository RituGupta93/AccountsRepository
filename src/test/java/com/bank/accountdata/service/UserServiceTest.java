package com.bank.accountdata.service;

import static org.mockito.ArgumentMatchers.any;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bank.accountdata.payload.TokenResponse;
import com.bank.accountdata.payload.UserRequest;
import com.bank.accountdata.util.JwtTokenUtil;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@InjectMocks
	UserService userService = new UserService();

	@Mock
	AuthenticationManager authenticationManager;

	@Mock
	CacheManager cacheManager;

	@Mock
	JwtTokenUtil jwtMockUtil;

	@Mock
	UserDetailsService UserDetailsService;

	@Before
	public void setUp() {
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
		Cache cache = Mockito.mock(Cache.class);
		ValueWrapper valueWrapper = new ValueWrapper() {
			@Override
			public Object get() {
				return "token";
			}
		};
		Mockito.when(cache.get(any())).thenReturn(valueWrapper);
		Mockito.when(cacheManager.getCache("userToken")).thenReturn(cache);
		Mockito.when(jwtMockUtil.isTokenExpired(any())).thenReturn(true);
		Mockito.when(jwtMockUtil.generateToken(any())).thenReturn("token");
		UserDetails user = User.builder().username("user").password("user").roles("USER").build();
		Mockito.when(UserDetailsService.loadUserByUsername(any())).thenReturn(user);
	}

	@Test
	public void testLogin_whenSuccess_thenStatus200() {
		UserRequest userRequest = UserRequest.builder().username("user").password("user").build();
		ResponseEntity<Object> tokenResponse = userService.login(userRequest);
		Assert.assertEquals(HttpStatus.OK, tokenResponse.getStatusCode());
		TokenResponse token = (TokenResponse) tokenResponse.getBody();
		Assert.assertEquals("token", token.getToken());
	}

	@Test
	public void testLogin_whenWrongCrdentials_thenStatus401() {
		Mockito.when(authenticationManager.authenticate(any()))
				.thenThrow(new BadCredentialsException("Invalid Credentials"));
		UserRequest userRequest = UserRequest.builder().username("user").password("abc").build();
		ResponseEntity<Object> tokenResponse = userService.login(userRequest);
		Assert.assertEquals(HttpStatus.UNAUTHORIZED, tokenResponse.getStatusCode());
	}

	@Test
	public void testLogout_whenTokenExpired_thenStatus400() {
		UserRequest userRequest = UserRequest.builder().username("user").password("user").build();
		ResponseEntity<String> response = userService.logout(userRequest);
		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testLogout_whenTokenNotExpired_thenStatus200() {
		Mockito.when(jwtMockUtil.isTokenExpired(any())).thenReturn(false);
		UserRequest userRequest = UserRequest.builder().username("user").password("user").build();
		ResponseEntity<String> response = userService.logout(userRequest);
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
