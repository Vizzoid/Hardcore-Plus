package com.hardcore.main.mobControl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTransformEvent;

import java.util.Objects;

import static com.hardcore.main.mobControl.PlayerInteractEntity.playerAndControlledMobs;

public class EntityEvents implements Listener {

    @EventHandler
    public void onControlledMobDied(EntityDeathEvent e) {
        if (playerAndControlledMobs.containsValue(e.getEntity().getUniqueId())) {
            Entity entity = e.getEntity();
            Player player = Bukkit.getPlayer(playerAndControlledMobs.inverse().get(entity.getUniqueId()));


            if (player != null) {
                PlayerEvents.normalizePlayer(player, player.getName());
            }
        }
    }

    @EventHandler
    public void onControlTarget(EntityTargetLivingEntityEvent e) {
        if (e.getTarget() != null) {
            if (playerAndControlledMobs.containsKey(Objects.requireNonNull(e.getTarget()).getName())) {
                if (playerAndControlledMobs.get(e.getTarget().getName()).equals(e.getEntity().getUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onNetherMobConvert(EntityTransformEvent e) {
        if (playerAndControlledMobs.containsValue(e.getEntity().getUniqueId())) {
            Player player = Bukkit.getPlayer(playerAndControlledMobs.inverse().get(e.getEntity().getUniqueId()));
            if (player != null) {
                PlayerEvents.normalizePlayer(player, player.getName());
            }
        }
    }

}
