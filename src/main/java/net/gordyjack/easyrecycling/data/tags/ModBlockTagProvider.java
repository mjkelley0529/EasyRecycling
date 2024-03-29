package net.gordyjack.easyrecycling.data.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.gordyjack.easyrecycling.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    //Common Tags

    //Minecraft Tags
    public final TagKey<Block> MINECRAFT_MINEABLE_PICKAXE = registerMinecraftTagKey("mineable/pickaxe");
    public final TagKey<Block> MINECRAFT_TOOL_STONE = registerMinecraftTagKey("needs_stone_tool");

    //Mod Tags
    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(MINECRAFT_MINEABLE_PICKAXE).add(ModBlocks.RECYCLING_TABLE);
        getOrCreateTagBuilder(MINECRAFT_TOOL_STONE).add(ModBlocks.RECYCLING_TABLE);
    }

    public TagKey<Block> registerModTagKey(String name) {
        return registerTagKey(EasyRecycling.MOD_ID, name);
    }
    public TagKey<Block> registerCommonTagKey(String name) {
        return registerTagKey("c", name);
    }
    public TagKey<Block> registerMinecraftTagKey(String name) {
        return registerTagKey("minecraft", name);
    }
    public TagKey<Block> registerTagKey(String namespace, String name) {
        return TagKey.of(this.registryRef, new Identifier(namespace, name));
    }
}
