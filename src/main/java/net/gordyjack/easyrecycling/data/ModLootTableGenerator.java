package net.gordyjack.easyrecycling.data;

import net.gordyjack.easyrecycling.EasyRecycling;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class ModLootTableGenerator extends FabricBlockLootTableProvider {
    public ModLootTableGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(EasyRecycling.RECYCLING_TABLE_BLOCK);
    }
}
