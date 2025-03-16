package com.tamizh.rest;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public abstract class BaseTest {

    ResponseSpecification globalResponseSpec =  new ResponseSpecBuilder().expectBody(not(empty())).
            expectContentType(ContentType.JSON).build();
    public static String baseDir = System.getProperty("baseDir")==null?System.getProperty("user.dir"):System.getProperty("baseDir");

    @BeforeClass
    public void initProp() {
        baseURI = "https://reqres.in/api";
    }

    @Test
    public void checkingServerStatus() {
        try {
            given().head(baseURI).then().assertThat().statusCode(404);
        }
        catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
