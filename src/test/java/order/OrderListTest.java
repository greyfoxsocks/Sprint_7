package order;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import utils.OrderClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrderList() {
        Response response = OrderClient.getOrderList();
        response.then()
                .statusCode(SC_OK)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));
    }
}