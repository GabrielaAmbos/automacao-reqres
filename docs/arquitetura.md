# Arquitetura

## Stack

| Componente | Versão | Papel |
|---|---|---|
| Java | source/target 15 | Linguagem (definido em `pom.xml`) |
| Maven | 3.x | Build e gerenciamento de dependências |
| REST Assured | 4.3.3 | DSL para requisições HTTP e validação de respostas |
| JUnit | 4.13.2 | Runner e ciclo de vida dos testes |
| Hamcrest | transitivo | Matchers de asserção (`is`, `notNullValue`) |

O Hamcrest não é declarado explicitamente no `pom.xml` — vem como dependência transitiva do REST Assured.

## Estrutura de pastas

```
desafio-automacao-reqres/
├── docs/                          # Documentação do projeto
├── src/
│   └── test/
│       └── java/
│           └── tests/
│               ├── BaseApiTest.java         # Spec compartilhada: baseURI, content-type, x-api-key
│               ├── GetUserListTest.java     # GET /api/users — paginação e delay
│               ├── GetSingleUserTest.java   # GET /api/users/{id} — sucesso e 404
│               ├── PostCreateTest.java      # POST /api/users
│               ├── PutUpdateTest.java       # PUT /api/users/{id}
│               ├── PatchUpdateTest.java     # PATCH /api/users/{id}
│               ├── DeleteUserTest.java      # DELETE /api/users/{id}
│               ├── GetResourceTest.java     # GET /api/unknown — lista, item e 404
│               ├── PostRegisterTest.java    # POST /api/register
│               └── PostLoginTest.java       # POST /api/login
├── target/                        # Artefatos de build (gerado)
├── pom.xml                        # Definição do projeto Maven
├── CLAUDE.md                      # Contexto para assistentes de IA
└── README.md
```

Não existe `src/main` — o projeto é exclusivamente de testes, então todas as dependências estão com `<scope>test</scope>`.

## Padrão dos testes

`BaseApiTest` centraliza o que é comum a todos os cenários — URL base, content-type e o header `x-api-key` — expondo um `RequestSpecification` compartilhado. Cada teste herda dela e declara só o que é próprio do cenário:

```java
public class GetSingleUserTest extends BaseApiTest {

    @Test
    public void shouldGetExistingUser() {
        given()
                .spec(requestSpec)       // Preparação herdada da base
        .when()
                .get("/api/users/3")     // Ação: verbo HTTP + path relativo
        .then()
                .statusCode(200)         // Verificação: status code + corpo
                .body("data.id", is(3));
    }
}
```

As asserções sobre o corpo usam **GPath** (sintaxe do Groovy) para navegar no JSON: `data.id`, `data.first_name`, etc.

Cada classe cobre um endpoint, o que mantém a suíte rastreável: para saber o que valida o `DELETE`, abre-se `DeleteUserTest`. Os payloads usam *text blocks* do Java 15, que preservam a forma do JSON sem concatenação.

## Autenticação

A chave de API é lida da variável de ambiente `REQRES_API_KEY` no `@BeforeClass` de `BaseApiTest`. Se estiver ausente, os testes falham com uma `IllegalStateException` explicando como obter a chave — falha explícita em vez de um `403` genérico difícil de diagnosticar.

A chave nunca é commitada. Ver [Como Executar](como-executar.md#configurando-a-chave-de-api).

## Decisões e limitações atuais

O projeto é pequeno e a estrutura foi mantida proporcional. Pontos que valem registro:

- **Body JSON inline**: os payloads são text blocks dentro dos testes. POJOs ou arquivos `.json` em `src/test/resources` fariam sentido se os corpos crescessem, mas hoje seriam indireção desnecessária.
- **Sem validação de schema**: as asserções são campo a campo. `JsonSchemaValidator` do REST Assured cobriria o contrato inteiro de uma vez, ao custo de uma dependência a mais.
- **Asserções sobre dados fixos**: valores como `Emma Wong` (usuário 3) e `fuchsia rose` (recurso 2) vêm da base estática do reqres. Se a API alterar esses registros, os testes quebram por motivo alheio ao código.
- **Testes de erro de autenticação ausentes**: o `401`/`403` do reqres é intermitente e tornaria a suíte instável. Ver [Casos de Teste](casos-de-teste.md#fora-de-cobertura).
- **`target/` e `.idea/` não ignorados**: o `.gitignore` cobre `*.class` e `*.jar`, mas não os diretórios `target/` e `.idea/` — este último inclusive está versionado.
