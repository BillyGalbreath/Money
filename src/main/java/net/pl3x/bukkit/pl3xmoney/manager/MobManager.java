package net.pl3x.bukkit.pl3xmoney.manager;

import net.pl3x.bukkit.pl3xmoney.Amount;
import net.pl3x.bukkit.pl3xmoney.Mob;
import net.pl3x.bukkit.pl3xmoney.configuration.MobConfig;

import java.util.HashMap;

public class MobManager {
    private final HashMap<String, Amount> mobAmounts = new HashMap<>();

    public void reloadMobs() {
        mobAmounts.clear();

        MobConfig mobConfig = MobConfig.getConfig();
        for (Mob mob : Mob.values()) {
            if (mobConfig.get(mob.name()) == null) {
                continue; // mob not set
            }

            double min = mobConfig.getDouble(mob.name() + ".min", 0);
            double max = mobConfig.getDouble(mob.name() + ".max", 0);

            if (min == 0D && max == 0D) {
                continue; // nothing to drop
            }

            mobAmounts.put(mob.name(), new Amount(min, max));
        }
    }

    public Amount getMobAmount(String name) {
        return mobAmounts.get(name);
    }
}
