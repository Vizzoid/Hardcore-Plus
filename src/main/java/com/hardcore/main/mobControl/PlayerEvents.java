package com.hardcore.main.mobControl;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

import static com.hardcore.main.Main.data;
import static com.hardcore.main.Main.plugin;
import static com.hardcore.main.mobControl.PlayerInteractEntity.PlayersToHide;
import static com.hardcore.main.mobControl.PlayerInteractEntity.playerAndControlledMobs;

public class PlayerEvents implements Listener {

    public static void normalizePlayer(Player player, String playerName) {

        if (player != null) {

            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            player.setInvulnerable(false);
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.showPlayer(plugin, player));
        }

        if (playerAndControlledMobs.get(playerName) != null && Bukkit.getEntity(playerAndControlledMobs.get(playerName)) != null) {
            Objects.requireNonNull(Bukkit.getEntity(playerAndControlledMobs.get(playerName))).setInvulnerable(false);
        }
        Objects.requireNonNull(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam("control")).removeEntry(playerName);
        playerAndControlledMobs.remove(playerName);
        PlayersToHide.remove(playerName);
    }

    @EventHandler
    public void onControllingPlayerDeath(PlayerDeathEvent e) {
        if (!data.getConfig().getBoolean("players." + e.getEntity().getName() + ".isAlive")) {
            e.deathMessage(Component.text(""));

            try {
                Player player = e.getEntity().getPlayer();
                Entity entity = null;
                if (player != null && playerAndControlledMobs.get(player.getName()) != null) {
                    entity = Bukkit.getEntity(playerAndControlledMobs.get(player.getName()));
                }
                if (player != null && entity instanceof Damageable) {

                    try {
                        entity.setInvulnerable(false);
                        ((Damageable) entity).damage(1000);
                    } catch (StackOverflowError | IllegalStateException ignored) {

                    }
                    normalizePlayer(player, player.getName());
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
            if (data.getConfig().getBoolean("players." + player.getName() + ".isAlive") || !data.getConfig().contains("players." + player.getName() + ".isAlive")) {
                data.getConfig().set("players." + player.getName() + ".isAlive", true);
            }
        }


        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!PlayersToHide.contains(player.getName()) && !PlayersToHide.isEmpty()) {
                // If joined player is alive
                PlayersToHide.forEach(player1 -> player.hidePlayer(plugin, Objects.requireNonNull(Bukkit.getPlayer(player1))));
            } else {
                // If joined player is dead
                Bukkit.getOnlinePlayers().forEach(player1 -> {
                    if (!PlayersToHide.contains(player1.getName())) {
                        player1.hidePlayer(plugin, player);
                    }
                });
            }
        }, 10L);
    }

}
