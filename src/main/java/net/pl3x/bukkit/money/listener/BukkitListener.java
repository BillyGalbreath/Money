package net.pl3x.bukkit.money.listener;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.EconomyResponse;
import net.pl3x.bukkit.money.Money;
import net.pl3x.bukkit.money.configuration.Lang;
import net.pl3x.bukkit.money.hook.VaultHook;
import net.pl3x.bukkit.money.manager.ItemManager;
import net.pl3x.bukkit.money.manager.MobManager;
import net.pl3x.bukkit.money.sound.Tune;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BukkitListener implements Listener {
    private final Money plugin;

    public BukkitListener(Money plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            return; // only handle mob deaths
        }

        LivingEntity entity = event.getEntity();
        if (!entity.hasAI()) {
            return; // mob doesnt have AI
        }

        if (entity.fromMobSpawner()) {
            return; // mob came from spawner cage
        }

        if (entity.getKiller() == null) {
            return; // mob not killed by player
        }

        double amount = MobManager.getMobAmount(entity.getType());
        if (amount <= 0D) {
            return; // no amount to drop
        }

        // spawn the money
        ItemManager.spawnMoney(entity.getLocation(), amount);
    }

    @EventHandler
    public void onPlayerPickupMoney(PlayerAttemptPickupItemEvent event) {
        Item item = event.getItem();
        if (!ItemManager.isMoney(item)) {
            return; // not a money item
        }

        double amount = ItemManager.getAmount(item);
        if (amount <= 0D) {
            return; // not a valid money value
        }

        // do not pick up the item
        event.setCancelled(true);

        // pay the player
        Player player = event.getPlayer();
        EconomyResponse response = VaultHook.getEconomy().depositPlayer(player, amount);
        if (!response.transactionSuccess()) {
            return; // could not pay the player
        }

        // make it appear like it's being picked up
        event.setFlyAtPlayer(true);

        // play a unique sound
        Tune.COIN_PICKUP.playTune(plugin, player);

        // notify the player
        String formattedAmount = ItemManager.FORMAT.format(amount);
        player.sendActionBar(ChatColor.translateAlternateColorCodes('&',
                Lang.RECEIVED_AMOUNT.replace("{amount}", formattedAmount)));

        // remove the item after it animates for a bit
        new BukkitRunnable() {
            @Override
            public void run() {
                item.remove();
            }
        }.runTaskLater(plugin, 5);
    }

    @EventHandler
    public void onEntityPickupMoney(EntityPickupItemEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            Item item = event.getItem();
            if (ItemManager.isMoney(item)) {
                item.setCanMobPickup(false);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHopperPickupMoney(InventoryPickupItemEvent event) {
        if (ItemManager.isMoney(event.getItem())) {
            event.setCancelled(true);
        }
    }
}
