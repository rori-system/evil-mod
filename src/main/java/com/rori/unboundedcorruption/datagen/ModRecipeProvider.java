package com.rori.unboundedcorruption.datagen;

import com.rori.unboundedcorruption.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider
    ) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(
                        RecipeCategory.BUILDING_BLOCKS,
                        ModItems.COLOR_CHANGING_BLOCK_ITEM.get()
                )
                .pattern("III")
                .pattern("ISI")
                .pattern("III")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STONE)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(
                        RecipeCategory.BUILDING_BLOCKS,
                        ModItems.CONDENSED_PERISHMENT.get(), 4
                )
                .pattern("SIS")
                .pattern("I I")
                .pattern("SIS")
                .define('S', ModItems.BLOOD_SLIME_ITEM)
                .define('I', ModItems.PERISHED_BALL)
                .unlockedBy("has_stone", has(ModItems.BLOOD_SLIME_ITEM))
                .save(recipeOutput);
    }
}