package com.hardcore.main.hardcore.respawning;

import com.hardcore.main.Main;
import com.hardcore.main.hardcore.Hardcore;
import com.hardcore.main.mobControl.Attributes;
import com.hardcore.main.mobControl.PlayerEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

import static com.hardcore.main.Main.data;
import static com.hardcore.main.Main.getOverworld;

public class CreateRespawnCraft implements Listener {

    public static void createRespawnCraft() {
        ItemStack item = new ItemStack(Material.LAPIS_BLOCK);
        item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
        ItemMeta meta = item.getItemMeta();


        if (meta != null) {
            meta.displayName(Component.text("Respawn Sigil").color(TextColor.color(255, 170, 0)));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "respawn");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe
                .shape(" D ", "BGB", " D ")
                .setIngredient('B', Material.HEART_OF_THE_SEA)
                .setIngredient('D', Material.DIAMOND)
                .setIngredient('G', Material.GOLD_BLOCK);

        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {

        HumanEntity player = e.getWhoClicked();
        if (player instanceof Player && !data.getConfig().getBoolean("players." + player.getName() + ".isAlive")) {

            data.getConfig().set("players." + player.getName() + ".isAlive", true);
            data.saveConfig();
            PlayerEvents.normalizePlayer((Player) player, player.getName());
            Attributes.resetAttributes((Player) player);
            player.getInventory().clear();
            ((Player) player).setExp(0);
            player.setGameMode(GameMode.SURVIVAL);

            if (data.getConfig().contains("players." + player.getName() + ".spawn.x")) {
                World world = Bukkit.getWorld(Objects.requireNonNull(data.getConfig().getString("players." + player.getName() + ".spawn.world")));
                int x = data.getConfig().getInt("players." + player.getName() + ".spawn.x");
                int y = data.getConfig().getInt("players." + player.getName() + ".spawn.y");
                int z = data.getConfig().getInt("players." + player.getName() + ".spawn.z");

                player.teleport(new Location(world, x, y, z));
                Hardcore.nerfOnDeath(((Player) player));
                assert world != null;
                world.spawnParticle(Particle.TOTEM, new Location(world, x, y, z), 1);
                world.playSound(new Location(world, x, y, z), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1F, 1F);
            } else {
                World world = getOverworld();
                player.teleport(world.getSpawnLocation());
                Hardcore.nerfOnDeath(((Player) player));
                world.spawnParticle(Particle.TOTEM, world.getSpawnLocation(), 1);
                world.playSound(world.getSpawnLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1F, 1F);
            }

            ((Player) player).sendTitle("", "You've Been Revived!", 5, 20, 5);

        }
    }

}
