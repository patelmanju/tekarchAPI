package com.test.tekarchapitest;

import java.util.Properties;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.test.constants.endpoints;
import com.test.models.loginrequestpojo;
import com.test.models.loginresponsepojo;
import com.test.utils.CommonUtility;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class SimpleCRUD {

	@BeforeClass
	public static void setUp() {
		CommonUtility CU = new CommonUtility();
		Properties sfDataFile = CU.loadFile("sfData");
		String url = CU.getApplicationProperty("BaseURI", sfDataFile);
		RestAssured.baseURI = url;
	}

	public static String login() {
		CommonUtility CU = new CommonUtility();
		Properties sfDataFile = CU.loadFile("sfData");
		Response res = RestAssured.given()
				.body(sfDataFile.getProperty("loginbody"))
				.contentType(ContentType.JSON).when().post(endpoints.LOGIN);
		String token = res.jsonPath().get("[0].token");
		return token;

	}

	@Test(enabled = true)
	public static void loginToApi() {
		loginrequestpojo loginData = new loginrequestpojo();
		loginData.setUsername("manju0383.patel@gmail.com");
		loginData.setPassword("manju123");
		Response res = RestAssured.given().body(loginData).contentType(ContentType.JSON).when().post("login");

		loginresponsepojo[] list = res.as(loginresponsepojo[].class);
		String token1 = list[0].getToken();
		System.out.println("token1=========" + token1);

		res.then().statusCode(201);
		String token = res.jsonPath().get("[0].token");
		String userid = res.jsonPath().get("[0].userid");
		System.out.println("token=" + token);
		System.out.println("user id=" + userid);
		res.prettyPrint();

	}

	@Test(enabled = true)
	public static void getUserDetails() {
		login();
		String token1 = login();
		System.out.println(token1);
		Header header = new Header("token", token1);
		Response res = RestAssured.given().header(header).when().get("/getdata");
		res.then().statusCode(200);
		System.out.println("number of records=" + res.jsonPath().get("size()"));
		String userId = res.jsonPath().get("[0].userid");
		String id = res.jsonPath().get("[0].id");
		String accountnum = res.jsonPath().getString("[0].accountno");
		System.out.println("account no=" + accountnum);
		int departmentno = res.jsonPath().get("[0].departmentno");
		System.out.println("department no=" + departmentno);
		String salary = res.jsonPath().getString("[0].salary");
		System.out.println("salary no=" + salary);
		System.out.println("first entry userdata userid and id is=" + userId + " and " + id);
		res.then().body("[0].departmentno", Matchers.equalTo(3));  

	}

	@Test(enabled = false)
	public static void createUSerDetails() {
		String token1 = login();
		Header header = new Header("token", token1);
		Response res = RestAssured.given().header(header).contentType(ContentType.JSON).body(
				"{\"accountno\":\"TA-Aug2202\",\"departmentno\":\"1\",\"salary\":\"5000\",\"pincode\":\"123123\"}")
				.when().post("/addData");

		res.then().statusCode(201);
		res.then().body("status", Matchers.equalTo("success"));

	}

	@Test(enabled = false)
	public static void updateUSerDetails() {
		String token1 = login();
		Header header = new Header("token", token1);
		Response res = RestAssured.given().header(header).contentType(ContentType.JSON).body(
				"{\"accountno\":\"TA-Aug2202\",\"departmentno\":3,\"salary\":6000,\"pincode\":123123,\"userid\":\"zhFI4oQlHjP2cn3mW3GP\",\"id\":\"3alpp4U70XFMCIOSIbrf\"}")
				.when().put("updateData");

		res.then().statusCode(200);
		res.then().body("status", Matchers.equalTo("success"));

	}

	@Test(enabled = false)
	public static void deleteUserInfo() {
		String token1 = login();
		Header header = new Header("token", token1);
		Response res = RestAssured.given().header(header).contentType(ContentType.JSON)
				.body("{\"userid\":\"zhFI4oQlHjP2cn3mW3GP\",\"id\":\"3alpp4U70XFMCIOSIbrf\"}").when()
				.delete("deleteData");
		res.then().statusCode(200);
		res.then().contentType(ContentType.JSON);
		res.then().body("status", Matchers.equalTo("success"));

	}

}