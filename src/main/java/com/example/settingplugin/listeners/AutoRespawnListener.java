package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class AutoRespawnListener implements Listener {
    private final SettingPlugin plugin;

    public AutoRespawnListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerSettings settings = plugin.getSettingsManager().getSettings(player);
        if (settings.isAutoRespawn()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (player.isDead()) {
                    player.spigot().respawn();
                    player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1.0f, 1.0f);
                }
            }, 1L);
        }
    }
}
