package com.tamizh.rest;

import com.tamizh.core.HttpStatusCode;
import com.tamizh.pojo.UserData;
import com.tamizh.pojo.user.expectedres.UserExpectedRes;
import com.tamizh.pojo.user.payload.User;
import com.tamizh.utils.FileUtils;
import com.tamizh.utils.JsonUtils;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;


public class TestCRUDOperationsOnUsers extends BaseTest{

    User testUser1 = null;

    @BeforeClass
    public void initUsers() throws FileNotFoundException {
        testUser1 = JsonUtils.getInstance().fromJson(FileUtils.getReader(resourceDir.toString()+"/TestData/users/user1.json"),User.class);
    }



    @Test
    public void createUser() throws FileNotFoundException {
        UserExpectedRes actualResult = given().contentType(ContentType.JSON).config(config).body(testUser1).log().all().when().post("/users").as(UserExpectedRes.class);
        assertThat(actualResult).usingRecursiveComparison().ignoringCollectionOrder().ignoringFields("createdAt","id").isEqualTo(testUser1);
    }

    @Test
    public void updateUser() {
        testUser1.setJob("zion resident");
        Response response = given().contentType(ContentType.JSON).body(testUser1).pathParam("id",2).config(config).when().put("/users/${id}");
        Assert.assertEquals(HttpStatusCode.OK.getCode(),response.getStatusCode());
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema("{\"\":\"\"}"));

    }


}
