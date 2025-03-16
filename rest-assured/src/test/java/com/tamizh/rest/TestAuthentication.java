package com.tamizh.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import com.tamizh.core.HttpStatusCode;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestAuthentication {

    RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://dummyjson.com").setContentType(ContentType.JSON).build();
    ResponseSpecification globalResponseSpec =  new ResponseSpecBuilder().expectBody(not(empty())).
            expectContentType(ContentType.JSON).build();

    @DataProvider(name = "userCredentials",parallel = true)
    public Object[][] usersDataProvider() {
        return new Object[][]{
                {"emilys","emilyspass","Valid user credentials"},
                {"michaelw","michaelwpass","Valid user credentials"},
                {"Sophia","Sophia","Invalid user credentials"}

        };
    }


    @Test(dataProvider = "userCredentials")
    public void testBasicAuth(String username,String password,String Description) {

        String requestBody = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password);
        given().spec(requestSpecification).body(requestBody).log().all().when().post("/auth/login").then().statusCode(HttpStatusCode.OK.getCode()).log().all();

    }


}
