package com.rori.evilportals.registry;

import com.rori.evilportals.blocks.corrupted_blocks.CorruptedBlock;
import com.rori.evilportals.blocks.blood_slime.*;
import com.rori.evilportals.blocks.corrupted_blocks.PerishableCorruptedBlock;
import com.rori.evilportals.blocks.other.ColorChangingBlock;
import com.rori.evilportals.blocks.other.ConnectedBlockOld;
import com.rori.evilportals.evilportal;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(evilportal.MODID);


    public static final DeferredBlock<ConnectedBlockOld> CONNECTING_SLIME_OLD =
            BLOCKS.registerBlock("connecting_slime", ConnectedBlockOld::new, BlockBehaviour.Properties.of()
            .destroyTime(1.0f)
            .sound(SoundType.SLIME_BLOCK)
            .noOcclusion()
    );

    public static final DeferredBlock<PerishableCorruptedBlock> AMALGAMATED_BLOCK = BLOCKS.registerBlock("amalgamated_block", PerishableCorruptedBlock::new, BlockBehaviour.Properties.of()
            .destroyTime(0.5f)
            .sound(SoundType.HONEY_BLOCK)
    );

    public static final DeferredBlock<CorruptedBlock> HARD_AMALGAMATED_BLOCK = BLOCKS.registerBlock("dense_amalgamated_block", CorruptedBlock::new, BlockBehaviour.Properties.of()
            .strength(2.5f, 7.0f)
            .sound(SoundType.SLIME_BLOCK)
            .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<SpreadingConnectedBlock> BLOOD_SLIME =
            BLOCKS.register("blood_slime",
                    () -> new SpreadingConnectedBlock(
                            BlockBehaviour.Properties.of()
                                    .destroyTime(0.7f)
                                    .sound(SoundType.ROOTED_DIRT)
                                    .noOcclusion()
                                    .noCollission()
                                    .replaceable()
                    ));

    public static final DeferredBlock<ColorChangingBlock> COLOR_CHANGING_BLOCK =
            BLOCKS.register("color_changing_block",
                    () -> new ColorChangingBlock(
                            BlockBehaviour.Properties.of()
                    ));


    private ModBlocks() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}