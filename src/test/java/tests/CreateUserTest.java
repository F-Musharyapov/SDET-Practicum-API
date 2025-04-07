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
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Класс тестирования POST-запроса
 */
public class CreateUserTest {

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
    @Description("Тестовый метод для проверки создания пользователя")
    public void createUserTest() {

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

        given()
                .spec(requestSpecification)
                .when()
                .get(REQUEST_GET + userID)
                .then()
                .statusCode(STATUS_CODE_OK)
                .body("id", equalTo(Integer.parseInt(userID)));
    }

    /**
     * Метод удаления соданного user из базы после всех запросов
     */
    @AfterClass
    public void userAfterCreationDelete() {
        BaseRequests.deleteUserById(String.valueOf(userID));
    }
}