# CLAUDE.md

Contexto do projeto para assistentes de IA.

## O que é

Projeto de automação de testes de API REST contra o serviço público [reqres.in](https://reqres.in/). Java + Maven + REST Assured + JUnit 4. Não há código de produção — só `src/test`.

Documentação completa em [docs/](docs/README.md).

## Stack

- Java 15 (source/target no `pom.xml`; ambiente local roda JDK 21)
- Maven 3.9
- REST Assured 4.3.3
- JUnit 4.13.2
- Hamcrest (transitivo do REST Assured)

## Estrutura

```
src/test/java/tests/
├── BaseApiTest.java         # Spec compartilhada: baseURI, content-type, x-api-key
├── PostCreateTest.java      # POST /api/users
└── GetSingleUserTest.java   # GET /api/users/3
```

## Comandos

```bash
export REQRES_API_KEY='...'   # obrigatório — chave gratuita em app.reqres.in/api-keys
mvn test                      # roda os testes
mvn test -Dtest=PostCreateTest
```

## Estado conhecido (verificado em 06/08/2026)

1. **A API exige `x-api-key`.** Sem header → `401`; chave inválida → `403`. A chave é lida de `REQRES_API_KEY` no `@BeforeClass` de `BaseApiTest`, que falha com `IllegalStateException` explicativa se a variável estiver ausente. **Nunca commitar a chave.**
2. **Asserções do GET dependem de dados fixos** do reqres (usuário id 3 = Emma Wong) — quebram se a API alterar o registro.
3. **`.gitignore` incompleto**: `target/` e `.idea/` não estão ignorados; `.idea/` está inclusive versionado.

## Convenções

- **Código em inglês** (variáveis, métodos, classes, arquivos).
- **Comentários em português**, e apenas quando explicam decisão de negócio.
- **Documentação em português.**
- **Commits em português**, padrão Conventional Commits: `feat(...)`, `fix(...)`, `chore(...)`.
- Trabalhar direto na `main`, salvo pedido explícito.
- Aplicar SOLID e evitar over engineering — o projeto é pequeno e a estrutura deve permanecer proporcional.

## Padrão dos testes

Classes estendem `BaseApiTest` e usam o `requestSpec` herdado. DSL Given-When-Then do REST Assured, com asserções GPath sobre o corpo JSON e paths relativos:

```java
public class GetSingleUserTest extends BaseApiTest {

    @Test
    public void shouldGetExistingUser() {
        given()
                .spec(requestSpec)
        .when()
                .get("/api/users/3")
        .then()
                .statusCode(200)
                .body("data.id", is(3));
    }
}
```
