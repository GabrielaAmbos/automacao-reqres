package tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostLoginTest extends BaseApiTest {

    // Só e-mails da base fixa do reqres conseguem autenticar
    private static final String REGISTERED_EMAIL = "eve.holt@reqres.in";

    @Test
    public void shouldLoginUser() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                            "email": "%s",
                            "password": "cityslicka"
                        }
                        """.formatted(REGISTERED_EMAIL))
        .when()
                .post("/api/login")
        .then()
                .statusCode(200)
                .body("token", is(notNullValue()));
    }

    @Test
    public void shouldRejectLoginWithoutPassword() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                            "email": "%s"
                        }
                        """.formatted(REGISTERED_EMAIL))
        .when()
                .post("/api/login")
        .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    public void shouldRejectLoginWithoutEmail() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                            "password": "cityslicka"
                        }
                        """)
        .when()
                .post("/api/login")
        .then()
                .statusCode(400)
                .body("error", is(notNullValue()));
    }
}
