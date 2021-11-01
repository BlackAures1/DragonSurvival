package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.registration.BlockInit;
import by.jackraidenph.dragonsurvival.registration.ItemsInit;
import by.jackraidenph.dragonsurvival.registration.ParticleRegistry;
import by.jackraidenph.dragonsurvival.tiles.DragonBeaconEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

public class DragonBeaconRenderer extends TileEntityRenderer<DragonBeaconEntity> {
    public DragonBeaconRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(DragonBeaconEntity dragonBeaconEntity, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
        matrixStack.pushPose();
        DragonBeaconEntity.Type type = dragonBeaconEntity.type;
        Item item = BlockInit.dragonBeacon.asItem();
        ClientWorld clientWorld = (ClientWorld) dragonBeaconEntity.getLevel();
        if (dragonBeaconEntity.getLevel().getBlockState(dragonBeaconEntity.getBlockPos().below()).is(BlockInit.dragonMemoryBlock)) {
            Minecraft minecraft = Minecraft.getInstance();
            Random random = clientWorld.random;
            double x = 0.25 + random.nextInt(5) / 10d;
            double z = 0.25 + random.nextInt(5) / 10d;
            switch (type) {
                case PEACE:
                    item = ItemsInit.passivePeaceBeacon;
                    try {
                        if (!minecraft.isPaused() && dragonBeaconEntity.tick % 5 == 0)
                            clientWorld.addParticle(ParticleRegistry.peaceBeaconParticle.getDeserializer().fromCommand(ParticleRegistry.peaceBeaconParticle, new StringReader("")), dragonBeaconEntity.getX() + x, dragonBeaconEntity.getY() + 0.5, dragonBeaconEntity.getZ() + z, 0, 0, 0);
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                    break;
                case MAGIC:
                    item = ItemsInit.passiveMagicBeacon;
                    try {
                        if (!minecraft.isPaused() && dragonBeaconEntity.tick % 5 == 0)
                            clientWorld.addParticle(ParticleRegistry.magicBeaconParticle.getDeserializer().fromCommand(ParticleRegistry.magicBeaconParticle, new StringReader("")), dragonBeaconEntity.getX() + x, dragonBeaconEntity.getY() + 0.5, dragonBeaconEntity.getZ() + z, 0, 0, 0);
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                    break;
                case FIRE:
                    item = ItemsInit.passiveFireBeacon;
                    try {
                        if (!minecraft.isPaused() && dragonBeaconEntity.tick % 5 == 0)
                            clientWorld.addParticle(ParticleRegistry.fireBeaconParticle.getDeserializer().fromCommand(ParticleRegistry.fireBeaconParticle, new StringReader("")), dragonBeaconEntity.getX() + x, dragonBeaconEntity.getY() + 0.5, dragonBeaconEntity.getZ() + z, 0, 0, 0);
                    } catch (CommandSyntaxException e) {
                        e.printStackTrace();
                    }
                    break;
            }

        } else {
            switch (type) {
                case PEACE:
                    item = BlockInit.peaceDragonBeacon.asItem();
                    break;
                case MAGIC:
                    item = BlockInit.magicDragonBeacon.asItem();
                    break;
                case FIRE:
                    item = BlockInit.fireDragonBeacon.asItem();
                    break;
            }
        }
        matrixStack.translate(0.5, 0.25, 0.5);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(dragonBeaconEntity.tick));
        matrixStack.scale(2, 2, 2);
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(item), ItemCameraTransforms.TransformType.GROUND, light, overlay, matrixStack, iRenderTypeBuffer);
        matrixStack.popPose();
    }
}
