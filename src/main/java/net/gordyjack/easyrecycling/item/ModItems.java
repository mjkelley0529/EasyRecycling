package net.gordyjack.easyrecycling.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<Item> ITEMS = new ArrayList<>();

    public static final Item STONE_NUGGET = registerItem("pebble", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item DIAMOND_NUGGET = registerItem("diamond_nugget", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);
    public static final Item NETHERITE_NUGGET = registerItem("netherite_nugget", new Item(new FabricItemSettings()), ItemGroups.INGREDIENTS);

    //Private Methods
    private static void addToItemGroup(ItemGroup group, Item item) {
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(item));
    }
    private static Item registerItem(String name, Item item, ItemGroup... itemGroups) {
        Item returnItem = Registry.register(Registries.ITEM, EasyRecycling.getID(name), item);
        for(ItemGroup itemGroup : itemGroups) {
            addToItemGroup(itemGroup, returnItem);
        }
        ITEMS.add(returnItem);
        return returnItem;
    }
    public static void registerItems() {
        EasyRecycling.logDebug("Registering Items");
    }
}
