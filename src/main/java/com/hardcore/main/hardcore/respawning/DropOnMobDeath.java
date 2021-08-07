package com.hardcore.main.hardcore.respawning;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.hardcore.main.Main.*;

//Commentated//
public class DropOnMobDeath implements Listener {

    public static void generateMobCoordinates(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);

        int x = randNum();
        int y;
        int z = randNum();

        y = getOverworld().getHighestBlockYAt(x, z);

        World nether = Bukkit.getWorld("world_nether");
        assert nether != null;

        int x1 = randNum();
        int y1 = rand(31, 100);
        int z1 = randNum();

        while (nether.getBlockAt(x1, y1 + 1, z1).isEmpty() && !nether.getBlockAt(x1, y1 - 1, z1).isEmpty()) {
            x1 = randNum();
            y1 = rand(31, 100);
            z1 = randNum();
        }

        assert player != null;
        Location loc = player.getLocation().clone().add(x, 0, z);
        loc.setY(y);
        Location loc1 = new Location(nether, (player.getLocation().getX() / 8) + x1, y1, (player.getLocation().getZ() / 8) + z1);
        GenerateMob.setConfig(loc, loc1, player);

    }

    private static int randNum() {
        int num = rand(-401, 401);

        while (num >= -100 && num <= 100) {
            num = rand(-401, 401);
        }

        return num;
    }

    static int rand(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    @EventHandler
    public void onControlledMobDied(EntityDeathEvent e) {
        LivingEntity entity = e.getEntity();

        if (data.getConfig().contains("bounties." + entity.getUniqueId() + ".biome")) {
            ItemStack artifact = new ItemStack(Material.HEART_OF_THE_SEA);
            artifact.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
            ItemMeta artifactMeta = artifact.getItemMeta();
            if (artifactMeta != null) {
                artifactMeta.displayName(Component.text(data.getConfig().getString("bounties." + entity.getUniqueId() + ".biome") + " Artifact").color(TextColor.color(255, 170, 0)));
                artifactMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            artifact.setItemMeta(artifactMeta);
            e.getDrops().add(artifact);

            if (entity.getWorld().equals(getOverworld())) {
                MapView mapView = Bukkit.createMap(entity.getWorld());
                mapView.setScale(MapView.Scale.NORMAL);
                mapView.setTrackingPosition(true);
                mapView.setWorld(getNether());

                String player = data.getConfig().getString("bounties." + entity.getUniqueId() + ".player");
                mapView.setCenterX(data.getConfig().getInt("players." + player + ".bounties.world_nether.location.x"));
                mapView.setCenterZ(data.getConfig().getInt("players." + player + ".bounties.world_nether.location.z"));
                mapView.addRenderer(new Renderer());

                ItemStack map = new ItemStack(Material.FILLED_MAP);
                MapMeta meta = (MapMeta) map.getItemMeta();
                meta.displayName(Component.text("Bounty Map #2").color(TextColor.color(255, 170, 0)));
                meta.setMapView(mapView);
                map.setItemMeta(meta);
                e.getDrops().add(map);
            }
        }
    }

    @EventHandler
    public void onBountyMobHurt(EntityCombustEvent e) {
        if (e.getEntity().getScoreboardTags().contains("bounty")) {
            if (!(e instanceof EntityCombustByEntityEvent)) {
                e.setCancelled(true);
                e.getEntity().setFireTicks(0);
            }
        }
    }

}
