# 📦 Serviço de Estoque - Resumo da Implementação

## ✅ O que foi implementado

### 1. **Banco de Dados PostgreSQL** (Docker)
- ✅ Container PostgreSQL no `docker-compose.yaml`
- ✅ Banco de dados `estoque_db` automaticamente criado
- ✅ Usuário `estoque_user` configurado
- ✅ Volume persistente para dados
- ✅ Health check configurado

### 2. **Modelo de Dados** (Entidade JPA)
Arquivo: [src/main/java/com/meuecommerce/estoque/models/Estoque.java](src/main/java/com/meuecommerce/estoque/models/Estoque.java)

Tabela `estoque` com campos:
- `id` - Chave primária (auto-incremento)
- `sku` - Código único do produto (UNIQUE)
- `descricao` - Descrição do produto
- `quantidade` - Quantidade atual
- `quantidade_minima` - Quantidade mínima para alerta
- `data_criacao` - Timestamp de criação
- `data_atualizacao` - Timestamp de última atualização

### 3. **Repository** (Acesso a Dados)
Arquivo: [src/main/java/com/meuecommerce/estoque/repositories/EstoqueRepository.java](src/main/java/com/meuecommerce/estoque/repositories/EstoqueRepository.java)

- ✅ Interface JPA para acesso CRUD
- ✅ Método customizado: `findBySku(String sku)`

### 4. **Service** (Lógica de Negócio)
Arquivo: [src/main/java/com/meuecommerce/estoque/services/EstoqueService.java](src/main/java/com/meuecommerce/estoque/services/EstoqueService.java)

**Métodos implementados:**

#### Criação
- `criarEstoque()` - Criar novo produto
- Validação de SKU duplicado

#### Adição
- `adicionarQuantidade(Long id, Integer quantidade)`
- `adicionarQuantidadePorSku(String sku, Integer quantidade)`

#### Remoção
- `removerQuantidade(Long id, Integer quantidade)` - Com verificação de disponibilidade
- `removerQuantidadePorSku(String sku, Integer quantidade)`
- Exceção: `QuantidadeInsuficienteException`

#### Consultas
- `buscarEstoquePorId(Long id)`
- `buscarEstoquePorSku(String sku)`
- `listarTodos()`
- `obterQuantidade(Long id)`
- `obterQuantidadePorSku(String sku)`

#### Verificações
- `verificarDisponibilidade(Long id, Integer quantidade)` - Booleano
- `verificarDisponibilidadePorSku(String sku, Integer quantidade)`
- `estaAbaixoDaMinima(Long id)` - Verifica se está abaixo do mínimo

#### Gerenciamento
- `atualizar()` - Atualizar descrição e quantidade mínima
- `deletarEstoque()` - Remover produto

### 5. **REST API** (Controller)
Arquivo: [src/main/java/com/meuecommerce/estoque/controllers/EstoqueController.java](src/main/java/com/meuecommerce/estoque/controllers/EstoqueController.java)

