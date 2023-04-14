package net.gordyjack.easyrecycling.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.world.GameRules;

import java.util.ArrayList;
import java.util.List;

public class RecyclingScreenHandler extends ScreenHandler {
    private final Inventory input = new SimpleInventory(1);
    private final Inventory result = new CraftingResultInventory();
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private final List<Item> legalInputs = new ArrayList<>();

    public RecyclingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public RecyclingScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(ScreenHandlerType.CRAFTING, syncId);
        this.context = context;
        this.player = playerInventory.player;

        legalInputs.add(IteS

        this.addSlot(new Slot(input, 0, 49, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return
            }
        });
        this.addSlot(new Slot(result, 2, 129, 34));
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }
    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}
