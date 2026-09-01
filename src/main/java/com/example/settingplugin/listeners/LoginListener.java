package com.example.settingplugin.listeners;

import com.example.settingplugin.SettingPlugin;
import com.example.settingplugin.PlayerSettings;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoginListener implements Listener {
    private final SettingPlugin plugin;

    public LoginListener(SettingPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getSettingsManager().onPlayerJoin(player);
        if (!plugin.getSettingsManager().getSettings(player).isLoggedIn()) {
            applyBlindness(player);
            applyInvisibility(player);
            player.sendMessage("Bạn cần đăng nhập! Sử dụng /login <mật khẩu> hoặc /register <mật khẩu> nếu chưa có.");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getSettingsManager().onPlayerQuit(event.getPlayer());
    }

    // Chặn di chuyển
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    // Chặn chat
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (isFrozen(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Bạn phải đăng nhập trước khi chat.");
        }
    }

    // Chặn lệnh (trừ /login, /register)
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().split(" ")[0].substring(1).toLowerCase();
        if (cmd.equals("login") || cmd.equals("register")) {
            return;
        }
        if (isFrozen(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Bạn phải đăng nhập trước khi dùng lệnh.");
        }
    }

    // Chặn tương tác
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    // Chặn mở inventory
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    // Chặn ném item
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    // Chặn nhặt item
    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player && isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    // Chặn mọi sát thương (trừ sát thương từ quái sẽ xử lý riêng ở dưới)
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && isFrozen(player)) {
            event.setCancelled(true);
        }
    }

    // Chặn phá block
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    // Chặn đặt block
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isFrozen(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    // Hủy sự kiện mob target người chơi chưa login
    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player player) {
            if (isFrozen(player)) {
                event.setCancelled(true);
                // Có thể thêm event.setTarget(null) nếu API hỗ trợ
            }
        }
    }

    // Hủy sát thương do quái vật gây ra cho người chơi chưa login
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player && isFrozen(player)) {
            if (event.getDamager() instanceof Monster) {
                event.setCancelled(true);
            }
        }
    }

    // Kiểm tra trạng thái chưa login
    private boolean isFrozen(Player player) {
        PlayerSettings settings = plugin.getSettingsManager().getSettings(player);
        return settings.isLoginEnabled() && !settings.isLoggedIn();
    }

    // Hiệu ứng mù (blindness)
    public static void applyBlindness(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 0, false, false));
    }

    public static void removeBlindness(Player player) {
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

    // Hiệu ứng tàng hình (invisibility)
    public static void applyInvisibility(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0, false, false));
    }

    public static void removeInvisibility(Player player) {
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
    }
}
