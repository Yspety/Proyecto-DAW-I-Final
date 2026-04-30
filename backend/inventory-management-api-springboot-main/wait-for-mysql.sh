#!/usr/bin/env bash
# wait-for-mysql.sh
# Espera a que MySQL acepte conexiones antes de iniciar la app.
# Uso: ./wait-for-mysql.sh <host> <port> -- <comando>

HOST="$1"
PORT="$2"
shift 2

# Saltar el separador "--"
if [[ "$1" == "--" ]]; then
  shift
fi

echo "⏳ Esperando a MySQL en $HOST:$PORT ..."

RETRIES=30
while ! (echo > /dev/tcp/$HOST/$PORT) 2>/dev/null; do
  RETRIES=$((RETRIES - 1))
  if [ $RETRIES -le 0 ]; then
    echo "❌ Timeout: MySQL no respondió a tiempo."
    exit 1
  fi
  echo "   ...reintentando en 3 segundos ($RETRIES intentos restantes)"
  sleep 3
done

echo "✅ MySQL listo. Iniciando aplicación..."
exec "$@"
