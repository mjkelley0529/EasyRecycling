package net.gordyjack.easyrecycling.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture, null);
    }
    //Common Tags

    //Minecraft Tags

    //Mod Tags

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

    }
    public TagKey<Item> registerModTagKey(String name) {
        return registerTagKey(EasyRecycling.MOD_ID, name);
    }
    public TagKey<Item> registerCommonTagKey(String name) {
        return registerTagKey("c", name);
    }
    public TagKey<Item> registerMinecraftTagKey(String name) {
        return registerTagKey("minecraft", name);
    }
    public TagKey<Item> registerTagKey(String namespace, String name) {
        return TagKey.of(this.registryRef, new Identifier(namespace, name));
    }
}
