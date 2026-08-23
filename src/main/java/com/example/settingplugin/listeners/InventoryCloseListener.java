package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {
    private final SettingPlugin plugin;

    public InventoryCloseListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        if (!event.getView().getTitle().equals(ChatColor.BLUE + "Cài đặt cá nhân")) return;

        // Xóa instance khi người chơi đóng menu
        plugin.removeOpenGUI(player.getUniqueId());
    }
}
