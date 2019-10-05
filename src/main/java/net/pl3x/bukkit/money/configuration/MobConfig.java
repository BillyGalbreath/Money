package net.pl3x.bukkit.money.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class MobConfig extends YamlConfiguration {
    private static MobConfig config;
    private static Plugin instance;

    public static MobConfig getConfig() {
        if (config == null) {
            config = new MobConfig();
        }
        return config;
    }

    public static void reload(Plugin plugin) {
        instance = plugin;
        if (!new File(instance.getDataFolder(), "mobs.yml").exists()) {
            instance.saveResource("mobs.yml", false);
        }
        getConfig().reload();
    }

    private File file;
    private final Object saveLock = new Object();

    private MobConfig() {
        super();
        file = new File(instance.getDataFolder(), "mobs.yml");
        reload();
    }


    private void reload() {
        synchronized (saveLock) {
            try {
                load(file);
            } catch (Exception ignore) {
            }
        }
    }
}
