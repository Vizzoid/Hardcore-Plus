package com.hardcore.main.hardcore;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static com.hardcore.main.Main.*;

public class EnderDragonGameRules implements Listener {

    @EventHandler
    public void onEnderDragonKill(EntityDeathEvent e) {
        if (data.getConfig().getBoolean("dragonDefeatedBefore")) {
            if (e.getEntity().getType() != EntityType.ENDER_DRAGON) {
                return;
            }
            setGameRules(false);
            startLocalDifficultyTimer();
        }
    }

    public void setGameRules(boolean reset) {
        World world = getOverworld();

        if (reset) world.setDifficulty(Difficulty.EASY);
        else {
            world.setDifficulty(Difficulty.HARD);
            data.getConfig().set("dragonDefeatedBefore", true);
            data.saveConfig();

        }

        world.setGameRule(GameRule.DISABLE_RAIDS, reset);
        world.setGameRule(GameRule.DO_FIRE_TICK, !reset);
        world.setGameRule(GameRule.DO_INSOMNIA, !reset);
        world.setGameRule(GameRule.DO_PATROL_SPAWNING, !reset);
        world.setGameRule(GameRule.MOB_GRIEFING, !reset);
        world.setGameRule(GameRule.UNIVERSAL_ANGER, !reset);
        world.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, reset);

    }

    public void startLocalDifficultyTimer() {
        if (getOverworld().getGameTime() < 1549000) getOverworld().setFullTime(1549000);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> getOverworld().getPlayers().forEach(player -> {
            Chunk chunk = player.getLocation().getChunk();

            if (chunk.getInhabitedTime() < 3600000) {
                chunk.setInhabitedTime(3600000);
            }
        }), 0L, 20L);
    }

}
