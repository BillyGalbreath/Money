package net.pl3x.bukkit.pl3xmoney.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    public static boolean COLOR_LOGS = true;
    public static boolean DEBUG_MODE = false;
    public static String LANGUAGE_FILE = "lang-en.yml";
    public static double MEDIUM_AMOUNT = 5.0;
    public static double LARGE_AMOUNT = 10.0;

    private Config() {
    }

    public static void reload(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        COLOR_LOGS = config.getBoolean("color-logs", true);
        DEBUG_MODE = config.getBoolean("debug-mode", false);
        LANGUAGE_FILE = config.getString("language-file", "lang-en.yml");
        MEDIUM_AMOUNT = config.getDouble("medium-amount", 5.0);
        LARGE_AMOUNT = config.getDouble("large-amount", 10.0);
    }
}
