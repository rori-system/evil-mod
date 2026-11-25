
package com.rori.unboundedcorruption.client.model;

import com.rori.unboundedcorruption.blocks.blood_slime.SpreadingConnectedBlock;
import com.rori.unboundedcorruption.evilportal;
import com.rori.unboundedcorruption.registry.ModBlocks;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(
        modid = evilportal.MODID,
        value = Dist.CLIENT
)
public class ModModelEvents {
    private static final String ASSET_NAMESPACE = "evilportal";

    // Register the two models that will be selected at render time.
    @SubscribeEvent
    public static void registerAdditionalModels(
            ModelEvent.RegisterAdditional event
    ) {
        event.register(ModelResourceLocation.standalone(
                ResourceLocation.fromNamespaceAndPath(
                        ASSET_NAMESPACE,
                        "block/color_changing_blue"
                )
        ));

        event.register(ModelResourceLocation.standalone(
                ResourceLocation.fromNamespaceAndPath(
                        ASSET_NAMESPACE,
                        "block/color_changing_red"
                )
        ));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_base"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_base_up"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_base_north"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_base_south"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_base_east"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_base_west"));

        // Additional core model
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_core"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_core_2"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_core_2a"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_core_2b"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_core_2c"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_core_2d"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_hair1"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_hair2"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_hair1_up"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_hair2_up"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses_east"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses_south"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses_west"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses2"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses2_east"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses2_south"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses2_west"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses3"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses3_east"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses3_south"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_noses3_west"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_eyes"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_eyes_east"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_eyes_south"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_eyes_west"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_eyes2"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_eyes2_east"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_eyes2_south"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_eyes2_west"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_isolated"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_north"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_south"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_east"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_west"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_north_upf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_south_upf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_east_upf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_west_upf"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_north_eastf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_south_eastf"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_north_westf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_south_westf"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_east_northf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_west_northf"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_east_southf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_west_southf"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_up_northf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_up_southf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_up_eastf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_up_westf"));

        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_down_northf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_down_southf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_down_eastf"));
        event.register(modelLocation("block/connecting_slime_models/connecting_slime_arm_down_westf"));
    }

    // Replace the block's normal baked model with our custom model.
    @SubscribeEvent
    public static void modifyBakingResult(
            ModelEvent.ModifyBakingResult event
    ) {
        modifyColorChangingBlock(event);
        modifyConnectingSlime(event);
    }

    private static void modifyColorChangingBlock(ModelEvent.ModifyBakingResult event) {
        // Color-changing block setup

        ModelResourceLocation blockLocation =
                BlockModelShaper.stateToModelLocation(
                        ModBlocks.COLOR_CHANGING_BLOCK.get()
                                .defaultBlockState()
                );

        ModelResourceLocation blueLocation =
                ModelResourceLocation.standalone(
                        ResourceLocation.fromNamespaceAndPath(
                                ASSET_NAMESPACE,
                                "block/color_changing_blue"
                        )
                );

        ModelResourceLocation redLocation =
                ModelResourceLocation.standalone(
                        ResourceLocation.fromNamespaceAndPath(
                                ASSET_NAMESPACE,
                                "block/color_changing_red"
                        )
                );

        BakedModel originalModel =
                event.getModels().get(blockLocation);

        BakedModel blueModel =
                event.getModels().get(blueLocation);

        BakedModel redModel =
                event.getModels().get(redLocation);

        if (originalModel == null || blueModel == null || redModel == null) {
            return;
        }

        ColorChangingBakedModel customModel =
                new ColorChangingBakedModel(
                        blueModel,
                        redModel
                );

        event.getModels().put(blockLocation, customModel);
    }

    private static void modifyConnectingSlime(ModelEvent.ModifyBakingResult event) {

        // Build the custom model once

        BakedModel base = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_base"));
        BakedModel baseUp = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_base_up"));
        BakedModel baseNorth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_base_north"));
        BakedModel baseSouth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_base_south"));
        BakedModel baseEast = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_base_east"));
        BakedModel baseWest = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_base_west"));

        BakedModel core = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_core"));
        BakedModel core2 = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_core_2"));
        BakedModel core2a = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_core_2a"));
        BakedModel core2b = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_core_2b"));
        BakedModel core2c = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_core_2c"));
        BakedModel core2d = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_core_2d"));

        BakedModel deco1 = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_hair1"));
        BakedModel deco2 = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_hair2"));
        BakedModel deco1a = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_hair1_up"));
        BakedModel deco2a = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_hair2_up"));

        BakedModel noses = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses"));
        BakedModel nosesEast = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses_east"));
        BakedModel nosesSouth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses_south"));
        BakedModel nosesWest = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses_west"));
        BakedModel noses2 = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses2"));
        BakedModel noses2East = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses2_east"));
        BakedModel noses2South = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses2_south"));
        BakedModel noses2West = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses2_west"));
        BakedModel noses3 = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses3"));
        BakedModel noses3East = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses3_east"));
        BakedModel noses3South = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses3_south"));
        BakedModel noses3West = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_noses3_west"));

        BakedModel eyes = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_eyes"));
        BakedModel eyesEast = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_eyes_east"));
        BakedModel eyesSouth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_eyes_south"));
        BakedModel eyesWest = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_eyes_west"));
        BakedModel eyes2 = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_eyes2"));
        BakedModel eyes2East = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_eyes2_east"));
        BakedModel eyes2South = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_eyes2_south"));
        BakedModel eyes2West = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_eyes2_west"));

        BakedModel north = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_north"));
        BakedModel south = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_south"));
        BakedModel east = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_east"));
        BakedModel west = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_west"));

        // Horizontal arms with Up face
        BakedModel northUp = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_north_upf"));
        BakedModel southUp = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_south_upf"));
        BakedModel eastUp = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_east_upf"));
        BakedModel westUp = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_west_upf"));

        // Horizontal arms with East face (only cross-directions exist)
        BakedModel northEast = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_north_eastf"));
        BakedModel southEast = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_south_eastf"));

        // Horizontal arms with West face (only cross-directions exist)
        BakedModel northWest = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_north_westf"));
        BakedModel southWest = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_south_westf"));

        // Horizontal arms with North face (only cross-directions exist)
        BakedModel eastNorth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_east_northf"));
        BakedModel westNorth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_west_northf"));

        // Horizontal arms with South face (only cross-directions exist)
        BakedModel eastSouth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_east_southf"));
        BakedModel westSouth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_west_southf"));

        // Up arms with cardinal faces
        BakedModel upNorth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_up_northf"));
        BakedModel upSouth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_up_southf"));
        BakedModel upEast = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_up_eastf"));
        BakedModel upWest = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_up_westf"));

        // Down arms with cardinal faces
        BakedModel downNorth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_down_northf"));
        BakedModel downSouth = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_down_southf"));
        BakedModel downEast = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_down_eastf"));
        BakedModel downWest = event.getModels().get(modelLocation("block/connecting_slime_models/connecting_slime_arm_down_westf"));

        if (base == null || baseUp == null || baseNorth == null
                || baseSouth == null || baseEast == null || baseWest == null
                || core == null || core2 == null
                || north == null || south == null || east == null || west == null) {
            throw new IllegalStateException("A connecting slime model failed to load!");
        }

        BakedModel customModel = new ConnectedBlockBakedModel(
                base, baseUp, baseNorth, baseSouth, baseEast, baseWest,
                core, core2, core2a, core2b, core2c, core2d,
                deco1, deco2, deco1a, deco2a,
                noses, nosesEast, nosesSouth, nosesWest,
                noses2, noses2East, noses2South, noses2West,
                noses3, noses3East, noses3South, noses3West,
                eyes, eyesEast, eyesSouth, eyesWest,
                eyes2, eyes2East, eyes2South, eyes2West,
                north, south, east, west,
                northUp, southUp, eastUp, westUp,
                northEast, southEast,
                northWest, southWest,
                eastNorth, westNorth,
                eastSouth, westSouth,
                upNorth, upSouth, upEast, upWest,
                downNorth, downSouth, downEast, downWest
        );

        // Register the custom model for all generation values (0-7)
        BlockState defaultState = ModBlocks.BLOOD_SLIME.get().defaultBlockState();
        for (int generation = 0; generation <= 3; generation++) {
            BlockState state = defaultState.setValue(SpreadingConnectedBlock.GENERATION, generation);
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(state);
            event.getModels().put(location, customModel);
        }
    }

    private static ModelResourceLocation modelLocation(String path) {
        return ModelResourceLocation.standalone(
                ResourceLocation.fromNamespaceAndPath(
                        ASSET_NAMESPACE, path
                )
        );
    }
}
