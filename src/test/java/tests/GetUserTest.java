package tests;

import helpers.BaseRequests;
import io.qameta.allure.Description;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.core.IsEqual;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.User;

import java.io.IOException;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;

/**
 * Класс тестирования GET-запроса
 */
public class GetUserTest {

    /**
     * Экземпляр спецификации RestAssured
     */
    private RequestSpecification requestSpecification;

    /**
     * Переменная для хранения user ID
     */
    private String userID;

    /**
     * Метод инициализации спецификации запроса
     *
     * @throws IOException если не удается инициализировать спецификацию запроса
     */
    @BeforeClass
    public void setup() throws IOException {
        requestSpecification = BaseRequests.initRequestSpecification();
    }

    @Test
    @Description("Тестовый метод для создания пользователя")
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
    @Description("Тестовый метод для сравнения отправленных данных пользователя с полученными")
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
                .body("addition.additional_info", IsEqual.equalTo(ADD_INFO))
                .body("addition.additional_number", IsEqual.equalTo(ADD_NUMBER))
                .body("important_numbers", IsEqual.equalTo(userPojo.getImportant_numbers()));
    }

    /**
     * Метод удаления соданного user из базы после всех запросов
     */
    @AfterClass
    public void deleteUserAfterCreation() {
        BaseRequests.deleteUserById(userID);
    }
}