package com.example.settingplugin.commands;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetPasswordCommand implements CommandExecutor {
    private final SettingPlugin plugin;

    public ResetPasswordCommand(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("settingplugin.admin")) {
            sender.sendMessage("Bạn không có quyền sử dụng lệnh này.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("Sử dụng: /resetpassword <player>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Không tìm thấy người chơi " + args[0]);
            return true;
        }
        PlayerSettings settings = plugin.getSettingsManager().getSettings(target);
        settings.setPasswordHash(null);
        settings.setLoggedIn(true);
        plugin.getSettingsManager().saveSettings(target.getUniqueId());
        sender.sendMessage("Đã xóa mật khẩu của " + target.getName());
        if (target.isOnline()) {
            target.sendMessage("Mật khẩu của bạn đã bị xóa bởi admin. Hãy đăng ký lại nếu cần.");
        }
        return true;
    }
}
