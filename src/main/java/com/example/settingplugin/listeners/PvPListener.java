package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPListener implements Listener {
    private final SettingPlugin plugin;

    public PvPListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player victim) || !(event.getDamager() instanceof Player attacker)) {
            return;
        }
        PlayerSettings victimSettings = plugin.getSettingsManager().getSettings(victim);
        PlayerSettings attackerSettings = plugin.getSettingsManager().getSettings(attacker);

        if (!victimSettings.isPvpEnabled() || !attackerSettings.isPvpEnabled()) {
            event.setCancelled(true);
            attacker.sendMessage("PvP đã bị tắt cho một trong hai người chơi.");
        }
    }
}
