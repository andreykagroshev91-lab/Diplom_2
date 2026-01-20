package api.steps;

import api.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.List;

import static api.constants.ApiEndpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderSteps {

    @Step("Создание заказа с авторизацией")
    public Response createOrderWithAuth(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .when()
                .get(INGREDIENTS);
    }

    @Step("Извлечение ID ингредиентов из ответа")
    public List<String> extractIngredientIds(Response response) {
        return response.then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .getList("data._id");
    }

    @Step("Проверка успешного создания заказа")
    public void validateSuccessfulOrderCreation(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("name", notNullValue());
    }

    @Step("Проверка ошибки отсутствия ингредиентов")
    public void validateNoIngredientsError(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка ошибки невалидного ингредиента")
    public void validateInvalidIngredientError(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}