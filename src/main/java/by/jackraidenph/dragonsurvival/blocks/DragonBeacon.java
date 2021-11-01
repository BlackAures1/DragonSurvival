package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.registration.BlockInit;
import by.jackraidenph.dragonsurvival.registration.Sounds;
import by.jackraidenph.dragonsurvival.registration.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.tiles.DragonBeaconEntity;
import by.jackraidenph.dragonsurvival.util.EffectInstance2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class DragonBeacon extends Block {
    public static BooleanProperty LIT = BlockStateProperties.LIT;
    public DragonBeacon(Properties p_i48440_1_) {
        super(p_i48440_1_);
        registerDefaultState(getStateDefinition().any().setValue(LIT, false));
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult p_225533_6_) {
        ItemStack itemStack = playerEntity.getItemInHand(hand);
        Item item = itemStack.getItem();
        //upgrading
        if (this == BlockInit.dragonBeacon) {
            DragonBeaconEntity old = (DragonBeaconEntity) world.getBlockEntity(pos);
            if (item == Items.GOLD_BLOCK) {
                world.setBlockAndUpdate(pos, BlockInit.peaceDragonBeacon.defaultBlockState());
                DragonBeaconEntity dragonBeaconEntity = (DragonBeaconEntity) world.getBlockEntity(pos);
                dragonBeaconEntity.type = DragonBeaconEntity.Type.PEACE;
                dragonBeaconEntity.tick = old.tick;
                itemStack.shrink(1);
                world.playSound(playerEntity, pos, Sounds.upgradeBeacon, SoundCategory.BLOCKS, 1, 1);
                return ActionResultType.SUCCESS;
            } else if (item == Items.DIAMOND_BLOCK) {
                world.setBlockAndUpdate(pos, BlockInit.magicDragonBeacon.defaultBlockState());
                DragonBeaconEntity dragonBeaconEntity = (DragonBeaconEntity) world.getBlockEntity(pos);
                dragonBeaconEntity.type = DragonBeaconEntity.Type.MAGIC;
                dragonBeaconEntity.tick = old.tick;
                itemStack.shrink(1);
                world.playSound(playerEntity, pos, Sounds.upgradeBeacon, SoundCategory.BLOCKS, 1, 1);
                return ActionResultType.SUCCESS;
            } else if (item == Items.NETHERITE_INGOT) {
                world.setBlockAndUpdate(pos, BlockInit.fireDragonBeacon.defaultBlockState());
                DragonBeaconEntity dragonBeaconEntity = (DragonBeaconEntity) world.getBlockEntity(pos);
                dragonBeaconEntity.type = DragonBeaconEntity.Type.FIRE;
                dragonBeaconEntity.tick = old.tick;
                itemStack.shrink(1);
                world.playSound(playerEntity, pos, Sounds.upgradeBeacon, SoundCategory.BLOCKS, 1, 1);
                return ActionResultType.SUCCESS;
            }
        }
        //apply temporary benefits
        if (itemStack.isEmpty()) {
            LazyOptional<DragonStateHandler> dragonState = DragonStateProvider.getCap(playerEntity);
            if (dragonState.isPresent()) {
                DragonStateHandler dragonStateHandler = dragonState.orElse(null);
                if (dragonStateHandler.isDragon() && (playerEntity.totalExperience >= 60 || playerEntity.isCreative())) {
                    if (this == BlockInit.peaceDragonBeacon) {
                        if (!world.isClientSide) {
                            ConfigHandler.COMMON.peaceBeaconEffects.get().forEach(s -> {
                                Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(s));
                                if (effect != null)
                                    playerEntity.addEffect(new EffectInstance2(effect, Functions.minutesToTicks(ConfigHandler.COMMON.secondsOfBeaconEffect.get())));
                            });
                        }
                    } else if (this == BlockInit.magicDragonBeacon) {
                        if (!world.isClientSide) {
                            ConfigHandler.COMMON.magicBeaconEffects.get().forEach(s -> {
                                Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(s));
                                if (effect != null)
                                    playerEntity.addEffect(new EffectInstance2(effect, Functions.minutesToTicks(ConfigHandler.COMMON.secondsOfBeaconEffect.get())));
                            });
                        }
                    } else if (this == BlockInit.fireDragonBeacon) {
                        if (!world.isClientSide) {
                            ConfigHandler.COMMON.fireBeaconEffects.get().forEach(s -> {
                                Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(s));
                                if (effect != null)
                                    playerEntity.addEffect(new EffectInstance2(effect, Functions.minutesToTicks(ConfigHandler.COMMON.secondsOfBeaconEffect.get())));
                            });
                        }
                    }

                    playerEntity.giveExperiencePoints(-60);
                    world.playSound(playerEntity, pos, Sounds.applyEffect, SoundCategory.PLAYERS, 1, 1);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        playerEntity.hurt(DamageSource.GENERIC, 1);
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypesInit.dragonBeacon.create();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_);
        p_206840_1_.add(LIT);
    }

    @Override
    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
