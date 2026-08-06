# Casos de Teste

## CT-01 — Criar usuário

**Arquivo**: [PostCreateTest.java](../src/test/java/tests/PostCreateTest.java)
**Método**: `shouldCreateUser()`

Valida que a API cria um usuário e devolve os dados enviados junto com os campos gerados pelo servidor.

### Requisição

```http
POST https://reqres.in/api/users
Content-Type: application/json
x-api-key: $REQRES_API_KEY

{
    "name": "Gabriela",
    "job": "QA"
}
```

### Asserções

| Campo | Validação |
|---|---|
| status code | `201 Created` |
| `id` | não nulo (gerado pelo servidor) |
| `name` | `"Gabriela"` — eco do enviado |
| `job` | `"QA"` — eco do enviado |
| `createdAt` | não nulo (timestamp gerado pelo servidor) |

Os campos gerados pelo servidor (`id` e `createdAt`) são validados apenas por presença, já que o valor é imprevisível.

---

## CT-02 — Consultar usuário existente

**Arquivo**: [GetSingleUserTest.java](../src/test/java/tests/GetSingleUserTest.java)
**Método**: `shouldGetExistingUser()`

Valida o contrato de retorno de um usuário conhecido da base de dados fixa do reqres.

### Requisição

```http
GET https://reqres.in/api/users/3
Content-Type: application/json
x-api-key: $REQRES_API_KEY
```

### Asserções

| Campo | Valor esperado |
|---|---|
| status code | `200 OK` |
| `data.id` | `3` |
| `data.email` | `emma.wong@reqres.in` |
| `data.first_name` | `Emma` |
| `data.last_name` | `Wong` |
| `data.avatar` | `https://reqres.in/img/faces/3-image.jpg` |

Este teste depende dos dados fixos (mock estático) do reqres. Se a API alterar o usuário de id 3, o teste quebra.

---

## Cobertura e lacunas

O que já está coberto:

- Caminho feliz de criação (POST)
- Caminho feliz de consulta individual (GET)

O que ainda não está coberto:

| Cenário | Endpoint sugerido |
|---|---|
| Usuário inexistente (404) | `GET /api/users/23` |
| Listagem paginada | `GET /api/users?page=2` |
| Atualização completa | `PUT /api/users/2` |
| Atualização parcial | `PATCH /api/users/2` |
| Exclusão (204) | `DELETE /api/users/2` |
| Login com sucesso e sem senha (400) | `POST /api/login` |
| Registro com sucesso e sem senha (400) | `POST /api/register` |
| Resposta com atraso | `GET /api/users?delay=3` |
| Validação de schema JSON | qualquer endpoint |
