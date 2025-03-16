package com.tamizh.rest;

import static io.restassured.RestAssured.*;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Paths;

public class TestUserDataSchema extends BaseTest{


    @Test
    public void testUserDataSchema() {
        File expectedSchema = Paths.get("src","test","resources","TestData" ,"JsonSchemas","UserDataSchema.json").toFile();
        given().contentType(ContentType.JSON).when().get("/users/2").then().spec(globalResponseSpec).assertThat().body(JsonSchemaValidator.matchesJsonSchema(expectedSchema));

    }
}
