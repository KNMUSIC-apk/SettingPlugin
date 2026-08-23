package com.example.settingplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingsManager {
    private final SettingPlugin plugin;
    private final Map<UUID, PlayerSettings> settingsMap = new HashMap<>();
    private File dataFile;
    private FileConfiguration dataConfig;

    public SettingsManager(SettingPlugin plugin) {
        this.plugin = plugin;
        loadData();
    }

    private void loadData() {
        dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        if (dataConfig.contains("players")) {
            for (String uuidStr : dataConfig.getConfigurationSection("players").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidStr);
                PlayerSettings settings = new PlayerSettings();
                String path = "players." + uuidStr + ".";
                settings.setPvpEnabled(dataConfig.getBoolean(path + "pvp-enabled", true));
                settings.setVoidProtection(dataConfig.getBoolean(path + "void-protection", false));
                settings.setLoginEnabled(dataConfig.getBoolean(path + "login-enabled", false));
                settings.setMobSpawnProtection(dataConfig.getBoolean(path + "mob-spawn-protection", false));
                settings.setAutoAcceptTpa(dataConfig.getBoolean(path + "auto-accept-tpa", false));
                settings.setAutoRespawn(dataConfig.getBoolean(path + "auto-respawn", false));
                settings.setPasswordHash(dataConfig.getString(path + "password-hash", null));
                settings.setLoggedIn(dataConfig.getBoolean(path + "logged-in", true));
                settingsMap.put(uuid, settings);
            }
        }
    }

    public PlayerSettings getSettings(UUID uuid) {
        return settingsMap.computeIfAbsent(uuid, k -> new PlayerSettings());
    }

    public PlayerSettings getSettings(Player player) {
        return getSettings(player.getUniqueId());
    }

    public void saveSettings(UUID uuid) {
        PlayerSettings settings = settingsMap.get(uuid);
        if (settings == null) return;
        String path = "players." + uuid.toString() + ".";
        dataConfig.set(path + "pvp-enabled", settings.isPvpEnabled());
        dataConfig.set(path + "void-protection", settings.isVoidProtection());
        dataConfig.set(path + "login-enabled", settings.isLoginEnabled());
        dataConfig.set(path + "mob-spawn-protection", settings.isMobSpawnProtection());
        dataConfig.set(path + "auto-accept-tpa", settings.isAutoAcceptTpa());
        dataConfig.set(path + "auto-respawn", settings.isAutoRespawn());
        dataConfig.set(path + "password-hash", settings.getPasswordHash());
        dataConfig.set(path + "logged-in", settings.isLoggedIn());
        saveDataFile();
    }

    public void saveAll() {
        for (UUID uuid : settingsMap.keySet()) {
            saveSettings(uuid);
        }
    }

    private void saveDataFile() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save data.yml!");
            e.printStackTrace();
        }
    }

    public void onPlayerJoin(Player player) {
        PlayerSettings settings = getSettings(player);
        if (settings.isLoginEnabled()) {
            settings.setLoggedIn(false);
        } else {
            settings.setLoggedIn(true);
        }
        saveSettings(player.getUniqueId());
    }

    public void onPlayerQuit(Player player) {
        saveSettings(player.getUniqueId());
    }
}
