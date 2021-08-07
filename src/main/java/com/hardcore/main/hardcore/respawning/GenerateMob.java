package com.hardcore.main.hardcore.respawning;

import com.hardcore.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import static com.hardcore.main.Main.*;

public class GenerateMob {

    static String player;

    public static void setConfig(Location loc, Location loc1, Player player) {

        GenerateMob.player = player.getName();

        set("world.location.x", loc.getX());
        set("world.location.y", loc.getY());
        set("world.location.z", loc.getZ());

        set("world_nether.location.x", loc1.getX());
        set("world_nether.location.y", loc1.getY());
        set("world_nether.location.z", loc1.getZ());

        set("part", 1);
        data.saveConfig();
    }

    static void set(String path, Object set) {
        data.getConfig().set("players." + player + ".bounties." + path, set);
    }

    public static void startBountyLoad() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> {

            if (Bukkit.getOnlinePlayers().stream().anyMatch(player1 -> !data.getConfig().getBoolean("players." + player1.getName() + ".isAlive"))) {
                Bukkit.getOnlinePlayers().stream().filter(player1 -> !data.getConfig().getBoolean("players." + player1.getName() + ".isAlive")).forEach(player -> {

                    Location location = new Location(getOverworld(), data.getConfig().getInt("players." + player.getName() + ".bounties.world.location.x"), data.getConfig().getInt("players." + player.getName() + ".bounties.world.location.y"), data.getConfig().getInt("players." + player.getName() + ".bounties.world.location.z"));
                    Location netherLocation = new Location(getNether(), data.getConfig().getInt("players." + player.getName() + ".bounties.world_nether.location.x"), data.getConfig().getInt("players." + player.getName() + ".bounties.world_nether.location.y"), data.getConfig().getInt("players." + player.getName() + ".bounties.world_nether.location.z"));

                    if (getOverworld().equals(player.getWorld())) {
                        if (!data.getConfig().contains("players." + player.getName() + ".bounties.part") || data.getConfig().getInt("players." + player.getName() + ".bounties.part2") == 1) {
                            if (location.distance(player.getLocation()) < 50) {
                                if (location.getChunk().isLoaded()) {

                                    SpawnBounty.spawnBounty(location, player.getName());
                                    data.getConfig().set("players." + player + ".bounties.part", 2);
                                    data.saveConfig();
                                }
                            }
                        }

                    } else if (getNether().equals(player.getWorld())) {
                        if (!data.getConfig().contains("players." + player.getName() + ".bounties.part") || data.getConfig().getInt("players." + player.getName() + ".bounties.part2") == 2) {
                            if (netherLocation.distance(player.getLocation()) < 50) {
                                if (netherLocation.getChunk().isLoaded()) {

                                    SpawnBounty.spawnBounty(netherLocation, player.getName());
                                    data.getConfig().set("players." + player + ".bounties.part", null);
                                    data.saveConfig();
                                }
                            }
                        }
                    }
                });
            }
        }, 10L, 1L);
    }
}
