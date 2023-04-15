package net.gordyjack.easyrecycling.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.gordyjack.easyrecycling.block.ModBlocks;
import net.gordyjack.easyrecycling.item.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import java.util.function.Consumer;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        //Crafting Recipes
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.DIAMOND_NUGGET, RecipeCategory.MISC, Items.DIAMOND);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.NETHERITE_NUGGET, RecipeCategory.MISC, Items.NETHERITE_INGOT);
        offerReversibleCompactingRecipes(exporter, RecipeCategory.MISC, ModItems.STONE_NUGGET, RecipeCategory.BUILDING_BLOCKS, Blocks.COBBLESTONE);

        offer2x2CompactingRecipe(exporter, RecipeCategory.MISC, Items.SCUTE, ModItems.SCUTE_SHARD);
        offerShapelessRecipe(exporter, ModItems.SCUTE_SHARD, Items.SCUTE, "misc", 4);
        offerShapelessRecipe(exporter, ModItems.LEATHER_STRIP, Items.LEATHER, "misc", 6);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Items.LEATHER)
                .input('S', ModItems.LEATHER_STRIP)
                .pattern("S S")
                .pattern(" S ")
                .pattern("SSS")
                .criterion(FabricRecipeProvider.hasItem(Items.LEATHER),
                        FabricRecipeProvider.conditionsFromItem(Items.LEATHER))
                .criterion(FabricRecipeProvider.hasItem(ModItems.LEATHER_STRIP),
                        FabricRecipeProvider.conditionsFromItem(ModItems.LEATHER_STRIP))
                .offerTo(exporter, new Identifier(FabricRecipeProvider.getRecipeName(Items.LEATHER)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.RECYCLING_TABLE)
                .input('A', Blocks.ANVIL)
                .input('G', Blocks.GRINDSTONE)
                .input('D', Items.DEEPSLATE_BRICKS)
                .pattern(" G ")
                .pattern("DAD")
                .pattern("DDD")
                .criterion(FabricRecipeProvider.hasItem(Blocks.ANVIL),
                        FabricRecipeProvider.conditionsFromItem(Blocks.ANVIL))
                .criterion(FabricRecipeProvider.hasItem(Blocks.GRINDSTONE),
                        FabricRecipeProvider.conditionsFromItem(Blocks.GRINDSTONE))
                .offerTo(exporter, new Identifier(FabricRecipeProvider.getRecipeName(ModBlocks.RECYCLING_TABLE)));
    }
}
