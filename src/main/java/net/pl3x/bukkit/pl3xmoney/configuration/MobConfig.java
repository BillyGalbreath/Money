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

    private File file = null;
    private final Object saveLock = new Object();

    private MobConfig() {
        super();
        file = new File(Pl3xMoney.getPlugin().getDataFolder(), "mobs.yml");
        reload();
    }


    public void reload() {
        synchronized (saveLock) {
            try {
                load(file);
            } catch (Exception ignore) {
            }
        }
    }

    public void save() {
        synchronized (saveLock) {
            try {
                save(file);
            } catch (Exception ignore) {
            }
        }
    }
}
