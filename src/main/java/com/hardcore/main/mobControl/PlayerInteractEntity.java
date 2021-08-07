package com.hardcore.main.mobControl;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.hardcore.main.Main.data;
import static com.hardcore.main.Main.plugin;

public class PlayerInteractEntity implements Listener {

    public static final BiMap<String, UUID> playerAndControlledMobs = HashBiMap.create();
    public static Set<String> PlayersToHide = new HashSet<>();

    @EventHandler
    public void playerInteractAtEntityEvent(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        Entity entity = e.getRightClicked();

        if (entity.getType().equals(EntityType.PLAYER)) return;
        if (entity instanceof Villager) {
            if (!((Villager) entity).getProfession().equals(Villager.Profession.NITWIT) && !((Villager) entity).getProfession().equals(Villager.Profession.NONE)) {
                return;
            }
        }
        if (Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam("control") == null) {
            Team team = Objects.requireNonNull(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().registerNewTeam("control"));

            team.setCanSeeFriendlyInvisibles(true);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            team.setAllowFriendlyFire(true);
        }
//      If they're dead
        if (!data.getConfig().getBoolean("players." + playerName + ".isAlive") || player.getGameMode().equals(GameMode.SPECTATOR)) {
            if (player.getGameMode().equals(GameMode.SPECTATOR)) {
                data.getConfig().set("players." + playerName + ".isAlive", false);
                data.saveConfig();
            }
//          If player isnt controlling any mobs
            if (!playerAndControlledMobs.containsKey(playerName) && !playerAndControlledMobs.containsValue(entity.getUniqueId()) && !entity.getScoreboardTags().contains("bounty")) {
                if (entity instanceof LivingEntity) {
                    if (e.getHand().equals(EquipmentSlot.HAND)) {

                        Objects.requireNonNull(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam("control")).addEntry(playerName);
                        DistributeMobEffects distributeMobEffects = new DistributeMobEffects();
//                      If conversion was successful
                        if (distributeMobEffects.applyEffects(entity)) {

                            PlayerInteractEntity.PlayersToHide.add(playerName);
                            Bukkit.getOnlinePlayers().forEach(player1 -> {
                                if (!PlayerInteractEntity.PlayersToHide.contains(player1.getName())) {
                                    player1.hidePlayer(plugin, player);
                                }
                            });

                            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
                            DistributeMobEffects.potionsToAdd.forEach(player::addPotionEffect);

                            playerAndControlledMobs.put(playerName, entity.getUniqueId());
                            player.setInvulnerable(DistributeMobEffects.ifPlayerInvulnerable);
                            entity.setInvulnerable(!DistributeMobEffects.ifPlayerInvulnerable);

                            player.teleport(entity);
                            player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation(), 1);
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1F, 1F);
                            player.setGameMode(GameMode.SURVIVAL);

                            Attributes.applyAttributes(player, (LivingEntity) entity);
                        }
                    }
                }
            } else {
                e.setCancelled(true);
            }
        }
    }


    public void startTeleportTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (!playerAndControlledMobs.isEmpty()) {
                playerAndControlledMobs.forEach((k, v) -> {
                    try {
                        Player player = Bukkit.getPlayer(k);
                        Entity entity = Bukkit.getEntity(v);

                        Chunk chunk = Objects.requireNonNull(entity).getLocation().getChunk();
                        if (!chunk.isLoaded()) {
                            chunk.load();
                        }

                        Objects.requireNonNull(Bukkit.getEntity(v)).teleport(Objects.requireNonNull(player).getLocation());
                    } catch (NullPointerException ignored) {
                    }
                });
            }
        }, 20L, 1L);
    }

}
