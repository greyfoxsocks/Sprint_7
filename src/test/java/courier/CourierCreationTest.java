package courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import models.Courier;
import models.LoginCredentials;
import org.junit.After;
import org.junit.Test;
import utils.CourierClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierCreationTest {
    private int courierId;
    private final String login = "ninja_" + System.currentTimeMillis();
    private final String password = "1234";
    private final String firstName = "saske";

    @After
    public void tearDown() {
        if (courierId != 0) {
            CourierClient.deleteCourier(courierId)
                    .then().statusCode(SC_OK);
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверка, что курьер может быть создан с валидными данными")
    public void testCreateCourierSuccess() {
        Courier courier = new Courier(login, password, firstName);
        CourierClient.createCourier(courier)
                .then().statusCode(SC_CREATED)
                .and().body("ok", is(true));

        courierId = CourierClient.loginCourier(new LoginCredentials(login, password))
                .then().extract().path("id");
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    @Description("Попытка создания двух одинаковых курьеров")
    public void testCreateDuplicateCourier() {
        Courier courier = new Courier(login, password, firstName);

        CourierClient.createCourier(courier)
                .then().statusCode(SC_CREATED);

        courierId = CourierClient.loginCourier(new LoginCredentials(login, password))
                .then().extract().path("id");

        CourierClient.createCourier(courier)
                .then().statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка обязательности поля login")
    public void testCreateCourierWithoutLogin() {
        Courier courier = new Courier("", password, firstName);
        CourierClient.createCourier(courier)
                .then().statusCode(SC_BAD_REQUEST)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка обязательности поля password")
    public void testCreateCourierWithoutPassword() {
        Courier courier = new Courier(login, "", firstName);
        CourierClient.createCourier(courier)
                .then().statusCode(SC_BAD_REQUEST)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}