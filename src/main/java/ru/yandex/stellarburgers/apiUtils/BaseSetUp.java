package ru.yandex.stellarburgers.apiUtils;

import io.restassured.RestAssured;
import static ru.yandex.stellarburgers.apiUtils.ApiUrls.*;

public class BaseSetUp {

    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}