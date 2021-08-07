package com.hardcore.main.hardcore.nerfs;

import org.bukkit.entity.Player;

import static com.hardcore.main.hardcore.Hardcore.*;

public class HeadNerf implements NerfInterface {
    @Override
    public void nerf(Player player) {
        player.getInventory().setHelmet(item);
        changeConfig(head);
    }
}
