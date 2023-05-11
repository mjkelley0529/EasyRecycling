package net.gordyjack.easyrecycling.recipe;

import com.google.gson.JsonObject;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.inventory.Inventory;
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

public class RecyclingTableRecipe implements Recipe<Inventory> {
    //Fields
    private static final String JSONID = "recycling_table";
    private final Identifier ID;
    private final ItemStack INPUT;
    private final ItemStack OUTPUT;
    private final int MIN_HARDNESS;

    //Constructor
    public RecyclingTableRecipe(Identifier id, ItemStack output, ItemStack input, int minHardness) {
        this.ID = id;
        this.OUTPUT = output;
        this.INPUT = input;
        this.MIN_HARDNESS = minHardness;
    }

    //Methods
    //Inherited Methods
    @Override
    public boolean matches(Inventory inventory, World world) {
        if (world.isClient()) {
            return false;
        }
        return INPUT.getItem().equals(inventory.getStack(0).getItem());
    }
    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return null;
    }
    @Override
    public boolean fits(int width, int height) {
        return true;
    }
    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return getOutput();
    }
    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return DefaultedList.ofSize(1, Ingredient.ofItems(getInput().getItem()));
    }
    public ItemStack getInput() {
        return this.INPUT.copy();
    }
    public ItemStack getOutput() {
        return this.OUTPUT.copy();
    }
    public int getHardness() {
        return this.MIN_HARDNESS;
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
            int minHardness = JsonHelper.getInt(json, "minimumHardness");

            //EasyRecycling.logError("read() 1 " + input + " | " + output + " | " + minHardness);

            return new RecyclingTableRecipe(id, output, input, minHardness);
        }
        @Override
        public RecyclingTableRecipe read(Identifier id, PacketByteBuf buf) {
            ItemStack input = buf.readItemStack();
            ItemStack output = buf.readItemStack();
            int minHardness = buf.readInt();

            //EasyRecycling.logError("read() 2 " + input + " | " + output + " | " + minHardness);

            return new RecyclingTableRecipe(id, output, input, minHardness);
        }
        @Override
        public void write(PacketByteBuf buf, RecyclingTableRecipe recipe) {
            ItemStack input , output;
            int minHardness;
            buf.writeItemStack(input = recipe.getInput());
            buf.writeItemStack(output = recipe.getOutput());
            buf.writeInt(minHardness = recipe.getHardness());

            //EasyRecycling.logError("write()" + input + " | " + output + " | " + minHardness);
        }
    }
}
