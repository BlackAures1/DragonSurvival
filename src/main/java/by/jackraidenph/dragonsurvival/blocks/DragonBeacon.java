package by.jackraidenph.dragonsurvival.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class DragonBeacon extends Block {
    public DragonBeacon(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos p_225533_3_, PlayerEntity playerEntity, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
        playerEntity.hurt(DamageSource.GENERIC, 1);
        return ActionResultType.SUCCESS;
    }
}
