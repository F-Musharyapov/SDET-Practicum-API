package tests;

import helpers.BaseRequests;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.Addition;
import pojo.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;

/**
 * Класс тестирования GET/ALL-запроса
 */
public class GetAllUserTest {

    /**
     * Экземпляр спецификации RestAssured
     */
    private RequestSpecification requestSpecification;

    /**
     * Переменная для хранения user ID
     */
    private int userIDFirst;

    /**
     * Переменная для хранения user ID
     */
    private int userIDSecond;

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
     * Метод создания двух пользователей
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

        userIDFirst = Integer.parseInt(given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .post(REQUEST_POST)
                .then()
                .statusCode(STATUS_CODE_OK)
                .extract().asString());

        userIDSecond = Integer.parseInt(given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .post(REQUEST_POST)
                .then()
                .statusCode(STATUS_CODE_OK)
                .extract().asString());
    }

    @Test
    @Description("Тестовый метод для получения списка пользователей и проверки наличия в списке ранее созданных")
    public void givenAllUsersTest() {
        Response response = given()
                .when()
                .get(REQUEST_POST_ALL)
                .then()
                .statusCode(STATUS_CODE_OK)
                .extract().response();
        List<Integer> allUser = response.jsonPath().getList("entity.id");
        List<Integer> createdUsers = Arrays.asList(userIDFirst, userIDSecond);
        Assert.assertNotNull(allUser, "Список пользователей не должен быть null");
        Assert.assertTrue(allUser.containsAll(createdUsers), "Созданные пользователи не найдены в ответе");
    }

    /**
     * Метод удаления соданных пользователей из базы после всех запросов
     */
    @AfterClass
    public void userAfterCreationDelete() {
        deleteUser(userIDFirst);
        deleteUser(userIDSecond);
    }

    /**
     * Приватный метод для выполнения общего кода удаления
     */
    private void deleteUser(int userID) {

        given()
                .when()
                .delete(REQUEST_DELETE + userID)
                .then()
                .statusCode(STATUS_CODE_NO_CONTENT);
    }

}