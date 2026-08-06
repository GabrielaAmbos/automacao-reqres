package tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PatchUpdateTest extends BaseApiTest {

    @Test
    public void shouldUpdateUserJobOnly() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                            "job": "QA Manager"
                        }
                        """)
        .when()
                .patch("/api/users/2")
        .then()
                .statusCode(200)
                .body("job", is("QA Manager"))
                .body("updatedAt", is(notNullValue()));
    }
}
