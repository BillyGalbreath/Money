package net.pl3x.bukkit.money.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private static Economy economy = null;

    public static Economy getEconomy() {
        return economy;
    }

    public static boolean setupEconomy() {
        RegisteredServiceProvider<Economy> provider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider != null) {
            economy = provider.getProvider();
        }
        return (economy != null);
    }
}
