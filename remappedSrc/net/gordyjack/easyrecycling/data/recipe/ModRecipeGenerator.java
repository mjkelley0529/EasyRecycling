package net.gordyjack.easyrecycling.data.recipe;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.gordyjack.easyrecycling.screen.RecyclingScreenHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        //Crafting Recipes
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, EasyRecycling.RECYCLING_TABLE_BLOCK)
                .input('A', Blocks.ANVIL)
                .input('G', Blocks.GRINDSTONE)
                .input('D', Items.DEEPSLATE_BRICKS)
                .pattern(" G ")
                .pattern("DAD")
                .criterion(FabricRecipeProvider.hasItem(Blocks.ANVIL),
                        FabricRecipeProvider.conditionsFromItem(Blocks.ANVIL))
                .criterion(FabricRecipeProvider.hasItem(Blocks.GRINDSTONE),
                        FabricRecipeProvider.conditionsFromItem(Blocks.GRINDSTONE))
                .offerTo(exporter, new Identifier(FabricRecipeProvider.getRecipeName(EasyRecycling.RECYCLING_TABLE_BLOCK)));

        /*
        RecipeManager recipeManager = new RecipeManager();
        RecyclingScreenHandler.generateLegalItems().forEach(item -> {
            EasyRecycling.logError(item.getName().getString());
            String recipeKey = item.getTranslationKey();
            recipeKey = recipeKey.substring(recipeKey.indexOf('.') + 1);
            recipeKey = recipeKey.replace('.', ':');
            Optional<? extends Recipe<?>> recipe = recipeManager.get(new Identifier(recipeKey));
            EasyRecycling.logError(recipeManager.getName() + " | " + recipeKey + " | " + recipe.toString() + " | " + recipeManager.listAllOfType(RecipeType.CRAFTING).toString());
            if (recipe.isPresent()) {
                CraftingRecipe craftingRecipe = (CraftingRecipe) recipe.get();
                DefaultedList<Ingredient> ingredients = craftingRecipe.getIngredients();

                for (Ingredient ingredient : ingredients) {
                    ItemStack[] matchingStacks = ingredient.getMatchingStacks();
                    for (ItemStack itemStack : matchingStacks) {
                        if (itemStack.getItem() == Items.STICK) {
                            continue;
                        }
                        offerRecyclingTableRecipe(exporter, itemStack.getItem(), item, itemStack.getCount());
                    }
                }
            }
        });
        */
        for(Item item : RecyclingScreenHandler.generateLegalItems()) {
            Item outputItem = Item.byRawId(0);

            boolean wood = itemIs("wood", item);
            boolean stone = itemIs("stone", item);
            boolean iron = itemIs("iron", item) || itemIs("chainmail", item);
            boolean gold = itemIs("gold", item);
            boolean diamond = itemIs("diamond", item);
            boolean netherite = itemIs("netherite", item);
            boolean leather = itemIs("leather", item);
            boolean turtle = itemIs("turtle", item);

            boolean axe = itemClassIs("axe", item);
            boolean hoe = itemClassIs("hoe", item);
            boolean pickaxe = itemClassIs("pickaxe", item);
            boolean shovel = itemClassIs("shovel", item);
            boolean sword = itemClassIs("sword", item);

            boolean helmet = itemIs("helmet", item);
            boolean chestplate = itemIs("chestplate", item);
            boolean legging = itemIs("legging", item);
            boolean boot = itemIs("boot", item);

            int itemCount = 0;
            int minHardness = 0;

            if (item.equals(Items.ELYTRA)) {
                itemCount = 2;
                outputItem = Items.PHANTOM_MEMBRANE;
            } else if (axe || pickaxe) itemCount = 3;
            else if (hoe || sword) itemCount = 2;
            else if (shovel) itemCount = 1;
            else if (helmet) itemCount = 5;
            else if (chestplate) itemCount = 8;
            else if (legging) itemCount = 7;
            else if (boot) itemCount = 4;

            if (wood) {
                minHardness = 0;
                itemCount *= 2;
                outputItem = Items.STICK;
            } else if (stone) {
                minHardness = 1;
                outputItem = Items.COBBLESTONE;
            } else if (leather) {
                minHardness = 0;
                outputItem = Items.LEATHER;
            } else if (iron) {
                minHardness = 2;
                outputItem = Items.IRON_INGOT;
            } else if (gold) {
                minHardness = 2;
                outputItem = Items.GOLD_INGOT;
            } else if (diamond) {
                minHardness = 3;
                outputItem = Items.DIAMOND;
            } else if (netherite) {
                itemCount = 1;
                minHardness = 4;
                outputItem = Items.NETHERITE_INGOT;
            } else if (turtle) {
                minHardness = 2;
                outputItem = Items.SCUTE;
            }


            offerRecyclingTableRecipe(exporter, outputItem, item, itemCount, minHardness);
        }
    }
    private void offerRecyclingTableRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible output,
                                           ItemConvertible input, int maxOutputCount, int minHardness) {
        RecyclingTableRecipeJsonBuilder.create(input.asItem(), output.asItem(), maxOutputCount, minHardness)
                .offerTo(exporter, getRecipeID(input, output, "recycling"));
    }
    private static Identifier getRecipeID(ItemConvertible output) {
        return EasyRecycling.getID(getItemPath(output));
    }
    private static Identifier getRecipeID(ItemConvertible input, ItemConvertible output, String appendType) {
        String inputKey = input.asItem().getTranslationKey(new ItemStack(input));
        String outputKey = output.asItem().getTranslationKey(new ItemStack(output));

        inputKey = inputKey.substring(inputKey.lastIndexOf('.') + 1);
        outputKey = outputKey.substring(outputKey.lastIndexOf('.') + 1);

        return EasyRecycling.getID(outputKey + "_" + appendType + "_from_" + inputKey);
    }
    private boolean itemIs(String type, Item item) {
        return item.getTranslationKey().contains(type);
    }
    private boolean itemClassIs(String type, Item item) {
        return item.getClass().getCanonicalName().toLowerCase().contains(type.toLowerCase());
    }
}
