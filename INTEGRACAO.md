# Guia de Integração - API de Estoque

## Casos de Uso Comuns

### 1. Venda de Produto (Remover do Estoque)

```java
// Em um serviço de vendas
@Service
public class VendaService {
    @Autowired
    private EstoqueService estoqueService;
    
    public void procesarVenda(String sku, Integer quantidade) {
        // Verificar disponibilidade
        if (!estoqueService.verificarDisponibilidadePorSku(sku, quantidade)) {
            throw new EstoqueInsuficienteException("Quantidade insuficiente");
        }
        
        // Remover do estoque
        estoqueService.removerQuantidadePorSku(sku, quantidade);
        
        // Continuar processamento da venda...
    }
}
```

### 2. Devolução de Produto (Adicionar ao Estoque)

```java
@Service
public class DevolucaoService {
    @Autowired
    private EstoqueService estoqueService;
    
    public void processarDevolucao(String sku, Integer quantidade) {
        // Adicionar quantidade devolvida
        Estoque estoque = estoqueService.adicionarQuantidadePorSku(sku, quantidade);
        
        // Log ou notificação
        System.out.println("Devolução processada. Quantidade atual: " + estoque.getQuantidade());
    }
}
```

### 3. Alertas de Estoque Baixo

```java
@Component
public class EstoqueAlertaTask {
    @Autowired
    private EstoqueService estoqueService;
    
    @Scheduled(fixedDelay = 3600000) // A cada 1 hora
    public void verificarEstoqueBaixo() {
        List<Estoque> todos = estoqueService.listarTodos();
        
        for (Estoque estoque : todos) {
            if (estoqueService.estaAbaixoDaMinima(estoque.getId())) {
                // Enviar alerta
                System.out.println("ALERTA: " + estoque.getSku() + 
                    " está abaixo do mínimo!");
            }
        }
    }
}
```

### 4. Ajuste de Inventário (Contagem Física)

```java
@Service
public class AjusteInventarioService {
    @Autowired
    private EstoqueService estoqueService;
    
    public void ajustarQuantidade(Long id, Integer quantidadeReal) {
        Estoque estoque = estoqueService.buscarEstoquePorId(id);
        Integer diferenca = quantidadeReal - estoque.getQuantidade();
        
        if (diferenca > 0) {
            // Adicionar diferença
            estoqueService.adicionarQuantidade(id, diferenca);
        } else if (diferenca < 0) {
            // Remover diferença
            estoqueService.removerQuantidade(id, Math.abs(diferenca));
        }
    }
}
```

## Exemplos com Cliente HTTP

### JavaScript/Fetch

```javascript
// Criar novo produto
async function criarProduto() {
    const response = await fetch('http://localhost:8080/api/estoque', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            sku: 'PROD001',
            descricao: 'Meu Produto',
            quantidade: 100,
            quantidadeMinima: 10
        })
    });
    return response.json();
}

// Remover quantidade
async function removerDoEstoque(id, quantidade) {
    const response = await fetch(
        `http://localhost:8080/api/estoque/${id}/remover`,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ quantidade })
        }
    );
    return response.json();
}

// Verificar disponibilidade
async function verificarDisponibilidade(sku, quantidade) {
    const response = await fetch(
        `http://localhost:8080/api/estoque/sku/${sku}/verificar/${quantidade}`
    );
    return response.json();
}
```

### Python

```python
import requests
import json

BASE_URL = 'http://localhost:8080/api/estoque'

# Criar produto
def criar_produto(sku, descricao, quantidade, quantidade_minima):
    data = {
        'sku': sku,
        'descricao': descricao,
        'quantidade': quantidade,
        'quantidadeMinima': quantidade_minima
    }
    response = requests.post(BASE_URL, json=data)
    return response.json()

# Remover quantidade
def remover_quantidade(product_id, quantidade):
    url = f'{BASE_URL}/{product_id}/remover'
    data = {'quantidade': quantidade}
    response = requests.post(url, json=data)
    return response.json()

