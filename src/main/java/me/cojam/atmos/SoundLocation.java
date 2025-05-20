package me.cojam.atmos;

import java.util.List;

public class SoundLocation {
    public final String world;
    public final double x, y, z, radius;
    public final List<String> daySounds;
    public final List<String> nightSounds;
    public final List<String> rainSounds;

    public SoundLocation(String world, Object x, Object y, Object z, Object radius,
                         List<String> daySounds, List<String> nightSounds, List<String> rainSounds) {
        if (world == null || world.isEmpty()) {
            throw new IllegalArgumentException("World name cannot be null or empty");
        }
        if (!(radius instanceof Number) || ((Number) radius).doubleValue() <= 0){
            throw new IllegalArgumentException("Radius must be greater than zero");
        }
		if (x == null || y == null || z == null || !(x instanceof Number) || !(y instanceof Number) || !(z instanceof Number)) {
            throw new IllegalArgumentException("Coordinates must be valid numbers");
        }
        
        this.world = world;
        this.x = ((Number) x).doubleValue();
        this.y = ((Number) y).doubleValue();
        this.z = ((Number) z).doubleValue();
        this.radius = ((Number) radius).doubleValue();

        // Проверка инициализации списков
        this.daySounds = daySounds != null ? daySounds : List.of();
        this.nightSounds = nightSounds != null ? nightSounds : List.of();
        this.rainSounds = rainSounds != null ? rainSounds : List.of();
    }

    // Опционально: Добавить геттеры и другие полезные методы
}
