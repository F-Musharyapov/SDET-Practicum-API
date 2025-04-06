package tests;

import helpers.BaseRequests;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.User;

import java.io.IOException;
import java.util.List;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class GetAllUserTest {

    private RequestSpecification requestSpecification;

    int userIDFirst;
    int userIDSecond;

    @BeforeClass
    public void setup() throws IOException {
        requestSpecification = BaseRequests.initRequestSpecification();
    }

    @Test
    public void requestCreateUser() {
        User.Addition userAddition = User.Addition.builder()
                .additional_info(ADD_INFO)
                .additional_number(ADD_NUMBER)
                .build();

        User userPojo = User.builder()
                .addition(userAddition)
                .build();

        userIDFirst = Integer.parseInt(given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .log().all()
                .post("api/create")
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().asString());

        userIDSecond = Integer.parseInt(given()
                .spec(requestSpecification)
                .body(userPojo)
                .when()
                .log().all()
                .post("api/create")
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().asString());
    }

    @Test(dependsOnMethods = "requestCreateUser")
    public void givenAllUsersTest() throws InterruptedException {
        // Получаем всех пользователей
        Response response = given()
                .spec(requestSpecification)
                .when()
                .log().all()
                .get("/api/getAll")
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .extract().response();
        System.out.println(response.asString());
        Thread.sleep(1000);
        List<Integer> allUser  = response.jsonPath().getList("entity.id");
        System.out.println(allUser);
        Assert.assertNotNull(allUser, "Список пользователей не должен быть null");
        Assert.assertTrue(allUser.contains(userIDFirst), "Первый пользователь не найден в ответе");
        Assert.assertTrue(allUser.contains(userIDSecond), "Второй пользователь не найден в ответе");
    }

    @Test(dependsOnMethods = "requestCreateUser")
    public void requestGetUser1() {
        given()
                //.spec(requestSpecification)
                .when()
                .log().all()
                .get("api/get/" + userIDFirst)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .body("id", equalTo(userIDFirst));
    }

    @Test(dependsOnMethods = "requestGetUser1")
    public void requestGetUser2() {
        given()
                //.spec(requestSpecification)
                .when()
                .log().all()
                .get("api/get/" + userIDSecond)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_OK)
                .body("id", equalTo(userIDSecond));
    }

    @AfterClass
    public void requestDeleteUser() {
        given()
                .when()
                .log().all()
                .delete("api/delete/" + userIDFirst)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_NO_CONTENT);
        given()
                .when()
                .log().all()
                .delete("api/delete/" + userIDSecond)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_NO_CONTENT);
    }
}


//.spec(requestSpecification)
//.when()
//.log().all()
//.get("/get/" + userID)
//.then()
//.log().all()
//.statusCode(200)
//.body("statusCode", equalTo(200))
//.body("name", equalTo(userPojo.getName()), "job", equalTo(userPojo.getJob())) //сравниваем что ушло и что пришл
//.extract().asString();
//users.stream().forEach(x-> Assert.assertTrue(x.get)).extract().body().jsonPath().getList(".", UserGet.class);



/*
{
    "id": 20,
    "title": "Заголовок 777",
    "verified": true,
    "addition": {
        "id": 20,
        "additional_info": "ДопИнфа",
        "additional_number": 777
    },
    "important_numbers": [
        11,
        22,
        33
    ]
}

@Test(dependsOnMethods = "testCreateUser")
    public void testGetUser() {
        List<UserGet> users = given()
                .spec(requestSpecification)
                .when()
                .log().all()
                .get("api/get/" + userID)
                .then()
                .log().all()
                .statusCode(200)
                //.body("id", IsEqual.equalTo(Integer.parseInt(userID)))
                .extract().body().jsonPath().getList(".", UserGet.class);
        users.stream().forEach(x-> Assert.assertTrue(x.getUserGet().containts(x.getId().toString));
    }


 */
/*
Создание сущности: POST /api/create
Получение сущности: GET /api/get/{id}
Получение всех сущностей: POST /api/getAll
 */