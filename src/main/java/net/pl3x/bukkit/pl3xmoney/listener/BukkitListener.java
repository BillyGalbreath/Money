package net.pl3x.bukkit.pl3xmoney.listener;

import net.milkbowl.vault.economy.EconomyResponse;
import net.pl3x.bukkit.pl3xmoney.Amount;
import net.pl3x.bukkit.pl3xmoney.Logger;
import net.pl3x.bukkit.pl3xmoney.Mob;
import net.pl3x.bukkit.pl3xmoney.Pl3xMoney;
import net.pl3x.bukkit.pl3xmoney.configuration.Lang;
import net.pl3x.bukkit.pl3xmoney.hook.VaultHook;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
    public void onPlayerDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getType() != EntityType.PLAYER) {
            return; // only handle players here
        }

        // drop % of player's money
        //
        //
        //
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getType() == EntityType.PLAYER) {
            return; // only handle mobs here
        }

        Player killer = entity.getKiller();
        if (killer == null) {
            return; // mob not killed by player
        }

        Mob mob = Mob.getMob(entity);
        if (mob == null) {
            return; // mob is not registered
        }

        Amount amount = plugin.getMobManager().getMobAmount(mob.name());
        if (amount == null) {
            return; // mob is not configured
        }

        Location droppedLocation = entity.getLocation();
        double droppedAmount = amount.getRandom();
        if (droppedAmount <= 0D) {
            return; // no amount to drop
        }

        plugin.getMoneyManager().spawnMoney(droppedLocation, droppedAmount);

        Logger.debug(mob.name() + " dropped "
                + plugin.getMoneyManager().FORMAT.format(droppedAmount)
                + " at " + droppedLocation);
    }

    @EventHandler
    public void onPlayerPickupMoney(PlayerPickupItemEvent event) {
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

        Player player = event.getPlayer();
        EconomyResponse response = VaultHook.getEconomy().depositPlayer(player, amount);
        if (!response.transactionSuccess()) {
            Logger.error("Error giving " + player.getName() + " money: ");
            Logger.error(response.errorMessage);
            return;
        }

        String formattedAmount = plugin.getMoneyManager().FORMAT.format(amount);
        player.sendActionBar(Lang.RECEIVED_AMOUNT
                .replace("{amount}", formattedAmount));

        Logger.debug(player.getName() + " picked up "
                + formattedAmount + " at " + player.getLocation());
    }

    @EventHandler
    public void onMobPickupMoney(EntityPickupItemEvent event) {
        Item item = event.getItem();
        if (plugin.getMoneyManager().isMoney(item)) {
            item.setCanEntityPickup(false);
            event.setCancelled(true);
        }
    }
}
