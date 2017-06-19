package net.pl3x.bukkit.pl3xmoney;

import net.pl3x.bukkit.pl3xmoney.command.CmdPl3xMoney;
import net.pl3x.bukkit.pl3xmoney.configuration.Config;
import net.pl3x.bukkit.pl3xmoney.configuration.Lang;
import net.pl3x.bukkit.pl3xmoney.configuration.MobConfig;
import net.pl3x.bukkit.pl3xmoney.hook.VaultHook;
import net.pl3x.bukkit.pl3xmoney.listener.BukkitListener;
import net.pl3x.bukkit.pl3xmoney.listener.CraftBookListener;
import net.pl3x.bukkit.pl3xmoney.manager.MobManager;
import net.pl3x.bukkit.pl3xmoney.manager.MoneyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

public class Pl3xMoney extends JavaPlugin {
    private MoneyManager moneyManager;
    private MobManager mobManager;

    @Override
    public void onEnable() {
        Config.reload(this);
        Lang.reload(this);
        MobConfig.reloadConfig();

        try {
            Class.forName("org.bukkit.event.player.PlayerAttemptPickupItemEvent");
            Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");
            Item.class.getMethod("canMobPickup");
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

        getMobManager().reloadMobs(); // load mob data

        getServer().getPluginManager().registerEvents(new BukkitListener(this), this);

        if (getServer().getPluginManager().isPluginEnabled("CraftBook")) {
            Logger.info("CraftBook found. Hooking into it...");
            getServer().getPluginManager().registerEvents(new CraftBookListener(this), this);
        }

        getCommand("pl3xmoney").setExecutor(new CmdPl3xMoney(this));

        Logger.info(getName() + " v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getWorlds().forEach(world -> world.getLivingEntities().stream()
                .filter(Entity::fromMobSpawner)
                .forEach(Entity::remove));

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

    public MobManager getMobManager() {
        if (mobManager == null) {
            mobManager = new MobManager();
        }
        return mobManager;
    }
}
