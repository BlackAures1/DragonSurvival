package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import by.jackraidenph.dragonsurvival.handlers.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * This is a 2x2 multi-block.
 */
public class MediumNestBlock extends NestBlock {
    static final BooleanProperty PRIMARY_BLOCK = BooleanProperty.create("primary");

    public MediumNestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PRIMARY_BLOCK);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockPos = context.getPos();
        World world = context.getWorld();
        PlayerEntity playerEntity = context.getPlayer();
        Direction direction = playerEntity.getHorizontalFacing();
        if (world.isAirBlock(blockPos.offset(direction)) && world.isAirBlock(blockPos.offset(direction.rotateYCCW()))
                && world.isAirBlock(blockPos.offset(direction).offset(direction.rotateYCCW())))
            return super.getStateForPlacement(context).with(HORIZONTAL_FACING, direction.getOpposite());
        return null;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (!state.get(PRIMARY_BLOCK))
            return TileEntityTypesInit.nestPlaceHolder.create();
        return super.createTileEntity(state, world);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        DragonStateHandler dragonStateHandler = player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null);
        DragonLevel dragonLevel = dragonStateHandler.getLevel();
        BlockPos rootPos = null;
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof NestEntity)
            rootPos = pos;
        else if (tileEntity instanceof NestPlaceHolder)
            rootPos = ((NestPlaceHolder) tileEntity).rootPos;
        DragonType dragonType = dragonStateHandler.getType();
        NestEntity nest = (NestEntity) worldIn.getTileEntity(rootPos);
        if (dragonStateHandler.isDragon() &&
                dragonLevel == DragonLevel.ADULT && nest.ownerUUID.equals(player.getUniqueID())
                && state.getBlock().getClass() == MediumNestBlock.class) {
            final Direction playerDirection = player.getHorizontalFacing();
            if (worldIn.isAirBlock(rootPos.offset(playerDirection.getOpposite())) &&
                    worldIn.isAirBlock(rootPos.offset(playerDirection).offset(playerDirection.rotateY())) &&
                    worldIn.isAirBlock(rootPos.offset(playerDirection.rotateY())) &&
                    worldIn.isAirBlock(rootPos.offset(playerDirection.getOpposite()).offset(playerDirection.rotateYCCW())) &&
                    worldIn.isAirBlock(rootPos.offset(playerDirection.getOpposite()).offset(playerDirection.rotateY()))) {
                CompoundNBT compoundNBT = nest.write(new CompoundNBT());
                final Direction placementDirection = playerDirection.getOpposite();
                switch (dragonType) {
                    case SEA:
                        worldIn.setBlockState(rootPos, BlockInit.bigSeaNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                        break;
                    case FOREST:
                        worldIn.setBlockState(rootPos, BlockInit.bigForestNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                        break;
                    case CAVE:
                        worldIn.setBlockState(rootPos, BlockInit.bigCaveNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                        break;
                }
                NestEntity nestEntity = getBlockEntity(worldIn, rootPos);
                nestEntity.read(compoundNBT);
                BlockState blockState = worldIn.getBlockState(rootPos);
                blockState.getBlock().onBlockPlacedBy(worldIn, rootPos, blockState, player, player.getHeldItem(handIn));

                return ActionResultType.SUCCESS;
            } else {
                if (worldIn.isRemote)
                    player.sendMessage(new TranslationTextComponent("ds.space.occupied"));
                return ActionResultType.CONSUME;
            }
        }
        if (!state.get(PRIMARY_BLOCK)) {
            NestPlaceHolder placeHolder = (NestPlaceHolder) worldIn.getTileEntity(pos);
            BlockPos root = placeHolder.rootPos;
            if (worldIn.getTileEntity(root) == null) {
                worldIn.destroyBlock(pos, false);
                return ActionResultType.SUCCESS;
            } else
                return super.onBlockActivated(worldIn.getBlockState(root), worldIn, root, player, handIn, hit);
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (placer != null) {
            Direction direction = placer.getHorizontalFacing();
            worldIn.setBlockState(pos.offset(direction), state.with(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder = (NestPlaceHolder) worldIn.getTileEntity(pos.offset(direction));
            placeHolder.rootPos = pos;
            worldIn.setBlockState(pos.offset(direction.rotateYCCW()), state.with(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder2 = (NestPlaceHolder) worldIn.getTileEntity(pos.offset(direction.rotateYCCW()));
            placeHolder2.rootPos = pos;
            worldIn.setBlockState(pos.offset(direction).offset(direction.rotateYCCW()), state.with(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder3 = (NestPlaceHolder) worldIn.getTileEntity(pos.offset(direction).offset(direction.rotateYCCW()));
            placeHolder3.rootPos = pos;
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(PRIMARY_BLOCK) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, worldIn, pos, newState, isMoving);
        if (state.get(PRIMARY_BLOCK)) {
            Direction direction = state.get(HORIZONTAL_FACING);
            worldIn.destroyBlock(pos.offset(direction.getOpposite()), false);
            worldIn.destroyBlock(pos.offset(direction.getOpposite()).offset(direction.rotateY()), false);
            worldIn.destroyBlock(pos.offset(direction.rotateY()), false);
        }
    }
}
