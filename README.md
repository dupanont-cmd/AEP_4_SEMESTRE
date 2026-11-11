# EcoPonto Inteligente de Maringá

**AEP - 4º semestre de 2026 | Engenharia de Software (ESOFT) | 2º bimestre**

Sistema de backend para gestão de ecopontos, compartimentos, descartes, pontuação ambiental, recompensas, resgates e relatórios. O projeto dá continuidade ao documento de arquitetura do 1º bimestre e mantém a ODS 12 - Consumo e Produção Responsáveis.

## 1. Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC + Jackson: API REST e conversão JSON.
- Spring Data JPA / Hibernate: persistência e mapeamento objeto-relacional.
- Spring Security: autenticação HTTP Basic e autorização por perfil.
- PostgreSQL 17 (ou versão compatível).
- Flyway: criação versionada do banco na inicialização.
- Maven: dependências, testes e execução.

O backend foi priorizado porque a avaliação de ESOFT concentra-se na arquitetura, no funcionamento, na POO e na persistência. A API pode ser exercitada pelo Postman, Insomnia ou `curl`; não há dependência de interface gráfica avançada.

## 2. Requisitos funcionais preservados do 1º bimestre

- **RF01:** O sistema deve permitir o cadastro e a autenticação de usuários, diferenciando os perfis Cidadão, Administrador e Parceiro.
- **RF02:** O sistema deve permitir o cadastro, a consulta, a edição e a remoção de ecopontos, contendo nome, endereço, localização e tipos de resíduos aceitos.
- **RF03:** O sistema deve permitir o registro de descartes vinculando cidadão, ecoponto, compartimento, tipo de resíduo, data/hora e peso estimado ou informado.
- **RF04:** O sistema deve calcular automaticamente a quantidade de pontos obtida em cada descarte, aplicando regras diferentes conforme o tipo de resíduo por meio de polimorfismo.
- **RF05:** O sistema deve manter e exibir o saldo de pontos do cidadão, atualizando-o após descartes e resgates.
- **RF06:** O sistema deve permitir que parceiros cadastrem recompensas e que cidadãos realizem o resgate dessas recompensas quando possuírem saldo suficiente.
- **RF07:** O sistema deve registrar e consultar o nível atual dos compartimentos dos ecopontos e emitir um alerta quando o nível atingir o limite configurado para coleta.
- **RF08:** O sistema deve gerar relatórios de descartes por período, tipo de resíduo e ecoponto, auxiliando a administração no acompanhamento do impacto ambiental e da operação.

## 3. Relação com os requisitos do 1º bimestre

| Requisito | Implementação na aplicação |
|---|---|
| RF01 - Cadastro e autenticação | Cadastro de cidadão/parceiro, login e consulta do usuário autenticado. Senhas são armazenadas com hash BCrypt. O administrador inicial é criado na primeira inicialização. |
| RF02 - CRUD de ecopontos | Criar, consultar, atualizar e excluir ecopontos. A exclusão é bloqueada quando existe histórico de descarte. |
| RF03 - Registro de descartes | O descarte registra cidadão, compartimento (e, por relacionamento, ecoponto), categoria, data/hora, peso e pontos. |
| RF04 - Pontuação polimórfica | A interface `Residuo` possui implementações para recicláveis, eletrônicos e orgânicos. O serviço escolhe a estratégia pelo tipo e chama `calcularPontuacao`. |
| RF05 - Saldo de pontos | O saldo do cidadão é atualizado ao registrar um descarte e ao resgatar recompensa. Pode ser consultado em `/api/auth/me`. |
| RF06 - Recompensas e resgates | Parceiros cadastram e desativam recompensas; cidadãos consultam recompensas ativas e resgatam quando possuem saldo suficiente. |
| RF07 - Nível e alerta de coleta | Atualização do nível de cada compartimento e retorno do status de alerta conforme o percentual configurado. |
| RF08 - Relatórios | Exportação CSV dos descartes com filtros opcionais por período, tipo de resíduo e ecoponto. |

### Backlog planejado na primeira entrega

| Sprint / período original | Épico | História/entrega | Responsável previsto |
|---|---|---|---|
| Sprint 1 / 14/09 a 27/09 | Base do Sistema e Usuários | Cadastro e autenticação por perfil | [Integrante 1] |
| Sprint 2 / 28/09 a 11/10 | Ecopontos e Compartimentos | Cadastro de ecopontos e acompanhamento dos compartimentos | [Integrante 2] |
| Sprint 3 / 12/10 a 25/10 | Descarte e Pontuação | Registro de descarte e geração automática de pontos | [Integrante 3] |
| Sprint 4 / 26/10 a 08/11 | Recompensas, Resgates e Relatórios | Resgate de benefícios e consulta de relatórios | Equipe |

