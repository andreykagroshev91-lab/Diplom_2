package api.tests;

import api.models.User;
import api.steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

@DisplayName("Тесты на создание пользователя")
public class UserRegistrationTest extends BaseTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Успешная регистрация уникального пользователя")
    @Description("Создание пользователя с валидными данными должно вернуть успешный ответ с токенами")
    public void testCreateUniqueUserSuccess() {

        User testUser = new User(
                generateUniqueEmail(),
                DEFAULT_PASSWORD,
                generateUniqueName()
        );

        Response response = userSteps.register(testUser);

        userSteps.validateSuccessfulRegistration(response, testUser);

        accessToken = userSteps.extractAccessToken(response);
    }

    @Test
    @DisplayName("Регистрация уже существующего пользователя")
    @Description("Попытка создать пользователя с уже зарегистрированным email должна вернуть ошибку")
    public void testCreateExistingUserError() {

        User testUser = new User(
                generateUniqueEmail(),
                DEFAULT_PASSWORD,
                generateUniqueName()
        );

        Response firstResponse = userSteps.register(testUser);
        accessToken = userSteps.extractAccessToken(firstResponse);
        Response duplicateResponse = userSteps.register(testUser);

        userSteps.validateDuplicateUserError(duplicateResponse);
    }

    @Test
    @DisplayName("Регистрация без email")
    @Description("Попытка создать пользователя без email должна вернуть ошибку")
    public void testCreateUserWithoutEmailError() {
        User testUser = new User(
                "",  // Пустой email
                DEFAULT_PASSWORD,
                generateUniqueName()
        );

        Response response = userSteps.register(testUser);

        userSteps.validateRequiredFieldsError(response);
    }

    @Test
    @DisplayName("Регистрация без пароля")
    @Description("Попытка создать пользователя без пароля должна вернуть ошибку")
    public void testCreateUserWithoutPasswordError() {

        User testUser = new User(
                generateUniqueEmail(),
                "",  // Пустой пароль
                generateUniqueName()
        );

        Response response = userSteps.register(testUser);

        userSteps.validateRequiredFieldsError(response);
    }

    @Test
    @DisplayName("Регистрация без имени")
    @Description("Попытка создать пользователя без имени должна вернуть ошибку")
    public void testCreateUserWithoutNameError() {

        User testUser = new User(
                generateUniqueEmail(),
                DEFAULT_PASSWORD,
                ""  // Пустое имя
        );

        Response response = userSteps.register(testUser);

        userSteps.validateRequiredFieldsError(response);
    }
}