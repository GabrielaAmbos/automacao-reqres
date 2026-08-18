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

A suíte tem 19 testes e leva cerca de 10 segundos — o `GetUserListTest` sozinho gasta 3s no cenário de `delay`.

## Relatórios

Após a execução, os resultados de cada classe ficam em `target/surefire-reports/`, um arquivo `.txt` e um `.xml` por classe de teste.

---

## Problemas conhecidos

### Asserções dependem de dados fixos

Vários testes validam registros da base estática do reqres — `Emma Wong` (usuário 3), `fuchsia rose` (recurso 2), os totais de paginação. Se a API alterar esses dados, os testes quebram por motivo alheio ao código.

### Exigência da chave é intermitente

Em 06/08/2026 a API alternou entre bloquear requisições sem chave (`401`) e respondê-las normalmente (`200`) no intervalo de poucos minutos. O header continua sendo enviado sempre — é o comportamento correto e funciona nos dois casos —, mas não há testes assertando `401`/`403`, que seriam instáveis.

### `.idea/` ainda versionado

O diretório já está no `.gitignore`, mas o ignore não vale para arquivos que já eram rastreados. Para removê-lo do repositório: `git rm -r --cached .idea`.
