package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.gui.DragonAltarGUI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DragonAltarBlock extends Block {
    VoxelShape SHAPE = VoxelShapes.fullCube();

    public DragonAltarBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState blockState, World worldIn, BlockPos blockPos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {

        if (worldIn.isRemote) {
            openGUi();
        }
        return ActionResultType.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUi() {
        Minecraft.getInstance().displayGuiScreen(new DragonAltarGUI(new TextComponent() {
            @Override
            public String getUnformattedComponentText() {
                return "Dragon altar";
            }

            @Override
            public ITextComponent shallowCopy() {
                return this;
            }
        }));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}
