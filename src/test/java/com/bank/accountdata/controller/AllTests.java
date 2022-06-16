package com.bank.accountdata.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.bank.accountdata.service.AccountServiceTest;
import com.bank.accountdata.service.UserServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ AccountSearchControllerTest.class, UserControllerTest.class, AccountServiceTest.class,
		UserServiceTest.class })
public class AllTests {

}
