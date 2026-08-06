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
├── PostCreate.java      # POST /api/users
└── GetSingleUser.java   # GET /api/users/3
```

## Comandos

```bash
mvn test -Dtest='PostCreate,GetSingleUser'   # roda os testes
mvn test -Dtest=PostCreate                   # roda um cenário
```

`mvn test` sozinho **não roda nada** — as classes não casam com os padrões de nome do Surefire (`*Test`, `Test*`, `*Tests`, `*TestCase`).

## Estado conhecido (verificado em 06/08/2026)

1. **Os dois testes falham com 401.** O reqres.in passou a exigir o header `x-api-key`. Correção: chave gratuita em app.reqres.in + header nas requisições, lida de variável de ambiente.
2. **`mvn test` é um falso positivo**: build passa com sucesso sem executar teste algum.
3. **`.gitignore` incompleto**: `target/` e `.idea/` não estão ignorados; `.idea/` está inclusive versionado.

## Convenções

- **Código em inglês** (variáveis, métodos, classes, arquivos). Os métodos existentes estão em português (`deveCriarUmUsuario`) por legado — não replicar em código novo.
- **Comentários em português**, e apenas quando explicam decisão de negócio.
- **Documentação em português.**
- **Commits em português**, padrão Conventional Commits: `feat(...)`, `fix(...)`, `chore(...)`.
- Trabalhar direto na `main`, salvo pedido explícito.
- Aplicar SOLID e evitar over engineering — o projeto é pequeno e a estrutura deve permanecer proporcional.

## Padrão dos testes

DSL Given-When-Then do REST Assured, com asserções GPath sobre o corpo JSON:

```java
given()
        .contentType(ContentType.JSON)
.when()
        .get("https://reqres.in/api/users/3")
.then()
        .statusCode(200)
        .body("data.id", is(3));
```

URL base hardcoded em cada teste — candidata a extração para `RestAssured.baseURI` num setup compartilhado quando o número de cenários crescer.
