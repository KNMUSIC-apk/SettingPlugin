package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class TpaListener implements Listener {
    private final SettingPlugin plugin;

    public TpaListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().substring(1); // Bỏ dấu /
        String[] parts = message.split(" ");
        if (parts.length < 2) return;
        String command = parts[0].toLowerCase();
        if (!command.equals("tpa")) return; // Chỉ xử lý lệnh /tpa

        Player requester = event.getPlayer();
        String targetName = parts[1];
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) return;

        PlayerSettings targetSettings = plugin.getSettingsManager().getSettings(target);
        if (!targetSettings.isAutoAcceptTpa()) return;

        // Đợi 2 ticks để plugin TPA tạo yêu cầu, sau đó tự động accept
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (target.isOnline() && plugin.getSettingsManager().getSettings(target).isAutoAcceptTpa()) {
                // Thay "tpaaccept" thành lệnh accept của plugin TPA bạn dùng (ví dụ "tpaccept")
                Bukkit.dispatchCommand(target, "tpaaccept " + requester.getName());
            }
        }, 2L);
    }
}
