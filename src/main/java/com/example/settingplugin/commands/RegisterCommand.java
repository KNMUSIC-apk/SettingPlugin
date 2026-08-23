package com.example.settingplugin.commands;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import com.example.settingplugin.utils.PasswordHasher;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
    private final SettingPlugin plugin;

    public RegisterCommand(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Chỉ người chơi mới dùng được lệnh này.");
            return true;
        }

        PlayerSettings settings = plugin.getSettingsManager().getSettings(player);
        if (!settings.isLoginEnabled()) {
            player.sendMessage("Bạn chưa bật hệ thống đăng nhập.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        if (settings.getPasswordHash() != null) {
            player.sendMessage("Bạn đã đăng ký rồi, hãy dùng /login <mật khẩu>.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        if (args.length < 1) {
            player.sendMessage("Sử dụng: /register <mật khẩu>");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        String password = args[0];
        if (password.length() < 4) {
            player.sendMessage("Mật khẩu phải có ít nhất 4 ký tự.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        String hash = PasswordHasher.hash(password);
        settings.setPasswordHash(hash);
        settings.setLoggedIn(true);
        plugin.getSettingsManager().saveSettings(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "Đăng ký thành công! Bạn đã đăng nhập.");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        return true;
    }
}
