package net.gordyjack.easyrecycling.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
    public static final Block RECYCLING_TABLE = registerBlock("recycling_table",
            new RecyclingBlock(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE)), ItemGroups.FUNCTIONAL);

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registries.BLOCK, EasyRecycling.getID(name), block);
    }
    private static Item registerBlockItem(String name, Block block, ItemGroup... itemGroups) {
        Item item = Registry.register(Registries.ITEM, EasyRecycling.getID(name),
                new BlockItem(block, new FabricItemSettings()));
        for (ItemGroup itemGroup : itemGroups)
            ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.add(item));
        return item;
    }

    public static void registerBlocks() {
        EasyRecycling.logDebug("Registering Blocks");
    }
}
