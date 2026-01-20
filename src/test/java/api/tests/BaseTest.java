package api.tests;

import api.constants.ApiEndpoints;
import api.utils.RandomDataGenerator;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;

public class BaseTest {

    protected static final String DEFAULT_PASSWORD = "Password123!";

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = ApiEndpoints.BASE_URL;
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    protected String generateUniqueEmail() {
        return RandomDataGenerator.generateRandomEmail();
    }

    protected String generateUniqueName() {
        return RandomDataGenerator.generateRandomName();
    }
}