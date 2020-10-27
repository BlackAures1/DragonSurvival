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
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class DragonAltarBlock extends Block {
    VoxelShape SHAPE = Stream.of(
            Block.makeCuboidShape(0, 0, 0, 16, 5, 16),
            Block.makeCuboidShape(2, 7, 7, 4, 13, 9),
            Block.makeCuboidShape(7, 7, 11, 9, 13, 13),
            Block.makeCuboidShape(11, 7, 7, 13, 13, 9),
            Block.makeCuboidShape(7, 7, 3, 9, 13, 5),
            Block.makeCuboidShape(7, 10, 7, 9, 16, 9)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR)).get();

    public DragonAltarBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.setRegistryName("dragon_altar");
    }

    @Override
    public ActionResultType onBlockActivated(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity p_225533_4_, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        Minecraft.getInstance().displayGuiScreen(new DragonAltarGUI(new TextComponent() {
            @Override
            public String getUnformattedComponentText() {
                return "test";
            }

            @Override
            public ITextComponent shallowCopy() {
                return this;
            }
        }));
        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        worldIn.addParticle(ParticleTypes.END_ROD, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0, rand.nextFloat() / 4.0f + 0.1f, 0);
    }*/
}
