package com.example.settingplugin;

import com.example.settingplugin.commands.*;
import com.example.settingplugin.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class SettingPlugin extends JavaPlugin {
    private SettingsManager settingsManager;

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
}
