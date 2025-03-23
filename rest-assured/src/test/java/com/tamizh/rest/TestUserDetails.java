package com.tamizh.rest;


import static  io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tamizh.core.HttpStatusCode;
import com.tamizh.pojo.Data;
import com.tamizh.pojo.Support;
import com.tamizh.pojo.UserData;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.internal.mapping.ObjectMapperDeserializationContextImpl;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestUserDetails extends BaseTest {

    RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("http://reqres.in/api").addHeader("Content-Type","Application/xml").addPathParam("id",2).build();


    @Test(description = "Checks whether first page is returned when page parameter is not specified",groups = {"slow"})
    public void testDefaultPageNumber() {
        given().get("/users").then().assertThat().statusCode(HttpStatusCode.OK.getCode()).spec(globalResponseSpec).body("page", equalTo(1));
    }

    @Test
    public void testPage2UsersCount() {
        given().param("page",2).when().get("/users").then().statusCode(HttpStatusCode.OK.getCode()).spec(globalResponseSpec).assertThat().body("data",hasSize(6));

    }

    @Test
    public void testUserDataContainsAUsers() {
        List<String> expectedUsersList = Arrays.asList("michael.lawson@reqres.in","lindsay.ferguson@reqres.in", "tobias.funke@reqres.in", "byron.fields@reqres.in", "george.edwards@reqres.in", "rachel.howell@reqres.in");
        given().param("page",2).when().get("/users").then().statusCode(HttpStatusCode.OK.getCode()).spec(globalResponseSpec).assertThat().body("data.email",containsInAnyOrder(expectedUsersList.toArray()));
    }

    @Test
    public void testUserDataOnPage2() {
        given().param("page",2).when().get("/users").then().statusCode(HttpStatusCode.OK.getCode()).spec(globalResponseSpec).assertThat().body("data.email",hasItems("michael.lawson@reqes.in","tobias.funke@reqres.in"));
    }

    @Test
    public void testAllUserData() {
        JsonPath jsonPath = JsonPath.from(Paths.get(baseDir,"src","test","resources","ExpectedUsers.json").toFile());
        List<Map<String,?>> expectedUsersData = jsonPath.get("data");
        Response response = given().param("page",2).when().get("/users").then().statusCode(HttpStatusCode.OK.getCode()).spec(globalResponseSpec).extract().response();
        List<Map<String,?>> allUserData= response.then().extract().jsonPath().get("data");
        Assert.assertEquals(expectedUsersData,allUserData);
    }

    @Test
    public void testSingleUserData() {
        Data data = new Data(2,"janet.weaver@reqres.in","Janet","Weaver");
        Support supportData = new Support("Tired of writing endless social media content? Let Content Caddy generate it for you.");
        UserData expectedData = new UserData(data,supportData);
    /*    RestAssuredConfig config = RestAssuredConfig.config().objectMapperConfig( new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper;
        }));*/
        UserData actualUserData = given().spec(requestSpecification).get("/users/{id}").as(UserData.class);
        System.out.println(actualUserData.equals(expectedData));
        assertThat(actualUserData).usingRecursiveComparison().ignoringFields("support.url").ignoringCollectionOrder().isEqualTo(expectedData);
    }


}
