package net.pl3x.bukkit.pl3xmoney.listener;

import com.sk89q.craftbook.mechanics.pipe.PipeRequestEvent;
import net.pl3x.bukkit.pl3xmoney.Pl3xMoney;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CraftBookListener implements Listener {
    private final Pl3xMoney plugin;

    public CraftBookListener(Pl3xMoney plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRangeCollectorCollects(PipeRequestEvent event) {
        for (ItemStack item : event.getItems()) {
            if (plugin.getMoneyManager().isMoney(item)) {
                event.setCancelled(true);
                event.setItems(new ArrayList<>());
            }
        }
    }
}
