package tests;

import helpers.BaseRequests;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.User;
import pojo.UserUpdate;

import java.io.IOException;
import java.util.Map;

import static helpers.TestDataHelper.*;
import static helpers.TestDataHelper.STATUS_CODE_OK;
import static io.restassured.RestAssured.given;

public class PatchUserTest {

    private RequestSpecification requestSpecification;
    Map<String, Object> oldData;
    String userID;

    @BeforeClass
    public void setup() throws IOException {
        requestSpecification = BaseRequests.initRequestSpecification();
    }

    @Test
    public void createUser() {
        User.Addition userAddition = User.Addition.builder()
                .additional_info(ADD_INFO)
                .additional_number(ADD_NUMBER)
                .build();

        User userPojo = User.builder()
                .addition(userAddition)
                .build();

        userID = given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .log().all()
                .post("api/create")
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().asString();
    }

    @Test(dependsOnMethods = "createUser")
    public void getUserFirst() {
        Response responseFirst = given()
                .when()
                .log().all()
                .get("api/get/" + userID)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().response();
        oldData = responseFirst.jsonPath().getMap("");
    }

    @Test(dependsOnMethods = "getUserFirst")
    public void patchUser() {
        UserUpdate.Addition userUpdateAddition = UserUpdate.Addition.builder()
                .additional_info(ADD_INFO_UPDATE)
                .additional_number(ADD_NUMBER_UPDATE)
                .build();

        UserUpdate userUpdatePojo = UserUpdate.builder()
                .addition(userUpdateAddition)
                .build();

        given()
                .spec(requestSpecification)
                .body(userUpdatePojo)
                .when()
                .log().all()
                .patch("/api/patch/" + userID)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_NO_CONTENT)
                .extract().asString();
    }

    @Test(dependsOnMethods = "patchUser")
    public void getUserSecond() {
        Response responseSecond = given()
                .when()
                .log().all()
                .get("api/get/" + userID)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().response();
        Map<String, Object> newData = responseSecond.jsonPath().getMap("");
        assert !oldData.equals(newData) : "Ошибка! Данные не изменились после обновления!";
    }

    @AfterClass
    public void deleteUserAfterCreation() {
        BaseRequests.deleteUserById(String.valueOf(userID));
    }
}