package com.bank.accountdata.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bank.accountdata.AccountDataApplicationTests;
import com.bank.accountdata.payload.AccountSearchRequest;
import com.bank.accountdata.payload.TokenResponse;
import com.bank.accountdata.payload.UserRequest;

/**
 * Integration Test Cases
 * 
 * @author @RG
 *
 */
public class AccountSearchControllerTest extends AccountDataApplicationTests {

	@Override
	@Before
	public void setUpForTesting() {
		super.setUpForTesting();
	}

	@Test
	@WithMockUser("user")
	public void accountSearchUser_whenAccountIdIsNull_then400() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser("user")
	public void accountSearch_whenAccountIdIsPresentAndRoleIsUser_then200() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isOk()).andExpect(content().json(
						"{\"accountId\":\"2\",\"accountType\":\"savings\",\"accountNumber\":\"678628fa6aea0ba2e471e4aa40ca8a2e746f80559ffafc09a5391620a19b4da6\",\"statements\":[]}"));
	}

	@Test
	@WithMockUser("user")
	public void accountSearch_whenParameterIsPresentAndRoleIsUser_then400() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").fromAmount("20")
				.build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("User is not allowed to enter search parameters except accountID."));
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearchAdmin_whenAccountIdIsNull_then400() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearch_whenAccountIdIsPresentAndRoleIsAdmin_then200() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isOk()).andExpect(content().json(
						"{\"accountId\":\"2\",\"accountType\":\"savings\",\"accountNumber\":\"678628fa6aea0ba2e471e4aa40ca8a2e746f80559ffafc09a5391620a19b4da6\",\"statements\":[]}"));
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearch_whenAmountRangeIssue1AndRoleIsAdmin_then400() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").fromAmount("20")
				.build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Please specify amount range properly to search accounts."));
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearch_whenAmountRangeIssue2AndRoleIsAdmin_then400() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").toAmount("20")
				.build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Please specify amount range properly to search accounts."));
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearch_whenDateRangeIssue1AndRoleIsAdmin_then400() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").fromDate("08.09.2022")
				.build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Please specify date range properly to search accounts."));
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearch_whenDateRangeIssue2AndRoleIsAdmin_then400() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").toDate("08.09.2022")
				.build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isBadRequest())
				.andExpect(content().string("Please specify date range properly to search accounts."));
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearch_whenDateAmountRangeAndRoleIsAdmin_then200() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").toDate("14.10.2020")
				.fromDate("09.08.2020").toAmount("680").fromAmount("535.88").build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearch_whenDateParseIssueAndRoleIsAdmin_then400() throws Exception {
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").toDate("1.10.2020")
				.fromDate("09.08.2020").toAmount("680").fromAmount("535.88").build();
		String inputJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isBadRequest()).andExpect(content()
						.string("Error parsing date passed in request. Please enter date in dd.MM.yyyy format."));
	}

	@Test
	@WithUserDetails("admin")
	public void accountSearch_AfterLoginPassToken_then200() throws Exception {
		// Login
		UserRequest userRequest = UserRequest.builder().username("admin").password("admin").build();
		String inputJson = super.mapToJson(userRequest);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/bank/login")
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andExpect(status().isOk())
				.andReturn();
		TokenResponse tokenResponse = super.mapFromJson(result.getResponse().getContentAsString(), TokenResponse.class);
		assertNotNull(tokenResponse.getToken());
		SecurityContextHolder.getContext().setAuthentication(null);
		String uri = "/bank/account/search";
		AccountSearchRequest accountSearchRequest = AccountSearchRequest.builder().accountId("2").toDate("14.10.2020")
				.fromDate("09.08.2020").toAmount("680").fromAmount("535.88").build();
		String inputAccountSearchJson = super.mapToJson(accountSearchRequest);
		mvc.perform(MockMvcRequestBuilders.post(uri).header("Authorization", "Bearer " + tokenResponse.getToken())
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputAccountSearchJson))
				.andExpect(status().isOk());
		mvc.perform(MockMvcRequestBuilders.post("/bank/logout").contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJson)).andExpect(status().isOk());
	}

}