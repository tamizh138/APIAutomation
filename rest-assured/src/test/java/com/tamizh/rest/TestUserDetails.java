package com.tamizh.rest;


import static  io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.tamizh.core.HttpStatusCode;
import com.tamizh.pojo.Data;
import com.tamizh.pojo.Support;
import com.tamizh.pojo.User;
import com.tamizh.pojo.UserData;
import io.restassured.builder.RequestSpecBuilder;
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
        Data data = new Data();
        data.setId(2);
        data.setEmail("janet.weaver@reqres.in");
        data.setFirstName("Janet");
        data.setLastname("Weaver");
        data.setAvatar("https://reqres.in/img/faces/2-image.jpg");
        Support supportData = new Support();
        supportData.setText("Tired of writing endless social media content? Let Content Caddy generate it for you.");
        supportData.setUrl("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral");
        UserData expectedData = new UserData();
        expectedData.setData(data);
        expectedData.setSupport(supportData);
        UserData actualUserData = given().spec(requestSpecification).log().all().get("/users/{id}").as(UserData.class);
        assertThat(actualUserData).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(expectedData);
    }


}
