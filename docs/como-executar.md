# Como Executar

## Pré-requisitos

- **JDK 15+** instalado e no `PATH`
- **Maven 3.6+**
- Conexão com a internet (os testes batem na API pública reqres.in)
- **Chave de API do reqres** — obrigatória, veja abaixo

Verificação rápida:

```bash
java -version
mvn -version
```

## Configurando a chave de API

O reqres.in exige o header `x-api-key` em todas as requisições. Sem ele a API responde `401`, e com uma chave inválida responde `403`.

1. Gere uma chave gratuita em <https://app.reqres.in/api-keys>
2. Exporte na variável de ambiente `REQRES_API_KEY`:

```bash
export REQRES_API_KEY='sua-chave-aqui'
```

Para tornar permanente, adicione a linha ao seu `~/.zshrc` ou `~/.bashrc`.

A chave **nunca** deve ser commitada no repositório. Se ela não estiver definida, os testes falham imediatamente com uma mensagem explicando o que fazer.

## Instalando as dependências

```bash
mvn clean install -DskipTests
```

## Executando os testes

```bash
mvn test
```

Rodando um único cenário:

```bash
mvn test -Dtest=PostCreateTest
```

Passando a chave apenas para uma execução:

```bash
REQRES_API_KEY='sua-chave-aqui' mvn test
```

### Pela IDE

No IntelliJ IDEA ou Eclipse, execute a classe ou o método diretamente. A variável `REQRES_API_KEY` precisa estar disponível para o processo da IDE — configure em *Run/Debug Configurations → Environment variables* ou exporte antes de abrir a IDE.

## Relatórios

Após a execução, os resultados ficam em:

```
target/surefire-reports/
├── tests.PostCreateTest.txt
└── tests.GetSingleUserTest.txt
```

---

## Problemas conhecidos

### Asserções do GET dependem de dados fixos

`GetSingleUserTest` valida os dados do usuário de id 3 (`Emma Wong`). Esses valores vêm da base estática do reqres — se a API alterar o registro, o teste quebra por motivo alheio ao código.

### `target/` e `.idea/` não estão no `.gitignore`

O `.gitignore` cobre `*.class` e `*.jar`, mas não os diretórios. O `.idea/` inclusive está versionado.
