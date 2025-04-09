package tests;

import helpers.BaseRequests;
import io.qameta.allure.Description;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Addition;
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

    /**
     * Метод для создания пользователя
     */
    @BeforeClass(dependsOnMethods = "setup")
    public void createUser() {

        userPojoSet = User.builder()
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
                .body(userPojoSet)
                .when()
                .post(REQUEST_POST)
                .then()
                .statusCode(STATUS_CODE_OK)
                .extract().asString();
    }

    @Test
    @Description("Тестовый метод для сравнения отправленных данных пользователя с полученными")
    public void getUserTest() {

        User userPojoGet = given()
                .when()
                .get(REQUEST_GET + userID)
                .then()
                .statusCode(STATUS_CODE_OK)
                .extract().as(User.class);
        Assert.assertEquals(userPojoGet, userPojoSet, "Отправленный и полученный объекты User не совпадают");
    }

    /**
     * Метод удаления соданного user из базы после всех запросов
     */
    @AfterClass
    public void userAfterCreationDelete() {
        BaseRequests.deleteUserById(userID);
    }
}