package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.EnumSet;
import java.util.Set;

public class MobSpawnListener implements Listener {
    private final SettingPlugin plugin;
    private static final Set<EntityType> PROTECTED_MOBS = EnumSet.of(
            EntityType.SKELETON, EntityType.SPIDER, EntityType.CREEPER, EntityType.ZOMBIE
    );
    private static final double PROTECT_RADIUS = 25.0;
    private static final double CANCEL_RADIUS = 15.0;

    public MobSpawnListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        if (!PROTECTED_MOBS.contains(event.getEntityType())) return;

        Location spawnLocation = event.getLocation();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerSettings settings = plugin.getSettingsManager().getSettings(player);
            if (!settings.isMobSpawnProtection()) continue;

            double distanceToSpawn = player.getLocation().distance(spawnLocation);
            if (distanceToSpawn <= PROTECT_RADIUS) {
                boolean hasNearbyNonProtected = false;
                for (Player other : plugin.getServer().getOnlinePlayers()) {
                    if (other.equals(player)) continue;
                    PlayerSettings otherSettings = plugin.getSettingsManager().getSettings(other);
                    if (!otherSettings.isMobSpawnProtection()) {
                        if (player.getLocation().distance(other.getLocation()) <= CANCEL_RADIUS) {
                            hasNearbyNonProtected = true;
                            break;
                        }
                    }
                }
                if (!hasNearbyNonProtected) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
