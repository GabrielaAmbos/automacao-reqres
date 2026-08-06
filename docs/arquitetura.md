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
│               ├── PostCreateTest.java      # Cenário de criação de usuário
│               └── GetSingleUserTest.java   # Cenário de consulta de usuário
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

## Autenticação

A chave de API é lida da variável de ambiente `REQRES_API_KEY` no `@BeforeClass` de `BaseApiTest`. Se estiver ausente, os testes falham com uma `IllegalStateException` explicando como obter a chave — falha explícita em vez de um `403` genérico difícil de diagnosticar.

A chave nunca é commitada. Ver [Como Executar](como-executar.md#configurando-a-chave-de-api).

## Decisões e limitações atuais

O projeto é pequeno e a estrutura foi mantida proporcional. Pontos que valem registro:

- **Body JSON inline como string**: em `PostCreateTest` o payload é uma string concatenada. POJOs ou arquivos `.json` em `src/test/resources` reduziriam o ruído quando o número de cenários crescer.
- **Sem validação de schema**: as asserções são campo a campo. `JsonSchemaValidator` do REST Assured cobriria o contrato inteiro de uma vez.
- **`target/` e `.idea/` não ignorados**: o `.gitignore` cobre `*.class` e `*.jar`, mas não os diretórios `target/` e `.idea/` — este último inclusive está versionado.
