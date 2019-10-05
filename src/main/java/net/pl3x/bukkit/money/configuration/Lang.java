package net.pl3x.bukkit.money.configuration;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Lang {
    public static String COMMAND_NO_PERMISSION;
    public static String RECEIVED_AMOUNT;

    private Lang() {
    }

    public static void reload(Plugin plugin) {
        String langFile = Config.LANGUAGE_FILE;
        File configFile = new File(plugin.getDataFolder(), langFile);
        plugin.saveResource(Config.LANGUAGE_FILE, false);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        COMMAND_NO_PERMISSION = config.getString("command-no-permission", "&4You do not have permission for that command!");
        RECEIVED_AMOUNT = config.getString("received-amount", "&aYou picked up {amount}");
    }

    public static void send(CommandSender recipient, String message) {
        if (message == null) {
            return; // do not send blank messages
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        if (ChatColor.stripColor(message).isEmpty()) {
            return; // do not send blank messages
        }

        for (String part : message.split("\n")) {
            recipient.sendMessage(part);
        }
    }
}
