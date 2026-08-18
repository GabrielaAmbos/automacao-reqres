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

Uma classe por endpoint, 19 testes no total:

```
src/test/java/tests/
├── BaseApiTest.java         # Spec compartilhada: baseURI, content-type, x-api-key
├── GetUserListTest.java     # GET /api/users — paginação e delay
├── GetSingleUserTest.java   # GET /api/users/{id} — sucesso e 404
├── PostCreateTest.java      # POST /api/users
├── PutUpdateTest.java       # PUT /api/users/{id}
├── PatchUpdateTest.java     # PATCH /api/users/{id}
├── DeleteUserTest.java      # DELETE /api/users/{id}
├── GetResourceTest.java     # GET /api/unknown — lista, item e 404
├── PostRegisterTest.java    # POST /api/register
└── PostLoginTest.java       # POST /api/login
```

## Comandos

```bash
export REQRES_API_KEY='...'   # obrigatório — chave gratuita em app.reqres.in/api-keys
mvn test                      # roda os testes
mvn test -Dtest=PostCreateTest
```

## Estado conhecido (verificado em 06/08/2026)

1. **Suíte verde**: `Tests run: 19, Failures: 0, Errors: 0`, estável em execuções consecutivas.
2. **A API usa `x-api-key`.** A chave é lida de `REQRES_API_KEY` no `@BeforeClass` de `BaseApiTest`, que falha com `IllegalStateException` explicativa se a variável estiver ausente. **Nunca commitar a chave.**
3. **A exigência da chave é intermitente**: no mesmo dia a API alternou entre `401` sem header e `200` sem header. Por isso não há testes assertando `401`/`403` — seriam flaky. O header continua sendo enviado sempre.
4. **Asserções dependem de dados fixos** do reqres (usuário 3 = Emma Wong, recurso 2 = fuchsia rose, `total` = 12) — quebram se a API alterar os registros.
5. **`.idea/` ainda versionado**: já está no `.gitignore`, mas o ignore não afeta o que já era rastreado. Remover exige `git rm -r --cached .idea` — não feito porque impacta quem já clonou.

## Convenções

- **Código em inglês** (variáveis, métodos, classes, arquivos).
- **Comentários em português**, e apenas quando explicam decisão de negócio.
- **Documentação em português.** Exceção: o README é bilíngue e o **inglês é a versão
  principal** — `README.md` (EN, exibido pelo GitHub na home) e `README.pt-BR.md` (PT),
  com bandeiras no topo linkando um para o outro. Ao alterar um, **atualizar o outro na
  mesma edição**, senão as versões divergem.
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
