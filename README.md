# 📦 Serviço de Estoque - Resumo da Implementação

## ✅ O que foi implementado

### 1. **Banco de Dados PostgreSQL** (Docker)
- ✅ Container PostgreSQL no `docker-compose.yaml`
- ✅ Banco de dados `estoque_db` automaticamente criado
- ✅ Usuário `estoque_user` configurado
- ✅ Volume persistente para dados
- ✅ Health check configurado
- ✅ Integração com Kafka e Zookeeper
- ✅ Kafdrop - Interface web para monitorar Kafka

### 2. **Modelo de Dados** (Entidade JPA)
**Arquivo:** [src/main/java/com/meuecommerce/estoque/domain/Estoque.java](src/main/java/com/meuecommerce/estoque/domain/Estoque.java)

**Tabela `estoque` com campos:**
- `id` - Chave primária (auto-incremento)
- `sku` - Código único do produto (UNIQUE)
- `descricao` - Descrição do produto
- `quantidade` - Quantidade atual
- `quantidade_minima` - Quantidade mínima para alerta
- `data_criacao` - Timestamp de criação
- `data_atualizacao` - Timestamp de última atualização

### 3. **Repository** (Camada de Acesso a Dados)
**Arquivo:** [src/main/java/com/meuecommerce/estoque/domain/EstoqueRepository.java](src/main/java/com/meuecommerce/estoque/domain/EstoqueRepository.java)

- ✅ Interface JPA para operações CRUD
- ✅ Método customizado: `findBySku(String sku)`
- ✅ Otimização de queries com indexação

### 4. **Camada de Serviço** (Lógica de Negócio)
**Arquivo:** [src/main/java/com/meuecommerce/estoque/application/EstoqueService.java](src/main/java/com/meuecommerce/estoque/application/EstoqueService.java)

**Métodos Implementados:**

#### Criação
- `criarEstoque()` - Criar novo produto
- Validação de SKU duplicado
- Quantidade mínima padrão de 10

#### Adição de Quantidade
- `adicionarQuantidade(Long id, Integer quantidade)` - Adicionar por ID
- `adicionarQuantidadePorSku(String sku, Integer quantidade)` - Adicionar por SKU
- Publica `EstoqueReservadoEvent` para rastreamento

#### Remoção de Quantidade
- `removerQuantidade(Long id, Integer quantidade)` - Remover por ID com verificação
- `removerQuantidadePorSku(String sku, Integer quantidade)` - Remover por SKU
- Exceção: `QuantidadeInsuficienteException` quando estoque insuficiente
- Publica `EstoqueBaixadoEvent` para rastreamento

#### Consultas
- `buscarEstoquePorId(Long id)` - Buscar por ID
- `buscarEstoquePorSku(String sku)` - Buscar por SKU
- `listarTodos()` - Listar todos os produtos
- `obterQuantidade(Long id)` - Obter quantidade atual por ID
- `obterQuantidadePorSku(String sku)` - Obter quantidade atual por SKU

#### Verificações
- `verificarDisponibilidade(Long id, Integer quantidade)` - Verificação booleana por ID
- `verificarDisponibilidadePorSku(String sku, Integer quantidade)` - Verificação booleana por SKU
- `estaAbaixoDaMinima(Long id)` - Verificar se está abaixo do mínimo

#### Gerenciamento
- `atualizar()` - Atualizar descrição e quantidade mínima
- `deletarEstoque()` - Remover produto do estoque

### 5. **REST API** (Controller)
**Arquivo:** [src/main/java/com/meuecommerce/estoque/controllers/EstoqueController.java](src/main/java/com/meuecommerce/estoque/controllers/EstoqueController.java)

