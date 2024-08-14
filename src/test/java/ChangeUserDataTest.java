import org.example.ChangeUser;
import org.example.CreateUser;
import org.example.LoginUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.UserController.*;

public class ChangeUserDataTest {
    private StepTest stepTestUser = new StepTest();
    private static CreateUser createUser;
    private static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        createUser = new CreateUser("winner001@yandex.ru","QWER!12345","Igor Win");
        executeCreate(createUser);
        token = getUserToken(new LoginUser(createUser.getEmail(), createUser.getPassword()));
    }

    @Test
    @DisplayName("Изменение e-mail пользователя с авторизацией")
    public void checkSuccessChangeUserEmailAuthTest() {
        ChangeUser changeUser = new ChangeUser("nagibator777@yandex.ru", createUser.getName());
        LoginUser loginUser = stepTestUser.loginUserStep(createUser.getEmail(), createUser.getPassword());
        stepTestUser.changeAuthUserStep(createUser, loginUser, changeUser);
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    public void checkSuccessChangeUserNameAuthTest() {
        ChangeUser changeUser = new ChangeUser(createUser.getEmail(), "Tom Redl");
        LoginUser loginUser = stepTestUser.loginUserStep(createUser.getEmail(), createUser.getPassword());
        stepTestUser.changeAuthUserStep(createUser, loginUser, changeUser);
    }

    @Test
    @DisplayName("Изменение e-mail пользователя без авторизации")
    public void changeUserEmailNoAuthTest() {
        ChangeUser changeUser = new ChangeUser("nagibator777@yandex.ru", createUser.getName());
        LoginUser loginUser = stepTestUser.loginUserStep(createUser.getEmail(), createUser.getPassword());
        stepTestUser.changeFailAuthUserStep(createUser, loginUser, changeUser);
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void changeUserNameNoAuthTest() {
        ChangeUser changeUser = new ChangeUser(createUser.getEmail(), "Tom");
        LoginUser loginUser = stepTestUser.loginUserStep(createUser.getEmail(), createUser.getPassword());
        stepTestUser.changeFailAuthUserStep(createUser, loginUser, changeUser);
    }

    @After
    public void deleteChanges() {
        executeDelete(token);
    }
}