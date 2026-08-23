package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.SettingsGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    private final SettingPlugin plugin;

    public InventoryClickListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Cài đặt cá nhân")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < 27) {
                new SettingsGUI(plugin, player).handleClick(slot);
            }
        }
    }
}
