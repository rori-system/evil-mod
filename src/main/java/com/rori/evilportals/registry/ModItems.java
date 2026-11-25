package com.rori.evilportals.registry;

import com.rori.evilportals.evilportal;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.swing.*;

import static com.rori.evilportals.registry.ModBlocks.*;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(evilportal.MODID);

    public static final DeferredItem<BlockItem> CONNECTING_SLIME_OLD_ITEM = ITEMS.registerSimpleBlockItem("connecting_slime", CONNECTING_SLIME_OLD);

    public static final DeferredItem<BlockItem> BLOOD_SLIME_ITEM = ITEMS.registerSimpleBlockItem("blood_slime", BLOOD_SLIME);

    public static final DeferredItem<Item> PERISHED_BALL = ITEMS.registerSimpleItem("perishing_extract", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(0).build()));

    public static final DeferredItem<Item> CONDENSED_PERISHMENT = ITEMS.registerSimpleItem("condensed_perishment", new Item.Properties());

    public static final DeferredItem<BlockItem> COLOR_CHANGING_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem(COLOR_CHANGING_BLOCK);

    public static final DeferredItem<BlockItem> CORRUPTED_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem(AMALGAMATED_BLOCK);

    public static final DeferredItem<BlockItem> HARD_CORRUPTED_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem(HARD_AMALGAMATED_BLOCK);

    private ModItems() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}