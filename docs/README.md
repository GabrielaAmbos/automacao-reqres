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

- **2 cenários automatizados**: criação de usuário (POST) e consulta de usuário (GET).
- **Requer chave de API**: o reqres.in exige o header `x-api-key`. Configure a variável `REQRES_API_KEY` antes de rodar — ver [Como Executar](como-executar.md#configurando-a-chave-de-api).

## Referências

- API sob teste: <https://reqres.in/>
- [REST Assured](https://rest-assured.io/)
- [JUnit 4](https://junit.org/junit4/)
