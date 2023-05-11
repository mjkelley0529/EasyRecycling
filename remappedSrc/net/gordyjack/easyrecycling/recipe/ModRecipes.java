package net.gordyjack.easyrecycling.recipe;

import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipes {
    public static void registerRecipes() {
        Registry.register(Registries.RECIPE_SERIALIZER, EasyRecycling.getID(RecyclingTableRecipe.Serializer.ID),
                RecyclingTableRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, EasyRecycling.getID(RecyclingTableRecipe.Type.ID),
                RecyclingTableRecipe.Type.INSTANCE);
    }
}