**Endpoints da API:**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/estoque` | Criar novo produto |
| GET | `/api/estoque` | Listar todos os produtos |
| GET | `/api/estoque/{id}` | Buscar produto por ID |
| GET | `/api/estoque/sku/{sku}` | Buscar produto por SKU |
| POST | `/api/estoque/{id}/adicionar` | Adicionar quantidade por ID |
| POST | `/api/estoque/sku/{sku}/adicionar` | Adicionar quantidade por SKU |
| POST | `/api/estoque/{id}/remover` | Remover quantidade por ID |
| POST | `/api/estoque/sku/{sku}/remover` | Remover quantidade por SKU |
| GET | `/api/estoque/{id}/verificar/{qty}` | Verificar disponibilidade por ID |
| GET | `/api/estoque/sku/{sku}/verificar/{qty}` | Verificar disponibilidade por SKU |
| GET | `/api/estoque/{id}/abaixo-minima` | Verificar se está abaixo do mínimo |
| PUT | `/api/estoque/{id}` | Atualizar informações do produto |
| DELETE | `/api/estoque/{id}` | Deletar produto |

### 6. **Arquitetura Orientada a Eventos**
**Localização:** [src/main/java/com/meuecommerce/estoque/domain/events/](src/main/java/com/meuecommerce/estoque/domain/events/)

**Eventos de Domínio:**
- `EstoqueReservadoEvent` - Publicado quando estoque é reservado
- `EstoqueBaixadoEvent` - Publicado quando estoque é reduzido
- `EstoqueLiberadoEvent` - Publicado quando reserva é liberada

**Publicador de Eventos:**
- [src/main/java/com/meuecommerce/estoque/application/ports/EstoqueEventPublisher.java](src/main/java/com/meuecommerce/estoque/application/ports/EstoqueEventPublisher.java)

### 7. **Integração Kafka**
**Localização:** [src/main/java/com/meuecommerce/estoque/infrastructure/messaging/kafka/](src/main/java/com/meuecommerce/estoque/infrastructure/messaging/kafka/)

**Componentes:**
- `KafkaEstoqueEventPublisher` - Publica eventos de estoque em tópicos Kafka
- `EstoqueEventListener` - Escuta eventos externos (Pagamento, Pedidos)
- `KafkaConsumerConfig` - Configuração do consumer Kafka

**Eventos Recebidos:**
- `PagamentoConfirmadoEvent` - Evento de pagamento confirmado do serviço de pagamento
- `PagamentoFalhouEvent` - Evento de pagamento falhou
- `PedidoCriadoEvent` - Evento de pedido criado

### 8. **Tratamento de Erros**
**Arquivo:** [src/main/java/com/meuecommerce/estoque/handlers/GlobalExceptionHandler.java](src/main/java/com/meuecommerce/estoque/handlers/GlobalExceptionHandler.java)

**Exceções Customizadas:**
- `EstoqueNaoEncontradoException` - Retorna 404 Not Found
- `QuantidadeInsuficienteException` - Retorna 400 Bad Request
- `IllegalArgumentException` - Retorna 400 Bad Request
- Tratamento genérico de exceções com respostas detalhadas

### 9. **Testes Unitários**
**Arquivo:** [src/test/java/com/meuecommerce/estoque/services/EstoqueServiceTest.java](src/test/java/com/meuecommerce/estoque/services/EstoqueServiceTest.java)

**Cobertura de Testes:**
- ✅ Criar item de estoque
- ✅ Adicionar quantidade
- ✅ Remover quantidade
- ✅ Verificar disponibilidade
- ✅ Tratamento de exceções

## 📋 Pré-requisitos

- Docker e Docker Compose
- Java 25+
- Maven 3.8+
- Linux/Mac ou Windows com WSL2

## 🚀 Início Rápido

### 1. Iniciar Serviços de Infraestrutura

```bash
docker-compose up -d
```

Será iniciado:
- **PostgreSQL** em `localhost:5432`
- **Kafka** em `localhost:9092` (interno), `localhost:29092` (externo)
- **Zookeeper** em `localhost:2181`
- **Kafdrop** (Interface Kafka) em `http://localhost:9000`

Verificar serviços:
```bash
docker-compose ps
```

### 2. Compilar e Executar a Aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### 3. Parar Serviços

```bash
docker-compose down
```

Ou usar o script fornecido:
```bash
./stop-docker.sh
```

## 📡 Exemplos de API

### Criar Produto

```bash
curl -X POST http://localhost:8080/api/estoque \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "PROD001",
    "descricao": "Notebook Premium",
    "quantidade": 100,
    "quantidadeMinima": 10
  }'
```

### Listar Todos os Produtos

```bash
curl http://localhost:8080/api/estoque
```

### Adicionar Estoque

```bash
curl -X POST http://localhost:8080/api/estoque/1/adicionar \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 50}'
```

### Remover Estoque

```bash
curl -X POST http://localhost:8080/api/estoque/1/remover \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 30}'
```

### Verificar Disponibilidade

```bash
curl http://localhost:8080/api/estoque/1/verificar/20
```

## 🏗️ Arquitetura

### Arquitetura Hexagonal (Ports & Adapters)

```
src/main/java/com/meuecommerce/estoque/
├── EstoqueApplication.java           # Aplicação Principal
├── domain/                           # Camada de Domínio (Lógica de Negócio)
│   ├── Estoque.java                 # Entidade
│   ├── EstoqueRepository.java       # Interface Repository
│   ├── ReservaEstoque.java          # Value Object
│   └── events/                      # Eventos de Domínio
├── application/                      # Camada de Aplicação
│   ├── EstoqueService.java          # Casos de Uso
│   └── ports/                       # Portas de Saída
│       └── EstoqueEventPublisher.java
├── controllers/                      # Pontos de Entrada (Adapter de Entrada)
│   └── EstoqueController.java
├── infrastructure/                   # Camada de Infraestrutura
│   ├── messaging/                   # Integração Kafka
│   │   └── kafka/
│   │       ├── EstoqueEventListener.java
│   │       ├── KafkaConsumerConfig.java
│   │       ├── KafkaEstoqueEventPublisher.java
│   │       └── in/                  # Eventos Recebidos
│   └── persistence/                 # Persistência de Dados (Adapter de Saída)
│       └── JpaEstoqueRepository.java
├── exceptions/                       # Exceções Customizadas
│   ├── EstoqueNaoEncontradoException.java
│   └── QuantidadeInsuficienteException.java
└── handlers/                        # Tratador Global de Exceções
    └── GlobalExceptionHandler.java
```

