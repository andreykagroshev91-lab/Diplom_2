package api.tests;

import api.models.User;
import api.steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

@DisplayName("Тесты на авторизацию пользователя")
public class UserLoginTest extends BaseTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Успешный вход под существующим пользователем")
    @Description("Авторизация с валидными учетными данными должна вернуть успешный ответ")
    public void testLoginWithExistingUserSuccess() {

        User testUser = new User(
                generateUniqueEmail(),
                DEFAULT_PASSWORD,
                generateUniqueName()
        );

        Response registerResponse = userSteps.register(testUser);
        accessToken = userSteps.extractAccessToken(registerResponse);

        Response loginResponse = userSteps.login(testUser);

        userSteps.validateSuccessfulLogin(loginResponse, testUser);
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Попытка авторизации с неправильным паролем должна вернуть ошибку")
    public void testLoginWithWrongPasswordError() {

        User testUser = new User(
                generateUniqueEmail(),
                DEFAULT_PASSWORD,
                generateUniqueName()
        );

        Response registerResponse = userSteps.register(testUser);
        accessToken = userSteps.extractAccessToken(registerResponse);

        User wrongPasswordUser = new User(
                testUser.getEmail(),
                "WrongPassword123",
                testUser.getName()
        );

        Response loginResponse = userSteps.login(wrongPasswordUser);

        userSteps.validateLoginError(loginResponse);
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Попытка авторизации с несуществующим email должна вернуть ошибку")
    public void testLoginWithWrongEmailError() {

        Response loginResponse = userSteps.login(
                "nonexistent@example.com",
                DEFAULT_PASSWORD
        );

        userSteps.validateLoginError(loginResponse);
    }

    @Test
    @DisplayName("Вход без email")
    @Description("Попытка авторизации без email должна вернуть ошибку")
    public void testLoginWithoutEmailError() {

        User userWithoutEmail = new User("", DEFAULT_PASSWORD, null);

        Response loginResponse = userSteps.login(userWithoutEmail);

        userSteps.validateLoginError(loginResponse);
    }

    @Test
    @DisplayName("Вход без пароля")
    @Description("Попытка авторизации без пароля должна вернуть ошибку")
    public void testLoginWithoutPasswordError() {

        User userWithoutPassword = new User(generateUniqueEmail(), "", null);


        Response loginResponse = userSteps.login(userWithoutPassword);


        userSteps.validateLoginError(loginResponse);
    }
}