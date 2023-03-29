package dev.timecoding.tpspyglass;

import dev.timecoding.tpspyglass.command.TPSpyGlassCommand;
import dev.timecoding.tpspyglass.command.completer.TpSpyGlassCommandCompleter;
import dev.timecoding.tpspyglass.config.ConfigHandler;
import dev.timecoding.tpspyglass.metrics.Metrics;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class TPSpyglass extends JavaPlugin implements Listener {

    private ConfigHandler configHandler;

    @Override
    public void onEnable() {
        ConsoleCommandSender consoleCommandSender = this.getServer().getConsoleSender();
        consoleCommandSender.sendMessage("§5TPSpyglass §7"+this.getDescription().getVersion()+" §aby §eTimeCode §agot enabled!");

        this.configHandler = new ConfigHandler(this);
        this.configHandler.init();
        getServer().getPluginManager().registerEvents(this, this);
        startScheduler();

        PluginCommand command = this.getCommand("tpspyglass");
        command.setExecutor(new TPSpyGlassCommand(this));
        command.setTabCompleter(new TpSpyGlassCommandCompleter(this));
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event){
        if(event.isSneaking() && usingSpyglass(event.getPlayer()) && hasPermission(event.getPlayer())){
            event.getPlayer().teleport(getLookingLocation(event.getPlayer()));
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf(configHandler.getString("Events.Teleport.Sound")), 2, 2);
        }
    }

    private BukkitTask bukkitTask = null;

    public void startScheduler(){
        if(bukkitTask == null) {
            this.bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    for (Player all : Bukkit.getOnlinePlayers()) {
                        if (usingSpyglass(all)) {
                            Location location = getLookingLocation(all.getPlayer());
                            if (location != null && hasPermission(all)) {
                                location.getWorld().spawnParticle(Particle.valueOf(configHandler.getString("Events.Looking.Particles.Type")), location, configHandler.getInteger("Events.Looking.Particles.Amount"));
                                all.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(configHandler.getString("Events.Looking.ActionBar").replace("&", "§")));
                            }
                        }
                    }
                }
            }, 0, this.configHandler.getInteger("Scheduler.PeriodInTicks"));
        }
    }

    private Metrics metrics = null;

    public void registerbStats(){
        if(!this.configHandler.getBoolean("bStats")){
            this.metrics = new Metrics(this,18086);
        }else{
            this.metrics = null;
        }
    }

    public void stopScheduler(){
        if(bukkitTask != null){
            bukkitTask.cancel();
            bukkitTask = null;
        }
    }

    private boolean hasPermission(Player player){
        if(this.configHandler.getBoolean("Permissions.Enabled")){
            if(player.hasPermission(this.configHandler.getString("Permissions.Permission"))){
                return true;
            }
        }else{
            return true;
        }
        return false;
    }

    private boolean usingSpyglass(Player player){
        if(player.isHandRaised() && player.getItemInHand().getType().equals(Material.SPYGLASS)){
            return true;
        }
        return false;
    }

    private Location getLookingLocation(Player player){
        if(player.rayTraceBlocks(this.configHandler.getInteger("Range")) != null) {
            return player.rayTraceBlocks(this.configHandler.getInteger("Range")).getHitBlock().getLocation().toCenterLocation().add(0, 1, 0);
        }
        return null;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }
}
