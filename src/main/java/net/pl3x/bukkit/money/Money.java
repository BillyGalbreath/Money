package net.pl3x.bukkit.money;

import net.pl3x.bukkit.money.command.CmdMoney;
import net.pl3x.bukkit.money.configuration.Config;
import net.pl3x.bukkit.money.configuration.Lang;
import net.pl3x.bukkit.money.configuration.MobConfig;
import net.pl3x.bukkit.money.hook.VaultHook;
import net.pl3x.bukkit.money.listener.BukkitListener;
import net.pl3x.bukkit.money.manager.ItemManager;
import net.pl3x.bukkit.money.manager.MobManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Money extends JavaPlugin {
    @Override
    public void onEnable() {
        Config.reload(this);
        Lang.reload(this);
        MobConfig.reload(this);

        if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
            getLogger().severe("Vault NOT found and/or enabled!");
            getLogger().severe(getName() + " requires Vault to be installed and enabled!");
            return;
        }

        if (!VaultHook.setupEconomy()) {
            getLogger().severe("No economy plugin installed!");
            getLogger().severe(getName() + "requires a Vault compatible Economy plugin to be installed!");
            return;
        }

        MobManager.reload(); // load mob data

        getServer().getPluginManager().registerEvents(new BukkitListener(this), this);

        if (getServer().getPluginManager().isPluginEnabled("CraftBook")) {
            getLogger().info("CraftBook found. Activating hook..");
            getServer().getPluginManager().registerEvents(new Listener() {
                @EventHandler
                public void onPipeRequest(com.sk89q.craftbook.mechanics.pipe.PipeRequestEvent event) {
                    for (ItemStack item : event.getItems()) {
                        if (ItemManager.isMoney(item)) {
                            event.setCancelled(true);
                            event.setItems(new ArrayList<>());
                        }
                    }
                }
            }, this);
        }

        getCommand("money").setExecutor(new CmdMoney(this));
    }

    @Override
    public void onDisable() {
        Bukkit.getWorlds().forEach(world -> world.getLivingEntities().stream()
                .filter(Entity::fromMobSpawner)
                .forEach(Entity::remove));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Lang.send(sender, "&4" + getName() + " is disabled. See console log for more information.");
        return true;
    }
}
