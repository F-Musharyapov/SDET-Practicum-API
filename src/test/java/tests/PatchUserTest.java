package tests;

import helpers.BaseRequests;
import io.qameta.allure.Description;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Addition;
import pojo.User;

import java.io.IOException;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotEquals;

/**
 * Класс тестирования PATCH-запроса
 */
public class PatchUserTest {

    /**
     * Экземпляр спецификации RestAssured
     */
    private RequestSpecification requestSpecification;

    /**
     * Переменная для хранения user ID
     */
    private String userID;

    /**
     * Переменная для хранения объекта pojo при post запросе
     */
    private User userPojoSet;

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

    @Test(dependsOnMethods = "createUser")
    @Description("Тестовый метод проверки добавления пользователя и добавления данных в массив")
    public void userFirstGet() {
        User userPojoSet = given()
                .when()
                .get(REQUEST_GET + userID)
                .then()
                .statusCode(STATUS_CODE_OK)
                .extract().as(User.class);
    }

    @Test(dependsOnMethods = "userFirstGet")
    @Description("Тестовый метод для изменения данных пользователя")
    public void userPatch() {
        User userPojo = User.builder()
                .addition(
                        Addition.builder()
                                .additional_info(ADD_INFO_UPDATE)
                                .additional_number(ADD_NUMBER_UPDATE)
                                .build())
                .important_numbers(IMPORTANT_NUMBERS_UPDATE)
                .title(TITLE_UPDATE)
                .verified(VERIFIED)
                .build();

        given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .patch(REQUEST_PATCH + userID)
                .then()
                .statusCode(STATUS_CODE_NO_CONTENT)
                .extract().asString();
    }

    @Test(dependsOnMethods = "userPatch")
    @Description("Тестовый метод проверки внесения изменений и сравнения с предыдущими данными")
    public void userSecondGet() {
        User userPojoGet = given()
                .when()
                .get(REQUEST_GET + userID)
                .then()
                .statusCode(STATUS_CODE_OK)
                .extract().as(User.class);
        assertNotEquals(userPojoGet, userPojoSet, "Ошибка! Данные не изменились после обновления!");
    }

    /**
     * Метод удаления соданного user из базы после всех запросов
     */
    @AfterClass
    public void userAfterCreationDelete() {
        BaseRequests.deleteUserById(String.valueOf(userID));
    }
}