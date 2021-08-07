package com.hardcore.main.mobControl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PufferFish;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

class DistributeMobEffects {

    public static final List<PotionEffect> potionsToAdd = new ArrayList<>();
    public static boolean ifPlayerInvulnerable;

    /**
     * @return whether or not control was successful
     */
    public Boolean applyEffects(Entity entity) {
        potionsToAdd.clear();
        ifPlayerInvulnerable = false;

        if (entity instanceof LivingEntity) {

            switch (entity.getType()) {
                case BAT, BEE, BLAZE, GHAST, PARROT, PHANTOM, VEX, CHICKEN -> addEffect(PotionEffectType.SLOW_FALLING);
                case COD, GLOW_SQUID, PUFFERFISH, SALMON, SQUID, TROPICAL_FISH -> {
                    addSlownessEffect();
                    addEffect(PotionEffectType.WATER_BREATHING);
                    if (entity instanceof PufferFish) {
                        ifPlayerInvulnerable = true;
                    }
                }
                case AXOLOTL, DROWNED, ELDER_GUARDIAN, GUARDIAN, TURTLE -> {
                    addSlownessEffect();
                    addEffect(PotionEffectType.CONDUIT_POWER);
                }
                case DOLPHIN -> {
                    addSlownessEffect();
                    addEffect(PotionEffectType.DOLPHINS_GRACE);
                }
                case PIGLIN, PIGLIN_BRUTE, PILLAGER, SKELETON, STRAY, VINDICATOR, WITHER_SKELETON, ZOMBIE, ZOMBIE_VILLAGER, ENDERMAN, ENDERMITE, SNOWMAN, CAT, CAVE_SPIDER, COW, CREEPER, DONKEY, EVOKER, FOX, GIANT, GOAT, HOGLIN, HORSE, HUSK, LLAMA, MULE, MUSHROOM_COW, OCELOT, PANDA, PIG, POLAR_BEAR, SHEEP, SILVERFISH, SPIDER, VILLAGER, WITCH, WOLF -> {
                }
                case MAGMA_CUBE, RABBIT, SLIME -> {
                    addSlownessEffect();
                    if (!entity.getType().equals(EntityType.RABBIT)) {
                        addEffect(PotionEffectType.JUMP);
                        ifPlayerInvulnerable = true;
                    }
                }
                case STRIDER, ZOGLIN, ZOMBIFIED_PIGLIN -> addEffect(PotionEffectType.FIRE_RESISTANCE);
                default -> {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    private void addEffect(PotionEffectType potion) {
        int i = 1;

        potionsToAdd.add(new PotionEffect(potion, 99999999, i, false, false));
    }

    private void addSlownessEffect() {
        potionsToAdd.add(new PotionEffect(PotionEffectType.SLOW, 99999999, 255, false, false));
    }

}
