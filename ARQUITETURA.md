# Estrutura do Projeto - Serviço de Estoque

## Arquitetura Implementada

```
src/main/java/com/meuecommerce/estoque/
├── EstoqueApplication.java          # Main Application
├── controllers/
│   └── EstoqueController.java       # REST API Endpoints
├── services/
│   └── EstoqueService.java          # Lógica de Negócio
├── models/
│   └── Estoque.java                 # Entidade JPA
├── repositories/
│   └── EstoqueRepository.java       # Data Access Layer
├── exceptions/
│   ├── EstoqueNaoEncontradoException.java
│   └── QuantidadeInsuficienteException.java
└── handlers/
    └── GlobalExceptionHandler.java  # Exception Handler Global

src/main/resources/
├── application.properties           # Configurações da aplicação
└── ...

compose.yaml                          # Docker Compose com PostgreSQL
pom.xml                              # Dependências Maven
```

## Camadas da Aplicação

### 1. **Controller Layer** (`EstoqueController`)
- Recebe requisições HTTP
- Valida entrada
- Retorna respostas HTTP

### 2. **Service Layer** (`EstoqueService`)
- Lógica de negócio
- Validações de regras
- Transações de banco de dados

### 3. **Data Layer** 
- `EstoqueRepository`: Interface JPA para acesso aos dados
- `Estoque`: Entidade mapeada para a tabela do banco

### 4. **Exception Handling** 
- `GlobalExceptionHandler`: Centraliza tratamento de erros
- Exceções customizadas para diferentes cenários

## Funcionalidades Implementadas

### Adicionar Estoque
- ✅ Criar novo produto com SKU único
- ✅ Adicionar quantidade existente

### Remover Estoque
- ✅ Remover quantidade com validação
- ✅ Verificação de quantidade insuficiente

### Consultas
- ✅ Listar todos os produtos
- ✅ Buscar por ID
- ✅ Buscar por SKU
- ✅ Verificar disponibilidade

### Gerenciamento
- ✅ Atualizar informações
- ✅ Deletar produto
- ✅ Verificar nível mínimo
- ✅ Obter quantidade atual

## Tecnologias Utilizadas

- **Framework**: Spring Boot 4.0.2
- **Banco de Dados**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Containerização**: Docker Compose
- **Testes**: JUnit 5
- **Linguagem**: Java 25

## Como Usar

1. **Iniciar PostgreSQL**:
   ```bash
   docker-compose up -d
   ```

2. **Executar Aplicação**:
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Acessar API**:
   ```
   http://localhost:8080/api/estoque
   ```

4. **Parar Tudo**:
   ```bash
   docker-compose down
   ./mvnw clean
   ```

## Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/estoque` | Criar novo produto |
| GET | `/api/estoque` | Listar todos |
| GET | `/api/estoque/{id}` | Buscar por ID |
| POST | `/api/estoque/{id}/adicionar` | Adicionar quantidade |
| POST | `/api/estoque/{id}/remover` | Remover quantidade |
| DELETE | `/api/estoque/{id}` | Deletar produto |

Veja [ESTOQUE_API.md](ESTOQUE_API.md) para documentação completa.

## Validações e Regras de Negócio

- ✅ SKU único
- ✅ Quantidade não pode ser negativa
- ✅ Verificação de quantidade insuficiente
- ✅ Quantidade mínima rastreada
- ✅ Timestamps automáticos (criação e atualização)
- ✅ Tratamento de erros com mensagens descritivas

## Banco de Dados

Configuração automática via Hibernate:
- Host: localhost
- Port: 5432
- Database: estoque_db
- User: estoque_user
- Password: estoque_password

Tabela criada automaticamente com campos:
- `id` (Primary Key)
- `sku` (Unique)
- `descricao`
- `quantidade`
- `quantidade_minima`
- `data_criacao`
- `data_atualizacao`
