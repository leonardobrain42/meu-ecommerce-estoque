#!/bin/bash

# Script de Teste da API de Estoque
# Certifique-se que a aplicação está rodando em http://localhost:8080

BASE_URL="http://localhost:8080/api/estoque"

echo "================================"
echo "API de Estoque - Script de Testes"
echo "================================"
echo ""

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 1. Criar Primeiro Produto
echo -e "${BLUE}1. Criando primeiro produto...${NC}"
RESPONSE=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "NOTEBOOK001",
    "descricao": "Notebook Dell XPS 13",
    "quantidade": 50,
    "quantidadeMinima": 5
  }')

PROD1_ID=$(echo $RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo -e "${GREEN}Produto criado com ID: $PROD1_ID${NC}"
echo $RESPONSE | jq '.'
echo ""

# 2. Criar Segundo Produto
echo -e "${BLUE}2. Criando segundo produto...${NC}"
RESPONSE=$(curl -s -X POST $BASE_URL \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "MOUSE001",
    "descricao": "Mouse Logitech MX Master",
    "quantidade": 100,
    "quantidadeMinima": 10
  }')

PROD2_ID=$(echo $RESPONSE | grep -o '"id":[0-9]*' | head -1 | grep -o '[0-9]*')
echo -e "${GREEN}Produto criado com ID: $PROD2_ID${NC}"
echo $RESPONSE | jq '.'
echo ""

# 3. Listar Todos os Produtos
echo -e "${BLUE}3. Listando todos os produtos...${NC}"
curl -s $BASE_URL | jq '.'
echo ""

# 4. Buscar Produto por ID
echo -e "${BLUE}4. Buscando produto por ID ($PROD1_ID)...${NC}"
curl -s $BASE_URL/$PROD1_ID | jq '.'
echo ""

# 5. Buscar Produto por SKU
echo -e "${BLUE}5. Buscando produto por SKU (MOUSE001)...${NC}"
curl -s $BASE_URL/sku/MOUSE001 | jq '.'
echo ""

# 6. Adicionar Quantidade
echo -e "${BLUE}6. Adicionando 25 unidades ao produto $PROD1_ID...${NC}"
curl -s -X POST $BASE_URL/$PROD1_ID/adicionar \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 25}' | jq '.'
echo ""

# 7. Adicionar Quantidade por SKU
echo -e "${BLUE}7. Adicionando 30 unidades via SKU (MOUSE001)...${NC}"
curl -s -X POST $BASE_URL/sku/MOUSE001/adicionar \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 30}' | jq '.'
echo ""

# 8. Verificar Disponibilidade
echo -e "${BLUE}8. Verificando se há 40 unidades disponíveis do produto $PROD1_ID...${NC}"
curl -s $BASE_URL/$PROD1_ID/verificar/40 | jq '.'
echo ""

# 9. Remover Quantidade
echo -e "${BLUE}9. Removendo 15 unidades do produto $PROD1_ID...${NC}"
curl -s -X POST $BASE_URL/$PROD1_ID/remover \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 15}' | jq '.'
echo ""

# 10. Verificar Abaixo da Mínima
echo -e "${BLUE}10. Verificando se está abaixo da quantidade mínima...${NC}"
curl -s $BASE_URL/$PROD1_ID/abaixo-minima | jq '.'
echo ""

# 11. Remover Bastante Quantidade
echo -e "${BLUE}11. Removendo 80 unidades do produto $PROD2_ID...${NC}"
curl -s -X POST $BASE_URL/$PROD2_ID/remover \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 80}' | jq '.'
echo ""

# 12. Verificar Abaixo da Mínima (deve estar abaixo agora)
echo -e "${BLUE}12. Verificando se está abaixo da quantidade mínima (agora deve estar)...${NC}"
curl -s $BASE_URL/$PROD2_ID/abaixo-minima | jq '.'
echo ""

# 13. Atualizar Informações
echo -e "${BLUE}13. Atualizando informações do produto $PROD1_ID...${NC}"
curl -s -X PUT $BASE_URL/$PROD1_ID \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Notebook Dell XPS 13 - Versão 2026",
    "quantidadeMinima": 8
  }' | jq '.'
echo ""

# 14. Tentar Remover Mais do que Há (Erro)
echo -e "${BLUE}14. Tentando remover 5000 unidades (deve dar erro)...${NC}"
curl -s -X POST $BASE_URL/$PROD1_ID/remover \
  -H "Content-Type: application/json" \
  -d '{"quantidade": 5000}' | jq '.'
echo ""

# 15. Tentar Acessar Produto Inexistente (Erro)
echo -e "${BLUE}15. Tentando acessar produto inexistente (deve dar erro)...${NC}"
curl -s $BASE_URL/9999 | jq '.'
echo ""

# 16. Deletar Produto
echo -e "${BLUE}16. Deletando produto com ID $PROD1_ID...${NC}"
curl -s -X DELETE $BASE_URL/$PROD1_ID -w "\nStatus: %{http_code}\n"
echo ""

# 17. Verificar que foi Deletado
echo -e "${BLUE}17. Tentando acessar produto deletado (deve dar erro)...${NC}"
curl -s $BASE_URL/$PROD1_ID | jq '.'
echo ""

# 18. Listar Novamente (deve mostrar apenas o segundo produto)
echo -e "${BLUE}18. Listando produtos finais...${NC}"
curl -s $BASE_URL | jq '.'
echo ""

echo -e "${GREEN}================================${NC}"
echo -e "${GREEN}Testes Concluídos!${NC}"
echo -e "${GREEN}================================${NC}"
