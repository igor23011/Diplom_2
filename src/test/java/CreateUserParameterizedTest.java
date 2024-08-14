import org.example.CreateUser;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.example.UserController.executeCreate;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class CreateUserParameterizedTest {
    private final CreateUser userFail;

    public CreateUserParameterizedTest(CreateUser userFail) {
        this.userFail = userFail;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Parameterized.Parameters
    public static Object[][] getCreateUserVariable() {
        return new Object[][] {
                {new CreateUser("ivan777@gm.com","","Ivan Ivanov")},
                {new CreateUser("","qwerty12345","Ivan Ivanov")},
                {new CreateUser("ivan777@gm.com","qwerty12345","")},
        };
    }
    @Test
    @DisplayName("Создание пользователя без заполнения одного из обязательных полей")
    public void checkFailCreateUserTest() {
        Response response = executeCreate(userFail);
        response.then().assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }
}