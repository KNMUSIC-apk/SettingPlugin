package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// Hãy thay thế TpaRequestEvent bằng event thực tế từ plugin TPA của bạn
// Ví dụ: import com.essentials.api.event.TpaRequestEvent;

public class TpaListener implements Listener {
    private final SettingPlugin plugin;

    public TpaListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    // @EventHandler
    // public void onTpaRequest(TpaRequestEvent event) {
    //     Player target = event.getTarget();
    //     PlayerSettings settings = plugin.getSettingsManager().getSettings(target);
    //     if (settings.isAutoAcceptTpa()) {
    //         event.setAccepted(true);
    //         target.sendMessage("Tự động chấp nhận TPA từ " + event.getSender().getName());
    //     }
    // }
}
