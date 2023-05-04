package net.gordyjack.easyrecycling.data.recipe;

import com.google.gson.JsonObject;
import net.gordyjack.easyrecycling.recipe.RecyclingTableRecipe;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RecyclingTableRecipeJsonBuilder {
    private final Item input;
    private final Item output;
    private final int maxOutputCount;
    private final int minHardness;
    private final RecipeSerializer<?> serializer;

    public RecyclingTableRecipeJsonBuilder(RecipeSerializer<?> serializer, Item input, Item output, int maxOutputCount) {
        this(serializer, input, output, maxOutputCount, 0);
    }
    public RecyclingTableRecipeJsonBuilder(RecipeSerializer<?> serializer, Item input, Item output, int maxOutputCount, int minHardness) {
        this.serializer = serializer;
        this.input = input;
        this.output = output;
        this.maxOutputCount = maxOutputCount;
        this.minHardness = minHardness;
    }

    public static RecyclingTableRecipeJsonBuilder create(Item input, Item output, int maxOutputCount) {
        return RecyclingTableRecipeJsonBuilder.create(input, output, maxOutputCount, 0);
    }
    public static RecyclingTableRecipeJsonBuilder create(Item input, Item output, int maxOutputCount, int minHardness) {
        return new RecyclingTableRecipeJsonBuilder(RecyclingTableRecipe.Serializer.INSTANCE, input, output, maxOutputCount, minHardness);
    }
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        exporter.accept(new RecyclingTableRecipeJsonProvider(recipeId, this.serializer, this.input,
                this.output, this.maxOutputCount, this.minHardness));
    }

    public static class RecyclingTableRecipeJsonProvider
            implements RecipeJsonProvider {
        private final Identifier recipeId;
        private final Item input;
        private final Item output;
        private final int maxOutputCount;
        private final int minimumHardness;
        private final RecipeSerializer<?> serializer;

        public RecyclingTableRecipeJsonProvider(Identifier recipeId, RecipeSerializer<?> serializer, Item input,
                                                Item output, int maxOutputCount, int minimumHardness) {
            this.recipeId = recipeId;
            this.serializer = serializer;
            this.input = input;
            this.output = output;
            this.maxOutputCount = maxOutputCount;
            this.minimumHardness = minimumHardness;
        }

        @Override
        public void serialize(JsonObject json) {
            JsonObject inputObject = new JsonObject();
            inputObject.addProperty("item", Registries.ITEM.getId(this.input).toString());
            json.add("input", inputObject);

            JsonObject outputObject = new JsonObject();
            outputObject.addProperty("item", Registries.ITEM.getId(this.output).toString());
            outputObject.addProperty("count", maxOutputCount);
            json.add("output", outputObject);

            json.addProperty("minimumHardness", this.minimumHardness);
        }

        @Override
        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return this.serializer;
        }

        @Override
        @Nullable
        public JsonObject toAdvancementJson() {
            return null;
        }

        @Override
        @Nullable
        public Identifier getAdvancementId() {
            return null;
        }
    }
}
