package net.gordyjack.easyrecycling.screen;

import net.gordyjack.easyrecycling.EasyRecycling;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclingScreenHandler extends ScreenHandler {
    //Fields
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
    private final List<Item> LEGALITEMS = new ArrayList<>();

    private final int INPUT_SLOT = 0;
    private final int OUTPUT_SLOT = 1;
    private final int PLAYER_INVENTORY_START = OUTPUT_SLOT + 1; //2
    private final int HOTBAR_START = PLAYER_INVENTORY_START + 27; //29
    private final int PLAYER_INVENTORY_END = HOTBAR_START - 1; //28
    private final int HOTBAR_END = HOTBAR_START + 8; //38

    //Constructors
    public RecyclingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }
    public RecyclingScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(ModScreenHandlerType.RECYCLING_TABLE, syncId);
        this.CONTEXT = context;
        this.PLAYER = playerInventory.player;

        LEGALITEMS.add(Items.WOODEN_AXE);
        LEGALITEMS.add(Items.STONE_AXE);
        LEGALITEMS.add(Items.IRON_AXE);
        LEGALITEMS.add(Items.GOLDEN_AXE);
        LEGALITEMS.add(Items.DIAMOND_AXE);
        LEGALITEMS.add(Items.NETHERITE_AXE);

        LEGALITEMS.add(Items.WOODEN_HOE);
        LEGALITEMS.add(Items.STONE_HOE);
        LEGALITEMS.add(Items.IRON_HOE);
        LEGALITEMS.add(Items.GOLDEN_HOE);
        LEGALITEMS.add(Items.DIAMOND_HOE);
        LEGALITEMS.add(Items.NETHERITE_HOE);

        LEGALITEMS.add(Items.WOODEN_PICKAXE);
        LEGALITEMS.add(Items.STONE_PICKAXE);
        LEGALITEMS.add(Items.IRON_PICKAXE);
        LEGALITEMS.add(Items.GOLDEN_PICKAXE);
        LEGALITEMS.add(Items.DIAMOND_PICKAXE);
        LEGALITEMS.add(Items.NETHERITE_PICKAXE);

        LEGALITEMS.add(Items.WOODEN_SHOVEL);
        LEGALITEMS.add(Items.STONE_SHOVEL);
        LEGALITEMS.add(Items.IRON_SHOVEL);
        LEGALITEMS.add(Items.GOLDEN_SHOVEL);
        LEGALITEMS.add(Items.DIAMOND_SHOVEL);
        LEGALITEMS.add(Items.NETHERITE_SHOVEL);

        LEGALITEMS.add(Items.WOODEN_SWORD);
        LEGALITEMS.add(Items.STONE_SWORD);
        LEGALITEMS.add(Items.IRON_SWORD);
        LEGALITEMS.add(Items.GOLDEN_SWORD);
        LEGALITEMS.add(Items.DIAMOND_SWORD);
        LEGALITEMS.add(Items.NETHERITE_SWORD);

        LEGALITEMS.add(Items.LEATHER_HELMET);
        LEGALITEMS.add(Items.CHAINMAIL_HELMET);
        LEGALITEMS.add(Items.IRON_HELMET);
        LEGALITEMS.add(Items.GOLDEN_HELMET);
        LEGALITEMS.add(Items.DIAMOND_HELMET);
        LEGALITEMS.add(Items.NETHERITE_HELMET);
        LEGALITEMS.add(Items.TURTLE_HELMET);

        LEGALITEMS.add(Items.LEATHER_CHESTPLATE);
        LEGALITEMS.add(Items.CHAINMAIL_CHESTPLATE);
        LEGALITEMS.add(Items.IRON_CHESTPLATE);
        LEGALITEMS.add(Items.GOLDEN_CHESTPLATE);
        LEGALITEMS.add(Items.DIAMOND_CHESTPLATE);
        LEGALITEMS.add(Items.NETHERITE_CHESTPLATE);
        LEGALITEMS.add(Items.ELYTRA);

        LEGALITEMS.add(Items.LEATHER_LEGGINGS);
        LEGALITEMS.add(Items.CHAINMAIL_LEGGINGS);
        LEGALITEMS.add(Items.IRON_LEGGINGS);
        LEGALITEMS.add(Items.GOLDEN_LEGGINGS);
        LEGALITEMS.add(Items.DIAMOND_LEGGINGS);
        LEGALITEMS.add(Items.NETHERITE_LEGGINGS);

        LEGALITEMS.add(Items.LEATHER_BOOTS);
        LEGALITEMS.add(Items.CHAINMAIL_BOOTS);
        LEGALITEMS.add(Items.IRON_BOOTS);
        LEGALITEMS.add(Items.GOLDEN_BOOTS);
        LEGALITEMS.add(Items.DIAMOND_BOOTS);
        LEGALITEMS.add(Items.NETHERITE_BOOTS);

        //TODO Mod Compatibility
        if (false) {

        }

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
                Random random = new Random();
                context.run((world, pos) -> {
                    if (world instanceof ServerWorld) {
                        ExperienceOrbEntity.spawn((ServerWorld)world, Vec3d.ofCenter(pos), random.nextInt(0, 5));
                    }
                });
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

    //Overrides
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.INPUT.canPlayerUse(player);
    }
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.CONTEXT.run((world, pos) -> this.dropInventory(player, this.INPUT));
    }
    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.INPUT) {
            this.updateResult();
        }
    }
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
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
    }

    private void updateResult() {
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
    }
}
