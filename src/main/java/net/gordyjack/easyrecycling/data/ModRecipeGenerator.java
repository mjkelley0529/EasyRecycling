package net.gordyjack.easyrecycling.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.gordyjack.easyrecycling.block.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import java.util.function.Consumer;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        //Crafting Recipes
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.RECYCLING_TABLE)
                .input('A', Blocks.ANVIL)
                .input('G', Blocks.GRINDSTONE)
                .input('D', Items.DEEPSLATE_BRICKS)
                .pattern(" G ")
                .pattern("DAD")
                .criterion(FabricRecipeProvider.hasItem(Blocks.ANVIL),
                        FabricRecipeProvider.conditionsFromItem(Blocks.ANVIL))
                .criterion(FabricRecipeProvider.hasItem(Blocks.GRINDSTONE),
                        FabricRecipeProvider.conditionsFromItem(Blocks.GRINDSTONE))
                .offerTo(exporter, new Identifier(FabricRecipeProvider.getRecipeName(ModBlocks.RECYCLING_TABLE)));
    }
}
