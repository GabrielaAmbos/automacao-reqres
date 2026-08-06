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
│               ├── PostCreate.java      # Cenário de criação de usuário
│               └── GetSingleUser.java   # Cenário de consulta de usuário
├── target/                        # Artefatos de build (gerado)
├── pom.xml                        # Definição do projeto Maven
├── CLAUDE.md                      # Contexto para assistentes de IA
└── README.md
```

Não existe `src/main` — o projeto é exclusivamente de testes, então todas as dependências estão com `<scope>test</scope>`.

## Padrão dos testes

Cada cenário é uma classe com um método `@Test` que segue a DSL Given-When-Then do REST Assured:

```java
given()                              // Preparação: headers, body, content-type
        .contentType(ContentType.JSON)
.when()                              // Ação: verbo HTTP + URL
        .get("https://reqres.in/api/users/3")
.then()                              // Verificação: status code + corpo
        .statusCode(200)
        .body("data.id", is(3));
```

As asserções sobre o corpo usam **GPath** (sintaxe do Groovy) para navegar no JSON: `data.id`, `data.first_name`, etc.

## Decisões e limitações atuais

O projeto está no estágio inicial, com estrutura mínima intencional. Pontos que valem registro:

- **Sem camada de configuração**: a URL base está hardcoded em cada teste. Uma evolução natural é extrair `RestAssured.baseURI` para uma classe de setup ou `@Before`.
- **Body JSON inline como string**: em `PostCreate` o payload é uma string concatenada. POJOs ou arquivos `.json` em `src/test/resources` reduziriam o ruído.
- **Nomes de métodos em português** (`deveCriarUmUsuario`): mantidos como estão para preservar o histórico; novos códigos devem seguir o padrão em inglês.
- **Nomes de classes fora da convenção do Surefire**: `PostCreate` e `GetSingleUser` não casam com os padrões `*Test`, `Test*` ou `*Tests`, o que faz o `mvn test` não executar nenhum teste. Ver [Como Executar](como-executar.md#problemas-conhecidos).
- **`target/` e `.idea/` não ignorados**: o `.gitignore` cobre `*.class` e `*.jar`, mas não os diretórios `target/` e `.idea/` — este último inclusive está versionado.
