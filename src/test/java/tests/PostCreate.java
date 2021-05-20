package tests;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


public class PostCreate {

    @Test
    public void deveCriarUmUsuario() {
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"name\": \"Gabriela\",\n" +
                        "    \"job\": \"QA\"\n" +
                        "}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("id", is(notNullValue()))
                .body("name", is("Gabriela"))
                .body("job", is("QA"))
                .body("createdAt", is(notNullValue()));

    }

}
