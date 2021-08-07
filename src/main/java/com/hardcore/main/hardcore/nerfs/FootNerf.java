package com.hardcore.main.hardcore.nerfs;

import org.bukkit.entity.Player;

import static com.hardcore.main.hardcore.Hardcore.*;

public class FootNerf implements NerfInterface {
    @Override
    public void nerf(Player player) {
        player.getInventory().setBoots(item);
        changeConfig(foot);
    }
}
