package api.tests;

import api.models.Order;
import api.models.User;
import api.steps.OrderSteps;
import api.steps.UserSteps;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@DisplayName("Тесты на создание заказа")
public class OrderCreationTest extends BaseTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private String accessToken;
    private List<String> ingredientIds;

    @Before
    public void initTestData() {

        User testUser = new User(
                generateUniqueEmail(),
                DEFAULT_PASSWORD,
                generateUniqueName()
        );

        Response registerResponse = userSteps.register(testUser);
        accessToken = userSteps.extractAccessToken(registerResponse);

        Response ingredientsResponse = orderSteps.getIngredients();
        ingredientIds = orderSteps.extractIngredientIds(ingredientsResponse);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Авторизованный пользователь может создать заказ с ингредиентами")
    public void testCreateOrderWithAuthSuccess() {

        String[] orderIngredients = {ingredientIds.get(0), ingredientIds.get(1)};
        Order order = new Order(orderIngredients);


        Response response = orderSteps.createOrderWithAuth(order, accessToken);


        orderSteps.validateSuccessfulOrderCreation(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Заказ можно создать без авторизации")
    public void testCreateOrderWithoutAuthSuccess() {

        String[] orderIngredients = {ingredientIds.get(0), ingredientIds.get(1)};
        Order order = new Order(orderIngredients);


        Response response = orderSteps.createOrderWithoutAuth(order);


        orderSteps.validateSuccessfulOrderCreation(response);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Заказ создается с несколькими ингредиентами")
    public void testCreateOrderWithIngredientsSuccess() {

        int count = Math.min(3, ingredientIds.size());
        String[] orderIngredients = new String[count];
        for (int i = 0; i < count; i++) {
            orderIngredients[i] = ingredientIds.get(i);
        }
        Order order = new Order(orderIngredients);


        Response response = orderSteps.createOrderWithAuth(order, accessToken);


        orderSteps.validateSuccessfulOrderCreation(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Попытка создать заказ без ингредиентов возвращает ошибку")
    public void testCreateOrderWithoutIngredientsError() {

        String[] emptyIngredients = {};
        Order order = new Order(emptyIngredients);

        Response response = orderSteps.createOrderWithAuth(order, accessToken);

        orderSteps.validateNoIngredientsError(response);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Попытка создать заказ с невалидными ингредиентами возвращает ошибку 500")
    public void testCreateOrderWithInvalidIngredientHashError() {

        String[] invalidIngredients = {"invalid_hash_123", "another_invalid_hash"};
        Order order = new Order(invalidIngredients);


        Response response = orderSteps.createOrderWithAuth(order, accessToken);


        orderSteps.validateInvalidIngredientError(response);
    }
}