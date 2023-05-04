package net.gordyjack.easyrecycling.block;

import net.gordyjack.easyrecycling.EasyRecycling;
import net.gordyjack.easyrecycling.recipe.RecyclingTableRecipe;
import net.gordyjack.easyrecycling.screen.RecyclingScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.input.Input;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RecyclingTableEntity extends BlockEntity implements NamedScreenHandlerFactory, SidedInventory {
    public static final int DELEGATE_SIZE = 4;
    public static final int INVENTORY_SIZE = 2;
    private static final int INPUT_SLOT_INDEX = 0;
    private static final int OUTPUT_SLOT_1_INDEX = 1;
    //private static final int OUTPUT_SLOT_2_INDEX = 2;

    private final DefaultedList<ItemStack> INVENTORY = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private final PropertyDelegate propertyDelegate;

    private int progress = 0;
    private int maxProgress = 5;
    private int fuelTime = 0;
    private int maxFuelTime = 0;

    public RecyclingTableEntity(BlockPos pos, BlockState state) {
        super(EasyRecycling.RECYCLING_TABLE_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> RecyclingTableEntity.this.progress;
                    case 1 -> RecyclingTableEntity.this.maxProgress;
                    case 2 -> RecyclingTableEntity.this.fuelTime;
                    case 3 -> RecyclingTableEntity.this.maxFuelTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RecyclingTableEntity.this.progress = value;
                    case 1 -> RecyclingTableEntity.this.maxProgress = value;
                    case 2 -> RecyclingTableEntity.this.fuelTime = value;
                    case 3 -> RecyclingTableEntity.this.maxFuelTime = value;
                }
            }

            @Override
            public int size() {
                return DELEGATE_SIZE;
            }
        };
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }
    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }
    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }
    @Override
    public void clear() {
        getItems().clear();
    }
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new RecyclingScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }
    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }

        return result;
    }
    @Override
    public Text getDisplayName() {
        return Text.translatable(RecyclingTableBlock.TITLE_KEY);
    }
    public DefaultedList<ItemStack> getItems() {
        return this.INVENTORY;
    }
    @Override
    public ItemStack getStack(int slot) {
        return getItems().get(slot);
    }
    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }
    @Override
    public ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }

        return result;
    }
    @Override
    public void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }
    @Override
    public int size() {
        return INVENTORY_SIZE;
    }

    //Logical Methods
    public static void tick(World world, BlockPos blockPos, BlockState state, RecyclingTableEntity entity) {
        if (world.isClient()) {
            return;
        }

        if (entity.hasRecipe() && entity.hasFuel()) {
            entity.progress++;
            markDirty(world, blockPos, state);
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity, 1);
            }
        } else if (entity.hasRecipe() && !entity.hasFuel()) {
            entity.decreaseProgress();
            markDirty(world, blockPos, state);
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }
    private static void craftItem(RecyclingTableEntity entity, int outputSlot) {
        SimpleInventory simpleInventory = entity.getCloneInventory();

        Optional<RecyclingTableRecipe> recipe = entity.getWorld().getRecipeManager()
                .getFirstMatch(RecyclingTableRecipe.Type.INSTANCE, simpleInventory, entity.getWorld());

        int outputIndex = switch (outputSlot) {
            case 1 -> OUTPUT_SLOT_1_INDEX;
            case 2 -> 2;
            default -> -1;
        };

        if (entity.hasRecipe()) {
            ItemStack input = entity.getStack(INPUT_SLOT_INDEX).copy();
            ItemStack output = recipe.get().getOutput();
            entity.removeStack(INPUT_SLOT_INDEX, 1);
            entity.setStack(OUTPUT_SLOT_1_INDEX, new ItemStack(output.getItem(),
                    entity.getStack(outputIndex).getCount()
                            + modifyOutputForDamage(input, output).getCount()));
            entity.resetProgress();
        }
    }
    /*TODO: Add grinding material compatibility with custom tags to denote grinding materials.
       Harder materials = faster grinding & faster decay.
       */
    private boolean hasFuel() {
        return true;
    }
    private boolean hasRecipe() {
        SimpleInventory simpleInventory = this.getCloneInventory();

        //EasyRecycling.logError(simpleInventory.toString());

        Optional<RecyclingTableRecipe> match = this.getWorld().getRecipeManager()
                .getFirstMatch(RecyclingTableRecipe.Type.INSTANCE, simpleInventory, this.getWorld());

        //EasyRecycling.logError(match + " " + match.isPresent());

        if (match.isPresent()) {
            ItemStack inputStack = simpleInventory.getStack(INPUT_SLOT_INDEX);
            ItemStack outputStack = modifyOutputForDamage(inputStack, match.get().getOutput());
            return canInsertIntoSlot(simpleInventory, outputStack, OUTPUT_SLOT_1_INDEX);
        }
        return false;

    }
    private static boolean canInsertIntoSlot(SimpleInventory inventory, ItemStack output, int slotIndex) {
        ItemStack outputStack = inventory.getStack(slotIndex);

        return outputStack.getMaxCount() >= outputStack.getCount() + output.getCount() &&
                (outputStack.getItem() == output.getItem() || outputStack.isEmpty());
    }
    private SimpleInventory getCloneInventory() {
        SimpleInventory simpleInventory = new SimpleInventory(this.size());
        for (int i = 0; i < this.size(); i++) {
            simpleInventory.setStack(i, this.getStack(i));
        }
        return simpleInventory;
    }
    private static ItemStack modifyOutputForDamage(ItemStack input, ItemStack output) {
        int outputCount = (int) (output.getCount() * (double) (input.getMaxDamage() - input.getDamage()) / input.getMaxDamage());
        return(outputCount > 0 ? new ItemStack(output.getItem(), outputCount) : ItemStack.EMPTY);
    }

    private int decreaseProgress() {
        return this.progress = Math.round(Math.max(this.progress - (((float) maxProgress)/50), 0));
    }
    private int resetProgress() {
        return this.progress = 0;
    }

    //NBT
    private final String PROGRESS_KEY = getNbtKey("progress");
    private final String FUEL_TIME_KEY = getNbtKey("fuelTime");
    private String getNbtKey(String name) {
        return "recycling_table." + name;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, INVENTORY);
        nbt.putInt(PROGRESS_KEY, progress);
        nbt.putInt(FUEL_TIME_KEY, fuelTime);
        super.readNbt(nbt);
    }
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, INVENTORY);
        this.progress = nbt.getInt(PROGRESS_KEY);
        this.fuelTime = nbt.getInt(FUEL_TIME_KEY);
    }
}
