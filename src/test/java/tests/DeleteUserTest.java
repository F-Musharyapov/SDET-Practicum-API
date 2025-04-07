package tests;

import helpers.BaseRequests;
import io.qameta.allure.Description;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Addition;
import pojo.User;

import java.io.IOException;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;

/**
 * Класс тестирования DELETE-запроса
 */
public class DeleteUserTest {

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

    /**
     * Метод для создания пользователя
     */
    @BeforeClass(dependsOnMethods = "setup")
    public void createUser() {

        User userPojo = User.builder()
                .addition(
                        Addition.builder()
                                .additional_info(ADD_INFO)
                                .additional_number(ADD_NUMBER)
                                .build())
                .important_numbers(IMPORTANT_NUMBERS)
                .title(TITLE)
                .verified(VERIFIED)
                .build();

        userID = given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .post(REQUEST_POST)
                .then()
                .statusCode(STATUS_CODE_OK)
                .extract().asString();
    }

    @Test
    @Description("Тестовый метод для проверки удаления пользователя")
    public void userDelete() {
        given()
                .when()
                .delete(REQUEST_DELETE + userID)
                .then()
                .statusCode(STATUS_CODE_NO_CONTENT);

        given()
                .when()
                .get(REQUEST_GET + userID)
                .then()
                .statusCode(STATUS_INTERAL_SERVER_ERROR);
    }
}