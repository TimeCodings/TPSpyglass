package dev.timecoding.tpspyglass.command.completer;

import dev.timecoding.tpspyglass.TPSpyglass;
import dev.timecoding.tpspyglass.config.ConfigHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TpSpyGlassCommandCompleter implements TabCompleter {


    private TPSpyglass plugin;

    private ConfigHandler configHandler;

    public TpSpyGlassCommandCompleter(TPSpyglass plugin) {
        this.plugin = plugin;
        this.configHandler = this.plugin.getConfigHandler();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("tpspyglass") || command.getName().equalsIgnoreCase("tpglass")){
            if(args.length == 1 && sender.hasPermission(configHandler.getString("Command-Permission"))){
                List<String> list = new ArrayList<>();
                list.add("reload");
                return list;
            }
        }
        return new ArrayList<>();
    }

}
