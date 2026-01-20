package api.steps;

import api.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static api.constants.ApiEndpoints.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserSteps {

    @Step("Регистрация нового пользователя")
    public Response register(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(REGISTER);
    }

    @Step("Авторизация пользователя")
    public Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(LOGIN);
    }

    @Step("Авторизация с email и паролем")
    public Response login(String email, String password) {
        User loginData = new User(email, password, null);
        return login(loginData);
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER);
    }

    @Step("Получение access token из ответа")
    public String extractAccessToken(Response response) {
        return response.then().extract().path("accessToken");
    }

    @Step("Проверка успешной регистрации")
    public void validateSuccessfulRegistration(Response response, User user) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Проверка ошибки дублирования пользователя")
    public void validateDuplicateUserError(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка ошибки обязательных полей")
    public void validateRequiredFieldsError(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка успешной авторизации")
    public void validateSuccessfulLogin(Response response, User user) {
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Проверка ошибки авторизации")
    public void validateLoginError(Response response) {
        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}