package com.example.settingplugin.commands;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import com.example.settingplugin.listeners.LoginListener;
import com.example.settingplugin.utils.PasswordHasher;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

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
            // Đăng nhập thành công
            settings.setLoggedIn(true);
            settings.setLoginFailCount(0); // Reset số lần sai
            plugin.getSettingsManager().saveSettings(player.getUniqueId());

            // Xóa hiệu ứng mù
            LoginListener.removeBlindness(player);

            player.sendMessage(ChatColor.GREEN + "Đăng nhập thành công!");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        } else {
            // Sai mật khẩu
            int failCount = settings.getLoginFailCount() + 1;
            settings.setLoginFailCount(failCount);
            plugin.getSettingsManager().saveSettings(player.getUniqueId());

            player.sendMessage(ChatColor.RED + "Mật khẩu sai.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);

            if (failCount >= 3) {
                // Ban 10 phút
                banPlayer(player);
                player.kickPlayer("Bạn đã bị cấm 10 phút vì sai mật khẩu quá nhiều lần.");
            } else {
                // Kick
                player.kickPlayer("Sai mật khẩu. Vui lòng thử lại.");
            }
        }
        return true;
    }

    private void banPlayer(Player player) {
        String reason = "Sai mật khẩu quá nhiều lần";
        Date expires = new Date(System.currentTimeMillis() + 10 * 60 * 1000); // 10 phút
        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), reason, expires, null);
    }
}
