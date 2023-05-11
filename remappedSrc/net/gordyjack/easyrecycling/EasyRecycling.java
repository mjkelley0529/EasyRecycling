package net.gordyjack.easyrecycling;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.gordyjack.easyrecycling.block.RecyclingTableEntity;
import net.gordyjack.easyrecycling.block.RecyclingTableBlock;
import net.gordyjack.easyrecycling.recipe.ModRecipes;
import net.gordyjack.easyrecycling.screen.ModScreenHandlerType;
import net.gordyjack.easyrecycling.screen.RecyclingScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EasyRecycling implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String  MOD_ID = "easyrecycling";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Block RECYCLING_TABLE_BLOCK = Registry.register(Registries.BLOCK, getID("recycling_table"),
			new RecyclingTableBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_BRICKS).requiresTool()));
	public static final Item RECYCLING_TABLE_ITEM = Registry.register(Registries.ITEM, getID("recycling_table"),
			new BlockItem(RECYCLING_TABLE_BLOCK, new FabricItemSettings()));
	public static final BlockEntityType<RecyclingTableEntity> RECYCLING_TABLE_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
			getID("recycling_table"), FabricBlockEntityTypeBuilder.create(RecyclingTableEntity::new, RECYCLING_TABLE_BLOCK).build(null));

	@Override
	public void onInitialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> entries.add(RECYCLING_TABLE_ITEM));
		ModScreenHandlerType.registerScreenHandlerTypes();
		ModRecipes.registerRecipes();
	}

	public static void logDebug(String string, Object... objects) {
		log('d', MOD_ID, string, objects);
	}
	public static void logError(String string, Object... objects) {
		log('e', MOD_ID, string, objects);
	}
	public static void logInfo(String string, Object... objects) {
		log('i', MOD_ID, string, objects);
	}
	public static void logTrace(String string, Object... objects) {
		log('t', MOD_ID, string, objects);
	}
	public static void logWarn(String string, Object... objects) {
		log('w', MOD_ID, string, objects);
	}
	private static void log(char switchCase, String namespace, String string, Object... objects) {
		String modID = "[" + namespace.toUpperCase() + "]: ";
		switch (switchCase) {
			case 'd' -> LOGGER.debug(modID + string, objects);
			case 'e' -> LOGGER.error(modID + string, objects);
			case 'i' -> LOGGER.info(modID + string, objects);
			case 't' -> LOGGER.trace(modID + string, objects);
			case 'w' -> LOGGER.warn(modID + string, objects);
		}
	}
	@Contract("_ -> new")
	public static @NotNull Identifier getID(String name) {
		return new Identifier(MOD_ID, name);
	}
	@Contract("_,_ -> new")
	public static @NotNull String getKey(String namespace, String name) {
		return namespace + "." + MOD_ID + "." + name;
	}
}