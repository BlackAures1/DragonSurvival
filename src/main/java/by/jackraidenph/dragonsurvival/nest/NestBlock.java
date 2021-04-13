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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class NestBlock extends HorizontalBlock {

    public static final VoxelShape SHAPE = VoxelShapes.create(0, 0, 0, 1, 0.1, 1);
    public static final VoxelShape OUTLINE = VoxelShapes.create(0, 0, 0, 1, 0.5, 1);

    public NestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(HORIZONTAL_FACING);
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
        return (NestEntity) world.getTileEntity(pos);
    }

    /**
     * Prevent anyone from breaking the nest
     */
    @Override
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        UUID uuid = player.getUniqueID();
        DragonStateHandler dragonStateHandler = player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null);
        DragonLevel dragonLevel = dragonStateHandler.getLevel();
        DragonType dragonType = dragonStateHandler.getType();
        TileEntity blockEntity = worldIn.getTileEntity(pos);
        if (blockEntity instanceof NestEntity && uuid.equals(((NestEntity) blockEntity).ownerUUID)) {
            final Direction playerHorizontalFacing = player.getHorizontalFacing();
            final Direction placementDirection = playerHorizontalFacing.getOpposite();
            if (state.getBlock().getClass() == NestBlock.class && dragonLevel == DragonLevel.YOUNG) {

                if (worldIn.isAirBlock(pos.offset(playerHorizontalFacing)) &&
                        worldIn.isAirBlock(pos.offset(playerHorizontalFacing.rotateYCCW())) &&
                        worldIn.isAirBlock(pos.offset(playerHorizontalFacing).offset(playerHorizontalFacing.rotateYCCW()))) {
                    CompoundNBT compoundNBT = blockEntity.write(new CompoundNBT());
                    switch (dragonType) {
                        case SEA:
                            worldIn.setBlockState(pos, BlockInit.mediumSeaNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                            break;
                        case FOREST:
                            worldIn.setBlockState(pos, BlockInit.mediumForestNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                            break;
                        case CAVE:
                            worldIn.setBlockState(pos, BlockInit.mediumCaveNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                    }
                    NestEntity nestEntity = getBlockEntity(worldIn, pos);
                    nestEntity.read(compoundNBT);
                    BlockState blockState = worldIn.getBlockState(pos);
                    blockState.getBlock().onBlockPlacedBy(worldIn, pos, blockState, player, player.getHeldItem(handIn));
                    return ActionResultType.SUCCESS;
                } else {
                    if (worldIn.isRemote) {
                        player.sendMessage(new TranslationTextComponent("ds.space.occupied"));
                    }
                    return ActionResultType.CONSUME;
                }
            } else if (state.getBlock().getClass() == NestBlock.class && dragonLevel == DragonLevel.ADULT) {
                if (worldIn.isAirBlock(pos.north()) && worldIn.isAirBlock(pos.south()) &&
                        worldIn.isAirBlock(pos.west()) && worldIn.isAirBlock(pos.east())
                        && worldIn.isAirBlock(pos.north().west()) && worldIn.isAirBlock(pos.north().east())
                        && worldIn.isAirBlock(pos.south().east()) && worldIn.isAirBlock(pos.south().west())) {
                    CompoundNBT compoundNBT = blockEntity.write(new CompoundNBT());
                    switch (dragonType) {
                        case SEA:
                            worldIn.setBlockState(pos, BlockInit.bigSeaNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                            break;
                        case FOREST:
                            worldIn.setBlockState(pos, BlockInit.bigForestNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                            break;
                        case CAVE:
                            worldIn.setBlockState(pos, BlockInit.bigCaveNest.getDefaultState().with(HORIZONTAL_FACING, placementDirection));
                    }
                    NestEntity nestEntity = getBlockEntity(worldIn, pos);
                    nestEntity.read(compoundNBT);
                    BlockState blockState = worldIn.getBlockState(pos);
                    blockState.getBlock().onBlockPlacedBy(worldIn, pos, blockState, player, player.getHeldItem(handIn));
                    return ActionResultType.SUCCESS;
                } else {
                    if (worldIn.isRemote) {
                        player.sendMessage(new TranslationTextComponent("ds.space.occupied"));
                    }
                    return ActionResultType.CONSUME;
                }
            }
        }
        if (player instanceof ServerPlayerEntity && player.getUniqueID().equals(getBlockEntity(worldIn, pos).ownerUUID)) {
            NetworkHooks.openGui((ServerPlayerEntity) player, getBlockEntity(worldIn, pos), packetBuffer -> packetBuffer.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    /**
     * Setting owner and type
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        NestEntity nestEntity = getBlockEntity(worldIn, pos);
        if (placer != null) {
            DragonStateProvider.getCap(placer).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon()) {
                    if (nestEntity.ownerUUID == null) {
                        nestEntity.ownerUUID = placer.getUniqueID();
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
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, @Nullable Entity player) {
        return true;
    }

    @Override
    public void setBedOccupied(BlockState state, IWorldReader world, BlockPos pos, LivingEntity sleeper, boolean occupied) {

    }
}
