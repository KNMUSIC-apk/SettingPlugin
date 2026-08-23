package com.example.settingplugin;

import com.example.settingplugin.commands.*;
import com.example.settingplugin.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingPlugin extends JavaPlugin {
    private SettingsManager settingsManager;
    private final Map<UUID, SettingsGUI> openGUIs = new HashMap<>();

    @Override
    public void onEnable() {
        settingsManager = new SettingsManager(this);

        // Register commands
        getCommand("setting").setExecutor(new SettingCommand(this));
        getCommand("register").setExecutor(new RegisterCommand(this));
        getCommand("login").setExecutor(new LoginCommand(this));
        getCommand("resetpassword").setExecutor(new ResetPasswordCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PvPListener(this), this);
        getServer().getPluginManager().registerEvents(new VoidListener(this), this);
        getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        getServer().getPluginManager().registerEvents(new MobSpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new TpaListener(this), this);
        getServer().getPluginManager().registerEvents(new AutoRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(this), this); // Đã thêm

        getLogger().info("SettingPlugin enabled!");
    }

    @Override
    public void onDisable() {
        settingsManager.saveAll();
        getLogger().info("SettingPlugin disabled!");
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    // Các phương thức quản lý GUI mở
    public void addOpenGUI(UUID playerUUID, SettingsGUI gui) {
        openGUIs.put(playerUUID, gui);
    }

    public void removeOpenGUI(UUID playerUUID) {
        openGUIs.remove(playerUUID);
    }

    public SettingsGUI getOpenGUI(UUID playerUUID) {
        return openGUIs.get(playerUUID);
    }
}
