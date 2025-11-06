package helpers;

import config.BaseConfig;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

import java.io.IOException;

import static helpers.TestDataHelper.REQUEST_DELETE;
import static helpers.TestDataHelper.STATUS_CODE_NO_CONTENT;
import static io.restassured.RestAssured.given;


/**
 * Базовый тестовый класс с общими настройками
 */
public class BaseRequests {

    /**
     * Экземпляр интерфейса с конфигурацией
     */
    private static final BaseConfig config = ConfigFactory.create(BaseConfig.class, System.getenv());

    /**
     * Метод для получения спецификации RestAssured
     *
     * @return спецификация
     * @throws IOException
     */
    @Description("Дефолтный метод для получения спецификации RestAssured")
    public static RequestSpecification initRequestSpecification() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder
                .setBaseUri(config.baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON);
        RequestSpecification requestSpecification;
        return requestSpecification = requestSpecBuilder.build();
    }

    /**
     * Удаление пользователя с заданным ID
     *
     * @param userID ID пользователя, которого необходимо удалить
     */
    @Description("Дефолтный метод для удаления данных из базы в конце тестов")
    public static void deleteUserById(String userID) {

        given()
                .when()
                .delete(REQUEST_DELETE + userID)
                .then()
                .statusCode(STATUS_CODE_NO_CONTENT);
    }
}