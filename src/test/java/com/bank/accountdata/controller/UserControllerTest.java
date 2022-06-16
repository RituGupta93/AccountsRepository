package com.bank.accountdata.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bank.accountdata.AccountDataApplicationTests;
import com.bank.accountdata.payload.TokenResponse;
import com.bank.accountdata.payload.UserRequest;

public class UserControllerTest extends AccountDataApplicationTests {

	@Override
	@Before
	public void setUpForTesting() {
		super.setUpForTesting();
	}

	@Test
	public void loginUser_whenUsernameAndPasswordIsEmpty_then401() throws Exception {
		String uri = "/bank/login";
		UserRequest userRequest = UserRequest.builder().build();
		String inputJson = super.mapToJson(userRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void loginUser_whenWrongCrdentials_then401() throws Exception {
		String uri = "/bank/login";
		UserRequest userRequest = UserRequest.builder().username("user").password("abc").build();
		String inputJson = super.mapToJson(userRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void loginUser_whenUserIsCorrect_then200() throws Exception {
		// First Login
		String uri = "/bank/login";
		UserRequest userRequest = UserRequest.builder().username("user").password("user").build();
		String inputJson = super.mapToJson(userRequest);
		MvcResult result = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isOk()).andReturn();
		TokenResponse tokenResponse = super.mapFromJson(result.getResponse().getContentAsString(), TokenResponse.class);
		assertNotNull(tokenResponse.getToken());
		// Second Login
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isForbidden());
		// Logout user
		mvc.perform(MockMvcRequestBuilders.post("/bank/logout").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andExpect(status().isOk());
		// Logout again
		mvc.perform(MockMvcRequestBuilders.post("/bank/logout").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andExpect(status().isBadRequest());
	}

}
