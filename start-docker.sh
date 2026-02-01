#!/bin/bash

# Script para executar a aplicação com Docker Compose

set -e

echo "========================================="
echo "Iniciando aplicação Estoque com Docker"
echo "========================================="

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não está instalado!"
    exit 1
fi

# Verificar se Docker Compose está instalado
if ! command -v docker compose &> /dev/null; then
    echo "❌ Docker Compose não está instalado!"
    exit 1
fi

echo "✓ Docker e Docker Compose encontrados"

# Build e start dos containers
echo ""
echo "📦 Iniciando containers..."
docker compose up --build -d 

echo ""
echo "⏳ Aguardando inicialização do PostgreSQL..."
sleep 5

echo ""
echo "✓ Containers iniciados com sucesso!"
echo ""
echo "========================================="
echo "Serviços disponíveis:"
echo "========================================="
echo "🔗 Aplicação Estoque: http://localhost:8080"
echo "🗄️  PostgreSQL: localhost:5432"
echo "📨 Kafka: localhost:9092"
echo "📌 Zookeeper: localhost:2181"
echo "UI Kafka (Kafdrop): http://localhost:9000"
echo ""
echo "Para ver os logs: docker-compose logs -f estoque_app"
echo "Para parar: docker-compose down"
echo "========================================="
