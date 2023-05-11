package net.gordyjack.easyrecycling.data.tags;

import com.ibm.icu.util.CodePointTrie;
import com.mojang.datafixers.types.templates.Tag;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture, null);
    }
    //Common Tags
    public final TagKey<Item> COMMON_CONCRETE_POWDER = registerCommonTagKey("concrete_powder");

    //Minecraft Tags

    //Mod Tags
    public static final TagKey<Item> LARGE_MATERIAL = registerModTagKey("large_material");
    public static final TagKey<Item> MEDIUM_MATERIAL = registerModTagKey("medium_material");
    public static final TagKey<Item> SMALL_MATERIAL = registerModTagKey("small_material");
    public static final TagKey<Item> HARDNESS_4_MATERIAL = registerModTagKey("hardness_4_material");
    public static final TagKey<Item> HARDNESS_3_MATERIAL = registerModTagKey("hardness_3_material");
    public static final TagKey<Item> HARDNESS_2_MATERIAL = registerModTagKey("hardness_2_material");
    public static final TagKey<Item> HARDNESS_1_MATERIAL = registerModTagKey("hardness_1_material");

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        //Common Tags
        getOrCreateTagBuilder(COMMON_CONCRETE_POWDER).add(
                Items.BLACK_CONCRETE_POWDER,
                Items.BLUE_CONCRETE_POWDER,
                Items.BROWN_CONCRETE_POWDER,
                Items.CYAN_CONCRETE_POWDER,
                Items.GRAY_CONCRETE_POWDER,
                Items.GREEN_CONCRETE_POWDER,
                Items.LIGHT_BLUE_CONCRETE_POWDER,
                Items.LIGHT_GRAY_CONCRETE_POWDER,
                Items.LIME_CONCRETE_POWDER,
                Items.MAGENTA_CONCRETE_POWDER,
                Items.ORANGE_CONCRETE_POWDER,
                Items.PINK_CONCRETE_POWDER,
                Items.PURPLE_CONCRETE_POWDER,
                Items.RED_CONCRETE_POWDER,
                Items.WHITE_CONCRETE_POWDER,
                Items.YELLOW_CONCRETE_POWDER
        );

        //Mod Tags
        getOrCreateTagBuilder(SMALL_MATERIAL).add(
                Items.AMETHYST_SHARD,
                Items.SMALL_AMETHYST_BUD,
                Items.MEDIUM_AMETHYST_BUD,
                Items.BONE,
                Items.DIAMOND,
                Items.EMERALD,
                Items.FLINT,
                Items.GLOWSTONE_DUST,
                Items.IRON_INGOT,
                Items.NETHERITE_SCRAP,
                Items.QUARTZ,
                Items.REDSTONE,
                Items.SCUTE
        );
        getOrCreateTagBuilder(MEDIUM_MATERIAL).add(
                Items.AMETHYST_CLUSTER,
                Items.LARGE_AMETHYST_BUD,
                Items.ANCIENT_DEBRIS,
                Items.BASALT,
                Items.BLACKSTONE,
                Items.DEEPSLATE,
                Items.GRAVEL,
                Items.NETHERITE_INGOT,
                Items.SAND,
                Items.TURTLE_HELMET
        );
        getOrCreateTagBuilder(LARGE_MATERIAL).add(
                Items.AMETHYST_BLOCK,
                Items.CRYING_OBSIDIAN,
                Items.DIAMOND_BLOCK,
                Items.EMERALD_BLOCK,
                Items.END_STONE,
                Items.IRON_BLOCK,
                Items.NETHERITE_BLOCK,
                Items.OBSIDIAN,
                Items.QUARTZ_BLOCK
        ).addOptionalTag(COMMON_CONCRETE_POWDER);

        getOrCreateTagBuilder(HARDNESS_4_MATERIAL).add(
                Items.ANCIENT_DEBRIS,
                Items.CRYING_OBSIDIAN,
                Items.NETHERITE_BLOCK,
                Items.NETHERITE_INGOT,
                Items.NETHERITE_SCRAP
        );
        getOrCreateTagBuilder(HARDNESS_3_MATERIAL).add(
                Items.AMETHYST_BLOCK,
                Items.AMETHYST_CLUSTER,
                Items.AMETHYST_SHARD,
                Items.SMALL_AMETHYST_BUD,
                Items.MEDIUM_AMETHYST_BUD,
                Items.LARGE_AMETHYST_BUD,
                Items.DIAMOND,
                Items.DIAMOND_BLOCK,
                Items.EMERALD,
                Items.EMERALD_BLOCK,
                Items.END_STONE,
                Items.OBSIDIAN
        );
        getOrCreateTagBuilder(HARDNESS_2_MATERIAL).add(
                Items.BASALT,
                Items.BLACKSTONE,
                Items.DEEPSLATE,
                Items.IRON_BLOCK,
                Items.IRON_INGOT,
                Items.QUARTZ,
                Items.QUARTZ_BLOCK,
                Items.REDSTONE,
                Items.SCUTE,
                Items.TURTLE_HELMET
        );
        getOrCreateTagBuilder(HARDNESS_1_MATERIAL).add(
                Items.BONE,
                Items.FLINT,
                Items.GLOWSTONE_DUST,
                Items.GRAVEL,
                Items.SAND
        ).addOptionalTag(COMMON_CONCRETE_POWDER);
    }
    public static TagKey<Item> registerModTagKey(String name) {
        return registerTagKey(EasyRecycling.MOD_ID, name);
    }
    public static TagKey<Item> registerCommonTagKey(String name) {
        return registerTagKey("c", name);
    }
    public static TagKey<Item> registerMinecraftTagKey(String name) {
        return registerTagKey("minecraft", name);
    }
    public static TagKey<Item> registerTagKey(String namespace, String name) {
        return TagKey.of(Registries.ITEM.getKey(), new Identifier(namespace, name));
    }
}
