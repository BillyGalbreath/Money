package net.pl3x.bukkit.pl3xmoney;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public enum Tune {
    COIN_PICKUP(Sound.BLOCK_NOTE_BELL, Arrays.asList(18, 23));

    private Sound sound;
    private List<Integer> tune;

    Tune(Sound sound, List<Integer> tune) {
        this.sound = sound;
        this.tune = tune;
    }

    public void playTune(JavaPlugin plugin, Player player) {
        int i = 0;
        for (Integer clicks : tune) {
            i++;
            if (clicks < 0) {
                continue;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), sound, 5.0F, getPitch(clicks));
                }
            }.runTaskLater(plugin, i);
        }
    }

    public float getPitch(int clicks) {
        return (float) Math.pow(2.0, ((double) clicks - 12.0) / 12.0);
    }
}
