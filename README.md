# AtMos

A Minecraft plugin for atmospheric sounds

This plugin adds ambient sounds to Minecraft, similar to what the MAtmos mod does.
It supports Paper servers (and forks).

## 🌸 Why this project?

Starting with Minecraft 1.20.4, MAtmos was only released for NeoForge. I ran into issues setting it up, and in the end, it was easier to just write my own plugin.

Unlike MAtmos, this plugin:
- Runs server-side (no client installation required)
- Is compatible with Paper and its forks
- Supports additional features not present in the original

## 🚀 Getting Started

To build the project, you’ll need:

1. **Java 17 or higher**
2. **Maven** (install from the [official website](https://maven.apache.org/))
3. Clone the repository:
    ```bash
    git clone https://github.com/ncojam/AtMos.git
    ```
4. Build the plugin:
    ```bash
    mvn clean package
    ```
5. The final `.jar` file will be in the `target/` folder

## 🛠 Installing on the Server

1. Drop the `.jar` file from `target/` into the `plugins/` folder of your Paper server
2. Add the required sound files to your resource pack
3. Put `biome_sounds.yml` and (optionally) `locations.yml` into `plugins/AtMos`
4. Add your custom locations to `plugins/AtMos/locations.yml` if needed
5. Make sure the sounds listed in `biome_sounds.yml` exist in your resource pack

## What to do about sounds

Some sounds used during development were taken from the original MAtmos (licensed under WTFPLv2), and some from other sources — I haven’t verified all the licensing details yet. I plan to release a sound pack with examples later.

A detailed guide to the sound structure can be found in [SOUNDS_README.md](SOUNDS_README.md)

## ⏳ Plugin Features

- Biome-based ambient sounds (day/night)
- Ambient sounds in specific locations
### to do:
- Time-of-day dependent sounds in custom locations
- Player position detection (in a house, cave, sky, underwater, etc.)
- Commands

## 🔮 Getting Help

If you run into issues:
- Open an issue here on GitHub
- Or contact me:
    - Telegram: [@cojam](https://t.me/cojam)
    - Discord: [@zcojam](cojam#6302)

## 👤 Support and Contribution

Author: cojam  
Currently maintained solely by me

## 📋 License

This project is licensed under [WTFPLv2 with a disclaimer of liability]. See the [LICENSE](./LICENSE.md) file for details.
