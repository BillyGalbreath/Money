package net.pl3x.bukkit.money.command;

import net.pl3x.bukkit.money.Money;
import net.pl3x.bukkit.money.configuration.Config;
import net.pl3x.bukkit.money.configuration.Lang;
import net.pl3x.bukkit.money.configuration.MobConfig;
import net.pl3x.bukkit.money.manager.MobManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

public class CmdMoney implements TabExecutor {
    private final Money plugin;

    public CmdMoney(Money plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && "reload".startsWith(args[0].toLowerCase()) && sender.hasPermission("command.money")) {
            return Collections.singletonList("reload");
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.money")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION);
            return true;
        }

        String response = "&d" + plugin.getName() + " v" + plugin.getDescription().getVersion();

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            Config.reload(plugin);
            Lang.reload(plugin);

            MobConfig.reload(plugin);
            MobManager.reload();

            response += " reloaded";
        }

        Lang.send(sender, response);
        return true;
    }
}
