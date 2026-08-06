package tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class PostCreateTest extends BaseApiTest {

    @Test
    public void shouldCreateUser() {
        given()
                .spec(requestSpec)
                .body("{\n"
                        + "    \"name\": \"Gabriela\",\n"
                        + "    \"job\": \"QA\"\n"
                        + "}")
        .when()
                .post("/api/users")
        .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Gabriela"))
                .body("job", is("QA"))
                .body("createdAt", is(notNullValue()));
    }
}
