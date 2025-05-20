# 🗂 Использование пользовательских звуков

Плагин позволяет подключать собственные звуки с помощью **ресурс-пака** и настроечных файлов.

---

## 📦 Структура ресурс-пака

Ваши звуки должны располагаться по следующему пути внутри ресурс-пака:
`assets/minecraft/sounds/ambience/`

---

### 🎧 Формат файлов

- Поддерживаются **только `.ogg`** файлы
- Стерео-звуки работают, **но** плохо сочетаются с позиционированием плагина. Рекомендуется использовать **моно**-аудио, хотя тут как сами хотите

---

## 🧾 Конфигурация `sounds.json`

В корне `assets/minecraft/` должен находиться файл `sounds.json` с описанием всех звуков. Пример:

```json
{
	"ambience/wood.bridge": {
		"sounds": [
			{ "name": "ambience/wood.bridge1", "stream": false },
			{ "name": "ambience/wood.bridge2", "stream": false }
		]
	},
	"ambience/meadow": {
		"sounds": [
			{ "name": "ambience/meadow1", "stream": false }
		]
	}
}
```

---

## 🌐 Подключение на сервере

1. Поместите готовый ресурс-пак на сервер и подключите его в server.properties:

```properties
resource-pack=<URL>
resource-pack-sha1=<SHA1-хеш файла>
```

2. Убедитесь, что путь и структура файлов строго соблюдаются.
3. Можете сделать ресурс-пак обязательным:

```properties
require-resource-pack=true
```

---

## 📁 biome_sounds.yml — соответствие звуков и биомов

Файл biome_sounds.yml должен быть размещён в папке:
`plugins/AtMos/`

Пример содержимого:

```yaml
biomes:
  PLAINS:
    day: [minecraft:ambience/biome.plain.day.bird]
    night: [minecraft:ambience/biome.plain.night.cricket]
  MEADOW:
    day: [minecraft:ambience/meadow]
    night: [minecraft:ambience/meadow]
```

⚠️ Важно: не используйте табуляцию в .yml — это приведёт к ошибкам.

---

## 📍 locations.yml — кастомные локации

Опционально вы можете создать файл locations.yml в той же папке (plugins/AtMos/). В нём указываются координаты особых зон со своими звуками:

```yaml
locations:
  - world: world
    x: 1360
    y: 97
    z: 173
    radius: 16
    daySounds:
      - minecraft:ambience/wood.bridge
    nightSounds:
      - minecraft:ambience/wood.bridge
    rainSounds:
      - minecraft:ambience/wood.bridge

```

Добавьте столько точек, сколько хотите. Все значения чувствительны к регистру и структуре YAML.

---

## ✅ Проверка

После настройки:
    Перезапустите сервер
    Убедитесь, что звуки проигрываются в нужных условиях
    Проверяйте консоль на наличие ошибок парсинга YAML