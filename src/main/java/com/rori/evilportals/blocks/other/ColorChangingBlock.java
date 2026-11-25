package com.rori.evilportals.blocks.other;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

public class ColorChangingBlock extends Block {

    public static final MapCodec<ColorChangingBlock> CODEC =
            simpleCodec(ColorChangingBlock::new);

    public ColorChangingBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
        );
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.@NotNull Builder<Block, BlockState> builder
    ) {
    }

    protected @NotNull BlockState updateShape(
            @NotNull BlockState state,
            @NotNull Direction direction,
            @NotNull BlockState neighborState,
            @NotNull LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos
    ) {

        return super.updateShape(
                state, direction, neighborState,
                level, pos, neighborPos
        );
    }
}
