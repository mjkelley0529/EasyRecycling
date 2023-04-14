package net.gordyjack.easyrecycling;

import net.fabricmc.api.ModInitializer;

import net.gordyjack.easyrecycling.block.ModBlocks;
import net.gordyjack.easyrecycling.item.ModItems;
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
	private static final Logger LOGGER = LoggerFactory.getLogger("easyrecycling");

	@Override
	public void onInitialize() {
		ModBlocks.registerBlocks();
		ModItems.registerItems();
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
	@Contract("_ -> new")
	public static @NotNull String getMessageKey(String name) {
		return getKey("message", name);
	}
}