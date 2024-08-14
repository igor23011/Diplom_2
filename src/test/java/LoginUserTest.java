import org.example.CreateUser;
import org.example.LoginUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.UserController.*;

public class LoginUserTest {
    private StepTest stepTestUser = new StepTest();
    private static CreateUser createUser;
    private static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        createUser = new CreateUser("micha007@yandex.ru","qwerty12345","Micha Ivanov");
        executeCreate(createUser);
        token = getUserToken(new LoginUser(createUser.getEmail(), createUser.getPassword()));
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void checkSuccessSingleTest() {
        LoginUser loginUser = stepTestUser.loginUserStep("micha007@yandex.ru","qwerty12345");
        stepTestUser.authResponseUserAutStep(loginUser, createUser);
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    public void checkFailLoginUserTest() {
        LoginUser loginUserFail = stepTestUser.loginUserStep("micha007@yandexxxxx.ru","qwerty12345");
        stepTestUser.authFailUserStep(loginUserFail);
    }
    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void checkFailPasswordUserTest() {
        LoginUser loginUserFail = stepTestUser.loginUserStep("micha007@yandex.ru","QW12345");
        stepTestUser.authFailUserStep(loginUserFail);
    }

    @After
    public void deleteChanges() {
        executeDelete(token);
    }
}