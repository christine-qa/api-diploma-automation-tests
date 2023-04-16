import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.stellarburgers.apiObjects.SuccessOrderCreationResponse;
import ru.yandex.stellarburgers.apiUtils.BaseSetUp;
import ru.yandex.stellarburgers.apiUtils.OrderApi;
import ru.yandex.stellarburgers.apiUtils.UserApi;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest {
    String[] ingredients = {"61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa71"};
    String[] noIngredients = {};
    String[] wrongIngredients = {"acaaaaaaaabdaaaa"};
    String email = "qwerty@qwe.ru";
    String password = "2345";
    String name = "simpson";

    @Before
    public void setUp() {
        BaseSetUp.setUp();

    }
    @Test
    @DisplayName("Check status code of POST /api/orders without auth")
    public void createOrderWithoutAuthShouldReturn200Test() {
        Response response = OrderApi.createOrderWithoutAuth(ingredients);
        response.then().assertThat().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Check body structure of POST /api/orders without auth")
    public void createOrderWithoutAuthShouldReturnValidBodyTest() {
        Response response = OrderApi.createOrderWithoutAuth(ingredients);
        response.body().as(SuccessOrderCreationResponse.class);
    }
    @Test
    @DisplayName("Check status code of POST /api/orders with auth")
    public void createOrderWithAuthShouldReturn200Test() {
        Response response = OrderApi.createOrderWithAuth(ingredients, email, password, name);
        response.then().assertThat().statusCode(SC_OK);
    }
    @Test
    @DisplayName("Check body structure code of POST /api/orders with auth")
    public void createOrderWithAuthShouldReturnValidBodyTest() {
        Response response = OrderApi.createOrderWithAuth(ingredients, email, password, name);
        response.body().as(SuccessOrderCreationResponse.class);
    }
    @Test
    @DisplayName("Check status code and body of POST /api/orders without auth")
    public void createOrderWithIngredientsShouldReturn200AndValidBurgerNameTest() {
        Response response = OrderApi.createOrderWithoutAuth(ingredients);
        response.then().assertThat().statusCode(SC_OK)
                .and().body("name", equalTo("Био-марсианский бессмертный бургер"));
    }
    @Test
    @DisplayName("Check status code and body of POST /api/orders without ingredients")
    public void createOrderWithoutIngredientsShouldReturn400AndValidMessageTest() {
        Response response = OrderApi.createOrderWithoutAuth(noIngredients);
        response.then().assertThat().statusCode(SC_BAD_REQUEST)
                .and().body("message", equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Check status code and body of POST /api/orders with wrong ingredient")
    public void createOrderWithWrongIngredientHashShouldReturn500Test() {
        Response response = OrderApi.createOrderWithoutAuth(wrongIngredients);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }
    @After
    public void tearDown() {
        try {
            UserApi.deleteUser(email, password);
        } catch (IllegalArgumentException exception) {
            System.out.println("Невозможно удалить несуществующего пользователя!");
        }
    }
}
