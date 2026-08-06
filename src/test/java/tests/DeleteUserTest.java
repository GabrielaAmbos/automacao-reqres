package tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

public class DeleteUserTest extends BaseApiTest {

    @Test
    public void shouldDeleteUser() {
        given()
                .spec(requestSpec)
        .when()
                .delete("/api/users/2")
        .then()
                .statusCode(204)
                .body(is(emptyString()));
    }
}
