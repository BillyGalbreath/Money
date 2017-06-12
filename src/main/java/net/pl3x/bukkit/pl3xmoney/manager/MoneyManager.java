package net.pl3x.bukkit.pl3xmoney.manager;

import net.pl3x.bukkit.pl3xmoney.Logger;
import net.pl3x.bukkit.pl3xmoney.configuration.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class MoneyManager {
    public final NumberFormat FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    public boolean isMoney(Item item) {
        if (!item.isCustomNameVisible()) {
            return false; // custom name not visible
        }

        String name = item.getCustomName();
        if (name == null || !name.matches("^\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?$")) {
            return false; // no name or not a money value
        }

        ItemStack stack = item.getItemStack();
        if (!stack.hasItemMeta()) {
            return false; // no meta
        }

        ItemMeta meta = stack.getItemMeta();
        if (!meta.hasLore() || !meta.getLore().contains("Pl3xMoney")) {
            return false; // no lore or missing lore text
        }

        Logger.debug("Scanned Money Item: " + item.toString());
        return true;
    }

    public double getAmount(Item item) {
        Number number = FORMAT.parse(item.getCustomName(), new ParsePosition(0));
        if (number == null) {
            return Double.MIN_VALUE;
        }

        Logger.debug("Money Value: " + number);
        return number.doubleValue();
    }

    public Item spawnMoney(Location location, double amount) {
        ItemStack stack = new ItemStack(amount < Config.MEDIUM_AMOUNT ? Material.GOLD_NUGGET
                : (amount < Config.LARGE_AMOUNT ? Material.GOLD_INGOT
                : Material.GOLD_BLOCK), 1);

        String formattedAmount = FORMAT.format(amount);
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("Pl3xMoney");
        lore.add("Amount: " + formattedAmount);
        lore.add(String.valueOf(ThreadLocalRandom.current().nextInt()));
        meta.setLore(lore);
        stack.setItemMeta(meta);

        Item item = location.getWorld().dropItemNaturally(location, stack);
        item.setCustomName(formattedAmount);
        item.setCustomNameVisible(true);
        item.setCanMobPickup(false);

        return item;
    }
}
