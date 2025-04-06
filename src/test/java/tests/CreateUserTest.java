package tests;

import helpers.BaseRequests;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.User;

import java.io.IOException;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class CreateUserTest {

    private RequestSpecification requestSpecification;

    private String userID;

    @BeforeClass
    public void setup() throws IOException {
        requestSpecification = BaseRequests.initRequestSpecification();
    }

    @Test()
    public void testCreateUser() {
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

    @Test(dependsOnMethods = "testCreateUser")
    public void testGetUser() {
        given()
                .spec(requestSpecification)
                .when()
                .log().all()
                .get("api/get/" + userID)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .body("id", equalTo(Integer.parseInt(userID)));
    }

    @AfterClass
    public void deleteUserAfterCreation() {
        BaseRequests.deleteUserById(String.valueOf(userID));
    }
}