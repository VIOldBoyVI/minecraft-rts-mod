# Minecraft RTS Mod

Real-Time Strategy mechanics for Minecraft 1.21 (Forge).

## Требования

- Java 21+
- Minecraft Forge 1.21-51.0.33

## Сборка и запуск

```bash
# Первый запуск — скачает Forge и сгенерирует среду (~5 мин)
./gradlew genEclipseRuns   # или genIntellijRuns для IDEA

# Запустить клиент прямо из проекта
./gradlew runClient

# Собрать .jar для установки в настоящий Minecraft
./gradlew build
# готовый файл: build/libs/rtsmod-1.0.0.jar
```

## Установка готового .jar

Скопируйте `rtsmod-1.0.0.jar` в папку `mods/` вашего Forge-профиля.

## Команды

| Команда | Описание |
|---|---|
| `/rts mode` | Включить / выключить RTS-режим |
| `/rts select <entityId>` | Добавить моба в выделение |
| `/rts deselect` | Сбросить выделение |
| `/rts list` | Показать выбранные юниты |
| `/rts move <x y z>` | Переместить юниты к позиции |
| `/rts attack <x y z>` | Атаковать-двигаться к позиции |
| `/rts stop` | Остановить все выбранные юниты |

## Как узнать ID моба

Включите отображение entityId командой:
```
/scoreboard objectives add id dummy "Entity ID"
/scoreboard objectives setdisplay list id
```
Или наведитесь на моба с включённым `F3` — ID указан в отладочном экране.

## Архитектура

```
RTSMod.java            — точка входа, регистрация
core/
  RTSPlayer.java       — состояние RTS для каждого игрока (выделение, режим, приказ)
  MoveOrder.java       — запись приказа (позиция + тип)
  RTSEventHandler.java — очистка при выходе игрока
units/
  UnitManager.java     — исполнение приказов (move / attack-move / stop)
commands/
  RTSCommands.java     — Brigadier-команды /rts *
network/
  RTSNetwork.java      — SimpleChannel (заготовка для HUD-пакетов)
```
