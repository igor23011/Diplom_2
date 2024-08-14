import org.example.CreateOrder;
import org.example.CreateUser;
import org.example.LoginUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.example.UserController.*;

public class GetOrderTest {
    private StepTest stepTestUser = new StepTest();
    private static CreateUser createUser;
    private static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    public void checkGetOrdersAuthUserTest() {
        CreateUser createUser = stepTestUser.createUserStep("victor111@yandex.ru","!qw12345","Victor Bail");
        token = stepTestUser.tokenUserStep(new LoginUser(createUser.getEmail(), createUser.getPassword()));
        CreateOrder createOrder = new CreateOrder(new String[]{"61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa72"});
        stepTestUser.createOrderStep(createOrder);
        stepTestUser.getOrdersAuthUserTestStep(token, true);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void checkGetOrdersNoAuthUserTest() {
        stepTestUser.getOrdersNoAuthUserTestStep(token, false);
    }

    @AfterClass
    public static void deleteChanges() {
        executeDelete(token);
    }
}
