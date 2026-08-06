# Documentação — Desafio Automação Reqres

Projeto de automação de testes de API REST sobre o serviço público [reqres.in](https://reqres.in/), escrito em Java com REST Assured e JUnit 4.

O objetivo é validar contratos e comportamentos dos endpoints de usuários por meio de testes automatizados executáveis via Maven ou IDE.

## Índice

| Documento | Conteúdo |
|---|---|
| [Arquitetura](arquitetura.md) | Stack, estrutura de pastas, padrões adotados |
| [Casos de Teste](casos-de-teste.md) | Cenários cobertos, requisições e asserções |
| [Como Executar](como-executar.md) | Pré-requisitos, comandos, troubleshooting |

## Status atual

- **19 cenários automatizados**, cobrindo todos os endpoints públicos do reqres: listagem paginada, consulta, criação, atualização (PUT/PATCH), exclusão, recursos, registro e login — incluindo os caminhos de erro `404` e `400`.
- **Suíte verde e estável**: `Tests run: 19, Failures: 0, Errors: 0` em execuções consecutivas.
- **Requer chave de API**: configure a variável `REQRES_API_KEY` antes de rodar — ver [Como Executar](como-executar.md#configurando-a-chave-de-api).

## Referências

- API sob teste: <https://reqres.in/>
- [REST Assured](https://rest-assured.io/)
- [JUnit 4](https://junit.org/junit4/)
