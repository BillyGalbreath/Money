package net.pl3x.bukkit.money.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
    public static String LANGUAGE_FILE = "lang-en.yml";

    public static void reload(Plugin plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        LANGUAGE_FILE = config.getString("language-file", "lang-en.yml");
    }
}
