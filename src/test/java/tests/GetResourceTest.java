package tests;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetResourceTest extends BaseApiTest {

    @Test
    public void shouldListResources() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/unknown")
        .then()
                .statusCode(200)
                .body("page", is(1))
                .body("total", is(12))
                .body("data", hasSize(6))
                .body("data[0].name", is("cerulean"))
                .body("data.pantone_value", everyItem(notNullValue()));
    }

    @Test
    public void shouldGetSingleResource() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/unknown/2")
        .then()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.name", is("fuchsia rose"))
                .body("data.year", is(2001))
                .body("data.color", is("#C74375"))
                .body("data.pantone_value", is("17-2031"));
    }

    @Test
    public void shouldReturnNotFoundForUnknownResource() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/unknown/23")
        .then()
                .statusCode(404);
    }
}
