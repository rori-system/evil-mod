
package com.rori.evilportals.client.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rori.evilportals.blocks.blood_slime.ConnectedBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public class ConnectedBlockBakedModel
        extends BakedModelWrapper<BakedModel> {

    private final BakedModel baseModel;
    private final BakedModel baseModelUp;
    private final BakedModel baseModelNorth;
    private final BakedModel baseModelSouth;
    private final BakedModel baseModelEast;
    private final BakedModel baseModelWest;

    private final BakedModel coreModel;
    private final BakedModel coreModel2;
    private final BakedModel coreModel2a;
    private final BakedModel coreModel2b;
    private final BakedModel coreModel2c;
    private final BakedModel coreModel2d;

    private final BakedModel decoModel1;
    private final BakedModel decoModel2;
    private final BakedModel decoModel1a;
    private final BakedModel decoModel2a;

    private final BakedModel nosesModel;
    private final BakedModel nosesModelEast;
    private final BakedModel nosesModelSouth;
    private final BakedModel nosesModelWest;

    private final BakedModel noses2Model;
    private final BakedModel noses2ModelEast;
    private final BakedModel noses2ModelSouth;
    private final BakedModel noses2ModelWest;

    private final BakedModel noses3Model;
    private final BakedModel noses3ModelEast;
    private final BakedModel noses3ModelSouth;
    private final BakedModel noses3ModelWest;

    private final BakedModel eyesModel;
    private final BakedModel eyesModelEast;
    private final BakedModel eyesModelSouth;
    private final BakedModel eyesModelWest;

    private final BakedModel eyes2Model;
    private final BakedModel eyes2ModelEast;
    private final BakedModel eyes2ModelSouth;
    private final BakedModel eyes2ModelWest;

    private static final Map<CacheKey, List<BakedQuad>> quadsCache = new LinkedHashMap<>(256, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(@NotNull java.util.Map.Entry<CacheKey, List<BakedQuad>> eldest) {
            return size() > 512;
        }
    };

    /**
         * Cache key combining the 52-bit neighbor config, face direction, and render type.
         */

    private record CacheKey(
            long neighborConfig,
            Direction side,
            RenderType renderType,
            int variant
    ) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey other = (CacheKey) o;
            return neighborConfig == other.neighborConfig &&
                    side == other.side &&
                    renderType == other.renderType &&
                    variant == other.variant;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(
                    neighborConfig,
                    side,
                    renderType,
                    variant
            );
        }
    }

    // Horizontal arms (basic)
    private final BakedModel northArmModel;
    private final BakedModel southArmModel;
    private final BakedModel eastArmModel;
    private final BakedModel westArmModel;

    // Horizontal arms with Up face
    private final BakedModel northArmModelUp;
    private final BakedModel southArmModelUp;
    private final BakedModel eastArmModelUp;
    private final BakedModel westArmModelUp;

    // Horizontal arms with East face (only cross-directions exist)
    private final BakedModel northArmModelEast;
    private final BakedModel southArmModelEast;

    // Horizontal arms with West face (only cross-directions exist)
    private final BakedModel northArmModelWest;
    private final BakedModel southArmModelWest;

    // Horizontal arms with North face (only cross-directions exist)
    private final BakedModel eastArmModelNorth;
    private final BakedModel westArmModelNorth;

    // Horizontal arms with South face (only cross-directions exist)
    private final BakedModel eastArmModelSouth;
    private final BakedModel westArmModelSouth;

    // Up arms with cardinal faces
    private final BakedModel upArmModelNorth;
    private final BakedModel upArmModelSouth;
    private final BakedModel upArmModelEast;
    private final BakedModel upArmModelWest;

    // Down arms with cardinal faces
    private final BakedModel downArmModelNorth;
    private final BakedModel downArmModelSouth;
    private final BakedModel downArmModelEast;
    private final BakedModel downArmModelWest;


    public ConnectedBlockBakedModel(
            BakedModel baseModel,
            BakedModel baseModelUp,
            BakedModel baseModelNorth,
            BakedModel baseModelSouth,
            BakedModel baseModelEast,
            BakedModel baseModelWest,

            BakedModel coreModel,
            BakedModel coreModel2,
            BakedModel coreModel2a,
            BakedModel coreModel2b,
            BakedModel coreModel2c,
            BakedModel coreModel2d,

            BakedModel decoModel1,
            BakedModel decoModel2,
            BakedModel decoModel1a,
            BakedModel decoModel2a,

            BakedModel nosesModel,
            BakedModel nosesModelEast,
            BakedModel nosesModelSouth,
            BakedModel nosesModelWest,
            BakedModel noses2Model,
            BakedModel noses2ModelEast,
            BakedModel noses2ModelSouth,
            BakedModel noses2ModelWest,
            BakedModel noses3Model,
            BakedModel noses3ModelEast,
            BakedModel noses3ModelSouth,
            BakedModel noses3ModelWest,

            BakedModel eyesModel,
            BakedModel eyesModelEast,
            BakedModel eyesModelSouth,
            BakedModel eyesModelWest,
            BakedModel eyes2Model,
            BakedModel eyes2ModelEast,
            BakedModel eyes2ModelSouth,
            BakedModel eyes2ModelWest,

            BakedModel northArmModel,
            BakedModel southArmModel,
            BakedModel eastArmModel,
            BakedModel westArmModel,
            BakedModel northArmModelUp,
            BakedModel southArmModelUp,
            BakedModel eastArmModelUp,
            BakedModel westArmModelUp,
            BakedModel northArmModelEast,
            BakedModel southArmModelEast,
            BakedModel northArmModelWest,
            BakedModel southArmModelWest,
            BakedModel eastArmModelNorth,
            BakedModel westArmModelNorth,
            BakedModel eastArmModelSouth,
            BakedModel westArmModelSouth,
            BakedModel upArmModelNorth,
            BakedModel upArmModelSouth,
            BakedModel upArmModelEast,
            BakedModel upArmModelWest,
            BakedModel downArmModelNorth,
            BakedModel downArmModelSouth,
            BakedModel downArmModelEast,
            BakedModel downArmModelWest
    ) {
        super(coreModel);
        this.baseModel = baseModel;
        this.baseModelUp = baseModelUp;
        this.baseModelNorth = baseModelNorth;
        this.baseModelSouth = baseModelSouth;
        this.baseModelEast = baseModelEast;
        this.baseModelWest = baseModelWest;

        this.coreModel = coreModel;
        this.coreModel2 = coreModel2;
        this.coreModel2a = coreModel2a;
        this.coreModel2b = coreModel2b;
        this.coreModel2c = coreModel2c;
        this.coreModel2d = coreModel2d;

        this.decoModel1 = decoModel1;
        this.decoModel2 = decoModel2;
        this.decoModel1a = decoModel1a;
        this.decoModel2a = decoModel2a;

        this.nosesModel = nosesModel;
        this.nosesModelEast = nosesModelEast;
        this.nosesModelSouth = nosesModelSouth;
        this.nosesModelWest = nosesModelWest;

        this.noses2Model = noses2Model;
        this.noses2ModelEast = noses2ModelEast;
        this.noses2ModelSouth = noses2ModelSouth;
        this.noses2ModelWest = noses2ModelWest;

        this.noses3Model = noses3Model;
        this.noses3ModelEast = noses3ModelEast;
        this.noses3ModelSouth = noses3ModelSouth;
        this.noses3ModelWest = noses3ModelWest;

        this.eyesModel = eyesModel;
        this.eyesModelEast = eyesModelEast;
        this.eyesModelSouth = eyesModelSouth;
        this.eyesModelWest = eyesModelWest;

        this.eyes2Model = eyes2Model;
        this.eyes2ModelEast = eyes2ModelEast;
        this.eyes2ModelSouth = eyes2ModelSouth;
        this.eyes2ModelWest = eyes2ModelWest;

        this.northArmModel = northArmModel;
        this.southArmModel = southArmModel;
        this.eastArmModel = eastArmModel;
        this.westArmModel = westArmModel;

        this.northArmModelUp = northArmModelUp;
        this.southArmModelUp = southArmModelUp;
        this.eastArmModelUp = eastArmModelUp;
        this.westArmModelUp = westArmModelUp;

        this.northArmModelEast = northArmModelEast;
        this.southArmModelEast = southArmModelEast;

        this.northArmModelWest = northArmModelWest;
        this.southArmModelWest = southArmModelWest;

        this.eastArmModelNorth = eastArmModelNorth;
        this.westArmModelNorth = westArmModelNorth;

        this.eastArmModelSouth = eastArmModelSouth;
        this.westArmModelSouth = westArmModelSouth;

        this.upArmModelNorth = upArmModelNorth;
        this.upArmModelSouth = upArmModelSouth;
        this.upArmModelEast = upArmModelEast;
        this.upArmModelWest = upArmModelWest;

        this.downArmModelNorth = downArmModelNorth;
        this.downArmModelSouth = downArmModelSouth;
        this.downArmModelEast = downArmModelEast;
        this.downArmModelWest = downArmModelWest;
    }

    private static BlockType classifyBlock(
            BlockAndTintGetter level,
            BlockPos pos,
            Direction... directions
    ) {
        BlockState state = level.getBlockState(pos);

        if (state.isAir()) return BlockType.AIR;
        if (state.getBlock() instanceof ConnectedBlock) return BlockType.SAME;

        for (Direction direction : directions) {
            if (!state.isFaceSturdy(level, pos, direction.getOpposite())) {
                return BlockType.AIR;
            }
        }

        return BlockType.SOLID;
    }

    @Override
    public @NotNull ModelData getModelData(
            @NotNull BlockAndTintGetter level,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @NotNull ModelData modelData
    ) {
        long seed = BlockPos.asLong(pos.getX(), pos.getY(), pos.getZ());

        seed ^= seed >>> 30;
        seed *= 0xbf58476d1ce4e5b9L;
        seed ^= seed >>> 27;
        seed *= 0x94d049bb133111ebL;
        seed ^= seed >>> 31;

        RandomSource positionalRandom = RandomSource.create(seed);
        int choice = positionalRandom.nextInt();

        return ModelData.builder()

                .with(ModModelData.VARIANT, choice)

                // Directly above and below
                .with(ModModelData.ABOVE, classifyBlock(level, pos.above(), Direction.UP))
                .with(ModModelData.BELOW, classifyBlock(level, pos.below(), Direction.DOWN))

                // Horizontal
                .with(ModModelData.NORTH, classifyBlock(level, pos.north(), Direction.NORTH))
                .with(ModModelData.SOUTH, classifyBlock(level, pos.south(), Direction.SOUTH))
                .with(ModModelData.EAST, classifyBlock(level, pos.east(), Direction.EAST))
                .with(ModModelData.WEST, classifyBlock(level, pos.west(), Direction.WEST))

                // Horizontal diagonals
                .with(ModModelData.NORTH_EAST, classifyBlock(level, pos.north().east(), Direction.NORTH, Direction.EAST))
                .with(ModModelData.NORTH_WEST, classifyBlock(level, pos.north().west(), Direction.NORTH, Direction.WEST))
                .with(ModModelData.SOUTH_EAST, classifyBlock(level, pos.south().east(), Direction.SOUTH, Direction.EAST))
                .with(ModModelData.SOUTH_WEST, classifyBlock(level, pos.south().west(), Direction.SOUTH, Direction.WEST))

                // Above diagonals
                .with(ModModelData.ABOVE_NORTH, classifyBlock(level, pos.above().north(), Direction.UP, Direction.NORTH))
                .with(ModModelData.ABOVE_SOUTH, classifyBlock(level, pos.above().south(), Direction.UP, Direction.SOUTH))
                .with(ModModelData.ABOVE_EAST, classifyBlock(level, pos.above().east(), Direction.UP, Direction.EAST))
                .with(ModModelData.ABOVE_WEST, classifyBlock(level, pos.above().west(), Direction.UP, Direction.WEST))

                // Below diagonals
                .with(ModModelData.BELOW_NORTH, classifyBlock(level, pos.below().north(), Direction.DOWN, Direction.NORTH))
                .with(ModModelData.BELOW_SOUTH, classifyBlock(level, pos.below().south(), Direction.DOWN, Direction.SOUTH))
                .with(ModModelData.BELOW_EAST, classifyBlock(level, pos.below().east(), Direction.DOWN, Direction.EAST))
                .with(ModModelData.BELOW_WEST, classifyBlock(level, pos.below().west(), Direction.DOWN, Direction.WEST))

                // Above corners
                .with(ModModelData.ABOVE_NORTH_EAST, classifyBlock(level, pos.above().north().east(), Direction.UP, Direction.NORTH, Direction.EAST))
                .with(ModModelData.ABOVE_NORTH_WEST, classifyBlock(level, pos.above().north().west(), Direction.UP, Direction.NORTH, Direction.WEST))
                .with(ModModelData.ABOVE_SOUTH_EAST, classifyBlock(level, pos.above().south().east(), Direction.UP, Direction.SOUTH, Direction.EAST))
                .with(ModModelData.ABOVE_SOUTH_WEST, classifyBlock(level, pos.above().south().west(), Direction.UP, Direction.SOUTH, Direction.WEST))

                // Below corners
                .with(ModModelData.BELOW_NORTH_EAST, classifyBlock(level, pos.below().north().east(), Direction.DOWN, Direction.NORTH, Direction.EAST))
                .with(ModModelData.BELOW_NORTH_WEST, classifyBlock(level, pos.below().north().west(), Direction.DOWN, Direction.NORTH, Direction.WEST))
                .with(ModModelData.BELOW_SOUTH_EAST, classifyBlock(level, pos.below().south().east(), Direction.DOWN, Direction.SOUTH, Direction.EAST))
                .with(ModModelData.BELOW_SOUTH_WEST, classifyBlock(level, pos.below().south().west(), Direction.DOWN, Direction.SOUTH, Direction.WEST))

                .build();
    }

    private void addQuadsIfPresent(
            List<BakedQuad> quads,
            BakedModel model,
            BlockState state,
            Direction side,
            RandomSource random,
            ModelData modelData,
            RenderType renderType,
            boolean shiftY,
            boolean shiftX,
            boolean shiftZ
    ) {
        if (model == null) return;

        List<BakedQuad> result = model.getQuads(
                state, side, random, modelData, renderType
        );

        int variant = modelData.get(ModModelData.VARIANT);
        RandomSource offsetRandom = RandomSource.create(variant);

        float offsetX = shiftX ? (offsetRandom.nextFloat() - 0.5f) * 0.25f : 0.0f;
        float offsetY = shiftY ? (offsetRandom.nextFloat() - 0.5f) * 0.25f : 0.0f;
        float offsetZ = shiftZ ? (offsetRandom.nextFloat() - 0.5f) * 0.25f : 0.0f;

        for (BakedQuad quad : result) {
            quads.add(shiftQuad(quad, offsetX, offsetY, offsetZ));
        }
    }
    /**
     * Build the 52-bit neighbor configuration from ModelData.
     * Each BlockType is encoded as 2 bits (AIR=0, SOLID=1, SAME=2).
     */
    private static long buildNeighborConfig(ModelData modelData) {
        long key = 0;
        key |= modelData.get(ModModelData.ABOVE).ordinal();
        key |= (long) modelData.get(ModModelData.BELOW).ordinal() << 2;
        key |= (long) modelData.get(ModModelData.NORTH).ordinal() << 4;
        key |= (long) modelData.get(ModModelData.SOUTH).ordinal() << 6;
        key |= (long) modelData.get(ModModelData.EAST).ordinal() << 8;
        key |= (long) modelData.get(ModModelData.WEST).ordinal() << 10;
        key |= (long) modelData.get(ModModelData.NORTH_EAST).ordinal() << 12;
        key |= (long) modelData.get(ModModelData.NORTH_WEST).ordinal() << 14;
        key |= (long) modelData.get(ModModelData.SOUTH_EAST).ordinal() << 16;
        key |= (long) modelData.get(ModModelData.SOUTH_WEST).ordinal() << 18;
        key |= (long) modelData.get(ModModelData.ABOVE_NORTH).ordinal() << 20;
        key |= (long) modelData.get(ModModelData.ABOVE_SOUTH).ordinal() << 22;
        key |= (long) modelData.get(ModModelData.ABOVE_EAST).ordinal() << 24;
        key |= (long) modelData.get(ModModelData.ABOVE_WEST).ordinal() << 26;
        key |= (long) modelData.get(ModModelData.BELOW_NORTH).ordinal() << 28;
        key |= (long) modelData.get(ModModelData.BELOW_SOUTH).ordinal() << 30;
        key |= (long) modelData.get(ModModelData.BELOW_EAST).ordinal() << 32;
        key |= (long) modelData.get(ModModelData.BELOW_WEST).ordinal() << 34;
        key |= (long) modelData.get(ModModelData.ABOVE_NORTH_EAST).ordinal() << 36;
        key |= (long) modelData.get(ModModelData.ABOVE_NORTH_WEST).ordinal() << 38;
        key |= (long) modelData.get(ModModelData.ABOVE_SOUTH_EAST).ordinal() << 40;
        key |= (long) modelData.get(ModModelData.ABOVE_SOUTH_WEST).ordinal() << 42;
        key |= (long) modelData.get(ModModelData.BELOW_NORTH_EAST).ordinal() << 44;
        key |= (long) modelData.get(ModModelData.BELOW_NORTH_WEST).ordinal() << 46;
        key |= (long) modelData.get(ModModelData.BELOW_SOUTH_EAST).ordinal() << 48;
        key |= (long) modelData.get(ModModelData.BELOW_SOUTH_WEST).ordinal() << 50;
        return key;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(
            BlockState state,
            Direction side,
            @NotNull RandomSource random,
            @NotNull ModelData modelData,
            RenderType renderType
    ) {
        return buildQuads(state, side, random, modelData, renderType);
    }

    /**
     * Build the combined quads for a specific face, with caching.
     */
    private List<BakedQuad> buildQuads(
            BlockState state,
            Direction side,
            RandomSource random,
            ModelData modelData,
            RenderType renderType
    ) {
        // Build the full cache key: neighbor config + side + renderType

        long neighborConfig = buildNeighborConfig(modelData);
        int variant = modelData.get(ModModelData.VARIANT);

        CacheKey key = new CacheKey(neighborConfig, side, renderType, variant);

        // Check cache first
        if (quadsCache.containsKey(key)) {
            return quadsCache.get(key);
        }

        // Build quads from scratch
        List<BakedQuad> quads = new ArrayList<>();

        int coreChoice = Math.floorMod(variant, 100);

        if (modelData.get(ModModelData.BELOW) == BlockType.SOLID) {

            addQuadsIfPresent(quads, baseModel, state, side, random, modelData, renderType, false, false, false);

            BakedModel[] coreModels = {
                    coreModel2, coreModel2a, coreModel2b, coreModel2c, coreModel2d
            };


            if (coreChoice < 5) {
                addQuadsIfPresent(quads, coreModels[coreChoice], state, side, random, modelData, renderType, false, true, true);
            } else if (coreChoice <= 6) {
                addQuadsIfPresent(quads, coreModel, state, side, random, modelData, renderType, false, true, true);
            }

            if (coreChoice < 35) {
                addQuadsIfPresent(quads, decoModel2, state, side, random, modelData, renderType, false, true, true);
            } else if (coreChoice < 59) {
                addQuadsIfPresent(quads, decoModel1, state, side, random, modelData, renderType, false, true, true);
            }

            if (modelData.get(ModModelData.NORTH) == BlockType.SOLID || ((modelData.get(ModModelData.NORTH) != BlockType.AIR || modelData.get(ModModelData.BELOW_NORTH) == BlockType.SAME) && modelData.get(ModModelData.BELOW_NORTH) != BlockType.AIR)) {
                addQuadsIfPresent(quads, northArmModel, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.WEST) == BlockType.SOLID || ((modelData.get(ModModelData.WEST) != BlockType.AIR || modelData.get(ModModelData.BELOW_WEST) == BlockType.SAME) && modelData.get(ModModelData.BELOW_WEST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, westArmModel, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.SOUTH) == BlockType.SOLID || ((modelData.get(ModModelData.SOUTH) != BlockType.AIR || modelData.get(ModModelData.BELOW_SOUTH) == BlockType.SAME) && modelData.get(ModModelData.BELOW_SOUTH) != BlockType.AIR)) {
                addQuadsIfPresent(quads, southArmModel, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.EAST) == BlockType.SOLID || ((modelData.get(ModModelData.EAST) != BlockType.AIR || modelData.get(ModModelData.BELOW_EAST) == BlockType.SAME) && modelData.get(ModModelData.BELOW_EAST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, eastArmModel, state, side, random, modelData, renderType, false, false, false);
            }
        }

        if (modelData.get(ModModelData.ABOVE) == BlockType.SOLID) {

            addQuadsIfPresent(quads, baseModelUp, state, side, random, modelData, renderType, false, false, false);

            if (coreChoice < 35) {
                addQuadsIfPresent(quads, decoModel2a, state, side, random, modelData, renderType, false, true, true);
            } else if (coreChoice < 59) {
                addQuadsIfPresent(quads, decoModel1a, state, side, random, modelData, renderType, false, true, true);
            }

            if (modelData.get(ModModelData.NORTH) == BlockType.SOLID || ((modelData.get(ModModelData.NORTH) != BlockType.AIR || modelData.get(ModModelData.ABOVE_NORTH) == BlockType.SAME) && modelData.get(ModModelData.ABOVE_NORTH) != BlockType.AIR)) {
                addQuadsIfPresent(quads, northArmModelUp, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.WEST) == BlockType.SOLID || ((modelData.get(ModModelData.WEST) != BlockType.AIR || modelData.get(ModModelData.ABOVE_WEST) == BlockType.SAME) && modelData.get(ModModelData.ABOVE_WEST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, westArmModelUp, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.SOUTH) == BlockType.SOLID || ((modelData.get(ModModelData.SOUTH) != BlockType.AIR || modelData.get(ModModelData.ABOVE_SOUTH) == BlockType.SAME) && modelData.get(ModModelData.ABOVE_SOUTH) != BlockType.AIR)) {
                addQuadsIfPresent(quads, southArmModelUp, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.EAST) == BlockType.SOLID || ((modelData.get(ModModelData.EAST) != BlockType.AIR || modelData.get(ModModelData.ABOVE_EAST) == BlockType.SAME) && modelData.get(ModModelData.ABOVE_EAST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, eastArmModelUp, state, side, random, modelData, renderType, false, false, false);
            }
        }

        if (modelData.get(ModModelData.NORTH) == BlockType.SOLID) {

            addQuadsIfPresent(quads, baseModelNorth, state, side, random, modelData, renderType, false, false, false);

            if (coreChoice <= 4) {
                if (coreChoice <= 2) {
                    addQuadsIfPresent(quads, nosesModel, state, side, random, modelData, renderType, true, true, false);
                }
                if (coreChoice <= 3 && coreChoice > 1) {
                    addQuadsIfPresent(quads, noses2Model, state, side, random, modelData, renderType, true, true, false);
                }
                if (coreChoice > 2) {
                    addQuadsIfPresent(quads, noses3Model, state, side, random, modelData, renderType, true, true, false);
                }
            } else if (coreChoice <= 8) {
                if (coreChoice <= 7) {
                    addQuadsIfPresent(quads, eyesModel, state, side, random, modelData, renderType, true, true, false);
                }
                if (coreChoice > 6) {
                    addQuadsIfPresent(quads, eyes2Model, state, side, random, modelData, renderType, true, true, false);
                }
            }

            if (modelData.get(ModModelData.ABOVE) == BlockType.SOLID || ((modelData.get(ModModelData.ABOVE) != BlockType.AIR || modelData.get(ModModelData.ABOVE_NORTH) == BlockType.SAME) && modelData.get(ModModelData.ABOVE_NORTH) != BlockType.AIR)) {
                addQuadsIfPresent(quads, upArmModelNorth, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.WEST) == BlockType.SOLID || ((modelData.get(ModModelData.WEST) != BlockType.AIR || modelData.get(ModModelData.NORTH_WEST) == BlockType.SAME) && modelData.get(ModModelData.NORTH_WEST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, westArmModelNorth, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.BELOW) == BlockType.SOLID || ((modelData.get(ModModelData.BELOW) != BlockType.AIR || modelData.get(ModModelData.BELOW_NORTH) == BlockType.SAME) && modelData.get(ModModelData.BELOW_NORTH) != BlockType.AIR)) {
                addQuadsIfPresent(quads, downArmModelNorth, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.EAST) == BlockType.SOLID || ((modelData.get(ModModelData.EAST) != BlockType.AIR || modelData.get(ModModelData.NORTH_EAST) == BlockType.SAME) && modelData.get(ModModelData.NORTH_EAST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, eastArmModelNorth, state, side, random, modelData, renderType, false, false, false);
            }
        }

        if (modelData.get(ModModelData.EAST) == BlockType.SOLID) {

            addQuadsIfPresent(quads, baseModelEast, state, side, random, modelData, renderType, false, false, false);

            if (coreChoice <= 4) {
                if (coreChoice <= 2) {
                    addQuadsIfPresent(quads, nosesModelEast, state, side, random, modelData, renderType, true, false, true);
                }
                if (coreChoice <= 3 && coreChoice > 1) {
                    addQuadsIfPresent(quads, noses2ModelEast, state, side, random, modelData, renderType, true, false, true);
                }
                if (coreChoice > 2) {
                    addQuadsIfPresent(quads, noses3ModelEast, state, side, random, modelData, renderType, true, false, true);
                }
            } else if (coreChoice <= 8) {
                if (coreChoice <= 7) {
                    addQuadsIfPresent(quads, eyesModelEast, state, side, random, modelData, renderType, true, false, true);
                }
                if (coreChoice > 6) {
                    addQuadsIfPresent(quads, eyes2ModelEast, state, side, random, modelData, renderType, true, false, true);
                }
            }

            if (modelData.get(ModModelData.ABOVE) == BlockType.SOLID || ((modelData.get(ModModelData.ABOVE) != BlockType.AIR || modelData.get(ModModelData.ABOVE_EAST) == BlockType.SAME) && modelData.get(ModModelData.ABOVE_EAST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, upArmModelEast, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.SOUTH) == BlockType.SOLID || ((modelData.get(ModModelData.SOUTH) != BlockType.AIR || modelData.get(ModModelData.SOUTH_EAST) == BlockType.SAME) && modelData.get(ModModelData.SOUTH_EAST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, southArmModelEast, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.BELOW) == BlockType.SOLID || ((modelData.get(ModModelData.BELOW) != BlockType.AIR || modelData.get(ModModelData.BELOW_EAST) == BlockType.SAME) && modelData.get(ModModelData.BELOW_EAST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, downArmModelEast, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.NORTH) == BlockType.SOLID || ((modelData.get(ModModelData.NORTH) != BlockType.AIR || modelData.get(ModModelData.NORTH_EAST) == BlockType.SAME) && modelData.get(ModModelData.NORTH_EAST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, northArmModelEast, state, side, random, modelData, renderType, false, false, false);
            }
        }

        if (modelData.get(ModModelData.WEST) == BlockType.SOLID) {

            addQuadsIfPresent(quads, baseModelWest, state, side, random, modelData, renderType, false, false, false);

            if (coreChoice <= 4) {
                if (coreChoice <= 2) {
                    addQuadsIfPresent(quads, nosesModelWest, state, side, random, modelData, renderType, true, false, true);
                }
                if (coreChoice <= 3 && coreChoice > 1) {
                    addQuadsIfPresent(quads, noses2ModelWest, state, side, random, modelData, renderType, true, false, true);
                }
                if (coreChoice > 2) {
                    addQuadsIfPresent(quads, noses3ModelWest, state, side, random, modelData, renderType, true, false, true);
                }
            } else if (coreChoice <= 8) {
                if (coreChoice <= 7) {
                    addQuadsIfPresent(quads, eyesModelWest, state, side, random, modelData, renderType, true, false, true);
                }
                if (coreChoice > 6) {
                    addQuadsIfPresent(quads, eyes2ModelWest, state, side, random, modelData, renderType, true, false, true);
                }
            }

            if (modelData.get(ModModelData.ABOVE) == BlockType.SOLID || ((modelData.get(ModModelData.ABOVE) != BlockType.AIR || modelData.get(ModModelData.ABOVE_WEST) == BlockType.SAME) && modelData.get(ModModelData.ABOVE_WEST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, upArmModelWest, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.NORTH) == BlockType.SOLID || ((modelData.get(ModModelData.NORTH) != BlockType.AIR || modelData.get(ModModelData.NORTH_WEST) == BlockType.SAME) && modelData.get(ModModelData.NORTH_WEST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, northArmModelWest, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.BELOW) == BlockType.SOLID || ((modelData.get(ModModelData.BELOW) != BlockType.AIR || modelData.get(ModModelData.BELOW_WEST) == BlockType.SAME) && modelData.get(ModModelData.BELOW_WEST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, downArmModelWest, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.SOUTH) == BlockType.SOLID || ((modelData.get(ModModelData.SOUTH) != BlockType.AIR || modelData.get(ModModelData.SOUTH_WEST) == BlockType.SAME) && modelData.get(ModModelData.SOUTH_WEST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, southArmModelWest, state, side, random, modelData, renderType, false, false, false);
            }
        }

        if (modelData.get(ModModelData.SOUTH) == BlockType.SOLID) {

            addQuadsIfPresent(quads, baseModelSouth, state, side, random, modelData, renderType, false, false, false);

            if (coreChoice <= 4) {
                if (coreChoice <= 2) {
                    addQuadsIfPresent(quads, nosesModelSouth, state, side, random, modelData, renderType, true, true, false);
                }
                if (coreChoice <= 3 && coreChoice > 1) {
                    addQuadsIfPresent(quads, noses2ModelSouth, state, side, random, modelData, renderType, true, true, false);
                }
                if (coreChoice > 2) {
                    addQuadsIfPresent(quads, noses3ModelSouth, state, side, random, modelData, renderType, true, true, false);
                }
            } else if (coreChoice <= 8) {
                if (coreChoice <= 7) {
                    addQuadsIfPresent(quads, eyesModelSouth, state, side, random, modelData, renderType, true, true, false);
                }
                if (coreChoice > 6) {
                    addQuadsIfPresent(quads, eyes2ModelSouth, state, side, random, modelData, renderType, true, true, false);
                }
            }

            if (modelData.get(ModModelData.ABOVE) == BlockType.SOLID || ((modelData.get(ModModelData.ABOVE) != BlockType.AIR || modelData.get(ModModelData.ABOVE_SOUTH) == BlockType.SAME) && modelData.get(ModModelData.ABOVE_SOUTH) != BlockType.AIR)) {
                addQuadsIfPresent(quads, upArmModelSouth, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.WEST) == BlockType.SOLID || ((modelData.get(ModModelData.WEST) != BlockType.AIR || modelData.get(ModModelData.SOUTH_WEST) == BlockType.SAME) && modelData.get(ModModelData.SOUTH_WEST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, westArmModelSouth, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.BELOW) == BlockType.SOLID || ((modelData.get(ModModelData.BELOW) != BlockType.AIR || modelData.get(ModModelData.BELOW_SOUTH) == BlockType.SAME) && modelData.get(ModModelData.BELOW_SOUTH) != BlockType.AIR)) {
                addQuadsIfPresent(quads, downArmModelSouth, state, side, random, modelData, renderType, false, false, false);
            }
            if (modelData.get(ModModelData.EAST) == BlockType.SOLID || ((modelData.get(ModModelData.EAST) != BlockType.AIR || modelData.get(ModModelData.SOUTH_EAST) == BlockType.SAME) && modelData.get(ModModelData.SOUTH_EAST) != BlockType.AIR)) {
                addQuadsIfPresent(quads, eastArmModelSouth, state, side, random, modelData, renderType, false, false, false);
            }
        }

//         Cache the result (make it unmodifiable to prevent mutation)
        List<BakedQuad> cached = List.copyOf(quads);
        quadsCache.put(key, cached);

        return cached;
    }

    private BakedQuad shiftQuad(BakedQuad quad, float offsetX, float offsetY, float offsetZ) {
        int[] vertices = quad.getVertices().clone();

        // BLOCK vertex format: 8 integers per vertex.
        for (int i = 0; i < 4; i++) {
            int index = i * 8;

            float x = Float.intBitsToFloat(vertices[index]);
            float y = Float.intBitsToFloat(vertices[index + 1]);
            float z = Float.intBitsToFloat(vertices[index + 2]);

            vertices[index] = Float.floatToRawIntBits(x + offsetX);
            vertices[index + 1] = Float.floatToRawIntBits(y + offsetY);
            vertices[index + 2] = Float.floatToRawIntBits(z + offsetZ);
        }

        return new BakedQuad(
                vertices,
                quad.getTintIndex(),
                quad.getDirection(),
                quad.getSprite(),
                quad.isShade(),
                quad.hasAmbientOcclusion()
        );
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(
            @NotNull BlockState state,
            @NotNull RandomSource random,
            @NotNull ModelData data
    ) {
        return ChunkRenderTypeSet.of(RenderType.cutout());
    }

}
