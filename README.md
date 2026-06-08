# MellVis — как собрать и использовать

MellVis — клиентский Fabric-мод под Minecraft `1.21.4`. Проект уже содержит Gradle/Fabric конфигурацию, Java-код, ресурсы Fabric и mixin-конфиг.


## Самый простой вариант для Chromebook: сборка через GitHub

Если локальная Linux-среда на Chromebook снова ломается на Gradle/Java, **не собирай локально**. Используй GitHub Actions — jar соберётся в облаке.

1. Загрузи этот проект в GitHub-репозиторий.
2. Открой вкладку **Actions**.
3. Выбери workflow **Build MellVis**.
4. Нажми **Run workflow**.
5. Дождись зелёной галочки.
6. Открой завершённый run и скачай artifact **MellVis-Fabric-1.21.4**.
7. Внутри будет готовый `mellvis-1.0.0.jar`.
8. Положи jar в `.minecraft/mods` вместе с Fabric API для Minecraft `1.21.4`.

Этот способ обычно проще Chromebook Linux, потому что GitHub сам ставит JDK 21 и Gradle cache.

## Быстрая локальная сборка на Chromebook

Если всё-таки хочешь собрать локально, установи Java 21 и Gradle:

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk gradle
```

Потом из папки проекта запусти готовый скрипт:

```bash
./scripts/build-chromebook.sh
```

Скрипт сам проверит, что используется Java 21, запустит Gradle и покажет путь к готовому jar.

## 1. Что нужно установить

1. **JDK 21**. Minecraft 1.21.x и Fabric Loom должны собираться на Java 21. Не используйте Java 25 для сборки этого проекта: Gradle/Kotlin DSL или Loom могут падать на новых версиях JDK.
2. **Gradle 8.x**. Если у вас нет `gradle`, откройте проект в IntelliJ IDEA как Gradle-проект или установите Gradle отдельно.
3. **Minecraft Launcher** с установленным **Fabric Loader** для Minecraft `1.21.4`.
4. **Fabric API** для Minecraft `1.21.4` — этот jar нужен в папке `mods` вместе с MellVis.

Версии проекта лежат в `gradle.properties`:

- `minecraft_version=1.21.4`
- `loader_version=0.16.10`
- `fabric_version=0.119.4+1.21.4`
- `yarn_mappings=1.21.4+build.8`

## 2. Как скомпилировать

Откройте терминал в папке проекта и выполните:

```bash
cd /path/to/MellVis
java -version
gradle clean build --refresh-dependencies
```

Если у вас несколько Java, явно укажите JDK 21:

### Linux/macOS

```bash
export JAVA_HOME=/path/to/jdk-21
export PATH="$JAVA_HOME/bin:$PATH"
gradle clean build --refresh-dependencies
```

### Windows PowerShell

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
gradle clean build --refresh-dependencies
```

После успешной сборки готовый jar будет в:

```text
build/libs/mellvis-1.0.0.jar
```

Если Gradle создаст ещё `*-sources.jar`, его в Minecraft класть не нужно — нужен обычный mod jar без `sources` в названии.

## 3. Если сборка не запускается

### Ошибка про Java 25 / Kotlin / `IllegalArgumentException: 25.0.2`

Запустите сборку на JDK 21, а не на JDK 25:

```bash
JAVA_HOME=/path/to/jdk-21 PATH=/path/to/jdk-21/bin:$PATH gradle clean build
```

### Ошибка `Plugin [id: 'fabric-loom'] was not found`

Проверьте интернет и доступ к Fabric Maven. В `settings.gradle.kts` уже прописан репозиторий:

```kotlin
maven("https://maven.fabricmc.net/")
```

Если вы в стране/сети, где Maven блокируется, включите VPN/прокси или попробуйте другую сеть, затем выполните:

```bash
gradle clean build --refresh-dependencies
```

## 4. Как установить jar в Minecraft

1. Установите Fabric Loader для Minecraft `1.21.4` через Fabric Installer.
2. Откройте папку Minecraft:
   - Windows: `%APPDATA%\.minecraft`
   - Linux: `~/.minecraft`
   - macOS: `~/Library/Application Support/minecraft`
3. Создайте папку `mods`, если её нет.
4. Положите туда:
   - `build/libs/mellvis-1.0.0.jar`
   - jar **Fabric API** для Minecraft `1.21.4`
5. Запустите профиль Fabric `1.21.4` в лаунчере.

## 5. Как пользоваться в игре

- **Right Shift** — открыть ClickGUI.
- В ClickGUI:
  - ЛКМ по модулю — включить/выключить.
  - ПКМ по модулю — раскрыть настройки.
  - ЛКМ по `ModeSetting` — переключить режим.
  - ЛКМ по `NumberSetting` — выставить значение слайдера.
  - ЛКМ по `Bind` — назначить клавишу, `ESC` снимает бинд.

Стандартные бинды:

| Модуль | Бинд | Что делает |
|---|---:|---|
| ClickGUI | Right Shift | Открывает меню |
| AutoSwap | G | Ручной swap offhand между двумя выбранными предметами |
| ElytraSwap | H | Ручной swap элитры и нагрудника |
| ElytraSwap FireworkBind | V | Берёт фейерверк в руку, использует, через 500 мс возвращает выбранный слот |
| AutoSprint | не назначен | Автоспринт при движении вперёд |

HUD-модули `Watermark`, `ActivePotions`, `TargetHUD`, `CustomParticles` и `TotemAngel` включены по умолчанию.

## 6. Конфиги `.cfg`

Команды вводятся в игровой чат как client-команды:

```text
.cfg save default
.cfg save pvp
.cfg load pvp
.cfg list
```

При закрытии ClickGUI автоматически сохраняется `default.cfg`. Конфиги лежат в папке Fabric config:

```text
.minecraft/config/mellvis/*.cfg
```

## 7. Важное про AutoSwap/ElytraSwap

В проекте намеренно нет обходов античита. `AutoSwap` и `ElytraSwap` — это обычные ручные действия по нажатию клавиш, без bypass-логики, задержек для обхода проверок или пакетных трюков.
