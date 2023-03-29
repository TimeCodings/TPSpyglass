package dev.timecoding.tpspyglass;

import dev.timecoding.tpspyglass.command.TPSpyGlassCommand;
import dev.timecoding.tpspyglass.command.completer.TpSpyGlassCommandCompleter;
import dev.timecoding.tpspyglass.config.ConfigHandler;
import dev.timecoding.tpspyglass.listener.PlayerToggleSneakListener;
import dev.timecoding.tpspyglass.metrics.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;

public final class TPSpyglass extends JavaPlugin {

    private ConfigHandler config;
    private BukkitTask bukkitTask = null;

    @Override
    public void onEnable() {
        config = new ConfigHandler(this);
        config.init();

        getServer().getPluginManager().registerEvents(new PlayerToggleSneakListener(this), this);
        startScheduler();

        PluginCommand command = this.getCommand("tpspyglass");
        command.setExecutor(new TPSpyGlassCommand(this));
        command.setTabCompleter(new TpSpyGlassCommandCompleter(this));

        getServer().getConsoleSender().sendMessage("§5TPSpyglass §7" + this.getDescription().getVersion() + " §aby §eTimeCode §agot enabled!");
    }

    public void startScheduler() {
        if (bukkitTask != null) return;
        bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new SpyGlassParticleRunnable(this), 0, this.config.getInteger("Scheduler.PeriodInTicks"));
    }

    public void registerbStats() {
        if (!config.getBoolean("bStats")) new Metrics(this, 18086);
    }

    public void stopScheduler() {
        if (bukkitTask == null) return;
        bukkitTask.cancel();
        bukkitTask = null;
    }

    public boolean hasPermission(Player player) {
        if (config.getBoolean("Permissions.Enabled"))
            return player.hasPermission(config.getString("Permissions.Permission"));
        else return true;
    }

    public boolean usingSpyglass(Player player) {
        return player.isHandRaised() && player.getItemInHand().getType().equals(Material.SPYGLASS);
    }

    public Location getLookingLocation(Player player) {
        RayTraceResult rayTraceResult = player.rayTraceBlocks(config.getInteger("Range"));
        if (rayTraceResult == null) return null;
        return rayTraceResult.getHitBlock().getLocation().add(0, 1, 0);
    }

    public ConfigHandler getConfigHandler() {
        return config;
    }
}
