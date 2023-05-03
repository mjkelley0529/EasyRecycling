package net.gordyjack.easyrecycling.recipe;

import com.google.gson.JsonObject;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class RecyclingTableRecipe implements Recipe<SimpleInventory> {
    //Fields
    private static final String JSONID = "recycling_table";
    private final Identifier ID;
    private final ItemStack INPUT;
    private final ItemStack OUTPUT;

    //Constructor
    public RecyclingTableRecipe(Identifier id, ItemStack output, ItemStack input) {
        this.ID = id;
        this.OUTPUT = output;
        this.INPUT = input;
    }

    //Methods
    //Inherited Methods
    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient()) {
            return false;
        }
        return INPUT.getItem().equals(inventory.getStack(0).getItem());
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return null;
    }
    @Override
    public boolean fits(int width, int height) {
        return true;
    }
    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return null;
    }
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.ofSize(1, Ingredient.ofItems(getInput().getItem()));
    }
    public ItemStack getInput() {
        return INPUT.copy();
    }
    public ItemStack getOutput() {
        return OUTPUT.copy();
    }
    @Override
    public Identifier getId() {
        return ID;
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }
    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    //Internal Classes
    public static class Type implements RecipeType<RecyclingTableRecipe> {
        //Fields
        public static final Type INSTANCE = new Type();
        public static final String ID = JSONID;

        //Constructor
        private Type() {

        }
    }
    public static class Serializer implements RecipeSerializer<RecyclingTableRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = JSONID;

        //Methods
        @Override
        public RecyclingTableRecipe read(Identifier id, JsonObject json) {
            ItemStack input = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "input"));
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            EasyRecycling.logError("read() 1 " + input + " | " + output);

            return new RecyclingTableRecipe(id, output, input);
        }
        @Override
        public RecyclingTableRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack input = buf.readItemStack();
            ItemStack output = buf.readItemStack();

            EasyRecycling.logError("read() 2 " + input + " | " + output);

            return new RecyclingTableRecipe(id, output, input);
        }
        @Override
        public void write(PacketByteBuf buf, RecyclingTableRecipe recipe) {
            ItemStack input , output;
            buf.writeItemStack(input = recipe.getInput());
            buf.writeItemStack(output = recipe.getOutput());

            EasyRecycling.logError("write()" + input + " | " + output);
        }
    }
}
