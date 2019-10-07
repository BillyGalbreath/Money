package net.pl3x.bukkit.money.manager;

import net.pl3x.bukkit.money.configuration.MobConfig;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class MobManager {
    private static final HashMap<EntityType, Amount> mobAmounts = new HashMap<>();

    public static void reload(Plugin plugin) {
        mobAmounts.clear();

        MobConfig.reload(plugin);
        MobConfig mobConfig = MobConfig.getConfig();

        for (EntityType type : EntityType.values()) {
            ConfigurationSection section = mobConfig.getConfigurationSection(type.name());
            if (section == null) {
                continue; // mob not set
            }

            double min = section.getDouble("min", 0D);
            double max = section.getDouble("max", 0D);

            if (min <= 0D || max <= 0D) {
                continue; // nothing to drop
            }

            mobAmounts.put(type, new Amount(min, max));
        }
    }

    public static Double getMobAmount(EntityType type) {
        Amount amount = mobAmounts.get(type);
        return amount == null ? Double.MIN_VALUE : amount.getRandom();
    }

    private static class Amount {
        private final double min;
        private final double max;

        Amount(double min, double max) {
            this.min = min;
            this.max = max;
        }

        double getRandom() {
            return ThreadLocalRandom.current().nextDouble(min, max);
        }
    }
}
