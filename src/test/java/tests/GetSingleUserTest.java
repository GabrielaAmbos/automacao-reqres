package tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class GetSingleUserTest extends BaseApiTest {

    @Test
    public void shouldGetExistingUser() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/users/3")
        .then()
                .statusCode(200)
                .body("data.id", is(3))
                .body("data.email", is("emma.wong@reqres.in"))
                .body("data.first_name", is("Emma"))
                .body("data.last_name", is("Wong"))
                .body("data.avatar", is("https://reqres.in/img/faces/3-image.jpg"));
    }
}
