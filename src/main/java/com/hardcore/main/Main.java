package com.hardcore.main;

import com.hardcore.main.files.DataManager;
import com.hardcore.main.hardcore.EnderDragonGameRules;
import com.hardcore.main.hardcore.Hardcore;
import com.hardcore.main.hardcore.respawning.CreateRespawnCraft;
import com.hardcore.main.hardcore.respawning.DropOnMobDeath;
import com.hardcore.main.mobControl.EntityEvents;
import com.hardcore.main.mobControl.PlayerEvents;
import com.hardcore.main.mobControl.PlayerInteractEntity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static com.hardcore.main.hardcore.respawning.CreateRespawnCraft.createRespawnCraft;
import static com.hardcore.main.hardcore.respawning.GenerateMob.startBountyLoad;

public class Main extends JavaPlugin {

    public static DataManager data;
    public static Plugin plugin;

    public static World getOverworld() {
        return Bukkit.getWorld("world");
    }

    public static World getNether() {
        return Bukkit.getWorld("world_nether");
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        data = new DataManager(this);
        plugin = this;

        EnderDragonGameRules enderDragonRules = new EnderDragonGameRules();
        PluginManager pluginManager = getServer().getPluginManager();
        PlayerInteractEntity playerControl = new PlayerInteractEntity();

        pluginManager.registerEvents(enderDragonRules, this);
        pluginManager.registerEvents(new Hardcore(), this);
        Hardcore.initializeItem();

        pluginManager.registerEvents(playerControl, this);
        pluginManager.registerEvents(new PlayerEvents(), this);
        pluginManager.registerEvents(new EntityEvents(), this);

        pluginManager.registerEvents(new DropOnMobDeath(), this);
        pluginManager.registerEvents(new CreateRespawnCraft(), this);

        PlayerInteractEntity.PlayersToHide = new HashSet<>(data.getConfig().getStringList("playersToHide"));
        List<String> players = data.getConfig().getStringList("playerControlledMobPairs.players");
        players.forEach(player -> PlayerInteractEntity.playerAndControlledMobs.put(player, UUID.fromString(Objects.requireNonNull(data.getConfig().getString("playerControlledMobPairs." + player)))));

        if (!data.getConfig().contains("dragonDefeatedBefore") || !data.getConfig().getBoolean("dragonDefeatedBefore")) {
            data.getConfig().set("dragonDefeatedBefore", false);
            data.saveConfig();
            enderDragonRules.setGameRules(true);
        } else {
            enderDragonRules.startLocalDifficultyTimer();
        }
        createRespawnCraft();
        playerControl.startTeleportTimer();
        startBountyLoad();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        data.getConfig().set("playersToHide", new ArrayList<>(PlayerInteractEntity.PlayersToHide));

        List<String> players = new ArrayList<>();
        PlayerInteractEntity.playerAndControlledMobs.forEach((k, v) -> {
            data.getConfig().set("playerControlledMobPairs." + k, v.toString());
            players.add(k);
        });
        data.getConfig().set("playerControlledMobPairs.players", players);
        data.saveConfig();
    }

}
