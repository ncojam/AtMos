package me.cojam.atmos;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class AtMos extends JavaPlugin {

	private final List<SoundLocation> soundLocations = new ArrayList<>();
	private String debugMessage;
	private BiomeSoundManager biomeSoundManager;
	private VolumeManager volumeManager;

	@Override
	public void onEnable() {
		//saveDefaultConfig();
		loadSoundLocations();
		try {
			biomeSoundManager = new BiomeSoundManager(getDataFolder());
			getLogger().info("BiomeSoundManager загружен.");
		} catch (Exception e) {
			getLogger().warning("Не удалось загрузить biome_sounds.yml: " + e.getMessage());
		}
		startSoundChecker();
		getLogger().info("AtMos включён.");
		
		volumeManager = new VolumeManager(getDataFolder());
		getCommand("atmosvol").setExecutor((sender, command, label, args) -> {
			if (!(sender instanceof Player player)) {
				sender.sendMessage("§cТолько игрок может использовать эту команду.");
				return true;
			}
			if (args.length != 1) {
				player.sendMessage("§cИспользование: /atmosvol <громкость от 0.0 до 1.0>");
				return true;
			}
			try {
				float vol = Float.parseFloat(args[0]);
				if (vol < 0.0f || vol > 1.0f) {
					player.sendMessage("§cГромкость должна быть от 0.0 до 1.0");
					return true;
				}
				volumeManager.setVolume(player, vol);
				player.sendMessage("§aГромкость установлена на: " + vol);
			} catch (NumberFormatException e) {
				player.sendMessage("§cНекорректное число.");
			}
			return true;
		});

	}

	@Override
	public void onDisable() {
		getLogger().info("AtMos отключён.");
	}

	private void loadSoundLocations() {
		File file = new File(getDataFolder(), "locations.yml");
		if (!file.exists()) {
			saveResource("locations.yml", false);
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		List<Map<?, ?>> rawList = config.getMapList("locations");

		for (Map<?, ?> map : rawList) {
			try {
				String world = (String) map.get("world");
				Object x = map.get("x");
				Object y = map.get("y");
				Object z = map.get("z");
				Object radius = map.get("radius");
				List<String> daySounds = (List<String>) map.get("daySounds");
				List<String> nightSounds = (List<String>) map.get("nightSounds");
				List<String> rainSounds = (List<String>) map.get("rainSounds");

				soundLocations.add(new SoundLocation(world, x, y, z, radius, daySounds, nightSounds, rainSounds));
			} catch (Exception e) {
				getLogger().warning("Ошибка при загрузке точки звука: " + e.getMessage());
			}
		}
		getLogger().info("Загружено точек: " + soundLocations.size());
	}

	private void startSoundChecker() {
		new BukkitRunnable() {
			Random random = new Random();

			@Override
			public void run() {
				//getLogger().info("Starting the sound check task..."); // Отладочное сообщение о начале задачи
				Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
				//getLogger().info("Online players: " + onlinePlayers.size()); // Отладочное сообщение с количеством игроков

				if (onlinePlayers.isEmpty()) {
					//getLogger().info("No players online. Skipping the check."); // Если игроков нет, выводим сообщение
				}

				for (Player player : onlinePlayers) {
					
					boolean found = false;
					//getLogger().info("Checking player: " + player.getName()); // Отладочное сообщение перед проверкой каждого игрока
					for (SoundLocation sl : soundLocations) {
						//getLogger().info("Checking sound location: " + sl.world); // Сообщение перед проверкой локации

						if (!player.getWorld().getName().equals(sl.world)) {
							//getLogger().info("Player is not in the correct world. Skipping sound location."); // Если мир не совпадает
							continue;
						}

						Location loc = new Location(player.getWorld(), sl.x, sl.y, sl.z);
						//getLogger().info("Sound location: " + loc.toString()); // Сообщение с координатами локации

						 //Собираем информацию для отладки
						String debugMessage = "Checking Zone: " + sl.world + " | Sound Location: (" + sl.x + ", " + sl.y + ", " + sl.z + ") | Radius: " + sl.radius;

						double distance = player.getLocation().distance(loc);
						//getLogger().info("Distance from player to sound location: " + distance); // Сообщение с расстоянием

						// Добавляем информацию о расстоянии в строку
						debugMessage += " | Distance from player to sound location: " + distance;

						// Выводим отладочную информацию в чат игрока
						//player.sendMessage("§7[DEBUG] " + debugMessage);

						// Проверка расстояния
						if (distance <= sl.radius) {
							//getLogger().info("Player " + player.getName() + " is within the zone."); // Если игрок в зоне
							String sound = getAppropriateSound(sl, player, random);
							if (sound != null) {
								Location soundLoc = generateRandomLocation(player.getLocation(), 5);
								float vol = volumeManager.getVolume(player);
								player.playSound(soundLoc, sound, vol, 1.0f);
							}
							found = true;
							break;
						} else {
							//getLogger().info("Player " + player.getName() + " is outside the zone."); // Если игрок вне зоны
						}
					}
					
					if (!found && biomeSoundManager != null) {
						String biomeName = player.getLocation().getBlock().getBiome().name();
						boolean isNight = player.getWorld().getTime() > 12500 && player.getWorld().getTime() < 23500;

						String fallbackSound = biomeSoundManager.getSound(biomeName, isNight, random);
						if (fallbackSound != null) {
							Location soundLoc = generateRandomLocation(player.getLocation(), 5);
							float vol = volumeManager.getVolume(player);
							player.playSound(soundLoc, fallbackSound, vol, 1.0f);
							//player.sendMessage("§7[AtMos] §cМы не в радиусе. Биом: §e" + biomeName + "§c, звук: §b" + fallbackSound);
						}
					}
				}
			}
		}.runTaskTimer(this, 0L, 40L); // 2 секунд
		
		// Сообщение каждые 3 секунды
		/*new BukkitRunnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					boolean inZone = false;
					for (SoundLocation sl : soundLocations) {
						if (!player.getWorld().getName().equals(sl.world)) continue;

						Location loc = new Location(player.getWorld(), sl.x, sl.y, sl.z);
						if (player.getLocation().distance(loc) <= sl.radius) {
							inZone = true;
							break;
						}
					}
					if (inZone) {
						player.sendMessage("§a✅ Вы находитесь в зоне звука!");
					} else {
						player.sendMessage("§c❌ Вы вне зоны.");
					}
				}
			}
		}.runTaskTimer(this, 0L, 60L); // каждые 3 сек (60 тиков)*/
	}

	private String getAppropriateSound(SoundLocation sl, Player player, Random random) {
		long time = player.getWorld().getTime();
		boolean isNight = time > 12500 && time < 23500;
		boolean isRaining = player.getWorld().hasStorm();

		if (isRaining && sl.rainSounds != null && !sl.rainSounds.isEmpty()) {
			return sl.rainSounds.get(random.nextInt(sl.rainSounds.size()));
		}
		if (isNight && sl.nightSounds != null && !sl.nightSounds.isEmpty()) {
			return sl.nightSounds.get(random.nextInt(sl.nightSounds.size()));
		}
		if (!isNight && sl.daySounds != null && !sl.daySounds.isEmpty()) {
			return sl.daySounds.get(random.nextInt(sl.daySounds.size()));
		}
		return null;
	}

	private Location generateRandomLocation(Location center, double radius) {
		Random rand = new Random();
		double xOffset = (rand.nextDouble() - 0.5) * 2 * radius;
		double yOffset = (rand.nextDouble() - 0.3) * 2;
		double zOffset = (rand.nextDouble() - 0.5) * 2 * radius;
		return center.clone().add(xOffset, yOffset, zOffset);
	}
}