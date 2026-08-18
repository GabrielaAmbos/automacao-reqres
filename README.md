<div align="right">
  <a href="README.md"><img src="https://flagcdn.com/24x18/br.png" alt="Português (Brasil)" title="Português (Brasil)"></a>
  &nbsp;
  <a href="README.en.md"><img src="https://flagcdn.com/24x18/us.png" alt="English" title="English"></a>
</div>

[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=18&pause=1000&color=DF62F7&width=435&lines=Projeto+de+Automa%C3%A7%C3%A3o+Reqres)](https://git.io/typing-svg)

Automação de testes de API REST sobre o serviço público [reqres.in](https://reqres.in/), em Java com [REST Assured](https://rest-assured.io/) e JUnit 4.

São **19 cenários** cobrindo todos os endpoints públicos da API, incluindo os caminhos de erro.

## Stack

| Ferramenta | Versão |
|---|---|
| Java | 15+ |
| Maven | 3.6+ |
| REST Assured | 4.3.3 |
| JUnit | 4.13.2 |

## Como rodar

O reqres.in usa autenticação por chave de API. Gere uma chave gratuita em [app.reqres.in/api-keys](https://app.reqres.in/api-keys) e exporte antes de rodar:

```bash
export REQRES_API_KEY='sua-chave-aqui'
mvn test
```

Rodando um cenário específico:

```bash
mvn test -Dtest=PostCreateTest
```

A chave nunca é commitada — se a variável não estiver definida, os testes falham com uma mensagem explicando o que fazer.

## Cobertura

| Endpoint | Cenários |
|---|---|
| `GET /api/users` | página padrão, página 2, página vazia, `delay=3` |
| `GET /api/users/{id}` | usuário existente, usuário inexistente (404) |
| `POST /api/users` | criação (201) |
| `PUT /api/users/{id}` | substituição (200) |
| `PATCH /api/users/{id}` | atualização parcial (200) |
| `DELETE /api/users/{id}` | exclusão (204) |
| `GET /api/unknown` | listagem, item único, item inexistente (404) |
| `POST /api/register` | sucesso, sem senha (400), sem e-mail (400) |
| `POST /api/login` | sucesso, sem senha (400), sem e-mail (400) |

Detalhes de cada cenário em [docs/casos-de-teste.md](docs/casos-de-teste.md).

## Estrutura

Uma classe de teste por endpoint. `BaseApiTest` centraliza a configuração comum — URL base, content-type e o header `x-api-key`:

```
src/test/java/tests/
├── BaseApiTest.java         # Spec compartilhada
├── GetUserListTest.java
├── GetSingleUserTest.java
├── PostCreateTest.java
├── PutUpdateTest.java
├── PatchUpdateTest.java
├── DeleteUserTest.java
├── GetResourceTest.java
├── PostRegisterTest.java
└── PostLoginTest.java
```

## Documentação

| Documento | Conteúdo |
|---|---|
| [Visão geral](docs/README.md) | Índice e status do projeto |
| [Arquitetura](docs/arquitetura.md) | Stack, estrutura, decisões e limitações |
| [Casos de teste](docs/casos-de-teste.md) | Todos os cenários com requisições e asserções |
| [Como executar](docs/como-executar.md) | Setup, comandos e problemas conhecidos |

> A documentação em `docs/` está disponível apenas em português.
