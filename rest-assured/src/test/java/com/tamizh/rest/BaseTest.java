package com.tamizh.rest;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import com.tamizh.utils.ExtentManager;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseTest {

    ResponseSpecification globalResponseSpec =  new ResponseSpecBuilder().expectBody(not(empty())).
            expectContentType(ContentType.JSON).build();
    public static String baseDir = System.getProperty("baseDir")==null?System.getProperty("user.dir"):System.getProperty("baseDir");
    public static Path resourceDir = Paths.get(baseDir,"src","test","resources");

    @BeforeSuite
    public void setUp() {
        ExtentManager.setExtent();
    }

    @BeforeClass
    public void initProp() {
        baseURI = "https://reqres.in/api";
        config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.GSON));
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

    @AfterSuite
    public void tearDown() {
        ExtentManager.endExtent();
    }
}
