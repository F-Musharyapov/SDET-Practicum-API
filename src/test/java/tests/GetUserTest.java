package tests;

import helpers.BaseRequests;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.core.IsEqual;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.User;

import java.io.IOException;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;

public class GetUserTest {

    private RequestSpecification requestSpecification;

    private String userID;

    @BeforeClass
    public void setup() throws IOException {
        requestSpecification = BaseRequests.initRequestSpecification();
    }

    @BeforeClass
    public void testPostCreateUser() {
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

    @Test
    public void testGetUser() {
        User.Addition userAddition = User.Addition.builder()
                .additional_info(ADD_INFO)
                .additional_number(ADD_NUMBER)
                .build();

        User userPojo = User.builder()
                .addition(userAddition)
                .build();

        given()
                .spec(requestSpecification)
                .when()
                .log().all()
                .get("api/get/" + userID)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .body("id", IsEqual.equalTo(Integer.parseInt(userID)))
                .body("title", IsEqual.equalTo(userPojo.getTitle()))
                .body("verified", IsEqual.equalTo(userPojo.getVerified()))
                //.body("additional_info", IsEqual.equalTo(ADD_INFO))
                //.body("additional_number", IsEqual.equalTo(ADD_NUMBER))
                .body("important_numbers", IsEqual.equalTo(userPojo.getImportant_numbers()));
    }


    @AfterClass
    public void deleteUserAfterCreation() {
        BaseRequests.deleteUserById(userID);
    }
}