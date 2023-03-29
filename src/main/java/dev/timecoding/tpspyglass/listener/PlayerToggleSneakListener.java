package dev.timecoding.tpspyglass.listener;

import dev.timecoding.tpspyglass.TPSpyglass;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerToggleSneakListener implements Listener {

    private final TPSpyglass tpSpyglass;

    public PlayerToggleSneakListener(TPSpyglass tpSpyglass) {
        this.tpSpyglass = tpSpyglass;
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (!player.isSneaking() || !tpSpyglass.usingSpyglass(player) || !tpSpyglass.hasPermission(player)) return;

        player.teleport(tpSpyglass.getLookingLocation(player));
        player.playSound(player.getLocation(), Sound.valueOf(tpSpyglass.getConfigHandler().getString("Events.Teleport.Sound")), 2, 2);

    }

}
