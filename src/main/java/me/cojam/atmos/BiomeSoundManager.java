package me.cojam.atmos;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class BiomeSoundManager {

	private final Map<String, List<String>> dayBiomeSounds = new HashMap<>();
	private final Map<String, List<String>> nightBiomeSounds = new HashMap<>();

	public BiomeSoundManager(File dataFolder) {
		File file = new File(dataFolder, "biome_sounds.yml");
		if (!file.exists()) {
			throw new RuntimeException("Файл biome_sounds.yml не найден!");
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (String biome : config.getConfigurationSection("biomes").getKeys(false)) {
			List<String> day = config.getStringList("biomes." + biome + ".day");
			List<String> night = config.getStringList("biomes." + biome + ".night");

			dayBiomeSounds.put(biome.toUpperCase(), day);
			nightBiomeSounds.put(biome.toUpperCase(), night);
		}
	}

	public String getSound(String biomeName, boolean isNight, Random random) {
		List<String> sounds = isNight ? nightBiomeSounds.get(biomeName.toUpperCase()) : dayBiomeSounds.get(biomeName.toUpperCase());
		if (sounds != null && !sounds.isEmpty()) {
			return sounds.get(random.nextInt(sounds.size()));
		}
		return null;
	}
}
