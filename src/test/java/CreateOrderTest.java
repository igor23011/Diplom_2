import org.example.CreateOrder;
import org.example.CreateUser;
import org.example.LoginUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.UserController.*;

public class CreateOrderTest {
    private StepTest stepTestUser = new StepTest();
    private static CreateUser createUser;
    private static String token;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void checkCreateOrderAuthAndIngredientsTest() {
        CreateUser createUser = stepTestUser.createUserStep("kesha007@yandex.ru","!qwert12345","Kos May");
        token = stepTestUser.tokenUserStep(new LoginUser(createUser.getEmail(), createUser.getPassword()));
        CreateOrder createOrder = new CreateOrder(new String[]{"61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa70"});
        stepTestUser.createOrderStep(createOrder);
    }

    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентами")
    public void checkCreateOrderNoAuthAndIngredientsTest() {
        CreateOrder createOrder = new CreateOrder(new String[]{"61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6e", "61c0c5a71d1f82001bdaaa70"});
        stepTestUser.createOrderStep(createOrder);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и без ингредиентов")
    public void checkCreateOrderAuthAndNoIngredientsTest() {
        CreateUser createUser = stepTestUser.createUserStep("kesha007@yandex.ru","!qwert12345","Kos May");
        token = stepTestUser.tokenUserStep(new LoginUser(createUser.getEmail(), createUser.getPassword()));
        CreateOrder createOrder = new CreateOrder(new String[]{});
        stepTestUser.createOrderNoIngredientsStep(createOrder);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void checkCreateOrderNoAuthAndNoIngredientsTest() {
        CreateOrder createOrder = new CreateOrder(new String[]{});
        stepTestUser.createOrderNoIngredientsStep(createOrder);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и неверным хэшем ингредиентов")
    public void checkCreateOrderAuthAndWrongIngredientsTest() {
        CreateUser createUser = stepTestUser.createUserStep("kesha007@yandex.ru","!qwert12345","Kos May");
        token = stepTestUser.tokenUserStep(new LoginUser(createUser.getEmail(), createUser.getPassword()));
        CreateOrder createOrder = new CreateOrder(new String[]{"34555WRONGHASHc0c5a71d1f82001bdaaa76"});
        stepTestUser.createOrderWrongHashIngredientsStep(createOrder);
    }

    @Test
    @DisplayName("Создание заказа без авторизации и неверным хэшем ингредиентов")
    public void checkCreateOrderNoAuthAndWrongIngredientsTest() {
        CreateOrder createOrder = new CreateOrder(new String[]{"61c1f82001bdaaa6c", "1wronghashwronghash31"});
        stepTestUser.createOrderWrongHashIngredientsStep(createOrder);
    }

    @After
    public void deleteChanges() {
        if(token!=null) {
            executeDelete(token);
        }
    }
}