# Listar todos
def listar_todos():
    response = requests.get(BASE_URL)
    return response.json()

# Exemplo de uso
if __name__ == '__main__':
    # Criar
    produto = criar_produto('SKU001', 'Produto Teste', 100, 10)
    print(f"Produto criado: {produto['id']}")
    
    # Remover
    resultado = remover_quantidade(produto['id'], 20)
    print(f"Quantidade após remoção: {resultado['quantidade']}")
    
    # Listar
    todos = listar_todos()
    print(f"Total de produtos: {len(todos)}")
```

## Tratamento de Erros

### Java

```java
try {
    estoqueService.removerQuantidade(id, 100);
} catch (QuantidadeInsuficienteException e) {
    System.err.println("Erro: " + e.getMessage());
    // Notificar usuário
} catch (EstoqueNaoEncontradoException e) {
    System.err.println("Estoque não encontrado");
    // Tratar erro de não encontrado
}
```

### JavaScript

```javascript
async function removerComErro(id, quantidade) {
    try {
        const response = await fetch(
            `http://localhost:8080/api/estoque/${id}/remover`,
            {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ quantidade })
            }
        );
        
        if (!response.ok) {
            const error = await response.json();
            console.error(`Erro ${error.status}: ${error.mensagem}`);
            return null;
        }
        
        return response.json();
    } catch (error) {
        console.error('Erro de rede:', error);
    }
}
```

## Docker Compose - Referência

Arquivo `compose.yaml` configurado com:

```yaml
services:
  postgres:
    image: 'postgres:latest'
    environment:
      - POSTGRES_DB=estoque_db
      - POSTGRES_PASSWORD=estoque_password
      - POSTGRES_USER=estoque_user
    ports:
      - '5432:5432'
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U estoque_user -d estoque_db" ]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
```

**Comandos úteis:**

```bash
# Iniciar
docker-compose up -d

# Ver logs
docker-compose logs -f postgres

# Parar
docker-compose stop

# Parar e remover
docker-compose down

# Ver status
docker-compose ps

# Acessar banco via psql
docker-compose exec postgres psql -U estoque_user -d estoque_db
```

## Performance e Otimizações

### Índices Recomendados

```sql
-- Já criado automaticamente (UNIQUE)
CREATE UNIQUE INDEX idx_estoque_sku ON estoque(sku);

-- Opcional para melhor performance em buscas
CREATE INDEX idx_estoque_quantidade_minima 
ON estoque(quantidade, quantidade_minima);
```

### Monitoramento

```java
// Logs automáticos (ativado em application.properties)
// spring.jpa.show-sql=true
// spring.jpa.properties.hibernate.format_sql=true

// Exemplo de log customizado
@Aspect
@Component
public class EstoqueAspect {
    
    @Around("@annotation(LogEstoque)")
    public Object logEstoque(ProceedingJoinPoint jp) throws Throwable {
        long inicio = System.currentTimeMillis();
        Object resultado = jp.proceed();
        long duracao = System.currentTimeMillis() - inicio;
        
        System.out.println("Operação: " + jp.getSignature().getName() + 
            " | Duração: " + duracao + "ms");
        
        return resultado;
    }
}
```

## Deploy em Produção

### Configuração para Ambiente de Produção

```properties
# application-prod.properties

# Banco de dados (usar variáveis de ambiente)
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# Logging
logging.level.root=WARN
logging.level.com.meuecommerce=INFO

# Servidor
server.port=${SERVER_PORT:8080}
```

### Docker para Aplicação

```dockerfile
FROM openjdk:25-slim

WORKDIR /app

COPY target/estoque-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml com App

```yaml
version: '3.8'

services:
  postgres:
    # ... configuração anterior ...
  
  estoque-api:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_URL=jdbc:postgresql://postgres:5432/estoque_db
      - DB_USER=estoque_user
      - DB_PASSWORD=estoque_password
      - SPRING_PROFILES_ACTIVE=prod
    depends_on:
      postgres:
        condition: service_healthy
```
