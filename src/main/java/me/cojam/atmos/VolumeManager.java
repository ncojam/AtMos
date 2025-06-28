package me.cojam.atmos;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class VolumeManager {
	private final File file;
	private final FileConfiguration config;
	private final HashMap<UUID, Float> volumeMap = new HashMap<>();

	public VolumeManager(File dataFolder) {
		this.file = new File(dataFolder, "volumes.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.config = YamlConfiguration.loadConfiguration(file);
		loadVolumes();
	}

	private void loadVolumes() {
		for (String key : config.getKeys(false)) {
			try {
				UUID uuid = UUID.fromString(key);
				float volume = (float) config.getDouble(key);
				volumeMap.put(uuid, volume);
			} catch (IllegalArgumentException e) {
				// Невалидный UUID — пропускаем
			}
		}
	}

	public float getVolume(Player player) {
		return volumeMap.getOrDefault(player.getUniqueId(), 1.0f);
	}

	public void setVolume(Player player, float volume) {
		volumeMap.put(player.getUniqueId(), volume);
		config.set(player.getUniqueId().toString(), volume);
		save();
	}

	private void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
