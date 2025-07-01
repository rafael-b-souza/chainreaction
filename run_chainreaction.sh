#!/bin/bash

# Caminho base do projeto
BASE_DIR="$(dirname "$(realpath "$0")")/chainreaction"
SRC_DIR="$BASE_DIR/src/main/java"
BIN_DIR="$BASE_DIR/bin"

echo "🔍 Verificando dependências..."
if ! command -v javac &> /dev/null || ! command -v java &> /dev/null; then
    echo "❌ Java não está instalado. Por favor instale o JDK (sudo apt install default-jdk)."
    exit 1
fi

echo "✅ Java encontrado."

# Criar diretório de saída
mkdir -p "$BIN_DIR"

echo "🔨 Compilando o projeto..."
find "$SRC_DIR" -name "*.java" > sources.txt
javac -d "$BIN_DIR" @sources.txt
rm sources.txt

if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação."
    exit 1
fi

echo "🚀 Executando o jogo..."
cd "$BIN_DIR"
cp -r "$BASE_DIR/src/main/resources"/* "$BIN_DIR"/ 2>/dev/null
java de.freewarepoint.cr.swing.UIGame
