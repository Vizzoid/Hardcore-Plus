package com.hardcore.main.hardcore.respawning;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Illager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.hardcore.main.Main.data;
import static com.hardcore.main.hardcore.respawning.DropOnMobDeath.rand;

public class SpawnBounty {

    public static void spawnBounty(Location location, String playerName) {
        Map.Entry<String, EntityType[]> entry = GenerateBiome.generateMobType(location.getBlock().getBiome());

        String biome = entry.getKey();
        World world = location.getWorld();

        EntityType bountyType = entry.getValue()[0];
        EntityType mainReinforcementType = entry.getValue()[1];
        EntityType subReinforcementsType = entry.getValue()[2];

        Location loc = location.clone();
        loc.setY(world.getHighestBlockYAt(loc) + 1);

        LivingEntity entity = (LivingEntity) world.spawnEntity(loc, bountyType);
        entity.setRemoveWhenFarAway(false);
        entity.setPersistent(true);
        entity.addScoreboardTag("bounty");

        if (entity.getEquipment() != null && createArmorPiece(bountyType).size() == 4) {
            List<ItemStack> armor = createArmorPiece(bountyType);
            entity.getEquipment().setHelmet(armor.get(0));
            entity.getEquipment().setChestplate(armor.get(1));
            entity.getEquipment().setLeggings(armor.get(2));
            entity.getEquipment().setBoots(armor.get(3));
            if (entity instanceof Illager) ((Illager) entity).setPatrolLeader(true);
        }

        for (int i = 0; i < 4; i++) {
            Location loc1 = loc.clone().add(rand(-3, 3), 0, rand(-3, 3));
            if (world.getName().equals("world"))
                loc1.setY(world.getHighestBlockAt(loc1).getY() + 1);
            LivingEntity livingEntity = ((LivingEntity) world.spawnEntity(loc1, mainReinforcementType));
            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setPersistent(true);
            livingEntity.addScoreboardTag("bounty");
            if (i > 1) {
                Location loc2 = loc.clone().add(rand(-3, 3), 0, rand(-3, 3));
                if (world.getName().equals("world"))
                    loc2.setY(world.getHighestBlockAt(loc2).getY() + 1);
                LivingEntity livingEntity1 = ((LivingEntity) world.spawnEntity(loc2, subReinforcementsType));
                livingEntity1.setRemoveWhenFarAway(false);
                livingEntity1.setPersistent(true);
                livingEntity1.addScoreboardTag("bounty");
                if (livingEntity1 instanceof Drowned)
                    Objects.requireNonNull(livingEntity1.getEquipment()).setItemInMainHand(new ItemStack(Material.TRIDENT));
            }
        }
        data.getConfig().set("bounties." + entity.getUniqueId() + ".world", "world");
        data.getConfig().set("bounties." + entity.getUniqueId() + ".biome", biome);
        data.getConfig().set("bounties." + entity.getUniqueId() + ".player", playerName);
        data.saveConfig();
    }

    private static List<ItemStack> createArmorPiece(EntityType entityType) {

        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack head;
        ItemStack chest;
        ItemStack leg;
        ItemStack boot;
        switch (entityType) {
            case SKELETON, STRAY -> {
                head = new ItemStack(Material.CHAINMAIL_HELMET);
                head.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                chest.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                leg = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                leg.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                boot = new ItemStack(Material.CHAINMAIL_BOOTS);
                boot.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            }
            case PIGLIN, ZOMBIFIED_PIGLIN -> {
                head = new ItemStack(Material.GOLDEN_HELMET);
                head.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                chest = new ItemStack(Material.GOLDEN_CHESTPLATE);
                chest.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                leg = new ItemStack(Material.GOLDEN_LEGGINGS);
                leg.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                boot = new ItemStack(Material.GOLDEN_BOOTS);
                boot.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            }
            default -> {
                head = new ItemStack(Material.IRON_HELMET);
                head.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                chest = new ItemStack(Material.IRON_CHESTPLATE);
                chest.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                leg = new ItemStack(Material.IRON_LEGGINGS);
                leg.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
                boot = new ItemStack(Material.IRON_BOOTS);
                boot.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
            }
        }
        itemStacks.add(head);
        itemStacks.add(chest);
        itemStacks.add(leg);
        itemStacks.add(boot);

        return itemStacks;
    }


}
