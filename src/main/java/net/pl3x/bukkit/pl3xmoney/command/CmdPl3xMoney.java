package net.pl3x.bukkit.pl3xmoney.command;

import net.pl3x.bukkit.pl3xmoney.Pl3xMoney;
import net.pl3x.bukkit.pl3xmoney.configuration.Config;
import net.pl3x.bukkit.pl3xmoney.configuration.Lang;
import net.pl3x.bukkit.pl3xmoney.configuration.MobConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

public class CmdPl3xMoney implements TabExecutor {
    private final Pl3xMoney plugin;

    public CmdPl3xMoney(Pl3xMoney plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && "reload".startsWith(args[0].toLowerCase())) {
            return Collections.singletonList("reload");
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.pl3xmoney")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            Config.reload(plugin);
            Lang.reload(plugin);

            MobConfig.reloadConfig();
            plugin.getMobManager().reloadMobs();

            Lang.send(sender, Lang.RELOAD
                    .replace("{plugin}", plugin.getName())
                    .replace("{version}", plugin.getDescription().getVersion()));
            return true;
        }

        Lang.send(sender, Lang.VERSION
                .replace("{version}", plugin.getDescription().getVersion())
                .replace("{plugin}", plugin.getName()));
        return true;
    }
}
