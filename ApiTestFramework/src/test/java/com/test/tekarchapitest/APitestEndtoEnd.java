package com.test.tekarchapitest;

import org.testng.annotations.Test;
import com.test.helpers.userhelper;

public class APitestEndtoEnd extends userhelper {

	@Test(priority = 1, enabled = true)
	public static void TC1loginToTekarch() {
		login();
		report.logTestInfo("user logged in and token1 created");
	}

	@Test(priority = 2, enabled = true)
	public static void TC2getUserDetails() {
		getdata();
		String res = getdata();
		System.out.println("data" + res);

	}

	@Test(priority = 3, enabled = true)
	public static void TC3createUSerDetails() {
		createdata();

	}

	@Test(priority = 4, enabled = true)
	public static void TC4updateUSerDetails() {

		updatedata();
	}

	@Test(priority = 5, enabled = true)
	public static void TC5deleteUserInfo() {

		deletedata();

	}

}