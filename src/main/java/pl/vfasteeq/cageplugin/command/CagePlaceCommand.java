package pl.vfasteeq.cageplugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.vfasteeq.cageplugin.MCPlugin;
import pl.vfasteeq.cageplugin.config.ConfigManager;
import pl.vfasteeq.cageplugin.util.ChatUtil;
import pl.vfasteeq.cageplugin.util.LocationUtil;

public class CagePlaceCommand implements CommandExecutor {
    private final MCPlugin mcPlugin;

    public CagePlaceCommand(MCPlugin mcPlugin) {
        this.mcPlugin = mcPlugin;
        this.mcPlugin.getCommand("acage").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player && (commandSender.hasPermission("cageplugin.place") || commandSender.isOp()))) {
            commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &fNie możesz użyć tej komendy."));
            return false;
        }
        Player player = (Player)commandSender;
        if (args.length != 1) {
            commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &f/acage <spawn, place1, place2>"));
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "spawn":
                ConfigManager.spawnLocation = LocationUtil.locationToString(player.getLocation());
                mcPlugin.getConfigManager().save();
                commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &fUstawiono spawn klatek."));
                return true;
            case "place1":
                ConfigManager.cageFirstPoint = LocationUtil.locationToString(player.getLocation());
                mcPlugin.getConfigManager().save();
                commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &fUstawiono miejsce pierwsze klatek."));
                return true;
            case "place2":
                ConfigManager.cageSecondPoint = LocationUtil.locationToString(player.getLocation());
                mcPlugin.getConfigManager().save();
                commandSender.sendMessage(ChatUtil.fixColor("&4CAGE &8>> &fUstawiono drugie miejsce klatek."));
                return true;
        }
        return true;
    }
}
