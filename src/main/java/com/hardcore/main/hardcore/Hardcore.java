package com.hardcore.main.hardcore;

import com.hardcore.main.hardcore.nerfs.*;
import com.hardcore.main.hardcore.nerfs.NerfInterface;
import com.hardcore.main.hardcore.respawning.DropOnMobDeath;
import com.hardcore.main.hardcore.respawning.Renderer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hardcore.main.Main.data;
import static com.hardcore.main.Main.getOverworld;

public class Hardcore implements Listener {

    public static ItemStack item;
    public static List<String> effects;
    public static String path;

    public static void changeConfig(String effect) {
        effects.add(effect);
        data.getConfig().set(path, effects);
        data.saveConfig();

    }

    public static void initializeItem() {
        item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        ItemMeta disabledMeta = item.getItemMeta();
        if (disabledMeta != null) {
            disabledMeta.displayName(Component.text("This Slot is Disabled!").color(TextColor.color(255, 85, 85)));
        }
        item.setItemMeta(disabledMeta);
    }

    public static void nerfOnDeath(Player player) {
        data.getConfig().set("players." + player.getName() + ".isAlive", true);
        data.saveConfig();

        path = "players." + player.getName() + ".effects";
        effects = data.getConfig().getStringList(path);

        Random r = new Random();
        int num = r.nextInt(7);
        List<NerfInterface> nerfs = new ArrayList<>();

        if (!effects.contains(Nerf.FOOT.getValue()))
            nerfs.add(new FootNerf());
        if (!effects.contains(Nerf.LEG.getValue()))
            nerfs.add(new LegNerf());
        if (!effects.contains(Nerf.CHEST.getValue()))
            nerfs.add(new ChestNerf());
        if (!effects.contains(Nerf.HEAD.getValue()))
            nerfs.add(new HeadNerf());

        for (int i = 0; i < 3; i++) {
            nerfs.add(new HealthNerf());
        }

        nerfs.get(num).nerf(player);

        List<String> hardcoreEffects = data.getConfig().getStringList("players." + player.getName() + ".effects");

        if (hardcoreEffects.contains(head)) {
            player.getInventory().setHelmet(item);
        }
        if (hardcoreEffects.contains(chest)) {
            player.getInventory().setChestplate(item);
        }
        if (hardcoreEffects.contains(leg)) {
            player.getInventory().setLeggings(item);
        }
        if (hardcoreEffects.contains(foot)) {
            player.getInventory().setBoots(item);
        }
    }

    @EventHandler
    public void onHardcorePlayerDeath(PlayerRespawnEvent e) {

        if (data.getConfig().getBoolean("players." + e.getPlayer().getName() + ".isAlive")) {
            DropOnMobDeath.generateMobCoordinates(e.getPlayer().getUniqueId());
            MapView mapView = Bukkit.createMap(e.getPlayer().getWorld());
            mapView.addRenderer(new Renderer());
            mapView.setScale(MapView.Scale.NORMAL);
            mapView.setTrackingPosition(true);
            mapView.setWorld(getOverworld());

            mapView.setCenterX(data.getConfig().getInt("players." + e.getPlayer().getName() + ".bounties.world.location.x"));
            mapView.setCenterZ(data.getConfig().getInt("players." + e.getPlayer().getName() + ".bounties.world.location.z"));

            ItemStack map = new ItemStack(Material.FILLED_MAP);
            MapMeta meta = (MapMeta) map.getItemMeta();
            meta.displayName(Component.text("Bounty Map #1").color(TextColor.color(255, 170, 0)));
            meta.setMapView(mapView);
            map.setItemMeta(meta);
            e.getPlayer().getInventory().setItemInMainHand(map);
        }
        String path = "players." + e.getPlayer().getName() + ".isAlive";

        data.getConfig().set(path, false);
        data.saveConfig();

    }

    @EventHandler
    public void onBedSpawnSet(PlayerBedEnterEvent e) {
        Block block = e.getBed();
        String path = "players." + e.getPlayer().getName() + ".spawn.";

        data.getConfig().set(path + "x", block.getX());
        data.getConfig().set(path + "y", block.getY());
        data.getConfig().set(path + "z", block.getZ());
        data.getConfig().set(path + "world", block.getWorld().getName());

        e.getPlayer().setBedSpawnLocation(getOverworld().getSpawnLocation());
    }

}
