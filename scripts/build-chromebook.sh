#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

need_cmd() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "[ERROR] Не найдена команда: $1"
    echo "Установи её и запусти скрипт ещё раз."
    exit 1
  fi
}

need_cmd java
need_cmd gradle

JAVA_VERSION_OUTPUT="$(java -version 2>&1 | head -n 1)"
echo "[INFO] Java: $JAVA_VERSION_OUTPUT"

if ! java -version 2>&1 | grep -Eq 'version "21\.|openjdk version "21\.'; then
  echo "[ERROR] Нужна Java/JDK 21. Сейчас используется другая версия."
  echo "Chromebook Debian/Ubuntu команда установки обычно такая:"
  echo "  sudo apt update && sudo apt install -y openjdk-21-jdk gradle"
  echo "Потом проверь:"
  echo "  java -version"
  exit 1
fi

echo "[INFO] Gradle: $(gradle --version | sed -n '3p' | xargs || true)"
echo "[INFO] Запускаю сборку MellVis..."

gradle clean build --refresh-dependencies --no-daemon

JAR="$(find build/libs -maxdepth 1 -type f -name '*.jar' ! -name '*-sources.jar' | head -n 1 || true)"
if [[ -z "$JAR" ]]; then
  echo "[ERROR] Сборка закончилась, но jar не найден в build/libs."
  exit 1
fi

echo ""
echo "[OK] Готово: $JAR"
echo "[OK] Скопируй этот файл в ~/.minecraft/mods вместе с Fabric API для Minecraft 1.21.4"
