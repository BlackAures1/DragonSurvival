package by.jackraidenph.dragonsurvival.models;// Made with Blockbench 3.5.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.function.Function;

public class MagicalPredatorModel extends EntityModel<MagicalPredatorEntity> {
    private final ModelRenderer Main;
    private final ModelRenderer All_body;
    private final ModelRenderer body;
    private final ModelRenderer hind;
    private final ModelRenderer pelvis_and_hind_legs;
    private final ModelRenderer tail;
    private final ModelRenderer tail_end;
    private final ModelRenderer right_front_leg;
    private final ModelRenderer right_front_shoulder;
    private final ModelRenderer right_front_forearm;
    private final ModelRenderer right_front_foot;
    private final ModelRenderer right_front_forearm_claws;
    private final ModelRenderer left_front_leg;
    private final ModelRenderer left_front_shoulder;
    private final ModelRenderer left_front_forearm;
    private final ModelRenderer left_front_foot;
    private final ModelRenderer left_front_forearm_claws;
    private final ModelRenderer back_leg_left;
    private final ModelRenderer back_lower_leg_left;
    private final ModelRenderer back_brush_left;
    private final ModelRenderer back_foot_left;
    private final ModelRenderer back_claws_foot_left;
    private final ModelRenderer back_leg_right;
    private final ModelRenderer back_lower_leg_right;
    private final ModelRenderer back_brush_right;
    private final ModelRenderer back_foot_right;
    private final ModelRenderer back_claws_foot_right;
    private final ModelRenderer Neck_and_head;
    private final ModelRenderer neck;
    private final ModelRenderer head;
    private final ModelRenderer lower_jaw;
    //private final ModelRenderer eyebrows;
    private final ModelRenderer star;

