package tests;

import helpers.BaseRequests;
import io.qameta.allure.Description;
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

/**
 * Класс тестирования PATCH-запроса
 */
public class PatchUserTest {

    /**
     * Экземпляр спецификации RestAssured
     */
    private RequestSpecification requestSpecification;

    /**
     * Экземпляр массива для хранения данных до изменения
     */
    Map<String, Object> oldData;

    /**
     * Переменная для хранения user ID
     */
    String userID;

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
    @Description("Тестовый метод проверки добавления пользователя и добавления данных в массив")
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
    @Description("Тестовый метод для изменения данных пользователя")
    public void patchUser() {
        UserUpdate.UpdateAddition userUpdateAddition = UserUpdate.UpdateAddition.builder()
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
    @Description("Тестовый метод проверки внесения изменений и сравнения с предыдущими данными")
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

    /**
     * Метод удаления соданного user из базы после всех запросов
     */
    @AfterClass
    public void deleteUserAfterCreation() {
        BaseRequests.deleteUserById(String.valueOf(userID));
    }
}