**Endpoints:**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/estoque` | Criar novo produto |
| GET | `/api/estoque` | Listar todos |
| GET | `/api/estoque/{id}` | Buscar por ID |
| GET | `/api/estoque/sku/{sku}` | Buscar por SKU |
| POST | `/api/estoque/{id}/adicionar` | Adicionar quantidade |
| POST | `/api/estoque/sku/{sku}/adicionar` | Adicionar por SKU |
| POST | `/api/estoque/{id}/remover` | Remover quantidade |
| POST | `/api/estoque/sku/{sku}/remover` | Remover por SKU |
| GET | `/api/estoque/{id}/verificar/{qty}` | Verificar disponibilidade |
| GET | `/api/estoque/sku/{sku}/verificar/{qty}` | Verificar por SKU |
| GET | `/api/estoque/{id}/abaixo-minima` | Verificar se abaixo da mínima |
| PUT | `/api/estoque/{id}` | Atualizar informações |
| DELETE | `/api/estoque/{id}` | Deletar produto |

### 6. **Tratamento de Erros**
Arquivo: [src/main/java/com/meuecommerce/estoque/handlers/GlobalExceptionHandler.java](src/main/java/com/meuecommerce/estoque/handlers/GlobalExceptionHandler.java)

**Exceções customizadas:**
- `EstoqueNaoEncontradoException` - 404 Not Found
- `QuantidadeInsuficienteException` - 400 Bad Request
- `IllegalArgumentException` - 400 Bad Request
- Tratamento genérico de exceções

### 7. **Testes Unitários**
Arquivo: [src/test/java/com/meuecommerce/estoque/services/EstoqueServiceTest.java](src/test/java/com/meuecommerce/estoque/services/EstoqueServiceTest.java)

Testes implementados:
- ✅ Criar estoque
- ✅ Adicionar quantidade
- ✅ Remover quantidade
- ✅ Remover quantidade insuficiente (erro)
- ✅ Buscar por ID
- ✅ Buscar por SKU
- ✅ SKU duplicado (erro)
- ✅ Quantidade negativa (erro)
- ✅ Verificar disponibilidade
- ✅ Estoque abaixo da mínima
- ✅ Deletar estoque

### 8. **Configuração**
Arquivo: [src/main/resources/application.properties](src/main/resources/application.properties)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/estoque_db
spring.datasource.username=estoque_user
spring.datasource.password=estoque_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 9. **Documentação**

Arquivos criados:
- **[ESTOQUE_API.md](ESTOQUE_API.md)** - API completa com exemplos
- **[ARQUITETURA.md](ARQUITETURA.md)** - Estrutura do projeto
- **[INTEGRACAO.md](INTEGRACAO.md)** - Guia de integração
- **[test-api.sh](test-api.sh)** - Script de testes automatizado

## 🚀 Como Usar

### 1. Iniciar PostgreSQL
```bash
cd /home/usuario/projetos-java/meuecommece/estoque
docker-compose up -d
```

### 2. Compilar e Executar
```bash
./mvnw clean install
./mvnw spring-boot:run
```

### 3. Testar API
```bash
# Opção 1: Script automático
./test-api.sh

# Opção 2: cURL manual
curl -X POST http://localhost:8080/api/estoque \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "PROD001",
    "descricao": "Teste",
    "quantidade": 100,
    "quantidadeMinima": 10
  }'
```

### 4. Parar Tudo
```bash
docker-compose down
```

## 📊 Arquitetura

```
┌─────────────┐
│   Cliente   │ (HTTP REST)
└──────┬──────┘
       │
┌──────▼──────────────────┐
│  EstoqueController      │ (REST API)
└──────┬──────────────────┘
       │
┌──────▼──────────────────┐
│  EstoqueService         │ (Lógica de Negócio)
└──────┬──────────────────┘
       │
┌──────▼──────────────────┐
│  EstoqueRepository      │ (Data Access)
└──────┬──────────────────┘
       │
┌──────▼──────────────────┐
│  PostgreSQL Database    │ (estoque_db)
└─────────────────────────┘
```

## 🔒 Validações e Regras

- ✅ SKU deve ser único
- ✅ Quantidade não pode ser negativa
- ✅ Não é possível remover mais do que há em estoque
- ✅ Quantidade mínima é rastreada
- ✅ Timestamps automáticos (criação e atualização)
- ✅ Transações gerenciadas pelo Spring

## 📝 Tecnologias

- **Spring Boot** 4.0.2
- **Spring Data JPA** - ORM
- **Hibernate** - Mapeamento de objetos
- **PostgreSQL** - Banco de dados
- **Docker** - Containerização
- **Maven** - Gerenciador de dependências
- **JUnit 5** - Testes
- **Java 25** - Linguagem


## 📞 Suporte

Para dúvidas sobre a implementação, consulte:
- [ESTOQUE_API.md](ESTOQUE_API.md) - Para usar a API
- [ARQUITETURA.md](ARQUITETURA.md) - Para entender a estrutura
- [INTEGRACAO.md](INTEGRACAO.md) - Para integrar em outras aplicações
