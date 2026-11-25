package com.rori.evilportals.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class ModDataGenerators {
    private ModDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        CompletableFuture<HolderLookup.Provider> lookupProvider =
                event.getLookupProvider();
        ExistingFileHelper existingFileHelper =
                event.getExistingFileHelper();

//        generator.addProvider(
//                event.includeClient(),
//                new ModBlockStateProvider(output, existingFileHelper)
//        );
        generator.addProvider(
                event.includeServer(),
                new ModBlockLootTableProvider(output, lookupProvider)
        );
        generator.addProvider(
                event.includeServer(),
                new ModRecipeProvider(output, lookupProvider)
        );

        String[] englishLocales = {"en_us", "en_gb", "en_ca", "en_au", "en_nz"};
        for (String locale : englishLocales) {
            generator.addProvider(event.includeClient(), new ModLanguageProvider(output, locale));
        }
    }
}