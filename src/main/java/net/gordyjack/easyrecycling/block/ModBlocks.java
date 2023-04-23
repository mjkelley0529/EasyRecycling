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

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final Block RECYCLING_TABLE = registerBlock("recycling_table",
            new RecyclingTableBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_BRICKS).requiresTool()), ItemGroups.FUNCTIONAL);

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        Block returnBlock = Registry.register(Registries.BLOCK, EasyRecycling.getID(name), block);
        BLOCKS.add(returnBlock);
        registerBlockItem(name, block, group);
        return returnBlock;
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
