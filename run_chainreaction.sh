#!/bin/bash
set -e

BASE_DIR="$(dirname "$(realpath "$0")")/chainreaction"
SRC_DIR="$BASE_DIR/src/main/java"
BIN_DIR="$BASE_DIR/bin"

if ! command -v javac &> /dev/null || ! command -v java &> /dev/null; then
    echo "Erro: JDK nao encontrado. Instale o Java (ex: sudo apt install default-jdk)." >&2
    exit 1
fi

mkdir -p "$BIN_DIR"
find "$SRC_DIR" -name "*.java" > /tmp/cr_sources.txt
javac -encoding UTF-8 -d "$BIN_DIR" @/tmp/cr_sources.txt
rm -f /tmp/cr_sources.txt

cd "$BIN_DIR"
cp -r "$BASE_DIR/src/main/resources"/* "$BIN_DIR"/ 2>/dev/null || true
java de.freewarepoint.cr.swing.UIGame
