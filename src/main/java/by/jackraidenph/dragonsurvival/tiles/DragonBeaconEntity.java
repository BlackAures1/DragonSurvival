package by.jackraidenph.dragonsurvival.tiles;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.blocks.DragonBeacon;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.registration.BlockInit;
import by.jackraidenph.dragonsurvival.registration.DragonEffects;
import by.jackraidenph.dragonsurvival.registration.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.util.EffectInstance2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class DragonBeaconEntity extends BaseBlockEntity implements ITickableTileEntity {
    public Type type = Type.NONE;
    public int tick;
    public enum Type {
        PEACE,
        MAGIC,
        VETO,
        NONE
    }

    public DragonBeaconEntity() {
        super(TileEntityTypesInit.dragonBeacon);
    }

    @Override
    public void tick() {
        BlockState below = level.getBlockState(getBlockPos().below());
        BlockState blockState = getBlockState();
        Block beacon = blockState.getBlock();
        if (type == Type.NONE) {
            if (beacon == BlockInit.magicDragonBeacon)
                type = Type.MAGIC;
            else if (beacon == BlockInit.peaceDragonBeacon)
                type = Type.PEACE;
            else if (beacon == BlockInit.vetoDragonBeacon)
                type = Type.VETO;
        }
        if (below.getBlock() == BlockInit.dragonMemoryBlock && type != Type.NONE) {
            if (!blockState.getValue(DragonBeacon.LIT))
                level.setBlockAndUpdate(getBlockPos(), blockState.cycle(DragonBeacon.LIT));
            if (!level.isClientSide) {
                List<PlayerEntity> dragons = level.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(getBlockPos()).inflate(50).expandTowards(0, level.getMaxBuildHeight(), 0), DragonStateProvider::isDragon);
                switch (type) {
                    case PEACE:
                        dragons.forEach(playerEntity -> playerEntity.addEffect(new EffectInstance2(DragonEffects.PEACE, Functions.secondsToTicks(20) + 5)));
                        break;
                    case MAGIC:
                        dragons.forEach(playerEntity -> playerEntity.addEffect(new EffectInstance2(DragonEffects.MAGIC, Functions.secondsToTicks(20) + 5)));
                        break;
                    case VETO:
                        dragons.forEach(playerEntity -> playerEntity.addEffect(new EffectInstance2(DragonEffects.VETO, Functions.secondsToTicks(20) + 5)));
                        break;
                }
            }
        } else {
            BlockState thisState = getBlockState();
            if (thisState.getValue(DragonBeacon.LIT))
                level.setBlockAndUpdate(getBlockPos(), thisState.cycle(DragonBeacon.LIT));
        }
        tick++;
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        compoundNBT.putString("Type", type.name());
        return super.save(compoundNBT);
    }

    @Override
    public void load(BlockState p_230337_1_, CompoundNBT compoundNBT) {
        super.load(p_230337_1_, compoundNBT);
        type = Type.valueOf(compoundNBT.getString("Type"));
    }
}
