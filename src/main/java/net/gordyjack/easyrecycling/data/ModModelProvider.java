package net.gordyjack.easyrecycling.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        EasyRecycling.logInfo("Generating Block Models");

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(
                        EasyRecycling.RECYCLING_TABLE_BLOCK, BlockStateVariant.create().put(
                                VariantSettings.MODEL, ModelIds.getBlockModelId(EasyRecycling.RECYCLING_TABLE_BLOCK)
                        )
                ).coordinate(
                        BlockStateVariantMap.create(Properties.WALL_MOUNT_LOCATION, Properties.HORIZONTAL_FACING)
                                .register(WallMountLocation.FLOOR, Direction.NORTH, BlockStateVariant.create())
                                .register(WallMountLocation.FLOOR, Direction.EAST, BlockStateVariant.create()
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                .register(WallMountLocation.FLOOR, Direction.SOUTH, BlockStateVariant.create()
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(WallMountLocation.FLOOR, Direction.WEST, BlockStateVariant.create()
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R270))
                                .register(WallMountLocation.WALL, Direction.NORTH, BlockStateVariant.create()
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90))
                                .register(WallMountLocation.WALL, Direction.EAST, BlockStateVariant.create()
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                .register(WallMountLocation.WALL, Direction.SOUTH, BlockStateVariant.create()
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(WallMountLocation.WALL, Direction.WEST, BlockStateVariant.create()
                                        .put(VariantSettings.X, VariantSettings.Rotation.R90)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R270))
                                .register(WallMountLocation.CEILING, Direction.SOUTH, BlockStateVariant.create()
                                        .put(VariantSettings.X, VariantSettings.Rotation.R180))
                                .register(WallMountLocation.CEILING, Direction.WEST, BlockStateVariant.create()
                                        .put(VariantSettings.X, VariantSettings.Rotation.R180)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R90))
                                .register(WallMountLocation.CEILING, Direction.NORTH, BlockStateVariant.create()
                                        .put(VariantSettings.X, VariantSettings.Rotation.R180)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R180))
                                .register(WallMountLocation.CEILING, Direction.EAST, BlockStateVariant.create()
                                        .put(VariantSettings.X, VariantSettings.Rotation.R180)
                                        .put(VariantSettings.Y, VariantSettings.Rotation.R270))));

    }
    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
