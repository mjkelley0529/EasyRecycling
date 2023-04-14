package net.gordyjack.easyrecycling;

import net.gordyjack.easyrecycling.data.*;
import net.gordyjack.easyrecycling.data.lang.*;
import net.gordyjack.easyrecycling.data.tags.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataProvider;

public class EasyRecyclingDataGenerator implements DataGeneratorEntrypoint {
	public DataProvider modelProvider;
	public DataProvider langENUSProvider;
	public DataProvider lootTableProvider;
	public DataProvider recipeProvider;
	public DataProvider tagProviderBlock;
	public DataProvider tagProviderItem;

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		langENUSProvider = pack.addProvider(ENUSLangProvider::new);
		lootTableProvider = pack.addProvider(ModLootTableGenerator::new);
		modelProvider = pack.addProvider(ModModelProvider::new);
		recipeProvider = pack.addProvider(ModRecipeGenerator::new);
		tagProviderBlock = pack.addProvider(ModBlockTagProvider::new);
		tagProviderItem = pack.addProvider(ModItemTagProvider::new);
	}
}
