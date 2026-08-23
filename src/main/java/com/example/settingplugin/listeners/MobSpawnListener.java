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
    
    // Chỉ bảo vệ các loại quái này
    private static final Set<EntityType> PROTECTED_MOBS = EnumSet.of(
            EntityType.SKELETON,
            EntityType.SPIDER,
            EntityType.CREEPER,
            EntityType.ZOMBIE
    );
    
    // Bán kính bảo vệ: 50 block
    private static final double PROTECT_RADIUS = 50.0;

    public MobSpawnListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // Chỉ xử lý spawn tự nhiên (không ảnh hưởng spawner, command...)
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.NATURAL) {
            return;
        }

        // Nếu mob không thuộc danh sách cần chặn thì bỏ qua
        if (!PROTECTED_MOBS.contains(event.getEntityType())) {
            return;
        }

        Location spawnLocation = event.getLocation();

        // Duyệt qua tất cả người chơi online
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerSettings settings = plugin.getSettingsManager().getSettings(player);

            // Nếu người chơi này bật bảo vệ mob spawn
            if (!settings.isMobSpawnProtection()) {
                continue;
            }

            // Kiểm tra khoảng cách từ mob spawn đến người chơi
            double distance = player.getLocation().distance(spawnLocation);
            if (distance <= PROTECT_RADIUS) {
                // Hủy sự kiện spawn mob
                event.setCancelled(true);
                return; // Chỉ cần một người chơi bật bảo vệ là đủ chặn
            }
        }
        // Nếu không có ai bật bảo vệ trong bán kính, mob spawn bình thường
    }
}
