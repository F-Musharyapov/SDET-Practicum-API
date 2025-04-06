package tests;

import helpers.BaseRequests;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.User;

import java.io.IOException;
import java.util.List;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetAllUserTest {

    private RequestSpecification requestSpecification;

    private int userIDFirst;
    private int userIDSecond;

    @BeforeClass
    public void setup() throws IOException {
        requestSpecification = BaseRequests.initRequestSpecification();
    }

    @Test
    public void requestCreateUser() {
        User.Addition userAddition = User.Addition.builder()
                .additional_info(ADD_INFO)
                .additional_number(ADD_NUMBER)
                .build();

        User userPojo = User.builder()
                .addition(userAddition)
                .build();

        userIDFirst = Integer.parseInt(given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .log().all()
                .post("api/create")
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().asString());

        userIDSecond = Integer.parseInt(given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .log().all()
                .post("api/create")
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().asString());
    }

    @Test(dependsOnMethods = "requestCreateUser")
    public void givenAllUsersTest() {
        Response response = given()
                .when()
                .log().all()
                .get("/api/getAll")
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().response();
        List<Integer> allUser = response.jsonPath().getList("entity.id");
        Assert.assertNotNull(allUser, "Список пользователей не должен быть null");
        Assert.assertTrue(allUser.contains(userIDFirst), "Первый пользователь не найден в ответе");
        Assert.assertTrue(allUser.contains(userIDSecond), "Второй пользователь не найден в ответе");
    }

    @Test(dependsOnMethods = "requestCreateUser")
    public void requestGetUser1() {
        given()
                .when()
                .log().all()
                .get("api/get/" + userIDFirst)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .body("id", equalTo(userIDFirst));
    }

    @Test(dependsOnMethods = "requestGetUser1")
    public void requestGetUser2() {
        given()
                .when()
                .log().all()
                .get("api/get/" + userIDSecond)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .body("id", equalTo(userIDSecond));
    }

    @AfterClass
    public void requestDeleteUser() {
        given()
                .when()
                .log().all()
                .delete("api/delete/" + userIDFirst)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_NO_CONTENT);
        given()
                .when()
                .log().all()
                .delete("api/delete/" + userIDSecond)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_NO_CONTENT);
    }
}