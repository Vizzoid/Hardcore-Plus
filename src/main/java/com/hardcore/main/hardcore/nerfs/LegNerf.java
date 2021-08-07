package com.hardcore.main.hardcore.nerfs;

import org.bukkit.entity.Player;

import static com.hardcore.main.hardcore.Hardcore.*;

public class LegNerf implements NerfInterface {
    @Override
    public void nerf(Player player) {
        player.getInventory().setLeggings(item);
        changeConfig(leg);
    }
}
