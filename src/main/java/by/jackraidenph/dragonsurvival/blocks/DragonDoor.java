package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DragonDoor extends Block {
    enum Part implements IStringSerializable {
        BOTTOM,
        MIDDLE,
        TOP;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);

    public DragonDoor(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(HINGE, DoorHingeSide.LEFT).setValue(POWERED, false).setValue(PART, Part.BOTTOM));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.getValue(FACING);
        boolean flag = !state.getValue(OPEN);
        boolean flag1 = state.getValue(HINGE) == DoorHingeSide.RIGHT;
        switch (direction) {
            case EAST:
            default:
                return flag ? EAST_AABB : (flag1 ? NORTH_AABB : SOUTH_AABB);
            case SOUTH:
                return flag ? SOUTH_AABB : (flag1 ? EAST_AABB : WEST_AABB);
            case WEST:
                return flag ? WEST_AABB : (flag1 ? SOUTH_AABB : NORTH_AABB);
            case NORTH:
                return flag ? NORTH_AABB : (flag1 ? WEST_AABB : EAST_AABB);
        }
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        Part part = stateIn.getValue(PART);
        //TODO
        if (facing.getAxis() == Direction.Axis.Y && part == Part.BOTTOM == (facing == Direction.UP)) {
            return facingState.getBlock() == this && facingState.getValue(PART) != part ? stateIn.setValue(FACING, facingState.getValue(FACING)).setValue(OPEN, facingState.getValue(OPEN)).setValue(HINGE, facingState.getValue(HINGE)).setValue(POWERED, facingState.getValue(POWERED)) : Blocks.AIR.defaultBlockState();
        } else {
            return part == Part.BOTTOM && facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    public void playerDestroy(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.playerDestroy(worldIn, player, pos, Blocks.AIR.defaultBlockState(), te, stack);
    }

    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        Part part = state.getValue(PART);
        BlockPos blockpos = part == Part.BOTTOM ? pos.above() : pos.below();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (blockstate.getBlock() == this && blockstate.getValue(PART) != part) {
            worldIn.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
            worldIn.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
            ItemStack itemstack = player.getMainHandItem();
            if (!worldIn.isClientSide && !player.isCreative() && player.hasCorrectToolForDrops(blockstate)) {
                Block.dropResources(state, worldIn, pos, null, player, itemstack);
            }
        }
        if ((part == Part.TOP || part == Part.MIDDLE) && !worldIn.isClientSide && !player.isCreative() && player.hasCorrectToolForDrops(state)) {
            Block.dropResources(BlockInit.dragonDoor.defaultBlockState(), worldIn, pos, null, player, player.getMainHandItem());
        }

        super.playerWillDestroy(worldIn, pos, state, player);
    }

    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        switch (type) {
            case LAND:
            case AIR:
                return state.getValue(OPEN);
            default:
                return false;
        }
    }

    private int getCloseSound() {
        return this.material == Material.METAL ? 1011 : 1012;
    }

    private int getOpenSound() {
        return this.material == Material.METAL ? 1005 : 1006;
    }

    private DoorHingeSide getHingeSide(BlockItemUseContext blockItemUseContext) {
        //TODO
        IBlockReader iblockreader = blockItemUseContext.getLevel();
        BlockPos blockpos = blockItemUseContext.getClickedPos();
        Direction direction = blockItemUseContext.getHorizontalDirection();
        BlockPos blockpos1 = blockpos.above();
        Direction direction1 = direction.getCounterClockWise();
        BlockPos blockpos2 = blockpos.relative(direction1);
        BlockState blockstate = iblockreader.getBlockState(blockpos2);
        BlockPos blockpos3 = blockpos1.relative(direction1);
        BlockState blockstate1 = iblockreader.getBlockState(blockpos3);
        Direction direction2 = direction.getClockWise();
        BlockPos blockpos4 = blockpos.relative(direction2);
        BlockState blockstate2 = iblockreader.getBlockState(blockpos4);
        BlockPos blockpos5 = blockpos1.relative(direction2);
        BlockState blockstate3 = iblockreader.getBlockState(blockpos5);
        int i = (blockstate.isCollisionShapeFullBlock(iblockreader, blockpos2) ? -1 : 0) + (blockstate1.isCollisionShapeFullBlock(iblockreader, blockpos3) ? -1 : 0) + (blockstate2.isCollisionShapeFullBlock(iblockreader, blockpos4) ? 1 : 0) + (blockstate3.isCollisionShapeFullBlock(iblockreader, blockpos5) ? 1 : 0);
        boolean flag = blockstate.getBlock() == this && blockstate.getValue(PART) == Part.BOTTOM;
        boolean flag1 = blockstate2.getBlock() == this && blockstate2.getValue(PART) == Part.BOTTOM;
        if ((!flag || flag1) && i <= 0) {
            if ((!flag1 || flag) && i >= 0) {
                int j = direction.getStepX();
                int k = direction.getStepZ();
                Vector3d vec3d = blockItemUseContext.getClickLocation();
                double d0 = vec3d.x - (double) blockpos.getX();
                double d1 = vec3d.z - (double) blockpos.getZ();
                return (j >= 0 || !(d1 < 0.5D)) && (j <= 0 || !(d1 > 0.5D)) && (k >= 0 || !(d0 > 0.5D)) && (k <= 0 || !(d0 < 0.5D)) ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
            } else {
                return DoorHingeSide.LEFT;
            }
        } else {
            return DoorHingeSide.RIGHT;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getClickedPos();
        if (blockpos.getY() < 255 && context.getLevel().getBlockState(blockpos.above()).canBeReplaced(context) && context.getLevel().getBlockState(blockpos.above(2)).canBeReplaced(context)) {
            World world = context.getLevel();
            boolean flag = world.hasNeighborSignal(blockpos) || world.hasNeighborSignal(blockpos.above());
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HINGE, this.getHingeSide(context)).setValue(POWERED, flag).setValue(OPEN, flag).setValue(PART, Part.BOTTOM);
        } else {
            return null;
        }
    }

    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        worldIn.setBlock(pos.above(), state.setValue(PART, Part.MIDDLE), 3);
        worldIn.setBlock(pos.above(2), state.setValue(PART, Part.TOP), 3);
    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        state = state.cycle(OPEN);
        worldIn.setBlock(pos, state, 10);
        worldIn.levelEvent(player, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), pos, 0);
        if (state.getValue(PART) == Part.TOP) {
            worldIn.setBlock(pos.below(2), state.setValue(PART, Part.BOTTOM), 10);
            worldIn.setBlock(pos.below(), state.setValue(PART, Part.MIDDLE), 10);
        }
        return ActionResultType.SUCCESS;
    }

    private void playSound(World worldIn, BlockPos pos, boolean isOpening) {
        worldIn.levelEvent(null, isOpening ? this.getOpenSound() : this.getCloseSound(), pos, 0);
    }

    /**
     * Used by {@link net.minecraft.entity.ai.brain.task.InteractWithDoorTask}
     */
    public void toggleDoor(World worldIn, BlockPos pos, boolean open) {
        BlockState blockstate = worldIn.getBlockState(pos);
        if (blockstate.getBlock() == this && blockstate.getValue(OPEN) != open) {
            worldIn.setBlock(pos, blockstate.setValue(OPEN, open), 10);
            this.playSound(worldIn, pos, open);
        }
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return mirrorIn == Mirror.NONE ? state : state.rotate(mirrorIn.getRotation(state.getValue(FACING))).cycle(HINGE);
    }

    public long getSeed(BlockState state, BlockPos pos) {
        //TODO
        return MathHelper.getSeed(pos.getX(), pos.below(state.getValue(PART) == Part.BOTTOM ? 0 : 1).getY(), pos.getZ());
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING, OPEN, HINGE, POWERED);
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        boolean flag = worldIn.hasNeighborSignal(pos) || worldIn.hasNeighborSignal(pos.relative(state.getValue(PART) == Part.BOTTOM ? Direction.UP : Direction.DOWN));
        if (blockIn != this && flag != state.getValue(POWERED)) {
            if (flag != state.getValue(OPEN)) {
                this.playSound(worldIn, pos, flag);
            }

            worldIn.setBlock(pos, state.setValue(POWERED, flag).setValue(OPEN, flag), 2);
        }
        BlockState blockStateDown = worldIn.getBlockState(pos.below());
        if (state.getValue(PART) == Part.TOP && blockStateDown.getBlock() == this) {
            worldIn.setBlock(pos.below(), blockStateDown.setValue(OPEN, flag), 2);
        }
    }

    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (state.getValue(PART) == Part.BOTTOM) {
            return blockstate.isFaceSturdy(worldIn, blockpos, Direction.UP);
        } else {
            return blockstate.getBlock() == this;
        }
    }
}

