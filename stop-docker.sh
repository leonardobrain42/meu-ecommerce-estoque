#!/bin/bash

# Script para parar e remover containers da aplicação Estoque

echo "========================================="
echo "Parando aplicação Estoque"
echo "========================================="

docker-compose down

echo ""
echo "✓ Containers parados e removidos!"
echo ""
echo "Para remover volumes também, execute:"
echo "docker-compose down -v"
echo "========================================="
