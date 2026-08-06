# Casos de Teste

19 cenários cobrindo todos os endpoints públicos do reqres.in.

| # | Cenário | Endpoint | Status esperado |
|---|---|---|---|
| [CT-01](#ct-01--listar-usuários-página-padrão) | Listar usuários (página padrão) | `GET /api/users` | 200 |
| [CT-02](#ct-02--listar-usuários-página-2) | Listar usuários (página 2) | `GET /api/users?page=2` | 200 |
| [CT-03](#ct-03--página-além-do-total) | Página além do total | `GET /api/users?page=99` | 200 |
| [CT-04](#ct-04--resposta-com-atraso) | Resposta com atraso | `GET /api/users?delay=3` | 200 |
| [CT-05](#ct-05--consultar-usuário-existente) | Consultar usuário existente | `GET /api/users/3` | 200 |
| [CT-06](#ct-06--consultar-usuário-inexistente) | Consultar usuário inexistente | `GET /api/users/23` | 404 |
| [CT-07](#ct-07--criar-usuário) | Criar usuário | `POST /api/users` | 201 |
| [CT-08](#ct-08--substituir-usuário) | Substituir usuário | `PUT /api/users/2` | 200 |
| [CT-09](#ct-09--atualizar-usuário-parcialmente) | Atualizar parcialmente | `PATCH /api/users/2` | 200 |
| [CT-10](#ct-10--excluir-usuário) | Excluir usuário | `DELETE /api/users/2` | 204 |
| [CT-11](#ct-11--listar-recursos) | Listar recursos | `GET /api/unknown` | 200 |
| [CT-12](#ct-12--consultar-recurso-existente) | Consultar recurso existente | `GET /api/unknown/2` | 200 |
| [CT-13](#ct-13--consultar-recurso-inexistente) | Consultar recurso inexistente | `GET /api/unknown/23` | 404 |
| [CT-14](#ct-14--registrar-usuário) | Registrar usuário | `POST /api/register` | 200 |
| [CT-15](#ct-15--registrar-sem-senha) | Registrar sem senha | `POST /api/register` | 400 |
| [CT-16](#ct-16--registrar-sem-e-mail) | Registrar sem e-mail | `POST /api/register` | 400 |
| [CT-17](#ct-17--autenticar-usuário) | Autenticar usuário | `POST /api/login` | 200 |
| [CT-18](#ct-18--autenticar-sem-senha) | Autenticar sem senha | `POST /api/login` | 400 |
| [CT-19](#ct-19--autenticar-sem-e-mail) | Autenticar sem e-mail | `POST /api/login` | 400 |

Todas as requisições enviam `Content-Type: application/json` e o header `x-api-key`, herdados de `BaseApiTest`. Os exemplos abaixo omitem esses headers.

---

## Listagem de usuários

**Arquivo**: [GetUserListTest.java](../src/test/java/tests/GetUserListTest.java)

### CT-01 — Listar usuários (página padrão)

`shouldGetFirstPageByDefault()` — sem query string, a API devolve a primeira página.

```http
GET /api/users
```

| Campo | Validação |
|---|---|
| status code | `200` |
| `page` | `1` |
| `data` | 6 itens |
| `data.id` | todos não nulos |
| `data.email` | todos não nulos |

### CT-02 — Listar usuários (página 2)

`shouldGetSecondPage()` — valida os metadados de paginação e o primeiro registro da página.

```http
GET /api/users?page=2
```

| Campo | Valor esperado |
|---|---|
| status code | `200` |
| `page` | `2` |
| `per_page` | `6` |
| `total` | `12` |
| `total_pages` | `2` |
| `data` | 6 itens |
| `data[0].id` | `7` |

### CT-03 — Página além do total

`shouldReturnEmptyListForPageBeyondTotal()` — pedir uma página inexistente devolve lista vazia, não erro.

```http
GET /api/users?page=99
```

| Campo | Valor esperado |
|---|---|
| status code | `200` |
| `data` | 0 itens |

### CT-04 — Resposta com atraso

`shouldRespectDelayParameter()` — o parâmetro `delay` força a API a demorar N segundos, útil para exercitar timeouts.

```http
GET /api/users?delay=3
```

| Validação | Esperado |
|---|---|
| status code | `200` |
| tempo de resposta | ≥ 3 segundos |
| `data` | 6 itens |

---

## Usuário individual

**Arquivo**: [GetSingleUserTest.java](../src/test/java/tests/GetSingleUserTest.java)

### CT-05 — Consultar usuário existente

`shouldGetExistingUser()`

```http
GET /api/users/3
```

| Campo | Valor esperado |
|---|---|
| status code | `200` |
| `data.id` | `3` |
| `data.email` | `emma.wong@reqres.in` |
| `data.first_name` | `Emma` |
| `data.last_name` | `Wong` |
| `data.avatar` | `https://reqres.in/img/faces/3-image.jpg` |

### CT-06 — Consultar usuário inexistente

`shouldReturnNotFoundForNonExistingUser()` — o corpo da resposta vem vazio (`{}`), então só o status é validado.

```http
GET /api/users/23
```

| Validação | Esperado |
|---|---|
| status code | `404` |

---

## Criação

**Arquivo**: [PostCreateTest.java](../src/test/java/tests/PostCreateTest.java)

### CT-07 — Criar usuário

`shouldCreateUser()`

```http
POST /api/users

{
    "name": "Gabriela",
    "job": "QA"
}
```

| Campo | Validação |
|---|---|
| status code | `201` |
| `id` | não nulo (gerado pelo servidor) |
| `name` | `"Gabriela"` — eco do enviado |
| `job` | `"QA"` — eco do enviado |
| `createdAt` | não nulo (gerado pelo servidor) |

Os campos gerados pelo servidor são validados apenas por presença, já que o valor é imprevisível.

---

## Atualização

### CT-08 — Substituir usuário

**Arquivo**: [PutUpdateTest.java](../src/test/java/tests/PutUpdateTest.java) — `shouldReplaceUser()`

```http
PUT /api/users/2

{
    "name": "Gabriela",
    "job": "QA Lead"
}
```

| Campo | Validação |
|---|---|
| status code | `200` |
| `name` | `"Gabriela"` |
| `job` | `"QA Lead"` |
| `updatedAt` | não nulo |

### CT-09 — Atualizar usuário parcialmente

**Arquivo**: [PatchUpdateTest.java](../src/test/java/tests/PatchUpdateTest.java) — `shouldUpdateUserJobOnly()`

Envia apenas `job`; a resposta reflete só o campo enviado.

```http
PATCH /api/users/2

{
    "job": "QA Manager"
}
```

| Campo | Validação |
|---|---|
| status code | `200` |
| `job` | `"QA Manager"` |
| `updatedAt` | não nulo |

---

## Exclusão

### CT-10 — Excluir usuário

**Arquivo**: [DeleteUserTest.java](../src/test/java/tests/DeleteUserTest.java) — `shouldDeleteUser()`

```http
DELETE /api/users/2
```

| Validação | Esperado |
|---|---|
| status code | `204` |
| corpo | vazio |

---

## Recursos (`/api/unknown`)

**Arquivo**: [GetResourceTest.java](../src/test/java/tests/GetResourceTest.java)

Endpoint de recursos genéricos do reqres — uma lista de cores Pantone.

### CT-11 — Listar recursos

`shouldListResources()`

```http
GET /api/unknown
```

| Campo | Valor esperado |
|---|---|
| status code | `200` |
| `page` | `1` |
| `total` | `12` |
| `data` | 6 itens |
| `data[0].name` | `cerulean` |
| `data.pantone_value` | todos não nulos |

### CT-12 — Consultar recurso existente

`shouldGetSingleResource()`

```http
GET /api/unknown/2
```

| Campo | Valor esperado |
|---|---|
| status code | `200` |
| `data.id` | `2` |
| `data.name` | `fuchsia rose` |
| `data.year` | `2001` |
| `data.color` | `#C74375` |
| `data.pantone_value` | `17-2031` |

### CT-13 — Consultar recurso inexistente

`shouldReturnNotFoundForUnknownResource()`

```http
GET /api/unknown/23
```

| Validação | Esperado |
|---|---|
| status code | `404` |

---

## Registro

**Arquivo**: [PostRegisterTest.java](../src/test/java/tests/PostRegisterTest.java)

Só e-mails da base fixa do reqres conseguem se registrar — daí o uso de `eve.holt@reqres.in`.

### CT-14 — Registrar usuário

`shouldRegisterUser()`

```http
POST /api/register

{
    "email": "eve.holt@reqres.in",
    "password": "pistol"
}
```

| Campo | Validação |
|---|---|
| status code | `200` |
| `id` | não nulo |
| `token` | não nulo |

### CT-15 — Registrar sem senha

`shouldRejectRegistrationWithoutPassword()`

```http
POST /api/register

{
    "email": "eve.holt@reqres.in"
}
```

| Campo | Valor esperado |
|---|---|
| status code | `400` |
| `error` | `Missing password` |

### CT-16 — Registrar sem e-mail

`shouldRejectRegistrationWithoutEmail()`

```http
POST /api/register

{
    "password": "pistol"
}
```

| Campo | Validação |
|---|---|
| status code | `400` |
| `error` | não nulo |

---

## Autenticação

**Arquivo**: [PostLoginTest.java](../src/test/java/tests/PostLoginTest.java)

### CT-17 — Autenticar usuário

`shouldLoginUser()`

```http
POST /api/login

{
    "email": "eve.holt@reqres.in",
    "password": "cityslicka"
}
```

| Campo | Validação |
|---|---|
| status code | `200` |
| `token` | não nulo |

### CT-18 — Autenticar sem senha

`shouldRejectLoginWithoutPassword()`

```http
POST /api/login

{
    "email": "eve.holt@reqres.in"
}
```

| Campo | Valor esperado |
|---|---|
| status code | `400` |
| `error` | `Missing password` |

### CT-19 — Autenticar sem e-mail

`shouldRejectLoginWithoutEmail()`

```http
POST /api/login

{
    "password": "cityslicka"
}
```

| Campo | Validação |
|---|---|
| status code | `400` |
| `error` | não nulo |

---

## Fora de cobertura

**Erros de autenticação (401 / 403).** O reqres devolve `401` sem o header `x-api-key` e `403` com chave inválida, mas o comportamento é intermitente — nas verificações de 06/08/2026 as mesmas requisições ora eram bloqueadas, ora respondiam `200`. Um teste com essa asserção seria instável, então esses cenários ficaram de fora deliberadamente.

**Validação de schema JSON.** As asserções são campo a campo. `JsonSchemaValidator` cobriria o contrato inteiro de uma vez, ao custo de uma dependência a mais (`json-schema-validator`).
