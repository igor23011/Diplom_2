package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.example.OrderController.executeGetOrder;
import static org.example.OrderController.executeMakeOrder;
import static org.example.UserController.*;
import static org.hamcrest.Matchers.*;

public class StepTest {

    @Step("Регистрация нового пользователя")
    public CreateUser createUserStep(String email, String password, String name) {
        CreateUser createCourier = new CreateUser(email, password, name);
        executeCreate(createCourier);
        return createCourier;
    }

    @Step("Авторизация созданного пользователя")
    public LoginUser loginUserStep(String email, String password) {
        LoginUser loginUser = new LoginUser(email, password);
        executeLogin(loginUser);
        return loginUser;
    }

    @Step("Получение токена авторизованного пользователя")
    public String tokenUserStep(LoginUser loginUser) {
        return getUserToken(loginUser);
    }

    @Step("Успешная регистрация и авторизации пользователя")
    public void createAndAutResponseUserStep(CreateUser createUser, LoginUser loginUser) {
        if (executeLogin(loginUser).getStatusCode() == SC_OK) {
        } else {
            Response response = executeCreate(createUser);
            response.then().assertThat()
                    .body("success", equalTo(true))
                    .and()
                    .body("user.email", equalTo(createUser.getEmail()))
                    .and()
                    .body("user.name", equalTo(createUser.getName()))
                    .and()
                    .body("accessToken", startsWith("Bearer"))
                    .and()
                    .body("refreshToken", notNullValue())
                    .and()
                    .statusCode(SC_OK);
        }
    }

    @Step("Регистрации существующего пользователя")
    public void createFailUserDoubleStep(CreateUser createUser) {
        executeCreate(createUser);
        Response response = executeCreate(createUser);
        response.then().assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Step("Авторизация пользователя валидными данными")
    public void authResponseUserAutStep(LoginUser loginUser, CreateUser createUser) {
        Response response = executeLogin(loginUser);
        response.then().assertThat()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(loginUser.getEmail()))
                .and()
                .body("user.name", equalTo(createUser.getName()))
                .and()
                .body("accessToken", startsWith("Bearer"))
                .and()
                .body("refreshToken", notNullValue())
                .statusCode(SC_OK);
    }

    @Step("Авторизация пользователя невалидными данными")
    public void authFailUserStep(LoginUser userFail) {
        Response response = executeLogin(userFail);
        response.then().assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Step("Изменение данных пользователя с авторизацией")
    public void changeAuthUserStep(CreateUser createUser, LoginUser loginUser, ChangeUser changeUser) {
        Response response = executeChangeUserData(loginUser, changeUser, true);
        response.then().assertThat()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(changeUser.getEmail()))
                .and()
                .body("user.name", equalTo(changeUser.getName()))
                .and()
                .statusCode(SC_OK);
    }

    @Step("Изменение данных пользователя без авторизации")
    public void changeFailAuthUserStep(CreateUser createUser, LoginUser loginUser, ChangeUser changeUserEmail) {
        Response response = executeChangeUserData(loginUser, changeUserEmail, false);
        response.then().assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Step("Cоздание заказа с ингридиентами")
    public void createOrderStep(CreateOrder createOrder) {
        Response response = executeMakeOrder(createOrder);
        response.then().assertThat()
                .body("name", notNullValue())
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Step("Cоздание заказа без ингридиентов")
    public void createOrderNoIngredientsStep(CreateOrder createOrder) {
        Response response = executeMakeOrder(createOrder);
        response.then().assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Step("Cоздание заказа c неверным хэшем ингредиентов")
    public void createOrderWrongHashIngredientsStep(CreateOrder createOrder) {
        Response response = executeMakeOrder(createOrder);
        response.then().assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public void getOrdersAuthUserTestStep(String token, boolean useAuth) {
        Response response = executeGetOrder(token, true);
        response.then().assertThat()
                .body("success", equalTo(true))
                .and()
                .body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Step("Получение списка заказов неавторизованного пользователя")
    public void getOrdersNoAuthUserTestStep(String token, boolean useAuth) {
        Response response = executeGetOrder(token, false);
        response.then().assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }
}