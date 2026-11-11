# Endpoints e roteiro de testes manuais

## Perfis

- `CIDADAO`: consulta ecopontos e recompensas, registra descartes, consulta saldo e realiza resgates.
- `PARCEIRO`: cadastra, consulta e desativa as próprias recompensas.
- `ADMINISTRADOR`: mantém ecopontos e compartimentos, consulta descartes e exporta relatórios.

Todas as rotas, exceto cadastro, login e saúde, exigem HTTP Basic.

## Testes de aceitação

| ID | Cenário | Resultado esperado |
|---|---|---|
| T01 | Cadastrar cidadão com e-mail ainda não utilizado | HTTP 201; resposta sem campo de senha |
| T02 | Cadastrar novamente o mesmo e-mail | Erro de conflito/regra de negócio |
| T03 | Login com senha incorreta | HTTP 401 |
| T04 | Criar ecoponto como administrador | HTTP 201; registro persistido |
| T05 | Criar ecoponto como cidadão | HTTP 403 |
| T06 | Consultar, atualizar e excluir ecoponto sem descartes | Operações CRUD concluídas |
| T07 | Excluir ecoponto com descartes associados | Operação bloqueada para preservar histórico |
| T08 | Registrar 2,5 kg de reciclável em compartimento compatível | 25 pontos com a política inicial |
| T09 | Registrar tipo diferente do compartimento | HTTP 400; nenhum descarte/ponto gravado |
| T10 | Registrar descarte acima da capacidade restante | HTTP 400; transação revertida |
| T11 | Consultar saldo após descarte | Saldo acrescido dos pontos calculados |
| T12 | Cadastrar recompensa como parceiro | HTTP 201 |
| T13 | Resgatar com saldo suficiente | Resgate confirmado e saldo debitado |
| T14 | Resgatar com saldo insuficiente | HTTP 400; sem registro de resgate |
| T15 | Atualizar nível para 85% de capacidade com limiar 80% | Status `COLETA_NECESSARIA` e alerta verdadeiro |
| T16 | Exportar relatório filtrado por período/tipo/ecoponto | Arquivo CSV com os registros correspondentes |

## Teste unitário

`ResiduoPontuacaoTest` cobre as três estratégias e a rejeição de peso zero/negativo. Execute com `mvn test`.

## Teste de integração manual

Siga a sequência de criação do ecoponto, compartimento, cadastro do cidadão, descarte, cadastro do parceiro, recompensa, resgate e exportação de relatório no README. Execute com PostgreSQL ativo.
