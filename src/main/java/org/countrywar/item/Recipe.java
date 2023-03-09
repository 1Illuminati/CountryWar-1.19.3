package org.countrywar.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.countrywar.CountryWar;

public class Recipe {
    public static final ShapedRecipe BLAZE_ROD;
    public static final ShapedRecipe NETHER_WART;
    public static final ShapedRecipe WITHER_SKELETON_SKULL;
    public static final ShapedRecipe SOUL_SAND;
    public static final ShapedRecipe NETHERITE_SCRAP;
    private Recipe() {
        throw new IllegalStateException("Utility class");
    }
    public static void register() {
        Bukkit.addRecipe(BLAZE_ROD);
        Bukkit.addRecipe(NETHER_WART);
        Bukkit.addRecipe(WITHER_SKELETON_SKULL);
        Bukkit.addRecipe(SOUL_SAND);
        Bukkit.addRecipe(NETHERITE_SCRAP);
    }

    static {
        BLAZE_ROD = new ShapedRecipe(new NamespacedKey(CountryWar.getPlugin(), "blazeRod"), new ItemStack(Material.BLAZE_ROD)).shape("AAA", "SIS", "AAA")
                .setIngredient('A', Material.STICK).setIngredient('S', Material.MAGMA_BLOCK).setIngredient('I', Material.LAVA_BUCKET);

        NETHER_WART = new ShapedRecipe(new NamespacedKey(CountryWar.getPlugin(), "netherWart"), new ItemStack(Material.NETHER_WART, 4)).shape("AAA", "AAA", "AAA")
                .setIngredient('A', Material.BEETROOT);

        WITHER_SKELETON_SKULL = new ShapedRecipe(new NamespacedKey(CountryWar.getPlugin(), "witherSkeletonSkull"), new ItemStack(Material.WITHER_SKELETON_SKULL, 1)).shape("AAA", "ASA", "AAA")
                .setIngredient('A', Material.OBSIDIAN).setIngredient('S', Material.SKELETON_SKULL);

        SOUL_SAND = new ShapedRecipe(new NamespacedKey(CountryWar.getPlugin(), "soulSand"), new ItemStack(Material.SOUL_SAND, 2)).shape("AAE", "AAE", "EEE")
                .setIngredient('A', Material.SAND);

        NETHERITE_SCRAP = new ShapedRecipe(new NamespacedKey(CountryWar.getPlugin(), "netheriteScrap"), new ItemStack(Material.NETHERITE_SCRAP, 1)).shape("ABA", "BEB", "ABA")
                .setIngredient('A', Material.GOLD_BLOCK).setIngredient('B', Material.COPPER_BLOCK).setIngredient('E', Material.EMERALD);
    }
}
