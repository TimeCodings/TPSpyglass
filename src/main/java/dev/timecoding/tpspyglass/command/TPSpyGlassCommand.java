package dev.timecoding.tpspyglass.command;

import dev.timecoding.tpspyglass.TPSpyglass;
import dev.timecoding.tpspyglass.config.ConfigHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TPSpyGlassCommand implements CommandExecutor {

    private TPSpyglass plugin;

    private ConfigHandler configHandler;

    public TPSpyGlassCommand(TPSpyglass plugin){
        this.plugin = plugin;
        this.configHandler = this.plugin.getConfigHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission(configHandler.getString("Command-Permission"))){
            if(args.length == 1) {
                if (args[0].equalsIgnoreCase("rl") || args[0].equalsIgnoreCase("reload")) {
                    configHandler.reload();
                    plugin.stopScheduler();
                    plugin.startScheduler();
                    plugin.registerbStats();
                    sender.sendMessage("§aThe config.yml was successfully reloaded!");
                }else{
                    sender.sendMessage("§c/tpspyglass reload");
                }
            }else{
                sender.sendMessage("§c/tpspyglass reload");
            }
        }else{
            sender.sendMessage("§cYou do not have the permission to use this command!");
        }
        return false;
    }

}
