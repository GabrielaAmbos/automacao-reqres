# Como Executar

## Pré-requisitos

- **JDK 15+** instalado e no `PATH`
- **Maven 3.6+**
- Conexão com a internet (os testes batem na API pública reqres.in)

Verificação rápida:

```bash
java -version
mvn -version
```

## Instalando as dependências

```bash
mvn clean install -DskipTests
```

## Executando os testes

Por causa da convenção de nomes das classes, o `mvn test` puro **não executa nada** (ver [Problemas conhecidos](#problemas-conhecidos)). Use a execução explícita:

```bash
mvn test -Dtest='PostCreate,GetSingleUser'
```

Rodando um único cenário:

```bash
mvn test -Dtest=PostCreate
```

### Pela IDE

No IntelliJ IDEA ou Eclipse, basta abrir a classe de teste e executar o método diretamente — a IDE invoca o runner do JUnit sem passar pelo Surefire, então a limitação de nomenclatura não se aplica.

## Relatórios

Após a execução, os resultados ficam em:

```
target/surefire-reports/
├── tests.PostCreate.txt
└── tests.GetSingleUser.txt
```

---

## Problemas conhecidos

### 1. API exige o header `x-api-key` (testes falham com 401)

O reqres.in passou a exigir autenticação por chave de API. Sem o header, qualquer requisição retorna:

```json
{
  "error": "missing_api_key",
  "message": "The x-api-key header is required for this endpoint."
}
```

Isso faz os dois testes atuais falharem com `Expected status code <201> but was <401>` e `Expected status code <200> but was <401>`.

**Como corrigir**: gerar uma chave gratuita em <https://app.reqres.in/api-keys> e adicionar o header nas requisições:

```java
given()
        .header("x-api-key", System.getenv("REQRES_API_KEY"))
        .contentType(ContentType.JSON)
```

O ideal é centralizar isso em um `@Before` ou em um `RequestSpecification` compartilhado, em vez de repetir em cada teste. A chave deve vir de variável de ambiente, nunca commitada no repositório.

### 2. `mvn test` não executa nenhum teste

O Maven Surefire só coleta classes que casem com `Test*`, `*Test`, `*Tests` ou `*TestCase`. As classes `PostCreate` e `GetSingleUser` não casam com nenhum padrão, então o build passa com sucesso sem rodar nada — um falso positivo perigoso em CI.

Há duas formas de resolver:

**Opção A — renomear as classes** (recomendada, segue a convenção da comunidade):

```
PostCreate.java    →  PostCreateTest.java
GetSingleUser.java →  GetSingleUserTest.java
```

**Opção B — configurar o Surefire** no `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <configuration>
                <includes>
                    <include>**/tests/*.java</include>
                </includes>
            </configuration>
        </plugin>
    </plugins>
</build>
```
