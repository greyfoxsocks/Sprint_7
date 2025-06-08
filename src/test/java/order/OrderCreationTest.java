package order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.OrderClient;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTest {
    private final String[] colors;
    private final String colorDescription;

    public OrderCreationTest(String[] colors, String colorDescription) {
        this.colors = colors;
        this.colorDescription = colorDescription;
    }

    @Parameterized.Parameters(name = "Цвета: {1}")
    public static Object[][] getColorData() {
        return new Object[][] {
                {new String[]{"BLACK"}, "Черный"},
                {new String[]{"GREY"}, "Серый"},
                {new String[]{"BLACK", "GREY"}, "Черный + Серый"},
                {null, "Без цвета"}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    @Description("Проверка создания заказа с различными вариантами цветов")
    public void testCreateOrderWithDifferentColors() {
        Order order = new Order(colors);
        OrderClient.createOrder(order)
                .then().statusCode(SC_CREATED)
                .and().body("track", notNullValue());
    }
}