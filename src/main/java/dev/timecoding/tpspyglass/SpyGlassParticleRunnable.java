package dev.timecoding.tpspyglass;

import dev.timecoding.tpspyglass.config.ConfigHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

public class SpyGlassParticleRunnable implements Runnable {
    private final TPSpyglass tpSpyglass;


    SpyGlassParticleRunnable(TPSpyglass tpSpyglass) {
        this.tpSpyglass = tpSpyglass;
    }

    @Override
    public void run() {
        ConfigHandler config = tpSpyglass.getConfigHandler();
        String particleType = config.getString("Events.Looking.Particles.Type");
        int particleAmount = config.getInteger("Events.Looking.Particles.Amount");
        BaseComponent[] actionBar = TextComponent.fromLegacyText(config.getString("Events.Looking.ActionBar").replace("&", "§"));

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!tpSpyglass.usingSpyglass(player)) return;

            Location location = tpSpyglass.getLookingLocation(player);

            if (location == null || !tpSpyglass.hasPermission(player)) return;

            location.getWorld().spawnParticle(Particle.valueOf(particleType), location, particleAmount);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, actionBar);
        });
    }
}
