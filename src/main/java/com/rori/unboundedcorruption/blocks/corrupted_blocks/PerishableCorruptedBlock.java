package com.rori.unboundedcorruption.blocks.corrupted_blocks;

import com.rori.unboundedcorruption.blocks.blood_slime.ConnectedBlock;
import com.rori.unboundedcorruption.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PerishableCorruptedBlock extends CorruptedBlock {

    // 60 seconds * 20 ticks per second
    public static final long REQUIRED_EXPOSURE_TICKS = 60L * 20L;

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST  = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST  = BooleanProperty.create("west");
    public static final BooleanProperty UP    = BooleanProperty.create("up");
    public static final BooleanProperty DOWN  = BooleanProperty.create("down");

    public static final Map<Direction, BooleanProperty> FACE_PROPERTIES = Map.of(
            Direction.NORTH, NORTH,
            Direction.EAST,  EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST,  WEST,
            Direction.UP,    UP,
            Direction.DOWN,  DOWN
    );

    // Stores per-face start gameTime (0L = not currently timing) for each block position
    private static final Map<ResourceKey<Level>, Map<Long, long[]>> EXPOSURE_TIMERS = new ConcurrentHashMap<>();

    // Tracks last sweep time per dimension to avoid sweeping every tick
    private static final Map<ResourceKey<Level>, AtomicLong> LAST_SWEEP_TIME = new ConcurrentHashMap<>();
    private static final long SWEEP_INTERVAL = 20L * 60L * 5L; // sweep every 5 minutes of game time

    public PerishableCorruptedBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    public boolean isFaceReady(BlockState state, Direction face) {
        BooleanProperty prop = FACE_PROPERTIES.get(face);
        return prop != null && state.getValue(prop);
    }

    public BlockState setFaceActive(BlockState state, Direction face, boolean active) {
        BooleanProperty prop = FACE_PROPERTIES.get(face);
        return prop != null ? state.setValue(prop, active) : state;
    }

    public BlockState resetFace(BlockState state, Direction face) {
        return setFaceActive(state, face, false);
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            syncExposureTimers(serverLevel, pos, state);
        }
    }

    @Override
    protected void neighborChanged(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Block neighborBlock,
            @NotNull BlockPos neighborPos,
            boolean movedByPiston
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            syncExposureTimers(serverLevel, pos, state);
        }
    }

    @Override
    protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        // Periodically sweep stale timer entries for blocks that no longer exist
        sweepStaleTimers(level);

        super.tick(state, level, pos, random);

        long[] timers = getTimers(level, pos, false);
        if (timers == null) {
            return;
        }

        long now = level.getGameTime();
        BlockState currentState = state;
        boolean stateChanged = false;
        long nextMinRemaining = Long.MAX_VALUE;

        for (Direction dir : Direction.values()) {
            int idx = dir.get3DDataValue();
            long startTick = timers[idx];

            if (startTick <= 0L) {
                continue;
            }

            if (!isExposedToConnectedBlock(level, pos, dir)) {
                timers[idx] = 0L;
                continue;
            }

            if (isFaceReady(currentState, dir)) {
                timers[idx] = 0L;
                continue;
            }

            long elapsed = now - startTick;
            if (elapsed >= REQUIRED_EXPOSURE_TICKS) {
                currentState = setFaceActive(currentState, dir, true);
                timers[idx] = 0L;
                stateChanged = true;
            } else {
                long remaining = REQUIRED_EXPOSURE_TICKS - elapsed;
                if (remaining < nextMinRemaining) {
                    nextMinRemaining = remaining;
                }
            }
        }

        if (stateChanged) {
            level.setBlock(pos, currentState, Block.UPDATE_ALL);
        }

        if (nextMinRemaining != Long.MAX_VALUE) {
            int delay = (int) Math.max(1L, Math.min(nextMinRemaining, Integer.MAX_VALUE));
            level.scheduleTick(pos, this, delay);
        } else {
            cleanupIfIdle(level, pos, timers);
        }
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level instanceof ServerLevel serverLevel) {
            Map<Long, long[]> dimMap = EXPOSURE_TIMERS.get(serverLevel.dimension());
            if (dimMap != null) {
                dimMap.remove(pos.asLong());
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull BlockHitResult hit
    ) {
        Direction clickedFace = hit.getDirection();

        if (!isFaceReady(state, clickedFace)) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            BlockState updatedState = resetFace(state, clickedFace);
            level.setBlock(pos, updatedState, Block.UPDATE_ALL);

            long[] timers = getTimers(serverLevel, pos, true);
            int idx = clickedFace.get3DDataValue();
            if (isExposedToConnectedBlock(serverLevel, pos, clickedFace)) {
                timers[idx] = serverLevel.getGameTime();
                serverLevel.scheduleTick(pos, this, (int) REQUIRED_EXPOSURE_TICKS);
            } else {
                timers[idx] = 0L;
                cleanupIfIdle(serverLevel, pos, timers);
            }

            RandomSource random = level.getRandom();
            List<ItemStack> drops = new ArrayList<>(2);

            if (random.nextFloat() < 0.25f) {
                drops.add(new ItemStack(Items.SLIME_BALL, 1));
            }

            if (random.nextFloat() < 0.25f) {
                drops.add(new ItemStack(ModItems.PERISHED_BALL.get(), 1));
            }

            double dropX = pos.getX() + 0.5 + clickedFace.getStepX() * 0.6;
            double dropY = pos.getY() + 0.5 + clickedFace.getStepY() * 0.6;
            double dropZ = pos.getZ() + 0.5 + clickedFace.getStepZ() * 0.6;
            BlockPos dropPos = BlockPos.containing(dropX, dropY, dropZ);

            for (ItemStack stack : drops) {
                Block.popResource(level, dropPos, stack);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void syncExposureTimers(ServerLevel level, BlockPos pos, BlockState state) {
        long now = level.getGameTime();
        long[] timers = getTimers(level, pos, false);
        long earliestStart = Long.MAX_VALUE;

        for (Direction dir : Direction.values()) {
            int idx = dir.get3DDataValue();
            boolean exposed = isExposedToConnectedBlock(level, pos, dir);
            boolean ready = isFaceReady(state, dir);

            if (exposed && !ready) {
                if (timers == null) {
                    timers = getTimers(level, pos, true);
                }
                if (timers[idx] <= 0L) {
                    timers[idx] = now;
                }
                if (timers[idx] < earliestStart) {
                    earliestStart = timers[idx];
                }
            } else if (timers != null && timers[idx] > 0L) {
                timers[idx] = 0L;
            }
        }

        if (timers != null) {
            if (earliestStart != Long.MAX_VALUE) {
                long elapsed = now - earliestStart;
                long remaining = Math.max(1L, REQUIRED_EXPOSURE_TICKS - elapsed);
                int delay = (int) Math.min(remaining, Integer.MAX_VALUE);
                if (!level.getBlockTicks().hasScheduledTick(pos, this)) {
                    level.scheduleTick(pos, this, delay);
                }
            } else {
                cleanupIfIdle(level, pos, timers);
            }
        }
    }

    private static boolean isExposedToConnectedBlock(Level level, BlockPos pos, Direction direction) {
        BlockState neighborState = level.getBlockState(pos.relative(direction));
        return neighborState.getBlock() instanceof ConnectedBlock;
    }

    private static long[] getTimers(ServerLevel level, BlockPos pos, boolean create) {
        Map<Long, long[]> dimMap = EXPOSURE_TIMERS.computeIfAbsent(level.dimension(), k -> new ConcurrentHashMap<>());
        long key = pos.asLong();
        if (create) {
            return dimMap.computeIfAbsent(key, k -> new long[6]);
        }
        return dimMap.get(key);
    }

    private static void cleanupIfIdle(ServerLevel level, BlockPos pos, long[] timers) {
        for (long t : timers) {
            if (t > 0L) {
                return;
            }
        }
        Map<Long, long[]> dimMap = EXPOSURE_TIMERS.get(level.dimension());
        if (dimMap != null) {
            dimMap.remove(pos.asLong());
        }
    }

    /**
     * Periodically sweep the EXPOSURE_TIMERS map for entries where the block no longer exists.
     * This prevents memory leaks when chunks unload without triggering onRemove().
     */
    private static void sweepStaleTimers(ServerLevel level) {
        ResourceKey<Level> dimKey = level.dimension();
        AtomicLong lastSweep = LAST_SWEEP_TIME.computeIfAbsent(dimKey, k -> new AtomicLong(0L));
        long now = level.getGameTime();

        // Only sweep if enough time has passed since the last sweep
        if (now - lastSweep.get() < SWEEP_INTERVAL) {
            return;
        }

        // Attempt to claim the sweep for this tick (only one thread sweeps at a time)
        if (!lastSweep.compareAndSet(lastSweep.get(), now)) {
            return;
        }

        Map<Long, long[]> dimMap = EXPOSURE_TIMERS.get(dimKey);
        if (dimMap == null || dimMap.isEmpty()) {
            return;
        }

        dimMap.entrySet().removeIf(entry -> {
            long[] timers = entry.getValue();
            // Remove if all timers are idle (0L)
            boolean allIdle = true;
            for (long t : timers) {
                if (t > 0L) {
                    allIdle = false;
                    break;
                }
            }
            if (allIdle) {
                return true;
            }
            // Check if the block at this position is still a PerishableCorruptedBlock
            BlockPos pos = BlockPos.of(entry.getKey());
            BlockState state = level.getBlockState(pos);
            return !(state.getBlock() instanceof PerishableCorruptedBlock);
        });
    }
}