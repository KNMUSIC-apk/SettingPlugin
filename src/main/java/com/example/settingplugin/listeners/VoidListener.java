package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoidListener implements Listener {
    private final SettingPlugin plugin;
    private final Map<UUID, Location> lastSafeLocation = new HashMap<>();

    public VoidListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerSettings settings = plugin.getSettingsManager().getSettings(player);

        Location loc = player.getLocation();
        if (isSafeLocation(loc)) {
            lastSafeLocation.put(player.getUniqueId(), loc.clone());
        }

        if (settings.isVoidProtection() && loc.getY() < player.getWorld().getMinHeight()) {
            Location safe = lastSafeLocation.get(player.getUniqueId());
            if (safe != null) {
                player.teleport(safe);
                player.setFallDistance(0);
                player.sendMessage("Bạn đã được đưa trở lại vị trí an toàn.");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
        }
    }

    private boolean isSafeLocation(Location loc) {
        Location below = loc.clone().subtract(0, 1, 0);
        return below.getBlock().getType().isSolid();
    }
}
