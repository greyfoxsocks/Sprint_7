package courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Courier;
import models.LoginCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CourierClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {
    private int courierId;
    private String login;
    private final String password = "1234";
    private final String firstName = "saske";

    @Before
    public void setUp() {
        login = "ninja_" + System.currentTimeMillis();
        Courier courier = new Courier(login, password, firstName);
        CourierClient.createCourier(courier);
    }

    @After
    public void tearDown() {
        Response loginResponse = CourierClient.loginCourier(new LoginCredentials(login, password));
        if (loginResponse.statusCode() == SC_OK) {
            courierId = loginResponse.then().extract().path("id");
            CourierClient.deleteCourier(courierId)
                    .then().statusCode(SC_OK);
        }
    }

    @Test
    @DisplayName("Успешный логин курьера")
    @Description("Проверка авторизации с валидными данными")
    public void testLoginCourierSuccess() {
        Response response = CourierClient.loginCourier(new LoginCredentials(login, password));
        response.then().statusCode(SC_OK)
                .and().body("id", notNullValue());
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