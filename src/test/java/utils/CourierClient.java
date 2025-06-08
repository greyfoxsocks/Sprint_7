package utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Courier;
import models.LoginCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";
    private static final String DELETE_PATH = "/api/v1/courier/";

    @Step("Создание курьера")
    public static Response createCourier(Courier courier) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Логин курьера")
    public static Response loginCourier(LoginCredentials credentials) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера")
    public static Response deleteCourier(int id) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body("{\"id\": " + id + "}")
                .when()
                .delete(DELETE_PATH + id);
    }
}