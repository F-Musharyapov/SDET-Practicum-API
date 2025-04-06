package tests;

import helpers.BaseRequests;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojo.User;

import java.io.IOException;

import static helpers.TestDataHelper.*;
import static io.restassured.RestAssured.given;

public class DeleteUserTest {

    private RequestSpecification requestSpecification;

    String userID;

    @BeforeClass
    public void setup() throws IOException {
        requestSpecification = BaseRequests.initRequestSpecification();
    }

    @Test()
    public void requestCreateUser() {
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

    @Test(dependsOnMethods = "requestCreateUser")
    public void requestDeleteUser() {
        given()
                //.spec(requestSpecification)
                .when()
                .log().all()
                .delete("api/delete/" + userID)
                .then()
                .log().all()
                .statusCode(STATUS_CODE_NO_CONTENT);
    }

    @AfterClass
    public void requestGetUser() {
        given()
                //.spec(requestSpecification)
                .when()
                .log().all()
                .get("api/get/" + userID)
                .then()
                .log().all()
                .statusCode(STATUS_INTERAL_SERVER_ERROR);
    }
}
/*
Создание сущности: POST /api/create
Удаление сущности: DELETE /api/delete/{id}
Получение сущности: GET /api/get/{id}
Получение всех сущностей: POST /api/getAll
Обновление сущности: PATCH /api/patch/{id}
 */

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