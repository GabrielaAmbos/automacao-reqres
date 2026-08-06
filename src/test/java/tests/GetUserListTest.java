package tests;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetUserListTest extends BaseApiTest {

    @Test
    public void shouldGetFirstPageByDefault() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/users")
        .then()
                .statusCode(200)
                .body("page", is(1))
                .body("data", hasSize(6))
                .body("data.id", everyItem(notNullValue()))
                .body("data.email", everyItem(notNullValue()));
    }

    @Test
    public void shouldGetSecondPage() {
        given()
                .spec(requestSpec)
                .queryParam("page", 2)
        .when()
                .get("/api/users")
        .then()
                .statusCode(200)
                .body("page", is(2))
                .body("per_page", is(6))
                .body("total", is(12))
                .body("total_pages", is(2))
                .body("data", hasSize(6))
                .body("data[0].id", is(7));
    }

    @Test
    public void shouldReturnEmptyListForPageBeyondTotal() {
        given()
                .spec(requestSpec)
                .queryParam("page", 99)
        .when()
                .get("/api/users")
        .then()
                .statusCode(200)
                .body("data", hasSize(0));
    }

    @Test
    public void shouldRespectDelayParameter() {
        given()
                .spec(requestSpec)
                .queryParam("delay", 3)
        .when()
                .get("/api/users")
        .then()
                .statusCode(200)
                .time(greaterThanOrEqualTo(3L), TimeUnit.SECONDS)
                .body("data", hasSize(6));
    }
}
