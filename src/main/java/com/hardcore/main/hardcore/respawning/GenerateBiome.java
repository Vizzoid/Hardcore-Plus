package com.hardcore.main.hardcore.respawning;

import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateBiome {

    public static Map.Entry<String, EntityType[]> generateMobType(Biome biome) {
        String biomeName = "Lost";
        EntityType[] reinforcements = new EntityType[0];

        switch (biome) {
            case OCEAN, RIVER, FROZEN_OCEAN, FROZEN_RIVER, DEEP_OCEAN, WARM_OCEAN, LUKEWARM_OCEAN, COLD_OCEAN, DEEP_WARM_OCEAN, DEEP_LUKEWARM_OCEAN, DEEP_COLD_OCEAN, DEEP_FROZEN_OCEAN, DESERT_LAKES, SNOWY_BEACH, STONE_SHORE, BEACH, MUSHROOM_FIELD_SHORE -> {
                reinforcements = new EntityType[]{EntityType.DROWNED, EntityType.DROWNED, EntityType.DROWNED};
                biomeName = "Ocean";
            }
            case DESERT, SAVANNA, SAVANNA_PLATEAU, DESERT_HILLS -> {
                reinforcements = decideReinforcements(new EntityType[]{EntityType.HUSK, EntityType.SKELETON}, EntityType.HUSK, EntityType.SKELETON, EntityType.EVOKER, EntityType.PILLAGER, EntityType.VINDICATOR);
                if (biome.equals(Biome.DESERT) || biome.equals(Biome.DESERT_HILLS)) {
                    biomeName = "Desert";
                } else {
                    biomeName = "Savanna";
                }
            }
            case MOUNTAINS, SNOWY_MOUNTAINS, WOODED_MOUNTAINS, GRAVELLY_MOUNTAINS, TAIGA_MOUNTAINS, SNOWY_TAIGA_MOUNTAINS, MODIFIED_GRAVELLY_MOUNTAINS, MOUNTAIN_EDGE -> {
                reinforcements = decideReinforcements(new EntityType[]{EntityType.STRAY}, EntityType.ZOMBIE, EntityType.STRAY, EntityType.ILLUSIONER, EntityType.PILLAGER, EntityType.VINDICATOR);
                biomeName = "Mountain";
            }
            case TAIGA, SNOWY_TUNDRA, TAIGA_HILLS, SNOWY_TAIGA, SNOWY_TAIGA_HILLS, GIANT_TREE_TAIGA, GIANT_TREE_TAIGA_HILLS, ICE_SPIKES, GIANT_SPRUCE_TAIGA, GIANT_SPRUCE_TAIGA_HILLS -> {
                reinforcements = decideReinforcements(new EntityType[]{EntityType.STRAY, EntityType.ZOMBIE}, EntityType.ZOMBIE, EntityType.STRAY, EntityType.ILLUSIONER, EntityType.PILLAGER, EntityType.VINDICATOR);
                biomeName = "Taiga";
            }
            case SWAMP, SWAMP_HILLS, JUNGLE, JUNGLE_HILLS, JUNGLE_EDGE, MODIFIED_JUNGLE, MODIFIED_JUNGLE_EDGE, BAMBOO_JUNGLE, BAMBOO_JUNGLE_HILLS -> {
                reinforcements = decideReinforcements(new EntityType[]{EntityType.SKELETON, EntityType.ZOMBIE_VILLAGER}, EntityType.ZOMBIE_VILLAGER, EntityType.SKELETON, EntityType.EVOKER, EntityType.PILLAGER, EntityType.VINDICATOR);
                if (biome.equals(Biome.SWAMP) || biome.equals(Biome.SWAMP_HILLS)) {
                    biomeName = "Swamp";
                } else {
                    biomeName = "Jungle";
                }
            }
            case MUSHROOM_FIELDS, BADLANDS, WOODED_BADLANDS_PLATEAU, BADLANDS_PLATEAU, ERODED_BADLANDS, MODIFIED_WOODED_BADLANDS_PLATEAU, MODIFIED_BADLANDS_PLATEAU -> reinforcements = new EntityType[]{EntityType.SKELETON, EntityType.SKELETON, EntityType.SPIDER};
            case NETHER_WASTES -> {
                if (ThreadLocalRandom.current().nextInt() != 1)
                    reinforcements = new EntityType[]{EntityType.PIGLIN, EntityType.PIGLIN, EntityType.PIGLIN};
                else
                    reinforcements = new EntityType[]{EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIFIED_PIGLIN};
                biomeName = "Nether";
            }
            case SOUL_SAND_VALLEY -> {
                reinforcements = decideReinforcements(new EntityType[]{EntityType.SKELETON}, EntityType.SKELETON, EntityType.SKELETON, EntityType.GHAST, null, null);
                biomeName = "Soul";
            }
            case CRIMSON_FOREST -> {
                reinforcements = decideReinforcements(new EntityType[]{EntityType.PIGLIN}, EntityType.PIGLIN, EntityType.HOGLIN, EntityType.PIGLIN_BRUTE, EntityType.PIGLIN, EntityType.HOGLIN);
                biomeName = "Crimson";
            }
            case WARPED_FOREST -> {
                if (ThreadLocalRandom.current().nextInt() != 1)
                    reinforcements = new EntityType[]{EntityType.ENDERMAN, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIFIED_PIGLIN};
                else
                    reinforcements = new EntityType[]{EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIFIED_PIGLIN};
                biomeName = "Warped";
            }
            case BASALT_DELTAS -> {
                reinforcements = new EntityType[]{EntityType.MAGMA_CUBE, EntityType.MAGMA_CUBE, EntityType.MAGMA_CUBE};
                biomeName = "Basalt";
            }
            case WOODED_HILLS, BIRCH_FOREST, BIRCH_FOREST_HILLS, DARK_FOREST, SUNFLOWER_PLAINS, FLOWER_FOREST, TALL_BIRCH_FOREST, TALL_BIRCH_HILLS, DARK_FOREST_HILLS, SHATTERED_SAVANNA, SHATTERED_SAVANNA_PLATEAU, PLAINS, FOREST -> biomeName = "Plains";
            default -> reinforcements = decideReinforcements(new EntityType[]{EntityType.ZOMBIE, EntityType.SKELETON}, EntityType.ZOMBIE, EntityType.SKELETON, EntityType.EVOKER, EntityType.PILLAGER, EntityType.VINDICATOR);
        }
        return new AbstractMap.SimpleEntry<>(biomeName, reinforcements);
    }

    private static EntityType[] decideReinforcements(EntityType[] bounty75, EntityType main75, EntityType sub75, EntityType bounty25, EntityType main25, EntityType sub25) {

        if (bounty25 != null && ThreadLocalRandom.current().nextInt(4) != 3) {
            return new EntityType[]{bounty25, main25, sub25};

        } else {

            EntityType bountyType;
            if (bounty75.length > 1) {
                if (ThreadLocalRandom.current().nextInt(2) == 1) {
                    bountyType = bounty75[0];
                } else {
                    bountyType = bounty75[1];
                }
            } else {
                bountyType = bounty75[0];
            }

            return new EntityType[]{bountyType, main75, sub75};
        }
    }

}
