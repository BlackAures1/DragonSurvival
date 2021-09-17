package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import by.jackraidenph.dragonsurvival.handlers.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class NestBlock extends HorizontalBlock {

    public static final VoxelShape SHAPE = VoxelShapes.box(0, 0, 0, 1, 0.1, 1);
    public static final VoxelShape OUTLINE = VoxelShapes.box(0, 0, 0, 1, 0.5, 1);

    public NestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypesInit.nestEntityTile.create();
    }

    public NestEntity getBlockEntity(World world, BlockPos pos) {
        return (NestEntity) world.getBlockEntity(pos);
    }

    /**
     * Prevent anyone from breaking the nest
     */
    @Override
    public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        UUID uuid = player.getUUID();
        DragonStateHandler dragonStateHandler = player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null);
        DragonLevel dragonLevel = dragonStateHandler.getLevel();
        DragonType dragonType = dragonStateHandler.getType();
        TileEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof NestEntity && uuid.equals(((NestEntity) blockEntity).ownerUUID)) {
            final Direction playerHorizontalFacing = player.getDirection();
            final Direction placementDirection = playerHorizontalFacing.getOpposite();
            if (state.getBlock().getClass() == NestBlock.class && dragonLevel == DragonLevel.YOUNG) {

                if (worldIn.isEmptyBlock(pos.relative(playerHorizontalFacing)) &&
                        worldIn.isEmptyBlock(pos.relative(playerHorizontalFacing.getCounterClockWise())) &&
                        worldIn.isEmptyBlock(pos.relative(playerHorizontalFacing).relative(playerHorizontalFacing.getCounterClockWise()))) {
                    CompoundNBT compoundNBT = blockEntity.save(new CompoundNBT());
                    switch (dragonType) {
                        case SEA:
                            worldIn.setBlockAndUpdate(pos, BlockInit.mediumSeaNest.defaultBlockState().setValue(FACING, placementDirection));
                            break;
                        case FOREST:
                            worldIn.setBlockAndUpdate(pos, BlockInit.mediumForestNest.defaultBlockState().setValue(FACING, placementDirection));
                            break;
                        case CAVE:
                            worldIn.setBlockAndUpdate(pos, BlockInit.mediumCaveNest.defaultBlockState().setValue(FACING, placementDirection));
                    }
                    NestEntity nestEntity = getBlockEntity(worldIn, pos);
                    BlockState blockState = worldIn.getBlockState(pos);
                    nestEntity.load(blockState, compoundNBT);
                    blockState.getBlock().setPlacedBy(worldIn, pos, blockState, player, player.getItemInHand(handIn));
                    return ActionResultType.SUCCESS;
                } else {
                    if (worldIn.isClientSide) {
                        player.sendMessage(new TranslationTextComponent("ds.space.occupied"), player.getUUID());
                    }
                    return ActionResultType.CONSUME;
                }
            } else if (state.getBlock().getClass() == NestBlock.class && dragonLevel == DragonLevel.ADULT) {
                if (worldIn.isEmptyBlock(pos.north()) && worldIn.isEmptyBlock(pos.south()) &&
                        worldIn.isEmptyBlock(pos.west()) && worldIn.isEmptyBlock(pos.east())
                        && worldIn.isEmptyBlock(pos.north().west()) && worldIn.isEmptyBlock(pos.north().east())
                        && worldIn.isEmptyBlock(pos.south().east()) && worldIn.isEmptyBlock(pos.south().west())) {
                    CompoundNBT compoundNBT = blockEntity.save(new CompoundNBT());
                    switch (dragonType) {
                        case SEA:
                            worldIn.setBlockAndUpdate(pos, BlockInit.bigSeaNest.defaultBlockState().setValue(FACING, placementDirection));
                            break;
                        case FOREST:
                            worldIn.setBlockAndUpdate(pos, BlockInit.bigForestNest.defaultBlockState().setValue(FACING, placementDirection));
                            break;
                        case CAVE:
                            worldIn.setBlockAndUpdate(pos, BlockInit.bigCaveNest.defaultBlockState().setValue(FACING, placementDirection));
                    }
                    NestEntity nestEntity = getBlockEntity(worldIn, pos);
                    BlockState blockState = worldIn.getBlockState(pos);
                    nestEntity.load(blockState, compoundNBT);
                    blockState.getBlock().setPlacedBy(worldIn, pos, blockState, player, player.getItemInHand(handIn));
                    return ActionResultType.SUCCESS;
                } else {
                    if (worldIn.isClientSide) {
                        player.sendMessage(new TranslationTextComponent("ds.space.occupied"), player.getUUID());
                    }
                    return ActionResultType.CONSUME;
                }
            }
        }
        if (player instanceof ServerPlayerEntity && player.getUUID().equals(getBlockEntity(worldIn, pos).ownerUUID)) {
            NetworkHooks.openGui((ServerPlayerEntity) player, getBlockEntity(worldIn, pos), packetBuffer -> packetBuffer.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    /**
     * Setting owner and type
     */
    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        NestEntity nestEntity = getBlockEntity(worldIn, pos);
        if (placer != null) {
            DragonStateProvider.getCap(placer).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon()) {
                    if (nestEntity.ownerUUID == null) {
                        nestEntity.ownerUUID = placer.getUUID();
                    }
                    if (nestEntity.type == DragonType.NONE) {
                        nestEntity.type = dragonStateHandler.getType();
                    }
                }
            });
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return OUTLINE;
    }

    @Override
    public boolean triggerEvent(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        return tileentity != null && tileentity.triggerEvent(id, param);
    }

    @Override
    public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, @Nullable Entity player) {
        return true;
    }

    @Override
    public void setBedOccupied(BlockState state, World world, BlockPos pos, LivingEntity sleeper, boolean occupied) {

    }
}
