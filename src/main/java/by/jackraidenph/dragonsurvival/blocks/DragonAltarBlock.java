package by.jackraidenph.dragonsurvival.blocks;


import javax.annotation.Nullable;


import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.gui.DragonAltarGUI;
import by.jackraidenph.dragonsurvival.handlers.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.tiles.AltarEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
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
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;






public class DragonAltarBlock extends Block {
    private final VoxelShape SHAPE = VoxelShapes.block();
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    
    
    public DragonAltarBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
        		.setValue(FACING, Direction.NORTH));
    }
    
    
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }
    
    @Override
    public ActionResultType use(BlockState blockState, World worldIn, BlockPos blockPos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
    	TileEntity tileEntity = worldIn.getBlockEntity(blockPos);
        if (tileEntity instanceof AltarEntity) {
            AltarEntity altarEntity = (AltarEntity) tileEntity;
            int cooldown=altarEntity.usageCooldowns.getOrDefault(player.getUUID(),0);
            if (cooldown > 0) {
                if (worldIn.isClientSide)
                    player.sendMessage(new TranslationTextComponent("ds.cooldown.active").append(": "+ Functions.ticksToSeconds(cooldown)), player.getUUID());
                return ActionResultType.CONSUME;
            } else {
                if (worldIn.isClientSide) {
                    openGUi();
                }
                altarEntity.usageCooldowns.put(player.getUUID(),Functions.secondsToTicks(1));
            }
        }
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUi() {
        Minecraft.getInstance().setScreen(new DragonAltarGUI(new TextComponent() {
            @Override
            public String getContents() {
                return "Dragon altar";
            }

            @Override
            public TextComponent plainCopy() {
                return this;
            }

        }));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypesInit.altarEntityTile.create();
    }

}
