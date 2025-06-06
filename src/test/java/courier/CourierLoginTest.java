package courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.LoginCredentials;
import org.junit.After;
import org.junit.Test;
import utils.CourierClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
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
    @DisplayName("Успешный логин курьера")
    @Description("Проверка авторизации с валидными данными")
    public void testLoginCourierSuccess() {
        Courier courier = new Courier(login, password, firstName);
        CourierClient.createCourier(courier);

        Response response = CourierClient.loginCourier(new LoginCredentials(login, password));
        response.then().statusCode(SC_OK)
                .and().body("id", notNullValue());

        courierId = response.then().extract().path("id");
    }

    @Test
    @DisplayName("Логин без пароля")
    @Description("Проверка обязательности поля password")
    public void testLoginWithoutPassword() {
        LoginCredentials credentials = new LoginCredentials(login, "");
        CourierClient.loginCourier(credentials)
                .then().statusCode(SC_BAD_REQUEST)
                .and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Проверка авторизации с неверным паролем")
    public void testLoginWithWrongPassword() {
        Courier courier = new Courier(login, password, firstName);
        CourierClient.createCourier(courier);
        courierId = CourierClient.loginCourier(new LoginCredentials(login, password))
                .then().extract().path("id");

        CourierClient.loginCourier(new LoginCredentials(login, "wrong_password"))
                .then().statusCode(SC_NOT_FOUND)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин несуществующего курьера")
    @Description("Проверка авторизации несуществующего пользователя")
    public void testLoginNonExistentCourier() {
        CourierClient.loginCourier(new LoginCredentials("nonexistent", "password"))
                .then().statusCode(SC_NOT_FOUND)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }
}