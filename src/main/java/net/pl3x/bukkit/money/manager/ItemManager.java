package net.pl3x.bukkit.money.manager;

import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class ItemManager {
    public static final NumberFormat FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    public static boolean isMoney(Item item) {
        if (!isMoney(item.getItemStack())) {
            return false; // not a custom coin/banknote item
        }

        if (item.getCustomName() == null || !item.isCustomNameVisible()) {
            return false; // does not have a name/value
        }

        // true if amount is greater than 0
        return getAmount(item) > 0.0D;
    }

    public static boolean isMoney(ItemStack stack) {
        return ItemsAdder.matchCustomItemName(stack, "coin") || ItemsAdder.matchCustomItemName(stack, "banknote");
    }

    public static double getAmount(Item item) {
        ParsePosition pos = new ParsePosition(0);
        Number amount = FORMAT.parse(item.getCustomName(), pos);
        return amount == null || pos.getIndex() == 0 ? Double.MIN_VALUE : amount.doubleValue();
    }

    public static Item spawnMoney(Location location, double amount) {
        ItemStack stack = ItemsAdder.getCustomItem(amount < 1.0D ? "coin" : "banknote");
        Item item = location.getWorld().dropItemNaturally(location, stack);
        item.setCustomName(FORMAT.format(amount));
        item.setCustomNameVisible(true);
        item.setCanMobPickup(false);
        item.setPickupDelay(10);
        return item;
    }
}