Os responsáveis permanecem como campos a serem substituídos pelos nomes reais da equipe. As datas reproduzem o backlog da primeira entrega; a tabela não afirma que os prazos foram cumpridos.

**Repositório público GitHub:** https://github.com/dupanont-cmd/AEP_4_SEMESTRE

## 4. POO implementada

### Abstração e herança

`Usuario` é uma classe abstrata. `Cidadao`, `Administrador` e `Parceiro` herdam seus atributos e comportamentos comuns. O mapeamento JPA utiliza herança `SINGLE_TABLE`, persistindo os perfis na tabela `usuarios` e diferenciando-os pela coluna `tipo_usuario`.

### Composição 1:N

`Ecoponto` mantém uma coleção de `Compartimento`. O relacionamento é mapeado com `@OneToMany`, `cascade = CascadeType.ALL` e `orphanRemoval = true`; cada compartimento possui a referência ao seu ecoponto.

### Polimorfismo

A interface `Residuo` declara `calcularPontuacao(BigDecimal pesoKg)`. As classes `ResiduoReciclavel`, `ResiduoEletronico` e `ResiduoOrganico` implementam o método com `@Override`. `ResiduoFactory` seleciona a estratégia correspondente ao tipo informado no descarte.

## 5. Regras iniciais de negócio adotadas

O documento de arquitetura do 1º bimestre solicitou pontuação proporcional ao peso e regras distintas por categoria, mas não fixou os valores. Para deixar a segunda entrega executável, foram configurados valores iniciais de demonstração:

| Categoria | Pontos por kg |
|---|---:|
| Reciclável | 10 |
| Eletrônico | 20 |
| Orgânico | 5 |

O resultado é arredondado para o inteiro mais próximo (`HALF_UP`). O alerta de coleta é acionado inicialmente em 80% da capacidade, podendo ser alterado ao criar o compartimento. Capacidade, nível atual e peso do descarte são medidos em quilogramas (kg). Esses valores devem ser validados pela equipe/professor e podem ser ajustados em `ResiduoReciclavel`, `ResiduoEletronico`, `ResiduoOrganico` e na requisição de criação do compartimento.

Outras validações: não é permitido registrar peso zero/negativo, descartar categoria incompatível com o compartimento, ultrapassar a capacidade, resgatar recompensa inativa ou resgatar sem saldo suficiente.

## 6. Pré-requisitos

1. JDK 21 instalado (`java -version`).
2. Apache Maven instalado (`mvn -version`).
3. PostgreSQL instalado e em execução, ou Docker com Docker Compose.
4. Um banco chamado `ecoponto` e um usuário com permissão para criar tabelas e índices.

