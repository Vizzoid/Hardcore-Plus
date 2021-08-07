package com.hardcore.main.hardcore.nerfs;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

import java.util.Objects;

import static com.hardcore.main.hardcore.Hardcore.changeConfig;
import static com.hardcore.main.hardcore.Hardcore.health;

public class HealthNerf implements NerfInterface {
    @Override
    public void nerf(Player player) {
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).addModifier(new AttributeModifier("Vitality", -2.0D, AttributeModifier.Operation.ADD_NUMBER));
        changeConfig(health);
    }
}
