package net.gordyjack.easyrecycling.screen;

import net.gordyjack.easyrecycling.EasyRecycling;
import net.gordyjack.easyrecycling.block.RecyclingTableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class RecyclingScreenHandler extends ScreenHandler {
    //Fields
    /*
    private final Inventory INPUT = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            RecyclingScreenHandler.this.onContentChanged(this);
        }
    };
    private final Inventory RESULT = new CraftingResultInventory();
     */
    private static final List<Item> LEGAL_ITEMS = new ArrayList<>();

    private final Inventory INVENTORY;
    private final PropertyDelegate DELEGATE;

    /*private final int INPUT_SLOT = 0;
    private final int OUTPUT_SLOT = 1;
    private final int PLAYER_INVENTORY_START = OUTPUT_SLOT + 1; //2
    private final int HOTBAR_START = PLAYER_INVENTORY_START + 27; //29
    private final int PLAYER_INVENTORY_END = HOTBAR_START - 1; //28
    private final int HOTBAR_END = HOTBAR_START + 8; //38 */

    //Constructors
    public RecyclingScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(RecyclingTableEntity.INVENTORY_SIZE),
                new ArrayPropertyDelegate(RecyclingTableEntity.DELEGATE_SIZE));
    }
    public RecyclingScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate) {
        super(ModScreenHandlerType.RECYCLING_TABLE, syncId);
        checkSize(inventory, RecyclingTableEntity.INVENTORY_SIZE);
        this.INVENTORY = inventory;
        inventory.onOpen(playerInventory.player);
        this.DELEGATE = delegate;

        generateLegalItems();

        this.addSlot(new Slot(inventory, 0, 49, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return LEGAL_ITEMS.contains(stack.getItem());
            }
        });
        this.addSlot(new Slot(inventory, 1, 129, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(delegate);

        /*
        this.addSlot(new Slot(INPUT, 0, 49, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return LEGALITEMS.contains(stack.getItem());
            }
        });
        this.addSlot(new Slot(RESULT, 0, 129, 34) {
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
         */
    }

    //Overrides
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.INVENTORY.canPlayerUse(player);
    }
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        int inventorySize = this.INVENTORY.size();
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (slotIndex < inventorySize) {
                if (!this.insertItem(originalStack, inventorySize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, inventorySize, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;


        /*
        //EasyRecycling.logError("quickMove");
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasStack()) {
            //EasyRecycling.logError("hasStack");
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();

            boolean INVENTORY_SLOT = slotIndex >= PLAYER_INVENTORY_START && slotIndex <= PLAYER_INVENTORY_END;
            boolean HOTBAR_SLOT = slotIndex >= HOTBAR_START && slotIndex <= HOTBAR_END;

            if (slotIndex == OUTPUT_SLOT) {
                //EasyRecycling.logError("OUTPUT_SLOT");
                if (!this.insertItem(itemStack2, PLAYER_INVENTORY_START, HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (slotIndex == INPUT_SLOT && !this.insertItem(itemStack2, PLAYER_INVENTORY_START, HOTBAR_END, false)) {
                //EasyRecycling.logError("INPUT_SLOT");
                return ItemStack.EMPTY;
            } else if (this.INPUT.getStack(0).isEmpty() && !this.insertItem(itemStack2, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                //EasyRecycling.logError("INPUT EMPTY");
                return ItemStack.EMPTY;
            } else if (INVENTORY_SLOT && !this.insertItem(itemStack2, HOTBAR_START, HOTBAR_END, false)) {
                //EasyRecycling.logError("INVENTORY_SLOT");
                return ItemStack.EMPTY;
            } else if (HOTBAR_SLOT && !this.insertItem(itemStack2, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, false)) {
                //EasyRecycling.logError("HOTBAR_SLOT");
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                //EasyRecycling.logError("itemStack2 Empty");
                slot.setStack(ItemStack.EMPTY);
            } else {
                //EasyRecycling.logError("markDirty");
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                //EasyRecycling.logError("EQUALS");
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
        */
    }
    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }
    public boolean isCrafting() {
        return DELEGATE.get(0) > 0;
    }
    public static List<Item> generateLegalItems() {
        LEGAL_ITEMS.add(Items.WOODEN_AXE);
        LEGAL_ITEMS.add(Items.STONE_AXE);
        LEGAL_ITEMS.add(Items.IRON_AXE);
        LEGAL_ITEMS.add(Items.GOLDEN_AXE);
        LEGAL_ITEMS.add(Items.DIAMOND_AXE);
        LEGAL_ITEMS.add(Items.NETHERITE_AXE);

        LEGAL_ITEMS.add(Items.WOODEN_HOE);
        LEGAL_ITEMS.add(Items.STONE_HOE);
        LEGAL_ITEMS.add(Items.IRON_HOE);
        LEGAL_ITEMS.add(Items.GOLDEN_HOE);
        LEGAL_ITEMS.add(Items.DIAMOND_HOE);
        LEGAL_ITEMS.add(Items.NETHERITE_HOE);

        LEGAL_ITEMS.add(Items.WOODEN_PICKAXE);
        LEGAL_ITEMS.add(Items.STONE_PICKAXE);
        LEGAL_ITEMS.add(Items.IRON_PICKAXE);
        LEGAL_ITEMS.add(Items.GOLDEN_PICKAXE);
        LEGAL_ITEMS.add(Items.DIAMOND_PICKAXE);
        LEGAL_ITEMS.add(Items.NETHERITE_PICKAXE);

        LEGAL_ITEMS.add(Items.WOODEN_SHOVEL);
        LEGAL_ITEMS.add(Items.STONE_SHOVEL);
        LEGAL_ITEMS.add(Items.IRON_SHOVEL);
        LEGAL_ITEMS.add(Items.GOLDEN_SHOVEL);
        LEGAL_ITEMS.add(Items.DIAMOND_SHOVEL);
        LEGAL_ITEMS.add(Items.NETHERITE_SHOVEL);

        LEGAL_ITEMS.add(Items.WOODEN_SWORD);
        LEGAL_ITEMS.add(Items.STONE_SWORD);
        LEGAL_ITEMS.add(Items.IRON_SWORD);
        LEGAL_ITEMS.add(Items.GOLDEN_SWORD);
        LEGAL_ITEMS.add(Items.DIAMOND_SWORD);
        LEGAL_ITEMS.add(Items.NETHERITE_SWORD);

        LEGAL_ITEMS.add(Items.LEATHER_HELMET);
        LEGAL_ITEMS.add(Items.CHAINMAIL_HELMET);
        LEGAL_ITEMS.add(Items.IRON_HELMET);
        LEGAL_ITEMS.add(Items.GOLDEN_HELMET);
        LEGAL_ITEMS.add(Items.DIAMOND_HELMET);
        LEGAL_ITEMS.add(Items.NETHERITE_HELMET);
        LEGAL_ITEMS.add(Items.TURTLE_HELMET);

        LEGAL_ITEMS.add(Items.LEATHER_CHESTPLATE);
        LEGAL_ITEMS.add(Items.CHAINMAIL_CHESTPLATE);
        LEGAL_ITEMS.add(Items.IRON_CHESTPLATE);
        LEGAL_ITEMS.add(Items.GOLDEN_CHESTPLATE);
        LEGAL_ITEMS.add(Items.DIAMOND_CHESTPLATE);
        LEGAL_ITEMS.add(Items.NETHERITE_CHESTPLATE);
        LEGAL_ITEMS.add(Items.ELYTRA);

        LEGAL_ITEMS.add(Items.LEATHER_LEGGINGS);
        LEGAL_ITEMS.add(Items.CHAINMAIL_LEGGINGS);
        LEGAL_ITEMS.add(Items.IRON_LEGGINGS);
        LEGAL_ITEMS.add(Items.GOLDEN_LEGGINGS);
        LEGAL_ITEMS.add(Items.DIAMOND_LEGGINGS);
        LEGAL_ITEMS.add(Items.NETHERITE_LEGGINGS);

        LEGAL_ITEMS.add(Items.LEATHER_BOOTS);
        LEGAL_ITEMS.add(Items.CHAINMAIL_BOOTS);
        LEGAL_ITEMS.add(Items.IRON_BOOTS);
        LEGAL_ITEMS.add(Items.GOLDEN_BOOTS);
        LEGAL_ITEMS.add(Items.DIAMOND_BOOTS);
        LEGAL_ITEMS.add(Items.NETHERITE_BOOTS);

        //TODO Mod Compatibility
        if (false) {

        }

        return LEGAL_ITEMS;
    }
    public int getScaledFuel() {
        int fuelTime = this.DELEGATE.get(2);
        int maxFuelTime = this.DELEGATE.get(3);
        int fuelIconHeight = 26;

        return maxFuelTime != 0 && fuelTime != 0 ? fuelTime*fuelIconHeight/maxFuelTime : 0;
    }
    public int getScaledProgress() {
        int progress = this.DELEGATE.get(0);
        int maxProgress = this.DELEGATE.get(1);
        int progressArrowLength = 26;

        return maxProgress != 0 && progress != 0 ? progress*progressArrowLength/maxProgress : 0;
    }

    /*private void updateResult() {
        ItemStack input = this.INPUT.getStack(0);
        Item inputItem = input.getItem();
        Item outputItem = Items.AIR;

        if (!input.equals(ItemStack.EMPTY)) {

            boolean wood = itemIs("wood", inputItem);
            boolean stone = itemIs("stone", inputItem);
            boolean iron = itemIs("iron", inputItem) || itemIs("chainmail", inputItem);
            boolean gold = itemIs("gold", inputItem);
            boolean diamond = itemIs("diamond", inputItem);
            boolean netherite = itemIs("netherite", inputItem);
            boolean leather = itemIs("leather", inputItem);
            boolean turtle = itemIs("turtle", inputItem);

            boolean axe = itemClassIs("axe", inputItem);
            boolean hoe = itemClassIs("hoe", inputItem);
            boolean pickaxe = itemClassIs("pickaxe", inputItem);
            boolean shovel = itemClassIs("shovel", inputItem);
            boolean sword = itemClassIs("sword", inputItem);

            boolean helmet = itemIs("helmet", inputItem);
            boolean chestplate = itemIs("chestplate", inputItem);
            boolean legging = itemIs("legging", inputItem);
            boolean boot = itemIs("boot", inputItem);

            int itemCount = 0;

            EasyRecycling.logError(inputItem.getClass().getCanonicalName() + " | " + inputItem.getTranslationKey());

            if (inputItem.equals(Items.ELYTRA)) {
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
                itemCount *= 2;
                outputItem = Items.STICK;
            } else if (stone) outputItem = Items.COBBLESTONE;
            else if (leather) outputItem = Items.LEATHER;
            else if (iron) outputItem = Items.IRON_INGOT;
            else if (gold) outputItem = Items.GOLD_INGOT;
            else if (diamond) outputItem = Items.DIAMOND;
            else if (netherite) {
                itemCount = 1;
                outputItem = Items.NETHERITE_INGOT;
            } else if (turtle) outputItem = Items.SCUTE;
            itemCount *= (double) (input.getMaxDamage() - input.getDamage()) / input.getMaxDamage();

            this.RESULT.setStack(0, itemCount > 0 ? new ItemStack(outputItem, itemCount) : ItemStack.EMPTY);
        } else {
            this.RESULT.setStack(0, ItemStack.EMPTY);
        }
        this.sendContentUpdates();
    }
    private boolean itemIs(String type, Item item) {
        return item.getTranslationKey().contains(type);
    }
    private boolean itemClassIs(String type, Item item) {
        return item.getClass().getCanonicalName().toLowerCase().contains(type.toLowerCase());
    }*/
}
