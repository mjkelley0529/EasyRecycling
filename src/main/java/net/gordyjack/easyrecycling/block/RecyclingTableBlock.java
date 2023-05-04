package net.gordyjack.easyrecycling.block;

import net.gordyjack.easyrecycling.EasyRecycling;
import net.gordyjack.easyrecycling.screen.RecyclingScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class RecyclingTableBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final String TITLE_KEY = EasyRecycling.getKey("container", "recycling_table");
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final EnumProperty<WallMountLocation> FACE = Properties.WALL_MOUNT_LOCATION;
    private static final VoxelShape X_FLOOR_SHAPE, Z_FLOOR_SHAPE,
            NORTH_WALL_SHAPE, SOUTH_WALL_SHAPE,
            EAST_WALL_SHAPE, WEST_WALL_SHAPE,
            X_CEILING_SHAPE, Z_CEILING_SHAPE;
    static {
        Z_FLOOR_SHAPE = Stream.of(
                Block.createCuboidShape(5, 3, 2, 11, 6, 14),
                Block.createCuboidShape(0, 0, 0, 16, 4, 16),
                Block.createCuboidShape(6, 7, 4, 10, 15, 12),
                Block.createCuboidShape(10, 4, 7, 12, 9, 9),
                Block.createCuboidShape(10, 9, 6, 12, 13, 10),
                Block.createCuboidShape(4, 4, 7, 6, 9, 9),
                Block.createCuboidShape(4, 9, 6, 6, 13, 10)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        X_FLOOR_SHAPE = Stream.of(
                Block.createCuboidShape(2, 3, 5, 14, 6, 11),
                Block.createCuboidShape(0, 0, 0, 16, 4, 16),
                Block.createCuboidShape(4, 7, 6, 12, 15, 10),
                Block.createCuboidShape(7, 4, 10, 9, 9, 12),
                Block.createCuboidShape(6, 9, 10, 10, 13, 12),
                Block.createCuboidShape(7, 4, 4, 9, 9, 6),
                Block.createCuboidShape(6, 9, 4, 10, 13, 6)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        SOUTH_WALL_SHAPE = Stream.of(
                Block.createCuboidShape(5, 2, 3, 11, 14, 6),
                Block.createCuboidShape(0, 0, 0, 16, 16, 4),
                Block.createCuboidShape(6, 4, 7, 10, 12, 15),
                Block.createCuboidShape(10, 7, 4, 12, 9, 9),
                Block.createCuboidShape(10, 6, 9, 12, 10, 13),
                Block.createCuboidShape(4, 7, 4, 6, 9, 9),
                Block.createCuboidShape(4, 6, 9, 6, 10, 13)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        WEST_WALL_SHAPE = Stream.of(
                Block.createCuboidShape(10, 2, 5, 13, 14, 11),
                Block.createCuboidShape(12, 0, 0, 16, 16, 16),
                Block.createCuboidShape(1, 4, 6, 9, 12, 10),
                Block.createCuboidShape(7, 7, 10, 12, 9, 12),
                Block.createCuboidShape(3, 6, 10, 7, 10, 12),
                Block.createCuboidShape(7, 7, 4, 12, 9, 6),
                Block.createCuboidShape(3, 6, 4, 7, 10, 6)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        NORTH_WALL_SHAPE = Stream.of(
                Block.createCuboidShape(5, 2, 10, 11, 14, 13),
                Block.createCuboidShape(0, 0, 12, 16, 16, 16),
                Block.createCuboidShape(6, 4, 1, 10, 12, 9),
                Block.createCuboidShape(4, 7, 7, 6, 9, 12),
                Block.createCuboidShape(4, 6, 3, 6, 10, 7),
                Block.createCuboidShape(10, 7, 7, 12, 9, 12),
                Block.createCuboidShape(10, 6, 3, 12, 10, 7)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        EAST_WALL_SHAPE = Stream.of(
                Block.createCuboidShape(3, 2, 5, 6, 14, 11),
                Block.createCuboidShape(0, 0, 0, 4, 16, 16),
                Block.createCuboidShape(7, 4, 6, 15, 12, 10),
                Block.createCuboidShape(4, 7, 4, 9, 9, 6),
                Block.createCuboidShape(9, 6, 4, 13, 10, 6),
                Block.createCuboidShape(4, 7, 10, 9, 9, 12),
                Block.createCuboidShape(9, 6, 10, 13, 10, 12)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        X_CEILING_SHAPE = Stream.of(
                Block.createCuboidShape(2, 10, 5, 14, 13, 11),
                Block.createCuboidShape(0, 12, 0, 16, 16, 16),
                Block.createCuboidShape(4, 1, 6, 12, 9, 10),
                Block.createCuboidShape(7, 7, 10, 9, 12, 12),
                Block.createCuboidShape(6, 3, 10, 10, 7, 12),
                Block.createCuboidShape(7, 7, 4, 9, 12, 6),
                Block.createCuboidShape(6, 3, 4, 10, 7, 6)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
        Z_CEILING_SHAPE = Stream.of(
                Block.createCuboidShape(5, 10, 2, 11, 13, 14),
                Block.createCuboidShape(0, 12, 0, 16, 16, 16),
                Block.createCuboidShape(6, 1, 4, 10, 9, 12),
                Block.createCuboidShape(4, 7, 7, 6, 12, 9),
                Block.createCuboidShape(4, 3, 6, 6, 7, 10),
                Block.createCuboidShape(10, 7, 7, 12, 12, 9),
                Block.createCuboidShape(10, 3, 6, 12, 7, 10)
        ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();
    }
    /*TODO: Placement fixed. Need to figure out why the block breaks when the block its placed on is removed.
       Grindstone does not have this behaviour*/
    public RecyclingTableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(FACE, WallMountLocation.WALL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return this.canPlaceAt(world, pos, getDirection(state).getOpposite());
    }
    public static boolean canPlaceAt(WorldView world, BlockPos pos, Direction direction) {
        BlockPos blockPos = pos.offset(direction);
        return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction.getOpposite());
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RecyclingTableEntity(pos, state);
    }
    protected static Direction getDirection(BlockState state) {
        switch (state.get(FACE)) {
            case CEILING: {
                return Direction.DOWN;
            }
            case FLOOR: {
                return Direction.UP;
            }
        }
        return state.get(FACING);
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        switch (state.get(FACE)) {
            case FLOOR -> {
                if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                    return Z_FLOOR_SHAPE;
                }
                return X_FLOOR_SHAPE;
            }
            case WALL -> {
                if (direction == Direction.NORTH) {
                    return NORTH_WALL_SHAPE;
                }
                if (direction == Direction.SOUTH) {
                    return SOUTH_WALL_SHAPE;
                }
                if (direction == Direction.EAST) {
                    return EAST_WALL_SHAPE;
                }
                return WEST_WALL_SHAPE;
            }
            case CEILING -> {
                if (direction == Direction.NORTH || direction == Direction.SOUTH) {
                    return Z_CEILING_SHAPE;
                }
                return X_CEILING_SHAPE;
            }
        }
        return X_FLOOR_SHAPE;
    }
    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        for (Direction direction : ctx.getPlacementDirections()) {
            BlockState blockState = direction.getAxis() == Direction.Axis.Y ?
                    this.getDefaultState().with(FACE, direction == Direction.UP ?
                            WallMountLocation.CEILING :
                            WallMountLocation.FLOOR).with(FACING, ctx.getHorizontalPlayerFacing()) :
                    this.getDefaultState().with(FACE, WallMountLocation.WALL).with(FACING, direction.getOpposite());
            if (!blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) continue;
            return blockState;
        }
        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (getDirection(state).getOpposite() == direction && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, EasyRecycling.RECYCLING_TABLE_ENTITY, RecyclingTableEntity::tick);
    }
    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof RecyclingTableEntity) {
                ItemScatterer.spawn(world, pos, (RecyclingTableEntity)blockEntity);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
        return ActionResult.CONSUME;
    }
    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
}
