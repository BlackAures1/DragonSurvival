package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.gui.DragonAltarGUI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class DragonAltarBlock extends Block {
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
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        worldIn.addParticle(ParticleTypes.END_ROD, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0, rand.nextFloat() / 4.0f + 0.1f, 0);
    }
}