### Explicação das Camadas

1. **Camada de Domínio** - Lógica de negócio principal, independente de frameworks
2. **Camada de Aplicação** - Casos de uso e definição de portas
3. **Camada de Infraestrutura** - Implementações de tecnologia (Kafka, JPA)
4. **Controllers** - Pontos de entrada HTTP

## 🔧 Tecnologias

| Tecnologia | Versão | Propósito |
|------------|--------|----------|
| Spring Boot | 4.0.2 | Framework Web |
| Spring Data JPA | Última | ORM & Acesso a Dados |
| Spring Kafka | Última | Mensageria de Eventos |
| PostgreSQL | 15+ | Banco de Dados Relacional |
| Hibernate | Última | Implementação JPA |
| Docker & Compose | Última | Containerização |
| Kafka | 3.x | Message Broker |
| JUnit 5 | Última | Testes Unitários |
| Java | 25 | Linguagem |

## 🧪 Executar Testes

```bash
./mvnw test
```

Executar classe de teste específica:
```bash
./mvnw test -Dtest=EstoqueServiceTest
```

## 📊 Monitoramento

### Kafdrop - Interface Kafka

Acessar Kafdrop para monitorar tópicos e mensagens Kafka:
- URL: `http://localhost:9000`
- Sem autenticação necessária
- Visualize tópicos, partições e mensagens

## 🔌 Pontos de Integração

Este serviço se integra com outros microserviços via Kafka:

- **Serviço de Pagamento** → Envia `PagamentoConfirmadoEvent`, `PagamentoFalhouEvent`
- **Serviço de Pedidos** → Envia `PedidoCriadoEvent`
- **Serviço de Estoque** ← Publica eventos de estoque para outros serviços

## 📝 Configuração

As configurações da aplicação estão em [src/main/resources/application.properties](src/main/resources/application.properties)

Configurações principais:
- Detalhes de conexão PostgreSQL
- Endereços do broker Kafka
- Porta do servidor da aplicação (8080)

## 🐛 Resolução de Problemas

### Porta Já em Uso
```bash
# Encontrar processo usando porta 8080
lsof -i :8080
# Matar processo
kill -9 <PID>
```

### Problemas de Conexão com Banco de Dados
```bash
# Verificar se PostgreSQL está rodando
docker-compose ps

# Ver logs do PostgreSQL
docker-compose logs postgres-estoque
```

### Problemas de Conexão Kafka
```bash
# Verificar se Kafka está rodando
docker-compose logs kafka

# Acessar interface Kafdrop
http://localhost:9000
```

## 📚 Arquivos de Documentação

- [ARQUITETURA.md](ARQUITETURA.md) - Documentação detalhada da arquitetura
- [ESTOQUE_API.md](ESTOQUE_API.md) - Documentação completa da API
- [INTEGRACAO.md](INTEGRACAO.md) - Guia de integração

## 📄 Licença

Este projeto faz parte da plataforma MeuEcommerce.

## 👨‍💻 Estrutura do Projeto

```
.
├── README.md                      # Este arquivo
├── pom.xml                        # Dependências Maven
├── docker-compose.yaml            # Configuração de serviços Docker
├── Dockerfile                     # Container da aplicação
├── compose.yaml                   # Arquivo compose alternativo
├── application.yml                # Configuração da aplicação
├── start-docker.sh                # Script para iniciar serviços
├── stop-docker.sh                 # Script para parar serviços
├── test-api.sh                    # Script para testar endpoints da API
├── mvnw, mvnw.cmd                # Maven wrapper
└── src/
    ├── main/
    │   ├── java/com/meuecommerce/estoque/  # Código fonte
    │   └── resources/                       # Arquivos de configuração
    └── test/
        └── java/com/meuecommerce/estoque/  # Código de testes
```

## 🚢 Deployment

### Construir Imagem Docker

```bash
docker build -t estoque-service:latest .
```

### Enviar para Registro

```bash
docker tag estoque-service:latest seu-registro/estoque-service:latest
docker push seu-registro/estoque-service:latest
```

### Deploy no Kubernetes

```bash
kubectl apply -f k8s-manifest.yaml
```

---

**Última Atualização:** 1 de fevereiro de 2026  
**Versão:** 0.0.1-SNAPSHOT
