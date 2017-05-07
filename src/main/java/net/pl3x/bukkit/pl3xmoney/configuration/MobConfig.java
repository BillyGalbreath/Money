package net.pl3x.bukkit.pl3xmoney.configuration;

import net.pl3x.bukkit.pl3xmoney.Pl3xMoney;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MobConfig extends YamlConfiguration {
    private static MobConfig config;

    public static MobConfig getConfig() {
        if (config == null) {
            config = new MobConfig();
        }
        return config;
    }

    public static void reloadConfig() {
        Pl3xMoney.getPlugin().saveResource("mobs.yml", false);
        getConfig().reload();
    }

    private File file = null;
    private final Object saveLock = new Object();

    private MobConfig() {
        super();
        file = new File(Pl3xMoney.getPlugin().getDataFolder(), "mobs.yml");
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
