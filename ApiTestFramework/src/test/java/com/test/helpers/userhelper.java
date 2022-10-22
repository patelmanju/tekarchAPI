package com.test.helpers;

import java.lang.reflect.Method;
import java.util.Properties;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import com.test.constants.endpoints;
import com.test.models.createuserrequestpojo;
import com.test.models.deleteuserrequestpojo;
import com.test.models.getuserresponsepojo;
import com.test.models.loginrequestpojo;
import com.test.models.loginresponsepojo;
import com.test.models.updateuserrequestpojo;
import com.test.utils.CommonUtility;
import com.test.utils.GenerateReports;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

public class userhelper {

	public static String token1;
	public static GenerateReports report = null;

	@BeforeTest
	public static void setupBeforeTest() {
		report = GenerateReports.getInstance();
		report.startExtentReport();
	}

	@AfterTest
	public static void teardownAfterTest() {
		report.endReport();
	}

	@BeforeClass
	public static void setup() {
		urisetup();
	}

	@BeforeMethod
	public static void startreport(Method m) {
		report.startSingleTestReport(m.getName());
	}

	public static void urisetup() {
		CommonUtility CU = new CommonUtility();
		Properties sfDataFile = CU.loadFile("sfData");
		String url = CU.getApplicationProperty("BaseURI", sfDataFile);
		RestAssured.baseURI = url;
	}

	public static String login() {
		CommonUtility CU = new CommonUtility();
		Properties sfDataFile = CU.loadFile("sfData");
		loginrequestpojo loginData = new loginrequestpojo();
		loginData.setUsername(sfDataFile.getProperty("username"));
		loginData.setPassword(sfDataFile.getProperty("password"));
		Response res = RestAssured.given()
				.body(loginData)
				.contentType(ContentType.JSON)
				.when().post(endpoints.LOGIN);

		loginresponsepojo[] list = res.as(loginresponsepojo[].class);
		token1 = list[0].getToken();
		System.out.println("token1=========" + token1);
		String userid = list[0].getUserid();
		System.out.println("user id=" + userid);
		res.prettyPrint();

		
		// String token = res.jsonPath().get("[0].token");
		// String userid = res.jsonPath().get("[0].userid");
		// System.out.println("token=" + token);
		
		return token1;
		
	}

	public static String getdata() {

		token1 = login();
		Header header = new Header("token", token1);

		Response res = RestAssured.given()
				.header(header)
				.when()
				.get(endpoints.GET_DATA);

		res.then().statusCode(200);
		report.logTestInfo("data details are:");
		getuserresponsepojo[] getdata = res.as(getuserresponsepojo[].class);
		String Responseofgetdata  = getdata.toString();
		
		String accountno = getdata[0].getAccountno();
		System.out.println("account no=====" + accountno);

		String departmentnum = getdata[0].getDepartmentno();
		System.out.println("departmentno=====" + departmentnum);

		String salary = getdata[0].getSalary();
		System.out.println("Salary=====" + salary);

		String pincode = getdata[0].getPincode();
		System.out.println("Pincode=====" + pincode);

		String userid = getdata[0].getUserid();
		System.out.println("user id =====" + userid);

		String id = getdata[0].getId();
		System.out.println("Id=====" + id);
       return  Responseofgetdata;
       

	}
	
	public static void createdata() {
	token1 = login();
	Header header = new Header("token", token1);
	CommonUtility CU = new CommonUtility();
	Properties addDataFile = CU.loadFile("addData");
	createuserrequestpojo userdata = new createuserrequestpojo();
	userdata.setAccountno(addDataFile.getProperty("accountno"));
	userdata.setDepartmentno(addDataFile.getProperty("departmentno"));
	userdata.setSalary(addDataFile.getProperty("salary"));
	userdata.setPincode(addDataFile.getProperty("pincode"));

	Response res = RestAssured.given().header(header).contentType(ContentType.JSON)

			.body(userdata).when().post(endpoints.ADD_DATA);

	System.out.println("user detail created");
	report.logTestInfo("user detail created");

	res.then().statusCode(201);
	res.then().body("status", Matchers.equalTo("success"));

}
	public static void updatedata() {

		token1 = login();
		Header header = new Header("token", token1);
		CommonUtility CU = new CommonUtility();
		Properties addDataFile = CU.loadFile("addData");
		updateuserrequestpojo updatedata = new updateuserrequestpojo();
		updatedata.setAccountno(addDataFile.getProperty("upaccountno"));
		updatedata.setDepartmentno(addDataFile.getProperty("updepartmentno"));
		updatedata.setSalary(addDataFile.getProperty("upsalary"));
		updatedata.setPincode(addDataFile.getProperty("uppincode"));
		updatedata.setUserid(addDataFile.getProperty("upuserid"));
		updatedata.setId(addDataFile.getProperty("upId"));

		Response res = RestAssured.given().header(header).contentType(ContentType.JSON).body(updatedata)

				.when().put(endpoints.UPDATE_DATA);

		res.then().statusCode(200);
		res.then().body("status", Matchers.equalTo("success"));
		String userid = updatedata.getUserid();
		System.out.println("user id =====" + userid);
		String id = updatedata.getId();
		System.out.println("Id=====" + id);
		System.out.println("user detail updated");
		report.logTestInfo("user detail updated");
	}
	public static void deletedata() {
	token1 = login();
	Header header = new Header("token", token1);
	CommonUtility CU = new CommonUtility();
	Properties addDataFile = CU.loadFile("addData");
	deleteuserrequestpojo deletedata = new deleteuserrequestpojo();

	deletedata.setId(addDataFile.getProperty("dlId"));
	deletedata.setUserid(addDataFile.getProperty("dluserid"));

	Response res = RestAssured.given()
			.header(header)
			.contentType(ContentType.JSON)
			.body(deletedata)
			.when().delete(endpoints.DELETE_DATA);

	res.then().statusCode(200);
	res.then().contentType(ContentType.JSON);
	res.then().body("status", Matchers.equalTo("success"));
	report.logTestInfo("user detail deleted");
}
}