    public MagicalPredatorModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);

        texWidth = 128;
        texHeight = 128;

        Main = new ModelRenderer(this);
        Main.setPos(3.0F, 13.0F, 1.0F);


        All_body = new ModelRenderer(this);
        All_body.setPos(0.0F, 0.0F, 0.0F);
        Main.addChild(All_body);


        body = new ModelRenderer(this);
        body.setPos(-3.0F, -7.0F, -9.0F);
        All_body.addChild(body);
        body.texOffs(0, 0).addBox(-4.5F, -1.0F, -4.0F, 9.0F, 8.0F, 15.0F, 0.0F, false);
        body.texOffs(30, 30).addBox(-1.01F, -2.0F, -4.1F, 2.0F, 2.0F, 15.0F, 0.0F, false);
        body.texOffs(0, 17).addBox(2.0F, -5.0F, -4.0F, 0.0F, 7.0F, 15.0F, 0.0F, false);
        body.texOffs(0, 8).addBox(0.0F, -6.0F, -4.0F, 0.0F, 9.0F, 15.0F, 0.0F, false);
        body.texOffs(0, 24).addBox(-2.0F, -5.0F, -4.0F, 0.0F, 7.0F, 15.0F, 0.0F, false);

        star = new ModelRenderer(this);
        star.setPos(0.0F, 3.0F, 0.0F);
        body.addChild(star);
        star.texOffs(51, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, 0.0F, false);
        star.texOffs(49, 41).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        hind = new ModelRenderer(this);
        hind.setPos(0.0F, -1.0F, 11.0F);
        body.addChild(hind);
        hind.texOffs(30, 30).addBox(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hind.texOffs(47, 47).addBox(-1.0F, -0.9F, -1.0F, 2.0F, 2.0F, 7.0F, 0.0F, false);

        pelvis_and_hind_legs = new ModelRenderer(this);
        pelvis_and_hind_legs.setPos(0.0F, 0.0F, 6.0F);
        hind.addChild(pelvis_and_hind_legs);
        pelvis_and_hind_legs.texOffs(33, 0).addBox(-3.0F, -1.5F, -1.0F, 6.0F, 4.0F, 6.0F, 0.0F, false);
        pelvis_and_hind_legs.texOffs(30, 30).addBox(0.0F, -3.0F, 0.0F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        tail = new ModelRenderer(this);
        tail.setPos(0.0F, 0.2929F, 4.7753F);
        pelvis_and_hind_legs.addChild(tail);
        tail.texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
        tail.texOffs(30, 20).addBox(0.0F, -2.0F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);

        tail_end = new ModelRenderer(this);
        tail_end.setPos(0.0F, 0.0F, 6.0F);
        tail.addChild(tail_end);
        tail_end.texOffs(0, 11).addBox(0.0F, -1.0F, 0.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);

        right_front_leg = new ModelRenderer(this);
        right_front_leg.setPos(-8.0F, -4.0F, -10.0F);
        All_body.addChild(right_front_leg);
        setRotationAngle(right_front_leg, -0.2618F, 0.0F, 0.0F);
        right_front_leg.texOffs(30, 56).addBox(-0.5F, -3.8637F, -2.0353F, 1.0F, 5.0F, 3.0F, 0.0F, false);

        right_front_shoulder = new ModelRenderer(this);
        right_front_shoulder.setPos(0.0F, 0.1363F, -0.0353F);
        right_front_leg.addChild(right_front_shoulder);
        setRotationAngle(right_front_shoulder, 0.5236F, 0.0F, 0.0F);
        right_front_shoulder.texOffs(44, 56).addBox(-0.6F, 0.0F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        right_front_forearm = new ModelRenderer(this);
        right_front_forearm.setPos(0.0F, 6.0F, 0.0F);
        right_front_shoulder.addChild(right_front_forearm);
        setRotationAngle(right_front_forearm, -0.2618F, 0.0F, 0.0F);
        right_front_forearm.texOffs(0, 56).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 9.0F, 2.0F, 0.0F, false);
        right_front_forearm.texOffs(38, 31).addBox(0.0F, -1.0F, 1.0F, 0.0F, 3.0F, 2.0F, 0.0F, false);

        right_front_foot = new ModelRenderer(this);
        right_front_foot.setPos(0.5F, 8.0F, 0.0F);
        right_front_forearm.addChild(right_front_foot);
        right_front_foot.texOffs(49, 31).addBox(-2.0F, 0.1F, -3.5F, 3.0F, 1.0F, 4.0F, 0.0F, false);

        right_front_forearm_claws = new ModelRenderer(this);
        right_front_forearm_claws.setPos(1.5F, 0.0F, 0.0F);
        right_front_foot.addChild(right_front_forearm_claws);
        right_front_forearm_claws.texOffs(40, 50).addBox(-1.5F, -1.0F, -4.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        right_front_forearm_claws.texOffs(49, 34).addBox(-0.6F, -1.0F, -4.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        right_front_forearm_claws.texOffs(29, 50).addBox(-2.5F, -1.0F, -4.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        right_front_forearm_claws.texOffs(49, 36).addBox(-3.4F, -1.0F, -4.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        left_front_leg = new ModelRenderer(this);
        left_front_leg.setPos(2.0F, -4.0F, -10.0F);
        All_body.addChild(left_front_leg);
        setRotationAngle(left_front_leg, -0.2618F, 0.0F, 0.0F);
        left_front_leg.texOffs(0, 46).addBox(-0.5F, -3.8637F, -2.0353F, 1.0F, 5.0F, 3.0F, 0.0F, false);

        left_front_shoulder = new ModelRenderer(this);
        left_front_shoulder.setPos(0.0F, 0.1363F, -0.0353F);
        left_front_leg.addChild(left_front_shoulder);
        setRotationAngle(left_front_shoulder, 0.5236F, 0.0F, 0.0F);
        left_front_shoulder.texOffs(38, 56).addBox(-0.4F, 0.0F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);

        left_front_forearm = new ModelRenderer(this);
        left_front_forearm.setPos(0.0F, 6.0F, 0.0F);
        left_front_shoulder.addChild(left_front_forearm);
        setRotationAngle(left_front_forearm, -0.2618F, 0.0F, 0.0F);
        left_front_forearm.texOffs(24, 55).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 9.0F, 2.0F, 0.0F, false);
        left_front_forearm.texOffs(33, 8).addBox(0.0F, -1.0F, 1.0F, 0.0F, 3.0F, 2.0F, 0.0F, false);

        left_front_foot = new ModelRenderer(this);
        left_front_foot.setPos(-0.5F, 8.0F, 0.0F);
        left_front_forearm.addChild(left_front_foot);
        left_front_foot.texOffs(40, 47).addBox(-1.0F, 0.1F, -3.5F, 3.0F, 1.0F, 4.0F, 0.0F, false);

        left_front_forearm_claws = new ModelRenderer(this);
        left_front_forearm_claws.setPos(-1.5F, 0.0F, 0.0F);
        left_front_foot.addChild(left_front_forearm_claws);
        left_front_forearm_claws.texOffs(40, 47).addBox(1.5F, -1.0F, -4.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        left_front_forearm_claws.texOffs(11, 46).addBox(0.6F, -1.0F, -4.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        left_front_forearm_claws.texOffs(40, 45).addBox(2.5F, -1.0F, -4.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        left_front_forearm_claws.texOffs(29, 45).addBox(3.4F, -1.0F, -4.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        back_leg_left = new ModelRenderer(this);
        back_leg_left.setPos(0.0F, -7.0F, 10.0F);
        All_body.addChild(back_leg_left);
        setRotationAngle(back_leg_left, -0.2618F, 0.0F, 0.0F);
        back_leg_left.texOffs(18, 55).addBox(0.0F, -1.0F, -1.0F, 1.0F, 10.0F, 2.0F, 0.0F, false);

        back_lower_leg_left = new ModelRenderer(this);
        back_lower_leg_left.setPos(0.5F, 7.7753F, 0.7071F);
        back_leg_left.addChild(back_lower_leg_left);
        setRotationAngle(back_lower_leg_left, 0.7854F, 0.0F, 0.0F);
        back_lower_leg_left.texOffs(12, 56).addBox(-0.3F, 0.0F, -1.5F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        back_brush_left = new ModelRenderer(this);
        back_brush_left.setPos(0.0F, 7.4641F, -0.2679F);
        back_lower_leg_left.addChild(back_brush_left);
        setRotationAngle(back_brush_left, -0.5236F, 0.0F, 0.0F);
        back_brush_left.texOffs(8, 0).addBox(-0.5F, -0.7394F, -0.866F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        back_brush_left.texOffs(43, 8).addBox(0.0F, -2.0F, 1.0F, 0.0F, 3.0F, 2.0F, 0.0F, false);

        back_foot_left = new ModelRenderer(this);
        back_foot_left.setPos(0.0F, 3.2321F, 0.134F);
        back_brush_left.addChild(back_foot_left);
        back_foot_left.texOffs(49, 36).addBox(-1.5F, -0.4714F, -4.0F, 3.0F, 1.0F, 4.0F, 0.0F, false);

        back_claws_foot_left = new ModelRenderer(this);
        back_claws_foot_left.setPos(-4.5F, -1.4714F, -19.5731F);
        back_foot_left.addChild(back_claws_foot_left);
        back_claws_foot_left.texOffs(49, 31).addBox(5.9F, 0.0F, 14.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        back_claws_foot_left.texOffs(49, 29).addBox(5.0F, 0.0F, 14.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        back_claws_foot_left.texOffs(48, 22).addBox(4.0F, 0.0F, 14.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        back_claws_foot_left.texOffs(47, 8).addBox(3.1F, 0.0F, 14.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        back_leg_right = new ModelRenderer(this);
        back_leg_right.setPos(-6.0F, -7.0F, 10.0F);
        All_body.addChild(back_leg_right);
        setRotationAngle(back_leg_right, -0.2618F, 0.0F, 0.0F);
        back_leg_right.texOffs(50, 10).addBox(-1.0F, -1.0F, -1.0F, 1.0F, 10.0F, 2.0F, 0.0F, false);

        back_lower_leg_right = new ModelRenderer(this);
        back_lower_leg_right.setPos(-0.5F, 7.7753F, 0.7071F);
        back_leg_right.addChild(back_lower_leg_right);
        setRotationAngle(back_lower_leg_right, 0.7854F, 0.0F, 0.0F);
        back_lower_leg_right.texOffs(6, 56).addBox(-0.7F, 0.0F, -1.5F, 1.0F, 8.0F, 2.0F, 0.0F, false);

        back_brush_right = new ModelRenderer(this);
        back_brush_right.setPos(0.0F, 7.4641F, -0.2679F);
        back_lower_leg_right.addChild(back_brush_right);
        setRotationAngle(back_brush_right, -0.5236F, 0.0F, 0.0F);
        back_brush_right.texOffs(0, 0).addBox(-0.5F, -0.7394F, -0.866F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        back_brush_right.texOffs(33, 0).addBox(0.0F, -2.0F, 1.0F, 0.0F, 3.0F, 2.0F, 0.0F, false);

        back_foot_right = new ModelRenderer(this);
        back_foot_right.setPos(0.0F, 3.2321F, 0.134F);
        back_brush_right.addChild(back_foot_right);
        back_foot_right.texOffs(33, 10).addBox(-1.5F, -0.4714F, -4.0F, 3.0F, 1.0F, 4.0F, 0.0F, false);

        back_claws_foot_right = new ModelRenderer(this);
        back_claws_foot_right.setPos(4.5F, -1.4714F, -19.5731F);
        back_foot_right.addChild(back_claws_foot_right);
        back_claws_foot_right.texOffs(44, 22).addBox(-5.9F, 0.0F, 14.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        back_claws_foot_right.texOffs(11, 44).addBox(-5.0F, 0.0F, 14.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        back_claws_foot_right.texOffs(5, 44).addBox(-4.0F, 0.0F, 14.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        back_claws_foot_right.texOffs(40, 41).addBox(-3.1F, 0.0F, 14.5F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        Neck_and_head = new ModelRenderer(this);
        Neck_and_head.setPos(-3.0F, -7.0F, -12.0F);
        Main.addChild(Neck_and_head);
        setRotationAngle(Neck_and_head, -0.5236F, 0.0F, 0.0F);


        neck = new ModelRenderer(this);
        neck.setPos(1.0F, 0.0F, 0.0F);
        Neck_and_head.addChild(neck);
        neck.texOffs(29, 47).addBox(-2.0F, -1.866F, -7.5F, 2.0F, 2.0F, 7.0F, 0.0F, false);
        neck.texOffs(0, 0).addBox(-1.01F, -3.866F, -7.5F, 0.0F, 5.0F, 7.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, -1.366F, -6.634F);
        Neck_and_head.addChild(head);
        setRotationAngle(head, 0.5236F, 0.0F, 0.0F);
        head.texOffs(30, 41).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);
        head.texOffs(54, 7).addBox(-2.0F, 0.0F, -8.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        head.texOffs(24, 46).addBox(1.0F, 0.0F, -8.0F, 1.0F, 2.0F, 3.0F, 0.0F, false);
        head.texOffs(42, 26).addBox(-2.0F, 0.0F, -9.0F, 4.0F, 2.0F, 1.0F, 0.0F, false);
        head.texOffs(30, 17).addBox(1.5F, 1.5F, -8.5F, 0.0F, 2.0F, 7.0F, 0.0F, false);
        head.texOffs(0, 5).addBox(-1.5F, 1.5F, -8.5F, 0.0F, 2.0F, 7.0F, 0.0F, false);
        head.texOffs(33, 0).addBox(-1.5F, 1.5F, -8.5F, 3.0F, 2.0F, 0.0F, 0.0F, false);
        head.texOffs(11, 46).addBox(-2.0F, -2.0F, -5.0F, 4.0F, 4.0F, 5.0F, 0.0F, false);
        head.texOffs(50, 47).addBox(-2.3F, -1.0F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        head.texOffs(46, 12).addBox(1.3F, -1.0F, -4.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        //head.texOffs(49, 24).addBox(-1.5F, -2.5F, -4.0F, 3.0F, 2.0F, 5.0F, 0.0F, false);

        lower_jaw = new ModelRenderer(this);
        lower_jaw.setPos(0.0F, 2.0F, -1.0F);
        head.addChild(lower_jaw);
        lower_jaw.texOffs(56, 56).addBox(0.9F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        lower_jaw.texOffs(50, 56).addBox(-1.9F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        lower_jaw.texOffs(0, 46).addBox(1.0F, 1.0F, -8.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        lower_jaw.texOffs(39, 14).addBox(-2.0F, 1.0F, -8.0F, 1.0F, 1.0F, 9.0F, 0.0F, false);
        lower_jaw.texOffs(44, 52).addBox(-1.0F, 1.0F, -8.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        lower_jaw.texOffs(30, 25).addBox(1.3F, -0.5359F, -7.0F, 0.0F, 2.0F, 6.0F, 0.0F, false);
        lower_jaw.texOffs(30, 23).addBox(-1.3F, -0.5359F, -7.0F, 0.0F, 2.0F, 6.0F, 0.0F, false);
        lower_jaw.texOffs(37, 41).addBox(-2.0F, -0.5359F, -7.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);
        lower_jaw.texOffs(6, 14).addBox(-1.0F, 2.0F, -7.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);

        /*eyebrows = new ModelRenderer(this);
        eyebrows.setPos(-2.0F, -1.0F, -4.0F);
        head.addChild(eyebrows);
        setRotationAngle(eyebrows, 0.2618F, 0.0F, 0.0F);
        eyebrows.texOffs(54, 20).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        eyebrows.texOffs(30, 49).addBox(3.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);*/
    }

    private static void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void setupAnim(MagicalPredatorEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        this.lower_jaw.xRot = MathHelper.cos(entity.attackAnim * 100 * 0.183f) * 0.0575f;

        this.back_leg_left.zRot = (((MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F) + ((MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F)) / -2.0F;
        this.back_leg_right.zRot = (((MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F) + ((MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F)) / -2.0F;

        this.back_leg_right.xRot = -0.2618F + (MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.4F;
        this.right_front_leg.xRot = -0.2618F + (MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.4F;

        this.left_front_leg.xRot = -0.2618F + (MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.4F;
        this.back_leg_left.xRot = -0.2618F + (MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.4F;

        this.Neck_and_head.yRot = MathHelper.wrapDegrees((float) (netHeadYaw * (Math.PI / 180.0F)));
        this.head.xRot = 0.5236F + MathHelper.wrapDegrees((float) (headPitch * (Math.PI / 180.0F)));

        this.lower_jaw.xRot = MathHelper.cos(ageInTicks * 0.183f) * 0.0575f;

        setRotationAngle(star,
                (float) Math.sin(ageInTicks* 0.225F),
                (float) Math.cos(ageInTicks* 0.225F),
                (float) Math.sin(ageInTicks* 0.225F));
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Main.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}