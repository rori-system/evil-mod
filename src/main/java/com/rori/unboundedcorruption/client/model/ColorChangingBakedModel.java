
package com.rori.unboundedcorruption.client.model;

import java.util.List;

import com.rori.unboundedcorruption.registry.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class ColorChangingBakedModel
        extends BakedModelWrapper<BakedModel> {

    private final BakedModel blueModel;
    private final BakedModel redModel;

    public ColorChangingBakedModel(
            BakedModel blueModel,
            BakedModel redModel
    ) {
        super(blueModel);

        this.blueModel = blueModel;
        this.redModel = redModel;
    }

    @Override
    public @NotNull ModelData getModelData(
            @NotNull BlockAndTintGetter level,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @NotNull ModelData modelData
    ) {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

        // Helper to classify a neighboring block
        java.util.function.Function<BlockPos, BlockType> getBlockType = neighborPos -> {
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighborState.isAir()) {
                return BlockType.AIR;
            }

            if (neighborState.is(ModBlocks.COLOR_CHANGING_BLOCK.get())) {
                return BlockType.SAME;
            }

            return BlockType.SOLID;
        };

        return ModelData.builder()

                // Directly above and below
                .with(ModModelData.ABOVE, getBlockType.apply(pos.above()))
                .with(ModModelData.BELOW, getBlockType.apply(pos.below()))

                // Horizontal
                .with(ModModelData.NORTH, getBlockType.apply(pos.north()))
                .with(ModModelData.SOUTH, getBlockType.apply(pos.south()))
                .with(ModModelData.EAST, getBlockType.apply(pos.east()))
                .with(ModModelData.WEST, getBlockType.apply(pos.west()))

                // Horizontal diagonals
                .with(ModModelData.NORTH_EAST, getBlockType.apply(pos.north().east()))
                .with(ModModelData.NORTH_WEST, getBlockType.apply(pos.north().west()))
                .with(ModModelData.SOUTH_EAST, getBlockType.apply(pos.south().east()))
                .with(ModModelData.SOUTH_WEST, getBlockType.apply(pos.south().west()))

                // Above diagonals
                .with(ModModelData.ABOVE_NORTH, getBlockType.apply(pos.above().north()))
                .with(ModModelData.ABOVE_SOUTH, getBlockType.apply(pos.above().south()))
                .with(ModModelData.ABOVE_EAST, getBlockType.apply(pos.above().east()))
                .with(ModModelData.ABOVE_WEST, getBlockType.apply(pos.above().west()))

                // Below diagonals
                .with(ModModelData.BELOW_NORTH, getBlockType.apply(pos.below().north()))
                .with(ModModelData.BELOW_SOUTH, getBlockType.apply(pos.below().south()))
                .with(ModModelData.BELOW_EAST, getBlockType.apply(pos.below().east()))
                .with(ModModelData.BELOW_WEST, getBlockType.apply(pos.below().west()))

                // Above corners
                .with(ModModelData.ABOVE_NORTH_EAST, getBlockType.apply(pos.above().north().east()))
                .with(ModModelData.ABOVE_NORTH_WEST, getBlockType.apply(pos.above().north().west()))
                .with(ModModelData.ABOVE_SOUTH_EAST, getBlockType.apply(pos.above().south().east()))
                .with(ModModelData.ABOVE_SOUTH_WEST, getBlockType.apply(pos.above().south().west()))

                // Below corners
                .with(ModModelData.BELOW_NORTH_EAST, getBlockType.apply(pos.below().north().east()))
                .with(ModModelData.BELOW_NORTH_WEST, getBlockType.apply(pos.below().north().west()))
                .with(ModModelData.BELOW_SOUTH_EAST, getBlockType.apply(pos.below().south().east()))
                .with(ModModelData.BELOW_SOUTH_WEST, getBlockType.apply(pos.below().south().west()))

                .build();
    }


    @Override
    public @NotNull List<BakedQuad> getQuads(
            BlockState state,
            Direction side,
            @NotNull RandomSource random,
            @NotNull ModelData modelData,
            RenderType renderType
    ) {

        boolean hasBlockAbove =
            (
                modelData.get(ModModelData.ABOVE) != BlockType.AIR
            );

        BakedModel selectedModel =
                hasBlockAbove ? redModel : blueModel;

        return selectedModel.getQuads(
                state, side, random, modelData, renderType
        );
    }
}
