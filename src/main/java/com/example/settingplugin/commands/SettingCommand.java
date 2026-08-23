package com.example.settingplugin.commands;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.SettingsGUI;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingCommand implements CommandExecutor {
    private final SettingPlugin plugin;

    public SettingCommand(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Chỉ người chơi mới dùng được lệnh này.");
            return true;
        }

        PlayerSettings settings = plugin.getSettingsManager().getSettings(player);
        if (settings.isLoginEnabled() && !settings.isLoggedIn()) {
            player.sendMessage("Bạn phải đăng nhập trước khi dùng lệnh này.");
            return true;
        }

        new SettingsGUI(plugin, player).open();
        return true;
    }
}
