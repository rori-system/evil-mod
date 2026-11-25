package com.rori.unboundedcorruption.blocks.blood_slime;

import static com.rori.unboundedcorruption.registry.ModBlocks.*;

import com.rori.unboundedcorruption.blocks.corrupted_blocks.CorruptedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpreadingConnectedBlock extends ConnectedBlock{

    public static final IntegerProperty GENERATION =
            IntegerProperty.create("generation", 0, 3);

    public SpreadingConnectedBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                defaultBlockState().setValue(GENERATION, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.@NotNull Builder<Block, BlockState> builder
    ) {
        super.createBlockStateDefinition(builder);
        builder.add(GENERATION);
    }

    @Override
    protected void tick(
            @NotNull BlockState state,
            @NotNull ServerLevel level,
            @NotNull BlockPos pos,
            @NotNull RandomSource random
    ) {
        int generation = state.getValue(GENERATION);

        if (generation >= 3) { return; }

        List<BlockPos> validTargets = new ArrayList<>(26);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    BlockPos candidatePos = pos.offset(x, y, z);
                    BlockState candidateState = level.getBlockState(candidatePos);

                    if (isValidTargetSpread(level, candidatePos, candidateState)) {
                        validTargets.add(candidatePos);
                    }
                }
            }
        }

        // Schedule the next attempt — increased delay to reduce tick queue pressure
        //  + random.nextInt((generation + 1) * 60
        int delay = 180 + generation * generation * 360;
        level.scheduleTick(pos, this, delay);

        if (!validTargets.isEmpty()) {
            BlockPos targetPos = validTargets.get(random.nextInt(validTargets.size()));
            if (random.nextInt(100) <= (51 - 3 * generation)) {
                generation++;
            }
            BlockState newState = defaultBlockState().setValue(GENERATION, generation);
            // Use setBlock with UPDATE_SKIP (flag 3) to avoid cascading neighbor updates
            level.setBlock(targetPos, newState, 3);
        }

        // Separate replacement logic onto a much longer interval to reduce per-tick work
        if (random.nextInt(4) <= 2) {
            tryReplaceNearbyBlock(level, pos, random);
        }
    }

    @Override
    protected void onPlace(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState oldState,
            boolean isMoving
    ) {
        super.onPlace(state, level, pos, oldState, isMoving);

        int generation = state.getValue(GENERATION);

        if (generation >= 3) { return; }

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            serverLevel.scheduleTick(pos, this, 40 + generation * 40);
        }
    }

    private void tryReplaceNearbyBlock(
            ServerLevel level,
            BlockPos pos,
            RandomSource random
    ) {
        List<BlockPos> validTargets = new ArrayList<>(26);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    BlockPos candidatePos = pos.offset(x, y, z);
                    BlockState candidateState = level.getBlockState(candidatePos);

                    if (isValidTargetReplacement(level, candidatePos, candidateState)) {
                        validTargets.add(candidatePos);
                    }
                }
            }
        }

        if (validTargets.isEmpty()) {
            return;
        }

        BlockPos targetPos = validTargets.get(random.nextInt(validTargets.size()));
        BlockState targetState = level.getBlockState(targetPos);

        // Use isEmpty() check instead of Shapes.block() comparison to avoid shape allocation
        boolean notFullBlock = targetState.getCollisionShape(level, targetPos).isEmpty();
        boolean handTier = targetState.getDestroySpeed(level, targetPos) < 1.0f
                || !targetState.requiresCorrectToolForDrops();

        if (notFullBlock || handTier) {
            level.setBlock(targetPos, AMALGAMATED_BLOCK.get().defaultBlockState(), 3);
        } else {
            level.setBlock(targetPos, HARD_AMALGAMATED_BLOCK.get().defaultBlockState(), 3);
        }

    }

    private static boolean isValidTargetReplacement(ServerLevel level, BlockPos targetPos, @NotNull BlockState targetState) {
        if (targetState.isAir()) {
            return false;
        }

        if (targetState.getBlock() instanceof SpreadingConnectedBlock || targetState.getBlock() instanceof CorruptedBlock) {
            return false;
        }

        float destroySpeed = targetState.getDestroySpeed(level, targetPos);
        // Negative destroy speed means unbreakable (e.g. bedrock); >= 2.0f is outside your stone tier
        return destroySpeed >= 0.0f && destroySpeed <= 2.0f;
    }

    private static boolean isValidTargetSpread(ServerLevel level, BlockPos targetPos, @NotNull BlockState targetState) {
        // Require a valid solid supporting surface on any of the 6 faces
        if (!hasSolidSurface(level, targetPos)) {
            return false;
        }

        return targetState.isAir() || targetState.canBeReplaced();
    }
}
