package net.gordyjack.easyrecycling.screen;

import net.gordyjack.easyrecycling.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclingScreenHandler extends ScreenHandler {
    private final Inventory INPUT = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            RecyclingScreenHandler.this.onContentChanged(this);
        }
    };
    private final Inventory RESULT = new CraftingResultInventory();
    private final ScreenHandlerContext CONTEXT;
    private final PlayerEntity PLAYER;
    private final List<Item> AXES = new ArrayList<>();
    private final List<Item> HOES = new ArrayList<>();
    private final List<Item> PICKAXES = new ArrayList<>();
    private final List<Item> SHOVELS = new ArrayList<>();
    private final List<Item> SWORDS = new ArrayList<>();
    private final List<Item> HELMETS = new ArrayList<>();
    private final List<Item> CHESTPLATES = new ArrayList<>();
    private final List<Item> PANTS = new ArrayList<>();
    private final List<Item> BOOTS = new ArrayList<>();

    public RecyclingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public RecyclingScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(ModScreenHandlerType.RECYCLING_TABLE, syncId);
        this.CONTEXT = context;
        this.PLAYER = playerInventory.player;

        AXES.add(Items.WOODEN_AXE);
        AXES.add(Items.STONE_AXE);
        AXES.add(Items.IRON_AXE);
        AXES.add(Items.GOLDEN_AXE);
        AXES.add(Items.DIAMOND_AXE);
        AXES.add(Items.NETHERITE_AXE);

        HOES.add(Items.WOODEN_HOE);
        HOES.add(Items.STONE_HOE);
        HOES.add(Items.IRON_HOE);
        HOES.add(Items.GOLDEN_HOE);
        HOES.add(Items.DIAMOND_HOE);
        HOES.add(Items.NETHERITE_HOE);

        PICKAXES.add(Items.WOODEN_PICKAXE);
        PICKAXES.add(Items.STONE_PICKAXE);
        PICKAXES.add(Items.IRON_PICKAXE);
        PICKAXES.add(Items.GOLDEN_PICKAXE);
        PICKAXES.add(Items.DIAMOND_PICKAXE);
        PICKAXES.add(Items.NETHERITE_PICKAXE);

        SHOVELS.add(Items.WOODEN_SHOVEL);
        SHOVELS.add(Items.STONE_SHOVEL);
        SHOVELS.add(Items.IRON_SHOVEL);
        SHOVELS.add(Items.GOLDEN_SHOVEL);
        SHOVELS.add(Items.DIAMOND_SHOVEL);
        SHOVELS.add(Items.NETHERITE_SHOVEL);

        SWORDS.add(Items.WOODEN_SWORD);
        SWORDS.add(Items.STONE_SWORD);
        SWORDS.add(Items.IRON_SWORD);
        SWORDS.add(Items.GOLDEN_SWORD);
        SWORDS.add(Items.DIAMOND_SWORD);
        SWORDS.add(Items.IRON_SWORD);

        HELMETS.add(Items.LEATHER_HELMET);
        HELMETS.add(Items.CHAINMAIL_HELMET);
        HELMETS.add(Items.IRON_HELMET);
        HELMETS.add(Items.GOLDEN_HELMET);
        HELMETS.add(Items.DIAMOND_HELMET);
        HELMETS.add(Items.NETHERITE_HELMET);
        HELMETS.add(Items.TURTLE_HELMET);

        CHESTPLATES.add(Items.LEATHER_CHESTPLATE);
        CHESTPLATES.add(Items.CHAINMAIL_CHESTPLATE);
        CHESTPLATES.add(Items.IRON_CHESTPLATE);
        CHESTPLATES.add(Items.GOLDEN_CHESTPLATE);
        CHESTPLATES.add(Items.DIAMOND_CHESTPLATE);
        CHESTPLATES.add(Items.NETHERITE_CHESTPLATE);
        CHESTPLATES.add(Items.ELYTRA);

        PANTS.add(Items.LEATHER_LEGGINGS);
        PANTS.add(Items.CHAINMAIL_LEGGINGS);
        PANTS.add(Items.IRON_LEGGINGS);
        PANTS.add(Items.GOLDEN_LEGGINGS);
        PANTS.add(Items.DIAMOND_LEGGINGS);
        PANTS.add(Items.NETHERITE_LEGGINGS);

        BOOTS.add(Items.LEATHER_BOOTS);
        BOOTS.add(Items.CHAINMAIL_BOOTS);
        BOOTS.add(Items.IRON_BOOTS);
        BOOTS.add(Items.GOLDEN_BOOTS);
        BOOTS.add(Items.DIAMOND_BOOTS);
        BOOTS.add(Items.NETHERITE_BOOTS);

        this.addSlot(new Slot(INPUT, 0, 49, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                Item item = stack.getItem();
                return AXES.contains(item) || HOES.contains(item) || PICKAXES.contains(item) || SHOVELS.contains(item) || SWORDS.contains(item) ||
                        HELMETS.contains(item) || CHESTPLATES.contains(item) || PANTS.contains(item) || BOOTS.contains(item);
            }
        });
        this.addSlot(new Slot(RESULT, 1, 129, 34){
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                RecyclingScreenHandler.this.INPUT.setStack(0, ItemStack.EMPTY);
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.INPUT) {
            this.updateResult();
        }
    }
    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            ItemStack itemStack3 = this.INPUT.getStack(0);
            if (slot == 2) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot2.onQuickTransfer(itemStack2, itemStack);
            } else if (slot == 0 || slot == 1 ?
                    !this.insertItem(itemStack2, 2, 38, false) :
                    (itemStack3.isEmpty() ?
                            !this.insertItem(itemStack2, 0, 1, false) :
                            (slot >= 3 && slot < 30 ?
                                    !this.insertItem(itemStack2, 30, 38, false) :
                                    slot >= 30 && slot < 38 && !this.insertItem(itemStack2, 2, 29, false)))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.INPUT.canPlayerUse(player);
    }
    private void updateResult() {
        ItemStack input = this.INPUT.getStack(0);
        Item inputItem = input.getItem();
        Item outputItem = Items.AIR;

        boolean wood = is("wood", inputItem);
        boolean stone = is("stone", inputItem);
        boolean iron = is("iron", inputItem);
        boolean gold = is("gold", inputItem);
        boolean diamond = is("diamond", inputItem);
        boolean netherite = is("netherite", inputItem);
        boolean leather = is("leather", inputItem);

        int count=0;

        if (is("axe", inputItem) || is("pickaxe", inputItem)) count = 3;
        else if (is("hoe", inputItem) || is("sword", inputItem)) count = 2;
        else if (is("shovel", inputItem)) count = 1;
        else if (is("helmet", inputItem)) count = 5;
        else if (is("chestplate", inputItem)) count = 8;
        else if (is("legging", inputItem)) count = 7;
        else if (is("boot", inputItem)) count = 4;
        else if (inputItem.equals(Items.ELYTRA)) {
            count = 2;
            outputItem = Items.PHANTOM_MEMBRANE;
        }

        if (wood) {
            outputItem = Items.STICK;
            count *= 2;
        } else if (stone) outputItem = Items.COBBLESTONE;
        else if (iron) outputItem = Items.IRON_INGOT;
        else if (gold) outputItem = Items.GOLD_INGOT;
        else if (diamond) outputItem = Items.DIAMOND;
        else if (netherite) outputItem = Items.NETHERITE_INGOT;
        else if (leather) outputItem = Items.LEATHER;

        if (outputItem.equals(Items.AIR)) {
            this.RESULT.setStack(0, ItemStack.EMPTY);
        } else {
            this.RESULT.setStack(0, new ItemStack(outputItem, count));
        }
    }
    private boolean is (String type, Item item) {
        return item.getName().contains(Text.literal(type));
    }
}
