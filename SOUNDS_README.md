# 🗂 Using Custom Sounds

The plugin allows you to add your own sounds using a **resource pack** and config files.

---

## 📦 Resource Pack Structure

Your sounds should be placed inside the resource pack at the following path:  
`assets/minecraft/sounds/ambience/`

---

### 🎧 File Format

- Only **`.ogg`** files are supported  
- Stereo sounds work, **but** don’t mix well with the plugin’s positioning. Mono audio is recommended, but it’s up to you.

---

## 🧾 Configuring `sounds.json`

In the root of `assets/minecraft/` there must be a `sounds.json` file describing all sounds. Example:

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

## 🌐 Server Setup

1. Upload your finished resource pack to the server and link it in server.properties:

```properties
resource-pack=<URL>
resource-pack-sha1=<SHA1-file-hash>
```

2. Make sure the path and file structure are strictly followed.
3. You can make the resource pack mandatory:

```properties
require-resource-pack=true
```

---

## 📁 biome_sounds.yml — Sound to Biome Mapping

The `biome_sounds.yml` file must be placed in:
`plugins/AtMos/`

Example content:

```yaml
biomes:
  PLAINS:
    day: [minecraft:ambience/biome.plain.day.bird]
    night: [minecraft:ambience/biome.plain.night.cricket]
  MEADOW:
    day: [minecraft:ambience/meadow]
    night: [minecraft:ambience/meadow]
```

⚠️ Important: do not use tabs in .yml files — this will cause parsing errors.

---

## 📍 locations.yml — Custom Locations

Optionally, you can create a `locations.yml` file in the same folder (`plugins/AtMos/`). It specifies coordinates of special zones with their own sounds:

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

Add as many points as you want. All values are case- and YAML-structure-sensitive.

---

## ✅ Testing

After setup:
    Restart the server
    Make sure sounds play under the correct conditions
    Check the console for YAML parsing errors