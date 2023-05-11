package net.gordyjack.easyrecycling.block;

import com.google.common.collect.Maps;
import net.gordyjack.easyrecycling.EasyRecycling;
import net.gordyjack.easyrecycling.data.tags.ModItemTagProvider;
import net.gordyjack.easyrecycling.recipe.RecyclingTableRecipe;
import net.gordyjack.easyrecycling.screen.RecyclingScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class RecyclingTableEntity extends BlockEntity implements NamedScreenHandlerFactory, SidedInventory {
    public static final int DELEGATE_SIZE = 4;
    public static final int INVENTORY_SIZE = 3;
    private static final int INPUT_SLOT_INDEX = 0;
    private static final int MATERIAL_SLOT_INDEX = 1;
    private static final int OUTPUT_SLOT_1_INDEX = 2;
    //private static final int OUTPUT_SLOT_2_INDEX = 2;

    private final DefaultedList<ItemStack> INVENTORY = DefaultedList.ofSize(INVENTORY_SIZE, ItemStack.EMPTY);
    private final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 5;
    private int grindTime = 0;
    private int maxGrindTime = 0;


    private final RecipeManager.MatchGetter<Inventory, ? extends RecyclingTableRecipe> matchGetter;

    public RecyclingTableEntity(BlockPos pos, BlockState state) {
        super(EasyRecycling.RECYCLING_TABLE_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> RecyclingTableEntity.this.progress;
                    case 1 -> RecyclingTableEntity.this.maxProgress;
                    case 2 -> RecyclingTableEntity.this.grindTime;
                    case 3 -> RecyclingTableEntity.this.maxGrindTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> RecyclingTableEntity.this.progress = value;
                    case 1 -> RecyclingTableEntity.this.maxProgress = value;
                    case 2 -> RecyclingTableEntity.this.grindTime = value;
                    case 3 -> RecyclingTableEntity.this.maxGrindTime = value;
                }
            }

            @Override
            public int size() {
                return DELEGATE_SIZE;
            }
        };
        this.matchGetter = RecipeManager.createCachedMatchGetter(RecyclingTableRecipe.Type.INSTANCE);
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

        if (entity.isCrafting()) {
            entity.progress++;
            entity.grindTime--;
            markDirty(world, blockPos, state);
            if (entity.progress >= entity.maxProgress) {
                craftItem(entity, 1);
            }
        } else if (entity.hasRecipe() && !entity.hasGrindingMaterial()) {
            entity.decreaseProgress();
            markDirty(world, blockPos, state);
        } else {
            entity.resetProgress();
            markDirty(world, blockPos, state);
        }
    }
    private boolean isCrafting() {
        return this.hasRecipe() && this.hasGrindingMaterial();
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
    private boolean hasGrindingMaterial() {
        return true;
    }
    private static Map<Item, Integer> createGrindTimeMap() {
        LinkedHashMap<Item, Integer> map = Maps.newLinkedHashMap();
        addGrindingMaterial(map, ModItemTagProvider.SMALL_MATERIAL, 2000);
        addGrindingMaterial(map, ModItemTagProvider.MEDIUM_MATERIAL, 10000);
        addGrindingMaterial(map, ModItemTagProvider.LARGE_MATERIAL, 20000);
        return map;
    }
    private static void addGrindingMaterial(Map<Item, Integer> grindTimes, TagKey<Item> tag, int baseGrindingTime) {
        for (RegistryEntry<Item> registryEntry : Registries.ITEM.iterateEntries(tag)) {
            int grindingTime = baseGrindingTime;
            grindingTime = modifyGrindTime(registryEntry.value(), baseGrindingTime);
            grindTimes.put(registryEntry.value(), grindingTime);
        }
    }
    private static void addGrindingMaterial(Map<Item, Integer> grindTimes, ItemConvertible item, int baseGrindingTime) {
        grindTimes.put(item.asItem(), baseGrindingTime);
    }
    private static boolean canUseAsGrindingMaterial(ItemStack itemStack) {
        return createGrindTimeMap().containsKey(itemStack.getItem());
    }
    private static int getGrindTime(ItemStack input) {
        if (input.isEmpty()) {
            return 0;
        }
        Item item = input.getItem();
        return createGrindTimeMap().getOrDefault(item, 0);
    }
    private static int modifyGrindTime(Item item, int baseGrindingTime) {
        int grindingTime = baseGrindingTime;
        if (itemInTag(item, ModItemTagProvider.HARDNESS_1_MATERIAL)) {
            grindingTime *= 2;
        } else if (itemInTag(item, ModItemTagProvider.HARDNESS_2_MATERIAL)) {
            grindingTime *= 1;
        } else if (itemInTag(item, ModItemTagProvider.HARDNESS_3_MATERIAL)) {
            grindingTime *= .9;
        } else if (itemInTag(item, ModItemTagProvider.HARDNESS_4_MATERIAL)) {
            grindingTime *= .75;
        }
        return grindingTime;
    }
    private static boolean itemInTag(Item item, TagKey tagId) {
        if (tagId == null) {
            return false;
        }
        return item.getRegistryEntry().isIn(tagId);
    }

    private boolean hasRecipe() {
        SimpleInventory simpleInventory = this.getCloneInventory();

        //EasyRecycling.logError(simpleInventory.toString());

        Optional<? extends RecyclingTableRecipe> match = this.matchGetter.getFirstMatch(this, world);

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
    private final String GRIND_TIME_KEY = getNbtKey("grindTime");
    private String getNbtKey(String name) {
        return "recycling_table." + name;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, INVENTORY);
        nbt.putInt(PROGRESS_KEY, progress);
        nbt.putInt(GRIND_TIME_KEY, grindTime);
        super.readNbt(nbt);
    }
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, INVENTORY);
        this.progress = nbt.getInt(PROGRESS_KEY);
        this.grindTime = nbt.getInt(GRIND_TIME_KEY);
    }

    /*
    public void test(World world, BlockPos pos, BlockState state, RecyclingTableEntity blockEntity) {
        boolean isBurningBeforeTick = blockEntity.isBurning();
        boolean shouldUpdateBlockState = false;
        if (isBurningBeforeTick) {
            --blockEntity.burnTime;
        }

        ItemStack fuelStack = blockEntity.inventory.get(1);
        boolean hasInputItem = !blockEntity.inventory.get(0).isEmpty();
        boolean hasFuel = !fuelStack.isEmpty();

        if (isBurningBeforeTick || (hasFuel && hasInputItem)) {
            Recipe<?> recipe = hasInputItem ? (Recipe<?>)blockEntity.matchGetter.getFirstMatch(blockEntity, world).orElse(null) : null;
            int maxStackSize = blockEntity.getMaxCountPerStack();

            if (!isBurningBeforeTick && AbstractFurnaceBlockEntity.canAcceptRecipeOutput(world.getRegistryManager(), recipe, blockEntity.inventory, maxStackSize)) {
                blockEntity.fuelTime = blockEntity.burnTime = blockEntity.getFuelTime(fuelStack);

                if (isBurningBeforeTick) {
                    shouldUpdateBlockState = true;

                    if (hasFuel) {
                        Item fuelItem = fuelStack.getItem();
                        fuelStack.decrement(1);

                        if (fuelStack.isEmpty()) {
                            Item remainderItem = fuelItem.getRecipeRemainder();
                            blockEntity.inventory.set(1, remainderItem == null ? ItemStack.EMPTY : new ItemStack(remainderItem));
                        }
                    }
                }
            }

            if (isBurningBeforeTick && AbstractFurnaceBlockEntity.canAcceptRecipeOutput(world.getRegistryManager(), recipe, blockEntity.inventory, maxStackSize)) {
                ++blockEntity.cookTime;

                if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
                    blockEntity.cookTime = 0;
                    blockEntity.cookTimeTotal = AbstractFurnaceBlockEntity.getCookTime(world, blockEntity);

                    if (AbstractFurnaceBlockEntity.craftRecipe(world.getRegistryManager(), recipe, blockEntity.inventory, maxStackSize)) {
                        blockEntity.setLastRecipe(recipe);
                    }

                    shouldUpdateBlockState = true;
                }
            } else {
                blockEntity.cookTime = 0;
            }
        } else if (!isBurningBeforeTick && blockEntity.cookTime > 0) {
            blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
        }

        boolean isBurningAfterTick = blockEntity.isBurning();

        if (isBurningBeforeTick != isBurningAfterTick) {
            shouldUpdateBlockState = true;
            state = (BlockState)state.with(AbstractFurnaceBlock.LIT, isBurningAfterTick);
            world.setBlockState(pos, state, Block.NOTIFY_ALL);
        }

        if (shouldUpdateBlockState) {
            AbstractFurnaceBlockEntity.markDirty(world, pos, state);
        }
    }
    */
}
