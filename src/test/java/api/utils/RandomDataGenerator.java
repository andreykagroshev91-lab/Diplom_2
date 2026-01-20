package api.utils;

import java.util.UUID;

public class RandomDataGenerator {

    public static String generateRandomEmail() {
        return "test_user_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 6) + "@example.com";
    }

    public static String generateRandomName() {
        return "User_" + System.currentTimeMillis();
    }
}