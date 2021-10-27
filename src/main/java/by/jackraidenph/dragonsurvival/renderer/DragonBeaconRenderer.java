package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.registration.BlockInit;
import by.jackraidenph.dragonsurvival.registration.ItemsInit;
import by.jackraidenph.dragonsurvival.tiles.DragonBeaconEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class DragonBeaconRenderer extends TileEntityRenderer<DragonBeaconEntity> {
    public DragonBeaconRenderer(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(DragonBeaconEntity dragonBeaconEntity, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int light, int overlay) {
        matrixStack.pushPose();
        DragonBeaconEntity.Type type = dragonBeaconEntity.type;
        Item item = BlockInit.dragonBeacon.asItem();
        if (dragonBeaconEntity.getLevel().getBlockState(dragonBeaconEntity.getBlockPos().below()).is(BlockInit.dragonMemoryBlock)) {
            switch (type) {
                case PEACE:
                    item = ItemsInit.passivePeaceBeacon;
                    break;
                case MAGIC:
                    item = ItemsInit.passiveMagicBeacon;
                    break;
                case VETO:
                    item = ItemsInit.passiveVetoBeacon;
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
                case VETO:
                    item = BlockInit.vetoDragonBeacon.asItem();
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
