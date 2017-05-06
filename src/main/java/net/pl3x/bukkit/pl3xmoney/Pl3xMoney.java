package net.pl3x.bukkit.pl3xmoney;

import net.pl3x.bukkit.pl3xmoney.command.CmdPl3xMoney;
import net.pl3x.bukkit.pl3xmoney.configuration.Config;
import net.pl3x.bukkit.pl3xmoney.configuration.Lang;
import net.pl3x.bukkit.pl3xmoney.hook.VaultHook;
import net.pl3x.bukkit.pl3xmoney.listener.BukkitListener;
import net.pl3x.bukkit.pl3xmoney.manager.MoneyManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

public class Pl3xMoney extends JavaPlugin {
    private MoneyManager moneyManager;

    @Override
    public void onEnable() {
        Config.reload();
        Lang.reload();

        try {
            Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");
            Item.class.getMethod("canEntityPickup", (Class<?>[]) null);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            Logger.error("# Missing needed classes/methods!");
            Logger.error("# This plugin is only compatible with Paper servers!");
            return;
        }

        if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
            Logger.error("# Vault NOT found and/or enabled!");
            Logger.error("# " + getName() + " requires Vault to be installed and enabled!");
            return;
        }

        if (!VaultHook.setupEconomy()) {
            Logger.error("# No economy plugin installed!");
            Logger.error("# This plugin requires a Vault compatible Economy plugin to be installed!");
            return;
        }

        getServer().getPluginManager().registerEvents(new BukkitListener(this), this);

        getCommand("pl3xmoney").setExecutor(new CmdPl3xMoney(this));

        Logger.info(getName() + " v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        Logger.info(getName() + " disabled.");
    }

    public static Pl3xMoney getPlugin() {
        return Pl3xMoney.getPlugin(Pl3xMoney.class);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&4" + getName() + " is disabled. See console log for more information."));
        return true;
    }

    public MoneyManager getMoneyManager() {
        if (moneyManager == null) {
            moneyManager = new MoneyManager();
        }
        return moneyManager;
    }
}