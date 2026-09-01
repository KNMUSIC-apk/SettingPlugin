package com.example.settingplugin;

import org.bukkit.Bukkit;
import com.example.settingplugin.listeners.LoginListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SettingsGUI {
    private final SettingPlugin plugin;
    private final SettingsManager settingsManager;
    private final Inventory inventory;
    private final Player player;

    public SettingsGUI(SettingPlugin plugin, Player player) {
        this.plugin = plugin;
        this.settingsManager = plugin.getSettingsManager();
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 27, ChatColor.BLUE + "Cài đặt cá nhân");
        initializeItems();
    }

    private void initializeItems() {
        PlayerSettings settings = settingsManager.getSettings(player);

        inventory.setItem(11, createToggleItem(Material.DIAMOND_SWORD, "PvP Combat",
                "Bật/tắt khả năng PvP", settings.isPvpEnabled()));

        inventory.setItem(12, createToggleItem(Material.ENDER_EYE, "Void Protection",
                "Bảo vệ khi rơi vào void", settings.isVoidProtection()));

        inventory.setItem(13, createToggleItem(Material.OAK_SIGN, "Login System",
                "Yêu cầu đăng nhập khi vào server", settings.isLoginEnabled()));

        inventory.setItem(14, createToggleItem(Material.ZOMBIE_HEAD, "Mob Spawn Protection",
                "Ngăn mob spawn trong bán kính 50 block", settings.isMobSpawnProtection()));

        inventory.setItem(15, createToggleItem(Material.ENDER_PEARL, "Auto Accept TPA",
                "Tự động chấp nhận mọi yêu cầu TPA", settings.isAutoAcceptTpa()));

        inventory.setItem(16, createToggleItem(Material.TOTEM_OF_UNDYING, "Auto Respawn",
                "Tự động hồi sinh sau khi chết", settings.isAutoRespawn()));
    }

    private ItemStack createToggleItem(Material material, String displayName, String description, boolean enabled) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + displayName);
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + description);
            lore.add("");
            if (enabled) {
                lore.add(ChatColor.GREEN + "☑ Enabled");
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } else {
                lore.add(ChatColor.RED + "☒ Disabled");
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public void open() {
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
        // Lưu instance để xử lý click
        plugin.addOpenGUI(player.getUniqueId(), this);
    }

    public void handleClick(int slot) {
        PlayerSettings settings = settingsManager.getSettings(player);
        boolean newState;
        String featureName;

        switch (slot) {
            case 11:
                newState = !settings.isPvpEnabled();
                settings.setPvpEnabled(newState);
                featureName = "PvP";
                break;
            case 12:
                newState = !settings.isVoidProtection();
                settings.setVoidProtection(newState);
                featureName = "Void Protection";
                break;
            case 13:
    newState = !settings.isLoginEnabled();
    settings.setLoginEnabled(newState);
    featureName = "Login System";
    if (newState) {
        settings.setLoggedIn(false);
        player.sendMessage(ChatColor.YELLOW + "Bạn đã bật hệ thống đăng nhập. Hãy đăng ký bằng /register <mật khẩu> nếu chưa có.");
        LoginListener.applyBlindness(player);
        LoginListener.applyInvisibility(player); // Thêm dòng này
    } else {
        settings.setLoggedIn(true);
        player.sendMessage(ChatColor.YELLOW + "Bạn đã tắt hệ thống đăng nhập.");
        LoginListener.removeBlindness(player);
        LoginListener.removeInvisibility(player); // Thêm dòng này
    }
    break;
            case 14:
                newState = !settings.isMobSpawnProtection();
                settings.setMobSpawnProtection(newState);
                featureName = "Mob Spawn Protection";
                break;
            case 15:
                newState = !settings.isAutoAcceptTpa();
                settings.setAutoAcceptTpa(newState);
                featureName = "Auto Accept TPA";
                break;
            case 16:
                newState = !settings.isAutoRespawn();
                settings.setAutoRespawn(newState);
                featureName = "Auto Respawn";
                break;
            default:
                return;
        }

        settingsManager.saveSettings(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + featureName + " đã được " + (newState ? "bật" : "tắt") + ".");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        initializeItems(); // Cập nhật lại inventory
    }
}
