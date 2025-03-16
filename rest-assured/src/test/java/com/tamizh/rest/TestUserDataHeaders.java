package com.tamizh.rest;

import com.tamizh.core.HttpStatusCode;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;

import static  io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class TestUserDataHeaders extends BaseTest{

    @Test
    public void testUserHeader() {
        given().contentType(ContentType.JSON).post("/users").then().assertThat().statusCode(HttpStatusCode.CREATED.getCode()).header("Content-type",equalTo("application/json; charset=utf-8"));
    }

    @Test
    public void testServerHeader() {
        given().contentType(ContentType.JSON).post("/users").then().spec(globalResponseSpec).statusCode(HttpStatusCode.CREATED.getCode()).header("server",is("cloudflare"));
    }

    @Test
    public void testGroupOfHeaders() {
        HashMap<String,String> expectedResponseHeaders = new HashMap<>() {
            {
                put("server","cloudflare");
                put("cf-cache-status","DYNAMIC");
            }
        };
        given().contentType(ContentType.JSON).post("/users").then().statusCode(HttpStatusCode.CREATED.getCode()).spec(globalResponseSpec).headers(expectedResponseHeaders);
    }

    @Test
    public void testCookiesSecureFlag()  {

        Cookie customCookie = new Cookie.Builder("session_id", "xyz456")
                .setSecured(true)
                .setHttpOnly(true)
                .setDomain("test.com")
                .setComment("This is a secure session cookie")
                .build();
        given().cookie(customCookie).when().get("/users").then().statusCode(HttpStatusCode.OK.getCode());

    }

    @Test(groups = {"slow"})
    public void testCookieDetails() {
       Response response =  given().contentType(ContentType.JSON).body("{\n" +
                "    \"username\":\"emilys\",\n" +
                "    \"password\":\"emilyspass\"\n" +
                "}")
                .when()
                .post("https://dummyjson.com/auth/login");


        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(),HttpStatusCode.OK.getCode());
        Cookies cookies = response.getDetailedCookies();
        Cookie cookie = cookies.get("accessToken");
        softAssert.assertFalse(cookie.isSecured());
        softAssert.assertAll();

    }

}
