package net.gordyjack.easyrecycling.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.gordyjack.easyrecycling.block.ModBlocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        EasyRecycling.logInfo("Generating Block Models");

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RECYCLING_TABLE);
    }
    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
