import org.example.CreateUser;
import org.example.LoginUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.example.StepTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.UserController.*;

public class CreateUserTest {

    private StepTest stepTestUser = new StepTest();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Создать нового пользователя")
    public void checkCreateSingleTest() {
        CreateUser createUser = stepTestUser.createUserStep("micha007@yandex.ru", "12345", "Micha Ivanov");
        LoginUser loginUser = stepTestUser.loginUserStep(createUser.getEmail(), createUser.getPassword());
        stepTestUser.createAndAutResponseUserStep(createUser, loginUser);
    }

    @Test
    @DisplayName("Создание пользователя, который ранее был зарегистрирован")
    public void checkFailUserDoubleTest() {
            CreateUser createUser = stepTestUser.createUserStep("micha007@yandex.ru", "12345", "Micha Ivanov");
            stepTestUser.createFailUserDoubleStep(createUser);
    }

    @After
    public void deleteChanges() {
        LoginUser loginUser = new LoginUser("micha007@yandex.ru","12345");
        executeDelete(loginUser);
    }
}
