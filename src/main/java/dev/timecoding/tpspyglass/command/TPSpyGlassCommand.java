package dev.timecoding.tpspyglass.command;

import dev.timecoding.tpspyglass.TPSpyglass;
import dev.timecoding.tpspyglass.config.ConfigHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TPSpyGlassCommand implements CommandExecutor {

    private final TPSpyglass tpSpyglass;
    private final ConfigHandler config;

    public TPSpyGlassCommand(TPSpyglass tpSpyglass) {
        this.tpSpyglass = tpSpyglass;
        this.config = tpSpyglass.getConfigHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(config.getString("Command-Permission"))) {
            sender.sendMessage("§cYou do not have the permission to use this command!");
            return false;
        }

        if (args.length != 1) return sendCommandInfo(sender);

        if (!args[0].equalsIgnoreCase("rl") && !args[0].equalsIgnoreCase("reload"))
            return sendCommandInfo(sender);

        config.reload();
        tpSpyglass.stopScheduler();
        tpSpyglass.startScheduler();
        tpSpyglass.registerbStats();
        sender.sendMessage("§aThe config.yml was successfully reloaded!");

        return true;
    }

    private boolean sendCommandInfo(CommandSender sender) {
        sender.sendMessage("§c/tpspyglass reload");
        return false;
    }
}
