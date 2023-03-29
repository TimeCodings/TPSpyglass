package dev.timecoding.tpspyglass.command.completer;

import dev.timecoding.tpspyglass.TPSpyglass;
import dev.timecoding.tpspyglass.config.ConfigHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TpSpyGlassCommandCompleter implements TabCompleter {

    private final ConfigHandler config;

    public TpSpyGlassCommandCompleter(TPSpyglass tpSpyglass) {
        this.config = tpSpyglass.getConfigHandler();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!isCommand(command)) return new ArrayList<>();

        if (args.length != 1 || !sender.hasPermission(config.getString("Command-Permission"))) return new ArrayList<>();

        return Collections.singletonList("reload");
    }

    private boolean isCommand(Command command) {
        if (command.getName().equalsIgnoreCase("tpspyglass")) return true;
        for (String commandAlias : command.getAliases()) {
            if (command.getName().equalsIgnoreCase(commandAlias)) return true;
        }
        return false;
    }
}
