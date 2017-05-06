package net.pl3x.bukkit.pl3xmoney.listener;

import net.pl3x.bukkit.pl3xmoney.Pl3xMoney;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class BukkitListener implements Listener {
    private final Pl3xMoney plugin;

    public BukkitListener(Pl3xMoney plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // drop money
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Item item = event.getItem();
        if (!plugin.getMoneyManager().isMoney(item)) {
            return;
        }

        double amount = plugin.getMoneyManager().getAmount(item);
        if (amount == Double.MIN_VALUE) {
            return;
        }

        item.remove();
        event.setCancelled(false);

        // pay player
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        Item item = event.getItem();
        if (plugin.getMoneyManager().isMoney(item)) {
            item.setCanEntityPickup(false);
            event.setCancelled(true);
        }
    }
}
