# API de Estoque - Documentação

## Pré-requisitos

- Docker e Docker Compose instalados
- Java 25+
- Maven

## Iniciar o PostgreSQL

```bash
docker-compose up -d
```

Este comando irá:
- Criar e iniciar um container PostgreSQL
- Criar o banco de dados `estoque_db`
- Criar o usuário `estoque_user` com a senha `estoque_password`
- Mapear a porta 5432 do container para a porta 5432 do host
- Iniciar o Zookeeper (porta 2181)
- Iniciar o Kafka (portas 9092 e 29092)
- Iniciar o Kafdrop - Interface web para monitorar Kafka (porta 9000)

Verificar status:
```bash
docker-compose ps
```

### Serviços disponíveis:

| Serviço | URL/Porta | Usuário/Senha |
|---------|-----------|---------------|
| PostgreSQL | `localhost:5432` | `estoque_user` / `estoque_password` |
| Kafka Broker | `localhost:9092` (interno), `localhost:29092` (externo) | N/A |
| Zookeeper | `localhost:2181` | N/A |
| **Kafdrop** | **http://localhost:9000** | **Acesso web aberto** |

## Compilar e Rodar a Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## Endpoints da API

### 1. Criar Novo Produto

**POST** `/api/estoque`

```json
{
  "sku": "PROD001",
  "descricao": "Produto Teste",
  "quantidade": 100,
  "quantidadeMinima": 10
}
```

Resposta (201 Created):
```json
{
  "id": 1,
  "sku": "PROD001",
  "descricao": "Produto Teste",
  "quantidade": 100,
  "quantidadeMinima": 10,
  "dataCriacao": "2026-01-31T10:30:00",
  "dataAtualizacao": "2026-01-31T10:30:00"
}
```

### 2. Listar Todos os Produtos

**GET** `/api/estoque`

Resposta (200 OK):
```json
[
  {
    "id": 1,
    "sku": "PROD001",
    "descricao": "Produto Teste",
    "quantidade": 100,
    "quantidadeMinima": 10,
    "dataCriacao": "2026-01-31T10:30:00",
    "dataAtualizacao": "2026-01-31T10:30:00"
  }
]
```

### 3. Buscar Produto por ID

**GET** `/api/estoque/{id}`

Exemplo: `/api/estoque/1`

### 4. Buscar Produto por SKU

**GET** `/api/estoque/sku/{sku}`

Exemplo: `/api/estoque/sku/PROD001`

### 5. Adicionar Quantidade ao Estoque

**POST** `/api/estoque/{id}/adicionar`

```json
{
  "quantidade": 50
}
```

### 6. Adicionar Quantidade por SKU

**POST** `/api/estoque/sku/{sku}/adicionar`

Exemplo: `/api/estoque/sku/PROD001/adicionar`

```json
{
  "quantidade": 50
}
```

### 7. Remover Quantidade do Estoque

**POST** `/api/estoque/{id}/remover`

```json
{
  "quantidade": 20
}
```

Se tentar remover mais do que há disponível, retorna erro 400.

### 8. Remover Quantidade por SKU

**POST** `/api/estoque/sku/{sku}/remover`

```json
{
  "quantidade": 20
}
```

### 9. Verificar Disponibilidade

**GET** `/api/estoque/{id}/verificar/{quantidade}`

Exemplo: `/api/estoque/1/verificar/50`

Resposta:
```json
{
  "id": 1,
  "quantidade_solicitada": 50,
  "disponivel": true
}
```

### 10. Verificar Disponibilidade por SKU

**GET** `/api/estoque/sku/{sku}/verificar/{quantidade}`

Exemplo: `/api/estoque/sku/PROD001/verificar/50`

### 11. Atualizar Informações

**PUT** `/api/estoque/{id}`

```json
{
  "descricao": "Nova Descrição",
  "quantidadeMinima": 20
}
```

### 12. Deletar Produto

**DELETE** `/api/estoque/{id}`

Retorna 204 No Content

### 13. Verificar se está Abaixo da Quantidade Mínima

**GET** `/api/estoque/{id}/abaixo-minima`

Resposta:
```json
{
  "id": 1,
  "quantidade_atual": 5,
  "quantidade_minima": 10,
  "abaixo_minima": true
}
```

## Exemplo de Teste com cURL

```bash
# Criar um novo produto
curl -X POST http://localhost:8080/api/estoque \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "PROD001",
    "descricao": "Notebook",
    "quantidade": 50,
    "quantidadeMinima": 5
  }'

# Listar todos
curl http://localhost:8080/api/estoque

# Buscar por ID
curl http://localhost:8080/api/estoque/1

# Adicionar quantidade
curl -X POST http://localhost:8080/api/estoque/1/adicionar \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 30}'

# Remover quantidade
curl -X POST http://localhost:8080/api/estoque/1/remover \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 10}'

# Verificar disponibilidade
curl http://localhost:8080/api/estoque/1/verificar/20

# Deletar
curl -X DELETE http://localhost:8080/api/estoque/1
```

## Estrutura do Banco de Dados

A tabela `estoque` é criada automaticamente pelo Hibernate com os seguintes campos:

```sql
CREATE TABLE estoque (
  id BIGSERIAL PRIMARY KEY,
  sku VARCHAR(255) NOT NULL UNIQUE,
  descricao VARCHAR(255) NOT NULL,
  quantidade INTEGER NOT NULL,
  quantidade_minima INTEGER NOT NULL,
  data_criacao TIMESTAMP NOT NULL,
  data_atualizacao TIMESTAMP NOT NULL
);
```

## Parar a Aplicação

```bash
# Parar o Spring Boot
Ctrl+C

# Parar o PostgreSQL
docker-compose down
```

## Tratamento de Erros

A API retorna os seguintes status HTTP:

- **200 OK**: Requisição bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Recurso deletado com sucesso
- **400 Bad Request**: Erro de validação ou quantidade insuficiente
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro do servidor

Exemplo de erro:
```json
{
  "timestamp": "2026-01-31T10:30:00",
  "status": 404,
  "erro": "Estoque não encontrado",
  "mensagem": "Estoque com ID 999 não encontrado"
}
```
