package tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;

public abstract class BaseApiTest {

    private static final String BASE_URI = "https://reqres.in";
    private static final String API_KEY_ENV = "REQRES_API_KEY";

    protected static RequestSpecification requestSpec;

    @BeforeClass
    public static void setUpRequestSpec() {
        String apiKey = System.getenv(API_KEY_ENV);

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Variável de ambiente " + API_KEY_ENV + " não definida. "
                            + "Gere uma chave gratuita em https://app.reqres.in/api-keys "
                            + "e exporte antes de rodar os testes.");
        }

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setContentType(ContentType.JSON)
                .addHeader("x-api-key", apiKey)
                .build();
    }
}
