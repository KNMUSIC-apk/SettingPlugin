package com.example.settingplugin.commands;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import com.example.settingplugin.listeners.LoginListener;
import com.example.settingplugin.utils.PasswordHasher;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {
    private final SettingPlugin plugin;

    public LoginCommand(SettingPlugin plugin) {
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
        if (settings.isLoggedIn()) {
            player.sendMessage("Bạn đã đăng nhập rồi.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        if (settings.getPasswordHash() == null) {
            player.sendMessage("Bạn chưa đăng ký, hãy dùng /register <mật khẩu>.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        if (args.length < 1) {
            player.sendMessage("Sử dụng: /login <mật khẩu>");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        String password = args[0];
        String hash = PasswordHasher.hash(password);
        if (hash.equals(settings.getPasswordHash())) {
            settings.setLoggedIn(true);
            plugin.getSettingsManager().saveSettings(player.getUniqueId());

            // Xóa hiệu ứng mù khi đăng nhập thành công
            LoginListener.removeBlindness(player);

            player.sendMessage(ChatColor.GREEN + "Đăng nhập thành công!");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        } else {
            player.sendMessage(ChatColor.RED + "Mật khẩu sai.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
        }
        return true;
    }
}
