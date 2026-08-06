package tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class PostRegisterTest extends BaseApiTest {

    // Só e-mails da base fixa do reqres conseguem se registrar
    private static final String REGISTERED_EMAIL = "eve.holt@reqres.in";

    @Test
    public void shouldRegisterUser() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                            "email": "%s",
                            "password": "pistol"
                        }
                        """.formatted(REGISTERED_EMAIL))
        .when()
                .post("/api/register")
        .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("token", is(notNullValue()));
    }

    @Test
    public void shouldRejectRegistrationWithoutPassword() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                            "email": "%s"
                        }
                        """.formatted(REGISTERED_EMAIL))
        .when()
                .post("/api/register")
        .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    public void shouldRejectRegistrationWithoutEmail() {
        given()
                .spec(requestSpec)
                .body("""
                        {
                            "password": "pistol"
                        }
                        """)
        .when()
                .post("/api/register")
        .then()
                .statusCode(400)
                .body("error", is(notNullValue()));
    }
}
