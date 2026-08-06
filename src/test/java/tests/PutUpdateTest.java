package tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PutUpdateTest extends BaseApiTest {

    @Test
    public void shouldReplaceUser() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                            "name": "Gabriela",
                            "job": "QA Lead"
                        }
                        """)
        .when()
                .put("/api/users/2")
        .then()
                .statusCode(200)
                .body("name", is("Gabriela"))
                .body("job", is("QA Lead"))
                .body("updatedAt", is(notNullValue()));
    }
}