A documentação oficial do Spring Boot 4.1.2 informa compatibilidade com Java 17 ou superior e suporte ao Maven 3.6.3 ou superior; esta aplicação foi configurada para Java 21. Consulte [requisitos oficiais](https://docs.spring.io/spring-boot/system-requirements.html).

## 7. Como executar

### Opção A - PostgreSQL local

Crie o banco no PostgreSQL:

```sql
CREATE DATABASE ecoponto;
```

Configure as variáveis de ambiente antes de executar. Os valores abaixo são apenas exemplos de desenvolvimento:

**Windows PowerShell:**

```powershell
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/ecoponto"
$env:DB_USER="postgres"
$env:DB_PASSWORD="sua_senha_postgres"
$env:ADMIN_EMAIL="admin@ecoponto.local"
$env:ADMIN_PASSWORD="TroqueEstaSenha123!"
```

**Linux/macOS:**

```bash
export DATABASE_URL="jdbc:postgresql://localhost:5432/ecoponto"
export DB_USER="postgres"
export DB_PASSWORD="sua_senha_postgres"
export ADMIN_EMAIL="admin@ecoponto.local"
export ADMIN_PASSWORD="TroqueEstaSenha123!"
```

### Opção B - PostgreSQL com Docker

Na raiz do projeto, execute:

```bash
docker compose up -d postgres
```

O compose cria o banco `ecoponto`, usuário `postgres` e senha de desenvolvimento `postgres`. Não utilize essa senha em ambiente de produção.

### Configurar o arquivo de ambiente (opcional)

Copie `.env.example` para `.env` e altere a senha do banco e a senha do administrador. O arquivo `.env` é carregado pelo `application.properties` e está no `.gitignore` para evitar o envio de credenciais ao GitHub.

**Windows PowerShell:** `Copy-Item .env.example .env`  
**Linux/macOS:** `cp .env.example .env`

### Iniciar a aplicação

Na raiz do projeto:

```bash
mvn clean test
mvn spring-boot:run
```

No Windows também é possível executar `executar.bat`; no Linux/macOS, `./executar.sh`. Esses scripts utilizam os padrões de desenvolvimento informados em `application.properties` caso as variáveis não estejam definidas.

Na primeira inicialização, o Flyway executa a migração `V1__criar_estrutura_ecoponto.sql`. O Hibernate valida o mapeamento das entidades contra as tabelas. O projeto não utiliza arrays ou listas em memória como substituto do banco. **Não execute `database/schema.sql` manualmente antes da primeira inicialização**, pois ele é a cópia de referência da migração que o Flyway aplicará automaticamente.

A API ficará disponível em `http://localhost:8080`. O endpoint de saúde é `http://localhost:8080/actuator/health`.

## 8. Acesso inicial

O sistema cria um administrador quando o e-mail configurado ainda não existe no banco:

- E-mail padrão de desenvolvimento: `admin@ecoponto.local`
- Senha padrão de desenvolvimento: `Admin123!`

**Altere a senha por variável de ambiente antes de qualquer uso compartilhado.** O armazenamento é feito por BCrypt. O cadastro público permite somente os perfis Cidadão e Parceiro; o perfil Administrador é provisionado pelo ambiente.

As rotas protegidas utilizam HTTP Basic. No Postman, selecione **Authorization > Basic Auth** e informe o e-mail e a senha do perfil que fará a operação. O endpoint `/api/auth/login` valida as credenciais e retorna os dados do perfil; as demais chamadas continuam autenticadas por HTTP Basic.

## 9. Endpoints principais

| Método | Rota | Perfil | Finalidade |
|---|---|---|---|
| POST | `/api/auth/cadastro` | Público | Cadastrar cidadão ou parceiro |
| POST | `/api/auth/login` | Público | Validar credenciais |
| GET | `/api/auth/me` | Autenticado | Consultar perfil e saldo |
| POST | `/api/ecopontos` | Administrador | Criar ecoponto |
| GET | `/api/ecopontos` | Autenticado | Listar ecopontos |
| GET | `/api/ecopontos/{id}` | Autenticado | Consultar ecoponto |
| PUT | `/api/ecopontos/{id}` | Administrador | Atualizar ecoponto |
| DELETE | `/api/ecopontos/{id}` | Administrador | Excluir ecoponto sem histórico |
| POST | `/api/ecopontos/{id}/compartimentos` | Administrador | Criar compartimento |
| GET | `/api/ecopontos/{id}/compartimentos` | Autenticado | Consultar compartimentos |
| PATCH | `/api/compartimentos/{id}/nivel` | Administrador | Atualizar nível / registrar leitura do sensor |
| POST | `/api/descartes` | Cidadão | Registrar descarte e gerar pontos |
| GET | `/api/descartes` | Cidadão/Admin | Consultar descartes próprios ou todos |
| GET | `/api/recompensas` | Autenticado | Listar recompensas ativas |
| POST | `/api/recompensas` | Parceiro | Cadastrar recompensa |
| GET | `/api/recompensas/minhas` | Parceiro | Listar próprias recompensas |
| PATCH | `/api/recompensas/{id}/desativar` | Parceiro proprietário | Desativar recompensa |
| POST | `/api/resgates/{recompensaId}` | Cidadão | Resgatar recompensa |
| GET | `/api/resgates` | Cidadão | Consultar histórico de resgates |
| GET | `/api/relatorios/descartes.csv` | Administrador | Exportar relatório CSV |

## 10. Fluxo rápido de demonstração (curl)

Execute a aplicação e utilize um terminal. Se necessário, troque a porta ou credenciais para refletir suas variáveis.

### 10.1 Criar um ecoponto (administrador)

```bash
curl -u 'admin@ecoponto.local:Admin123!' \
  -H "Content-Type: application/json" \
  -d '{"nome":"EcoPonto Centro","endereco":"Avenida Brasil, Maringá - PR","latitude":-23.4205,"longitude":-51.9333}' \
  http://localhost:8080/api/ecopontos
```

Guarde o `id` retornado. Supondo que seja `1`, crie um compartimento:

```bash
curl -u 'admin@ecoponto.local:Admin123!' \
  -H "Content-Type: application/json" \
  -d '{"tipoResiduo":"RECICLAVEL","capacidadeMaxima":100,"alertaPercentual":80}' \
  http://localhost:8080/api/ecopontos/1/compartimentos
```

Guarde também o `id` do compartimento.

### 10.2 Cadastrar cidadão

```bash
curl -X POST http://localhost:8080/api/auth/cadastro \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ana Cidadã","email":"ana@example.com","senha":"SenhaForte123!","perfil":"CIDADAO"}'
```

### 10.3 Registrar descarte

Substitua `1` pelo ID real do compartimento criado:

```bash
curl -u ana@example.com:SenhaForte123! \
  -H "Content-Type: application/json" \
  -d '{"compartimentoId":1,"tipoResiduo":"RECICLAVEL","pesoKg":2.5}' \
  http://localhost:8080/api/descartes
```

Com a regra inicial, 2,5 kg de recicláveis geram 25 pontos. Consulte o saldo:

```bash
curl -u ana@example.com:SenhaForte123! http://localhost:8080/api/auth/me
```

### 10.4 Cadastrar parceiro e recompensa

```bash
curl -X POST http://localhost:8080/api/auth/cadastro \
  -H "Content-Type: application/json" \
  -d '{"nome":"Mercado Parceiro","email":"mercado@example.com","senha":"SenhaParceiro123!","perfil":"PARCEIRO","cnpj":"12345678000190"}'
```

```bash
curl -u mercado@example.com:SenhaParceiro123! \
  -H "Content-Type: application/json" \
  -d '{"descricao":"Desconto de R$ 5,00","custoPontos":20}' \
  http://localhost:8080/api/recompensas
```

O cidadão pode consultar `/api/recompensas` e resgatar pelo ID retornado:

```bash
curl -X POST -u ana@example.com:SenhaForte123! \
  http://localhost:8080/api/resgates/1
```

### 10.5 Atualizar nível e gerar relatório

```bash
curl -u 'admin@ecoponto.local:Admin123!' \
  -H "Content-Type: application/json" \
  -X PATCH -d '{"nivelAtual":85}' \
  http://localhost:8080/api/compartimentos/1/nivel
```

Para uma capacidade de 100 kg e alerta configurado em 80%, a resposta informa `alertaColeta: true` e `status: COLETA_NECESSARIA`.

```bash
curl -u 'admin@ecoponto.local:Admin123!' \
  "http://localhost:8080/api/relatorios/descartes.csv?tipoResiduo=RECICLAVEL" \
  -o relatorio-descartes.csv
```

Filtros opcionais: `inicio=2026-11-01T00:00:00`, `fim=2026-11-30T23:59:59`, `tipoResiduo=ELETRONICO` e `ecopontoId=1`.

## 11. Organização do repositório

```text
Projeto_EcoPonto_Inteligente/
├── database/
│   └── schema.sql
├── docs/
│   ├── ARQUITETURA_E_IMPLEMENTACAO.md
│   └── ENDPOINTS_E_TESTES.md
├── postman/
│   └── EcoPonto.postman_collection.json
├── src/
│   ├── main/java/br/com/ecoponto/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   ├── enums/
│   │   │   └── strategy/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── repository/
│   │   └── service/
│   ├── main/resources/
│   │   ├── application.properties
│   │   └── db/migration/
│   └── test/java/
├── docker-compose.yml
├── pom.xml
├── executar.bat
├── executar.sh
└── README.md
```

## 12. Testes

Os testes unitários em `src/test/java` verificam a pontuação das três categorias e a rejeição de pesos inválidos. Para executar:

```bash
mvn test
```

A validação de integração com PostgreSQL deve ser realizada em ambiente com o banco disponível, seguindo a sequência da seção 9. O projeto está configurado para validar o schema na inicialização; se houver divergência entre script e entidades, a aplicação não deve ser considerada pronta para demonstração.

## 13. GitHub e trabalho em equipe

O repositório público é https://github.com/dupanont-cmd/AEP_4_SEMESTRE e deve conter este README, o código, `database/schema.sql` e a documentação. Cada integrante deve realizar commits com suas próprias credenciais, em tarefas distintas (por exemplo: banco/migração, entidades e POO, serviços/CRUD, segurança/testes/documentação). **O histórico de commits deve refletir o trabalho real de cada integrante.** Não compartilhe credenciais nem simule autoria.

Antes de enviar ao professor:

- [ ] Substituir os nomes dos integrantes no README/documentação, se necessário.
- [ ] Criar o repositório público no GitHub e inserir o link na documentação de entrega.
- [ ] Configurar e testar o PostgreSQL no computador da apresentação.
- [ ] Trocar a senha padrão do administrador.
- [ ] Executar `mvn clean test` e seguir o fluxo de demonstração.
- [ ] Cada integrante realizar commits reais.

## 14. Observação de escopo

O primeiro documento de arquitetura não definiu taxas de pontuação numéricas, percentual obrigatório de alerta, autenticação por protocolo ou formato de API. Para entregar uma aplicação executável, esta implementação adotou as políticas iniciais e as escolhas técnicas descritas neste README. O projeto mantém os oito requisitos do primeiro bimestre; alterações futuras devem ser registradas no próprio repositório e justificadas pela equipe.
