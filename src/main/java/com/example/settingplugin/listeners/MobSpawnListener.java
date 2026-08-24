package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
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
            EntityType.SKELETON,
            EntityType.SPIDER,
            EntityType.CREEPER,
            EntityType.ZOMBIE
    );

    // Bán kính chặn spawn: 50 block
    private static final double PROTECT_RADIUS = 50.0;
    // Bán kính kiểm tra người chơi không bật bảo vệ để vô hiệu hóa chặn spawn
    private static final double CANCEL_RADIUS = 15.0;
    // Bán kính clear mob định kỳ: 15 block
    private static final double CLEAR_RADIUS = 15.0;
    // Thời gian lặp clear: 30 giây = 600 ticks
    private static final long CLEAR_INTERVAL = 600L;

    public MobSpawnListener(SettingPlugin plugin) {
        this.plugin = plugin;
        startClearTask();
    }

    private void startClearTask() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, this::clearMobsAroundProtectedPlayers, CLEAR_INTERVAL, CLEAR_INTERVAL);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // Chỉ xử lý spawn tự nhiên
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) return;
        // Chỉ quan tâm đến các loại mob chỉ định
        if (!PROTECTED_MOBS.contains(event.getEntityType())) return;

        Location spawnLocation = event.getLocation();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerSettings settings = plugin.getSettingsManager().getSettings(player);
            if (!settings.isMobSpawnProtection()) continue;

            double distanceToPlayer = player.getLocation().distance(spawnLocation);
            if (distanceToPlayer <= PROTECT_RADIUS) {
                // Kiểm tra xem có người chơi không bật mobspawn trong bán kính 15 block không
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
                // Nếu không có người không bật đứng gần thì hủy spawn
                if (!hasNearbyNonProtected) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    private void clearMobsAroundProtectedPlayers() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerSettings settings = plugin.getSettingsManager().getSettings(player);
            if (!settings.isMobSpawnProtection()) continue;

            // Kiểm tra có người chơi không bật mobspawn trong bán kính 50 block không
            boolean hasNearbyNonProtected = false;
            for (Player other : plugin.getServer().getOnlinePlayers()) {
                if (other.equals(player)) continue;
                PlayerSettings otherSettings = plugin.getSettingsManager().getSettings(other);
                if (!otherSettings.isMobSpawnProtection()) {
                    if (player.getLocation().distance(other.getLocation()) <= PROTECT_RADIUS) {
                        hasNearbyNonProtected = true;
                        break;
                    }
                }
            }
            // Nếu có người không bật gần thì bỏ qua clear cho người này
            if (hasNearbyNonProtected) continue;

            // Clear các mob chỉ định trong bán kính 15 block quanh người chơi
            World world = player.getWorld();
            for (Entity entity : world.getEntities()) {
                if (PROTECTED_MOBS.contains(entity.getType())) {
                    if (entity.getLocation().distance(player.getLocation()) <= CLEAR_RADIUS) {
                        entity.remove();
                    }
                }
            }
        }
    }
}
