package utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Создание заказа")
    public static Response createOrder(Order order) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получение списка заказов")
    public static Response getOrderList() {
        return given()
                .baseUri(BASE_URI)
                .when()
                .get(ORDER_PATH);
    }
}