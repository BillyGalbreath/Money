package net.pl3x.bukkit.pl3xmoney.manager;

import net.pl3x.bukkit.pl3xmoney.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
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

        if (!isMoney(item.getItemStack())) {
            return false;
        }

        Logger.debug("Scanned Money Item: " + item.toString());
        return true;
    }

    public boolean isMoney(ItemStack stack) {
        if (stack.getType() != Material.DIAMOND_HOE) {
            return false; // not new item
        }

        //noinspection deprecation
        if (stack.getData().getData() != 103 && stack.getData().getData() != 104) {
            return false;
        }

        if (!stack.hasItemMeta()) {
            return false; // no meta
        }

        ItemMeta meta = stack.getItemMeta();
        if (!meta.getItemFlags().contains(ItemFlag.HIDE_ATTRIBUTES) ||
                !meta.getItemFlags().contains(ItemFlag.HIDE_UNBREAKABLE)) {
            return false; // must contain all flags
        }

        if (!meta.hasLore() || !meta.getLore().contains("Pl3xMoney")) {
            return false; // no lore or missing lore text
        }

        // is money stack
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
        ItemStack stack = new ItemStack(Material.DIAMOND_HOE, 1, (short) (amount < 1.0D ? 103 : 104));

        String formattedAmount = FORMAT.format(amount);
        ItemMeta meta = stack.getItemMeta();

        List<String> lore = new ArrayList<>();
        lore.add("Pl3xMoney");
        lore.add("Amount: " + formattedAmount);
        lore.add(String.valueOf(ThreadLocalRandom.current().nextInt()));
        meta.setLore(lore);

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        stack.setItemMeta(meta);

        Item item = location.getWorld().dropItemNaturally(location, stack);
        item.setCustomName(formattedAmount);
        item.setCustomNameVisible(true);
        item.setCanMobPickup(false);

        return item;
    }
}
