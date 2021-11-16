package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.registration.BlockInit;
import by.jackraidenph.dragonsurvival.registration.TileEntityTypesInit;
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PRIMARY_BLOCK);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockPos = context.getClickedPos();
        World world = context.getLevel();
        PlayerEntity playerEntity = context.getPlayer();
        Direction direction = playerEntity.getDirection();
        if (Functions.isAirOrFluid(blockPos.relative(direction), world) && Functions.isAirOrFluid(blockPos.relative(direction.getCounterClockWise()), world)
                && Functions.isAirOrFluid(blockPos.relative(direction).relative(direction.getCounterClockWise()), world))
            return super.getStateForPlacement(context).setValue(FACING, direction.getOpposite());
        return null;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (!state.getValue(PRIMARY_BLOCK))
            return TileEntityTypesInit.nestPlaceHolder.create();
        return super.createTileEntity(state, world);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        DragonStateHandler dragonStateHandler = player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null);
        DragonLevel dragonLevel = dragonStateHandler.getLevel();
        BlockPos rootPos = null;
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof NestEntity)
            rootPos = pos;
        else if (tileEntity instanceof NestPlaceHolder)
            rootPos = ((NestPlaceHolder) tileEntity).rootPos;
        DragonType dragonType = dragonStateHandler.getType();
        NestEntity nest = (NestEntity) worldIn.getBlockEntity(rootPos);
        if (!state.getValue(PRIMARY_BLOCK) || nest == null || nest.ownerUUID == null) {
        	if (nest == null || nest.ownerUUID == null) {
        		worldIn.destroyBlock(pos, false);
                return ActionResultType.SUCCESS;
        	}
            NestPlaceHolder placeHolder = (NestPlaceHolder) worldIn.getBlockEntity(pos);
            BlockPos root = placeHolder.rootPos;
            if (worldIn.getBlockEntity(root) == null) {
                worldIn.destroyBlock(pos, false);
                return ActionResultType.SUCCESS;
            } else
                return super.use(worldIn.getBlockState(root), worldIn, root, player, handIn, hit);
        }
        if (dragonStateHandler.isDragon() &&
                dragonLevel == DragonLevel.ADULT && nest.ownerUUID.equals(player.getUUID())
                && state.getBlock().getClass() == MediumNestBlock.class) {
            final Direction playerDirection = player.getDirection();
            if (Functions.isAirOrFluid(rootPos.relative(playerDirection.getOpposite()), worldIn) &&
                    Functions.isAirOrFluid(rootPos.relative(playerDirection).relative(playerDirection.getClockWise()), worldIn) &&
                    Functions.isAirOrFluid(rootPos.relative(playerDirection.getClockWise()), worldIn) &&
                    Functions.isAirOrFluid(rootPos.relative(playerDirection.getOpposite()).relative(playerDirection.getCounterClockWise()), worldIn) &&
                    Functions.isAirOrFluid(rootPos.relative(playerDirection.getOpposite()).relative(playerDirection.getClockWise()), worldIn)) {
                CompoundNBT compoundNBT = nest.save(new CompoundNBT());
                final Direction placementDirection = playerDirection.getOpposite();
                switch (dragonType) {
                    case SEA:
                        worldIn.setBlockAndUpdate(rootPos, BlockInit.bigSeaNest.defaultBlockState().setValue(FACING, placementDirection));
                        break;
                    case FOREST:
                        worldIn.setBlockAndUpdate(rootPos, BlockInit.bigForestNest.defaultBlockState().setValue(FACING, placementDirection));
                        break;
                    case CAVE:
                        worldIn.setBlockAndUpdate(rootPos, BlockInit.bigCaveNest.defaultBlockState().setValue(FACING, placementDirection));
                        break;
                }
                NestEntity nestEntity = getBlockEntity(worldIn, rootPos);
                BlockState blockState = worldIn.getBlockState(rootPos);
                nestEntity.load(blockState, compoundNBT);
                blockState.getBlock().setPlacedBy(worldIn, rootPos, blockState, player, player.getItemInHand(handIn));

                return ActionResultType.SUCCESS;
            } else {
                if (worldIn.isClientSide)
                    player.sendMessage(new TranslationTextComponent("ds.space.occupied"), player.getUUID());
                return ActionResultType.CONSUME;
            }
        }
        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (placer != null) {
            Direction direction = placer.getDirection();
            worldIn.setBlockAndUpdate(pos.relative(direction), state.setValue(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder = (NestPlaceHolder) worldIn.getBlockEntity(pos.relative(direction));
            placeHolder.rootPos = pos;
            worldIn.setBlockAndUpdate(pos.relative(direction.getCounterClockWise()), state.setValue(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder2 = (NestPlaceHolder) worldIn.getBlockEntity(pos.relative(direction.getCounterClockWise()));
            placeHolder2.rootPos = pos;
            worldIn.setBlockAndUpdate(pos.relative(direction).relative(direction.getCounterClockWise()), state.setValue(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder3 = (NestPlaceHolder) worldIn.getBlockEntity(pos.relative(direction).relative(direction.getCounterClockWise()));
            placeHolder3.rootPos = pos;
        }
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return state.getValue(PRIMARY_BLOCK) ? BlockRenderType.MODEL : BlockRenderType.INVISIBLE;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, worldIn, pos, newState, isMoving);
        if (state.getValue(PRIMARY_BLOCK)) {
            Direction direction = state.getValue(FACING);
            worldIn.destroyBlock(pos.relative(direction.getOpposite()), false);
            worldIn.destroyBlock(pos.relative(direction.getOpposite()).relative(direction.getClockWise()), false);
            worldIn.destroyBlock(pos.relative(direction.getClockWise()), false);
        }
    }
}
