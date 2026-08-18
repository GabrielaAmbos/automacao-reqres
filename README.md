<div align="right">
  <a href="README.pt-BR.md"><img src="https://flagcdn.com/24x18/br.png" alt="Português (Brasil)" title="Português (Brasil)"></a>
  &nbsp;
  <a href="README.md"><img src="https://flagcdn.com/24x18/us.png" alt="English" title="English"></a>
</div>

[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=18&pause=1000&color=DF62F7&width=435&lines=Reqres+Test+Automation+Project)](https://git.io/typing-svg)

REST API test automation against the public [reqres.in](https://reqres.in/) service, written in Java with [REST Assured](https://rest-assured.io/) and JUnit 4.

**19 scenarios** covering every public endpoint of the API, error paths included.

## Stack

| Tool | Version |
|---|---|
| Java | 15+ |
| Maven | 3.6+ |
| REST Assured | 4.3.3 |
| JUnit | 4.13.2 |

## Running the tests

reqres.in requires API key authentication. Get a free key at [app.reqres.in/api-keys](https://app.reqres.in/api-keys) and export it before running:

```bash
export REQRES_API_KEY='your-key-here'
mvn test
```

Running a single scenario:

```bash
mvn test -Dtest=PostCreateTest
```

The key is never committed — if the variable is not set, the tests fail with a message explaining what to do.

## Coverage

| Endpoint | Scenarios |
|---|---|
| `GET /api/users` | default page, page 2, empty page, `delay=3` |
| `GET /api/users/{id}` | existing user, non-existing user (404) |
| `POST /api/users` | creation (201) |
| `PUT /api/users/{id}` | replacement (200) |
| `PATCH /api/users/{id}` | partial update (200) |
| `DELETE /api/users/{id}` | deletion (204) |
| `GET /api/unknown` | list, single item, non-existing item (404) |
| `POST /api/register` | success, missing password (400), missing email (400) |
| `POST /api/login` | success, missing password (400), missing email (400) |

Each scenario is detailed in [docs/casos-de-teste.md](docs/casos-de-teste.md) (Portuguese).

## Structure

One test class per endpoint. `BaseApiTest` holds the shared setup — base URL, content type and the `x-api-key` header:

```
src/test/java/tests/
├── BaseApiTest.java         # Shared request spec
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

## Documentation

| Document | Contents |
|---|---|
| [Overview](docs/README.md) | Index and project status |
| [Architecture](docs/arquitetura.md) | Stack, structure, decisions and limitations |
| [Test cases](docs/casos-de-teste.md) | Every scenario with requests and assertions |
| [How to run](docs/como-executar.md) | Setup, commands and known issues |

> The documentation under `docs/` is available in Portuguese only.
