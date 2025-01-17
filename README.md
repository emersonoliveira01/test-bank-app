# API de Webhooks e Gerenciamento de Cartões

---

## Funcionalidades

### Webhooks
- **Webhook de Processador de Cartões**
    - Endpoint para processar atualizações de CVV e data de expiração dos cartões físicos.
- **Webhook de Entregas**
    - Endpoint para processar atualizações de status de entrega e ativar cartões quando entregues.

### Gerenciamento
- **Clientes**
    - Criar novos cadastros de clientes.
    - Cancelar contas associadas a clientes existentes.
- **Cartões**
    - Reemissão de cartões físicos com validação de motivos autorizados.

---

## Tecnologias Utilizadas

- **Linguagem**: Java 17
- **Framework**: Quarkus
- **Persistência**: JPA/Hibernate com MySQL
- **Dependências Adicionais**:
    - **MicroProfile OpenAPI** para geração de documentação Swagger.
    - **JUnit 5** para testes unitários.
    - **Mockito** para mock de dependências. 
- **Ferramentas de Build**: Maven

---

## Estrutura do Projeto

A arquitetura do projeto foi organizada nas seguintes camadas:

1. **Controller**: Responsável por expor os endpoints da API.
2. **Service**: Contém a lógica de negócio.
3. **Repository**: Gerencia as operações de persistência com o banco de dados.
4. **Model**: Define as entidades.

---

## Endpoints Documentados com Swagger

A API foi documentada utilizando **MicroProfile OpenAPI**. A documentação está disponível automaticamente na URL: http://localhost:8080/q/swagger-ui

## Configuração do Banco de Dados

A aplicação utiliza um banco de dados **MySQL**. Certifique-se de que o banco esteja configurado antes de executar o projeto.

### Script de Criação do Banco de Dados
O script `import.sql` contém as definições iniciais para criação das tabelas e dados básicos.

---

## Testes

### Testes Unitários
Os testes unitários foram implementados utilizando **JUnit 5** e **Mockito**, cobrindo as principais classes de serviço e controladores.

### Como Executar os Testes
1. Navegue até o diretório raiz do projeto.
2. Execute o comando:
   ```bash
   mvn test
## Execute a aplicação:

1. Execute o comando:
```bash
   ./mvnw compile quarkus:dev
