package com.rori.evilportals.datagen;

import com.rori.evilportals.evilportal;
import com.rori.evilportals.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public final class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(
            PackOutput output,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, evilportal.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}