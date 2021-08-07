package com.hardcore.main.mobControl;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Attributes {

    public static void applyAttributes(Player player, LivingEntity entity) {
        try {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(Math.min(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue(), 20));
        } catch (NullPointerException ignored) {

        }
        player.setHealth(entity.getHealth());
        try {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(Math.min((Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getValue()), 2.0));
        } catch (NullPointerException ignored) {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(0.0D);
        }
        try {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)).setBaseValue(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)).getValue());

        } catch (NullPointerException ignored) {

        }
        try {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(Math.min(Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).getValue(), 0.1));
        } catch (NullPointerException ignored) {

        }
    }

    public static void resetAttributes(Player player) {
        try {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(20);
        } catch (NullPointerException ignored) {

        }
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(2.0);
        try {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK)).setBaseValue(0);

        } catch (NullPointerException ignored) {

        }
        try {
            Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).setBaseValue(0.1);
        } catch (NullPointerException ignored) {

        }
    }
}
