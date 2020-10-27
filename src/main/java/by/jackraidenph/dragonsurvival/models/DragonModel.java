package by.jackraidenph.dragonsurvival.models;
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DragonModel<T extends Entity> extends EntityModel<T> {
    private final ModelRenderer main;
    private final ModelRenderer leg_fl;
    private final ModelRenderer bone6;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer leg_fr;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone7;
    private final ModelRenderer leg_bl;
    private final ModelRenderer bone8;
    private final ModelRenderer bone5;
    private final ModelRenderer bone4;
    private final ModelRenderer leg_br;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;
    private final ModelRenderer main_body;
    private final ModelRenderer main_pelvis;
    private final ModelRenderer Tail;
    private final ModelRenderer bone;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone16;
    private final ModelRenderer bone14;
    private final ModelRenderer neck_and_head;
    private final ModelRenderer NeckandMain;
    private final ModelRenderer NeckandFOne;
    private final ModelRenderer NeckandTwo;
    private final ModelRenderer NeckandThree;
    private final ModelRenderer Head;
    private final ModelRenderer central_horn;
    private final ModelRenderer Horn_left;
    private final ModelRenderer bone25;
    private final ModelRenderer Hprn_right;
    private final ModelRenderer bone27;
    private final ModelRenderer nose;
    private final ModelRenderer teeth;
    private final ModelRenderer lower_jaw;
    private final ModelRenderer bone18;
    private final ModelRenderer muscles;
    private final ModelRenderer teeth2;
    List<ModelRenderer> tail;
    List<ModelRenderer> NeckHead;
    float partialTicks = 0.0F;
    PlayerEntity player;
    float[] offsets;
    double fl;
    double bl;
    double fr;
    double br;

    public DragonModel() {
        textureWidth = 128;
        textureHeight = 128;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);


        leg_fl = new ModelRenderer(this);
        leg_fl.setRotationPoint(5.0F, -19.0F, -5.0F);
        main.addChild(leg_fl);
        leg_fl.setTextureOffset(91, 42).addBox(-3.75F, -1.0F, -3.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        leg_fl.setTextureOffset(22, 69).addBox(0.1F, -8.0F, -3.0F, 0.0F, 11.0F, 9.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(-1.0F, 6.0F, 0.0F);
        leg_fl.addChild(bone6);
        setRotationAngle(bone6, 0.5236F, 0.0F, 0.0F);
        bone6.setTextureOffset(20, 91).addBox(-2.0F, -1.7193F, -3.2182F, 4.0F, 9.0F, 6.0F, -1.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 5.9962F, -1.0872F);
        bone6.addChild(bone9);
        setRotationAngle(bone9, 1.0472F, 0.0F, 0.0F);
        bone9.setTextureOffset(0, 104).addBox(-0.9F, -1.2487F, -5.9903F, 2.0F, 3.0F, 7.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, -0.5038F, -5.9385F);
        bone9.addChild(bone10);
        setRotationAngle(bone10, -1.5708F, 0.0F, 0.0F);
        bone10.setTextureOffset(105, 35).addBox(-2.0F, -1.5F, -2.8F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        bone10.setTextureOffset(55, 23).addBox(-1.0F, -1.0F, -4.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        bone10.setTextureOffset(0, 44).addBox(0.0F, -1.0F, -4.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        bone10.setTextureOffset(21, 43).addBox(1.0F, -1.0F, -4.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);

        leg_fr = new ModelRenderer(this);
        leg_fr.setRotationPoint(-5.0F, -19.0F, -5.0F);
        main.addChild(leg_fr);
        leg_fr.setTextureOffset(0, 89).addBox(-0.25F, -1.0F, -3.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        leg_fr.setTextureOffset(72, 73).addBox(0.0F, -8.0F, -3.0F, 0.0F, 11.0F, 9.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(1.0F, 6.0F, 0.0F);
        leg_fr.addChild(bone2);
        setRotationAngle(bone2, 0.5236F, 0.0F, 0.0F);
        bone2.setTextureOffset(48, 88).addBox(-2.0F, -1.7193F, -3.2182F, 4.0F, 9.0F, 6.0F, -1.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 5.9962F, -1.0872F);
        bone2.addChild(bone3);
        setRotationAngle(bone3, 1.0472F, 0.0F, 0.0F);
        bone3.setTextureOffset(103, 57).addBox(-1.1F, -1.2487F, -5.9903F, 2.0F, 3.0F, 7.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, -0.5038F, -5.9385F);
        bone3.addChild(bone7);
        setRotationAngle(bone7, -1.5708F, 0.0F, 0.0F);
        bone7.setTextureOffset(104, 92).addBox(-2.0F, -1.5F, -2.8F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        bone7.setTextureOffset(0, 25).addBox(-1.0F, -1.0F, -4.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        bone7.setTextureOffset(0, 23).addBox(0.0F, -1.0F, -4.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        bone7.setTextureOffset(0, 0).addBox(1.0F, -1.0F, -4.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        leg_bl = new ModelRenderer(this);
        leg_bl.setRotationPoint(5.0F, -18.0F, 10.0F);
        main.addChild(leg_bl);
        leg_bl.setTextureOffset(68, 96).addBox(-3.9F, -2.0F, -3.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        leg_bl.setTextureOffset(88, 94).addBox(0.0F, -6.0F, -3.0F, 0.0F, 9.0F, 8.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(-2.0F, 7.0F, -1.0F);
        leg_bl.addChild(bone8);
        setRotationAngle(bone8, 0.5236F, 0.0F, 0.0F);
        bone8.setTextureOffset(86, 62).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 9.0F, 5.0F, -1.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 4.1025F, -0.1415F);
        bone8.addChild(bone5);
        setRotationAngle(bone5, 0.9599F, 0.0F, 0.0F);
        bone5.setTextureOffset(56, 105).addBox(-1.0F, -1.2169F, -6.0948F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        bone5.setTextureOffset(6, 38).addBox(0.0F, 0.043F, -1.6346F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, -0.622F, -6.3679F);
        bone5.addChild(bone4);
        setRotationAngle(bone4, -1.4835F, 0.0F, 0.0F);
        bone4.setTextureOffset(18, 106).addBox(-2.0F, -1.3415F, -3.4342F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        bone4.setTextureOffset(41, 63).addBox(-1.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        bone4.setTextureOffset(61, 22).addBox(0.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        bone4.setTextureOffset(21, 43).addBox(1.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        leg_br = new ModelRenderer(this);
        leg_br.setRotationPoint(-5.0F, -18.0F, 10.0F);
        main.addChild(leg_br);
        leg_br.setTextureOffset(84, 87).addBox(-0.1F, -2.0F, -3.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        leg_br.setTextureOffset(40, 95).addBox(0.0F, -6.0F, -3.0F, 0.0F, 9.0F, 8.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(2.0F, 7.0F, -1.0F);
        leg_br.addChild(bone19);
        setRotationAngle(bone19, 0.5236F, 0.0F, 0.0F);
        bone19.setTextureOffset(79, 13).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 9.0F, 5.0F, -1.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 4.1025F, -0.1415F);
        bone19.addChild(bone20);
        setRotationAngle(bone20, 0.9599F, 0.0F, 0.0F);
        bone20.setTextureOffset(88, 0).addBox(-1.0F, -1.2169F, -6.0948F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        bone20.setTextureOffset(6, 22).addBox(0.0F, 0.043F, -1.6346F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, -0.622F, -6.3679F);
        bone20.addChild(bone21);
        setRotationAngle(bone21, -1.4835F, 0.0F, 0.0F);
        bone21.setTextureOffset(104, 104).addBox(-2.0F, -1.3415F, -3.4342F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        bone21.setTextureOffset(63, 31).addBox(-1.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        bone21.setTextureOffset(63, 29).addBox(0.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        bone21.setTextureOffset(47, 63).addBox(1.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        main_body = new ModelRenderer(this);
        main_body.setRotationPoint(0.0F, -17.0F, 1.0F);
        main.addChild(main_body);
        main_body.setTextureOffset(0, 0).addBox(-5.5F, -5.5F, -11.5F, 11.0F, 11.0F, 14.0F, -0.5F, false);
        main_body.setTextureOffset(0, 38).addBox(-2.0F, -7.0F, -11.01F, 4.0F, 8.0F, 13.0F, 0.0F, false);
        main_body.setTextureOffset(0, 59).addBox(-1.0F, -8.0F, -11.0F, 2.0F, 8.0F, 11.0F, 0.0F, false);
        main_body.setTextureOffset(0, 74).addBox(0.0F, 5.0F, -10.0F, 0.0F, 4.0F, 11.0F, 0.0F, false);
        main_body.setTextureOffset(46, 35).addBox(0.0F, -8.0F, -11.0F, 0.0F, 11.0F, 13.0F, 0.0F, false);

        main_pelvis = new ModelRenderer(this);
        main_pelvis.setRotationPoint(0.0F, -21.0F, 1.0F);
        main.addChild(main_pelvis);
        main_pelvis.setTextureOffset(27, 28).addBox(-4.5F, -1.4F, 0.5F, 9.0F, 10.0F, 10.0F, -0.5F, false);
        main_pelvis.setTextureOffset(96, 5).addBox(-1.0F, -3.0F, 2.0F, 2.0F, 5.0F, 8.0F, 0.0F, false);
        main_pelvis.setTextureOffset(90, 72).addBox(-0.02F, -5.0F, 2.0F, 0.0F, 7.0F, 8.0F, 0.0F, false);

        Tail = new ModelRenderer(this);
        Tail.setRotationPoint(0.0F, 2.0F, 7.0F);
        main_pelvis.addChild(Tail);


        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 2.0F);
        Tail.addChild(bone);
        bone.setTextureOffset(26, 61).addBox(0.0F, -7.0F, -1.0F, 0.0F, 4.0F, 13.0F, 0.0F, false);
        bone.setTextureOffset(22, 48).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 12.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, -1.9074F, 11.1464F);
        bone.addChild(bone11);
        bone11.setTextureOffset(50, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 4.0F, 13.0F, 0.0F, false);
        bone11.setTextureOffset(15, 53).addBox(0.0F, -5.0F, -2.0F, 0.0F, 4.0F, 13.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 1.2957F, 9.604F);
        bone11.addChild(bone12);
        bone12.setTextureOffset(26, 59).addBox(0.0F, -6.342F, -0.9397F, 0.0F, 4.0F, 11.0F, 0.0F, false);
        bone12.setTextureOffset(74, 48).addBox(-1.0F, -2.342F, -0.9397F, 2.0F, 3.0F, 11.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, -0.2817F, 9.4023F);
        bone12.addChild(bone13);
        bone13.setTextureOffset(30, 79).addBox(-1.0F, -2.0603F, -0.342F, 2.0F, 2.0F, 10.0F, 0.0F, false);
        bone13.setTextureOffset(86, 66).addBox(0.0F, -6.0603F, -0.342F, 0.0F, 4.0F, 10.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, -1.0F, 9.0F);
        bone13.addChild(bone16);
        bone16.setTextureOffset(101, 18).addBox(-1.0F, -0.7709F, 0.2067F, 2.0F, 1.0F, 8.0F, 0.0F, false);
        bone16.setTextureOffset(0, 25).addBox(-6.0F, -0.1066F, 1.0622F, 12.0F, 0.0F, 13.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone16.addChild(bone14);
        setRotationAngle(bone14, 0.0F, 0.0F, -1.5708F);
        bone14.setTextureOffset(23, 0).addBox(-5.0F, 0.0359F, 1.0622F, 12.0F, 0.0F, 13.0F, 0.0F, false);

        neck_and_head = new ModelRenderer(this);
        neck_and_head.setRotationPoint(0.0F, -19.0F, -9.0F);
        main.addChild(neck_and_head);
        setRotationAngle(neck_and_head, 2.0071F, 0.0F, 0.0F);


        NeckandMain = new ModelRenderer(this);
        NeckandMain.setRotationPoint(0.0F, 0.0F, -2.0F);
        neck_and_head.addChild(NeckandMain);
        setRotationAngle(NeckandMain, -0.4363F, 0.0F, 0.0F);
        NeckandMain.setTextureOffset(49, 59).addBox(-4.0F, -4.0805F, -5.1263F, 8.0F, 6.0F, 9.0F, 0.0F, false);
        NeckandMain.setTextureOffset(0, 30).addBox(-0.01F, -4.342F, -8.1547F, 0.0F, 7.0F, 4.0F, 0.0F, false);

        NeckandFOne = new ModelRenderer(this);
        NeckandFOne.setRotationPoint(0.0F, -3.295F, 0.3825F);
        NeckandMain.addChild(NeckandFOne);
        setRotationAngle(NeckandFOne, -0.5236F, 0.0F, 0.0F);
        NeckandFOne.setTextureOffset(44, 74).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
        NeckandFOne.setTextureOffset(15, 56).addBox(0.0F, -4.0F, -7.9282F, 0.0F, 7.0F, 3.0F, 0.0F, false);
        NeckandFOne.setTextureOffset(21, 35).addBox(0.0F, -3.6693F, 2.648F, 0.0F, 8.0F, 3.0F, 0.0F, false);

        NeckandTwo = new ModelRenderer(this);
        NeckandTwo.setRotationPoint(0.0F, -3.7679F, -0.866F);
        NeckandFOne.addChild(NeckandTwo);
        setRotationAngle(NeckandTwo, -0.5236F, 0.0F, 0.0F);
        NeckandTwo.setTextureOffset(88, 27).addBox(-2.0F, -4.2726F, -2.9294F, 4.0F, 6.0F, 7.0F, 0.0F, false);
        NeckandTwo.setTextureOffset(74, 58).addBox(-0.01F, -4.0706F, -6.5863F, 0.0F, 6.0F, 4.0F, 0.0F, false);
        NeckandTwo.setTextureOffset(0, 61).addBox(-0.01F, -3.9057F, 2.6229F, 0.0F, 4.0F, 4.0F, 0.0F, false);

        NeckandThree = new ModelRenderer(this);
        NeckandThree.setRotationPoint(0.0F, -3.5655F, 0.8458F);
        NeckandTwo.addChild(NeckandThree);
        setRotationAngle(NeckandThree, -0.2618F, 0.0F, 0.0F);
        NeckandThree.setTextureOffset(100, 81).addBox(-1.99F, -5.0457F, -2.9541F, 4.0F, 5.0F, 6.0F, 0.0F, false);
        NeckandThree.setTextureOffset(72, 44).addBox(0.0F, -4.8298F, -6.5064F, 0.0F, 6.0F, 4.0F, 0.0F, false);
        NeckandThree.setTextureOffset(0, 55).addBox(0.0F, -5.7939F, 1.8238F, 0.0F, 6.0F, 4.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, -7.4033F, 1.4716F);
        NeckandThree.addChild(Head);
        Head.setTextureOffset(71, 0).addBox(-3.1F, -3.7602F, -3.7807F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        Head.setTextureOffset(65, 36).addBox(2.1F, -3.7602F, -3.7807F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        Head.setTextureOffset(50, 9).addBox(0.0F, -6.3403F, -5.1667F, 0.0F, 8.0F, 8.0F, 0.0F, false);
        Head.setTextureOffset(65, 32).addBox(-3.5F, -3.6667F, -5.6819F, 7.0F, 7.0F, 9.0F, -0.5F, false);
        Head.setTextureOffset(0, 67).addBox(2.9F, -0.6983F, -4.5513F, 0.0F, 7.0F, 11.0F, 0.0F, false);
        Head.setTextureOffset(64, 64).addBox(-2.9F, -0.6983F, -4.5513F, 0.0F, 7.0F, 11.0F, 0.0F, false);
        Head.setTextureOffset(108, 0).addBox(1.01F, -3.4167F, -4.4319F, 3.0F, 4.0F, 4.0F, -1.0F, false);
        Head.setTextureOffset(106, 72).addBox(-4.01F, -3.4167F, -4.4319F, 3.0F, 4.0F, 4.0F, -1.0F, false);

        central_horn = new ModelRenderer(this);
        central_horn.setRotationPoint(0.0F, -0.6667F, -1.1819F);
        Head.addChild(central_horn);
        setRotationAngle(central_horn, 0.0873F, 0.0F, 0.0F);
        central_horn.setTextureOffset(98, 0).addBox(-0.5F, -2.7309F, 3.1629F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        Horn_left = new ModelRenderer(this);
        Horn_left.setRotationPoint(0.0F, -2.1667F, 1.8181F);
        Head.addChild(Horn_left);
        setRotationAngle(Horn_left, 0.0F, 0.3491F, 0.0F);
        Horn_left.setTextureOffset(55, 32).addBox(0.5F, -0.8264F, 0.9848F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(1.9397F, -1.1585F, -0.4691F);
        Horn_left.addChild(bone25);
        setRotationAngle(bone25, 0.0F, -0.3491F, 0.0F);
        bone25.setTextureOffset(34, 91).addBox(0.0152F, 0.6265F, 4.6769F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        bone25.setTextureOffset(14, 89).addBox(0.485F, 1.3588F, 7.3602F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        Hprn_right = new ModelRenderer(this);
        Hprn_right.setRotationPoint(0.0F, -2.1667F, 1.8181F);
        Head.addChild(Hprn_right);
        setRotationAngle(Hprn_right, 0.0F, -0.3491F, 0.0F);
        Hprn_right.setTextureOffset(0, 0).addBox(-2.5F, -0.8264F, 0.9848F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(-1.9397F, -1.1585F, -0.4691F);
        Hprn_right.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.3491F, 0.0F);
        bone27.setTextureOffset(62, 88).addBox(-2.0152F, 0.6265F, 4.6769F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        bone27.setTextureOffset(72, 54).addBox(-1.485F, 1.3588F, 7.3602F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        nose = new ModelRenderer(this);
        nose.setRotationPoint(0.0F, -3.1667F, -4.1819F);
        Head.addChild(nose);
        nose.setTextureOffset(71, 0).addBox(-2.0F, 0.118F, -8.7783F, 4.0F, 4.0F, 9.0F, 0.0F, false);
        nose.setTextureOffset(0, 41).addBox(-0.5F, -0.4196F, -8.2258F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        teeth = new ModelRenderer(this);
        teeth.setRotationPoint(0.0F, 0.0F, 0.0F);
        nose.addChild(teeth);
        teeth.setTextureOffset(0, 3).addBox(1.5F, 2.2944F, -8.3455F, 0.0F, 3.0F, 6.0F, 0.0F, false);
        teeth.setTextureOffset(8, 0).addBox(-1.7F, 2.2944F, -7.7783F, 3.0F, 3.0F, 0.0F, 0.0F, false);
        teeth.setTextureOffset(0, 0).addBox(-1.5F, 2.2944F, -8.3455F, 0.0F, 3.0F, 7.0F, 0.0F, false);
        teeth.setTextureOffset(8, 36).addBox(-1.99F, 3.2944F, -8.3555F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        teeth.setTextureOffset(9, 33).addBox(0.99F, 3.2944F, -8.3555F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lower_jaw = new ModelRenderer(this);
        lower_jaw.setRotationPoint(0.0F, 3.1277F, -2.6274F);
        Head.addChild(lower_jaw);
        setRotationAngle(lower_jaw, 0.3491F, 0.0F, 0.0F);


        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, -3.1005F, -1.2829F);
        lower_jaw.addChild(bone18);
        bone18.setTextureOffset(103, 27).addBox(-1.0F, 1.5981F, -6.5F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        bone18.setTextureOffset(37, 13).addBox(-0.1F, 4.0F, -8.0F, 0.0F, 3.0F, 12.0F, 0.0F, false);
        bone18.setTextureOffset(55, 17).addBox(-2.0F, 2.2672F, -8.7467F, 4.0F, 2.0F, 13.0F, 0.0F, false);

        muscles = new ModelRenderer(this);
        muscles.setRotationPoint(0.0F, -1.9128F, 0.9962F);
        bone18.addChild(muscles);
        muscles.setTextureOffset(0, 25).addBox(0.91F, -0.532F, -2.4607F, 1.0F, 6.0F, 4.0F, 0.0F, false);
        muscles.setTextureOffset(0, 41).addBox(-1.91F, 0.468F, -2.4607F, 1.0F, 5.0F, 4.0F, 0.0F, false);

        teeth2 = new ModelRenderer(this);
        teeth2.setRotationPoint(0.0F, -1.0872F, -0.9962F);
        muscles.addChild(teeth2);
        teeth2.setTextureOffset(0, 7).addBox(1.6F, 4.468F, -7.3607F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        teeth2.setTextureOffset(8, 3).addBox(-1.8F, 4.2944F, -7.8499F, 3.0F, 1.0F, 0.0F, 0.0F, false);
        teeth2.setTextureOffset(0, 0).addBox(-1.6F, 4.468F, -7.5651F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        teeth2.setTextureOffset(8, 39).addBox(-2.0F, 4.2944F, -8.4455F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        teeth2.setTextureOffset(0, 0).addBox(0.88F, 4.2944F, -8.4455F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        tail = new ArrayList<>(Arrays.asList(bone, bone11, bone12, bone13, bone14));
        NeckHead = new ArrayList<>(Arrays.asList(NeckandMain, NeckandFOne, NeckandTwo, Head));
        offsets = new float[]{-0.4363F, -0.2618F, -0.0873F, -1.3963F};
    }

    public static BlockPos rotatePosXDouble(double x, double z, double angle) {
        double angleDeg = Math.toRadians(angle);
        double[] coords = new double[2];
        coords[0] = x * Math.cos(angleDeg) - z * Math.sin(angleDeg);
        coords[1] = x * Math.sin(angleDeg) + z * Math.cos(angleDeg);
        return new BlockPos(coords[0], 0, coords[1]);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        player = (PlayerEntity) entity;

        //this.main_pelvis.rotateAngleY = MathHelper.lerp(this.partialTicks, ((PlayerEntity) entity).prevRenderYawOffset, ((PlayerEntity) entity).renderYawOffset);
        //this.main_body.rotateAngleY = MathHelper.lerp(this.partialTicks, ((PlayerEntity) entity).prevRenderYawOffset, ((PlayerEntity) entity).renderYawOffset);
        //this.main.rotateAngleY = (float) (MathHelper.lerp(this.partialTicks, ((PlayerEntity) entity).prevRenderYawOffset, ((PlayerEntity) entity).renderYawOffset) * (Math.PI / 180.0F));

        this.tail.get(entity.world.rand.nextInt(tail.size())).rotateAngleX = MathHelper.cos(ageInTicks * 0.183f) * 0.0575f;
        this.lower_jaw.rotateAngleX = MathHelper.cos(ageInTicks * 0.183f) * 0.0575f;
        this.main_body.rotateAngleX = Math.abs(MathHelper.cos(ageInTicks * 0.083f)) * -0.0775f;
        this.neck_and_head.rotateAngleX = MathHelper.cos(ageInTicks * 0.083f) * -0.0575f + 1.8326f;
        this.leg_fl.rotateAngleX = -this.main_body.rotateAngleX;
        this.leg_fr.rotateAngleX = -this.main_body.rotateAngleX;

        /*this.NeckandMain.rotateAngleY = (float) (MathHelper.wrapDegrees(netHeadYaw) * (Math.PI / 180.0F)) - this.main.rotateAngleY;
        this.NeckandFOne.rotateAngleY = (float) (MathHelper.wrapDegrees(netHeadYaw) * (Math.PI / 180.0F)) - this.main.rotateAngleY;
        this.NeckandTwo.rotateAngleY = (float) (MathHelper.wrapDegrees(netHeadYaw) * (Math.PI / 180.0F)) - this.main.rotateAngleY;*/
        //this.NeckandThree.rotateAngleY = (float) (MathHelper.wrapDegrees(netHeadYaw) * (Math.PI / 180.0F)) - this.main.rotateAngleY;

        this.main_body.rotateAngleZ = (((MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F) + ((MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F)) / -5.0F;
        this.main_pelvis.rotateAngleZ = (((MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F) + ((MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F)) / 2.0F;

        //this.Leg1.rotateAngleZ = (((MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F) + ((MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F)) / 2.0F;
        //this.Leg2.rotateAngleZ = (((MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F) + ((MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F)) / 2.0F;
        this.leg_bl.rotateAngleZ = (((MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F) + ((MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F)) / -2.0F;
        this.leg_br.rotateAngleZ = (((MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F) + ((MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F)) / -2.0F;

        this.leg_br.rotateAngleX = (MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.2F;
        this.leg_fr.rotateAngleX = (MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F;

        this.leg_fl.rotateAngleX = (MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.2F;
        this.leg_bl.rotateAngleX = (MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F;

        this.bone9.rotateAngleX = -(MathHelper.sin(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F + 1.0472F;
        this.bone3.rotateAngleX = -(MathHelper.sin((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F + 1.0472F;
        this.bone20.rotateAngleX = (MathHelper.sin((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount) * 0.2F + 0.9599F;
        this.bone5.rotateAngleX = (MathHelper.sin(limbSwing * 0.6662F) * 1.4F * limbSwingAmount) * 0.33F + 0.9599F;

        this.bone7.rotateAngleX = MathHelper.clamp(this.bone7.rotateAngleX, -2.0F, -1.5708F);
        this.bone4.rotateAngleX = MathHelper.clamp(this.bone4.rotateAngleX, -2.0F, -1.5708F);
        this.bone10.rotateAngleX = MathHelper.clamp(this.bone10.rotateAngleX, -2.0F, -1.5708F);
        this.bone21.rotateAngleX = MathHelper.clamp(this.bone21.rotateAngleX, -2.0F, -1.5708F);

        this.main.rotateAngleZ = (float) (MathHelper.lerp(this.partialTicks, ((PlayerEntity) entity).prevRenderYawOffset, ((PlayerEntity) entity).renderYawOffset) * (Math.PI / 180.0F));
        this.neck_and_head.rotateAngleY = (float) (netHeadYaw * (Math.PI / 180.0F));
    }

    public int getHeightAtPos(World worldIn, double x, double y, double z) {
        int i = (int) y;
        while (!worldIn.getBlockState(new BlockPos(x, i, z)).isSolid())
            --i;
        return i + 1;
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float partialTicks, float headYaw, float headPitch, float scale) {

        fl = MathHelper.clamp(this.player.getPosY() - getHeightAtPos(this.player.world, this.player.getPosX() + 0.25, this.player.getPosY(), this.player.getPosZ() + 0.4375D), 0.0D, 1.0D);
        bl = MathHelper.clamp(this.player.getPosY() - getHeightAtPos(this.player.world, this.player.getPosX() + 0.25D, this.player.getPosY(), this.player.getPosZ() - 0.375D), 0.0D, 1.0D);
        fr = MathHelper.clamp(this.player.getPosY() - getHeightAtPos(this.player.world, this.player.getPosX() - 0.5D, this.player.getPosY(), this.player.getPosZ() + 0.4375D), 0.0D, 1.0D);
        br = MathHelper.clamp(this.player.getPosY() - getHeightAtPos(this.player.world, this.player.getPosX() - 0.5D, this.player.getPosY(), this.player.getPosZ() - 0.375D), 0.0D, 1.0D);
        this.partialTicks = partialTicks;

        matrixStack.push();
        Minecraft.getInstance().getItemRenderer().renderItem(this.player.getHeldItemMainhand(), ItemCameraTransforms.TransformType.GROUND, packedLight, packedOverlay, matrixStack, IRenderTypeBuffer.getImpl(new BufferBuilder(256)));
        matrixStack.pop();

        matrixStack.push();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        matrixStack.scale(scale, scale, scale);
        //matrixStack.translate(0F, 1.5F, 0F);
        matrixStack.rotate(new Quaternion(180f, 0f, 0f, true));
        double max = 0.0F;
        if (Math.max(fl, fr) != Math.max(bl, br))
            max = Math.max(fl, fr) > Math.max(bl, br) ? Math.max(fl, fr) : Math.max(bl, br) * -1;
        matrixStack.rotate(new Quaternion((float) (max * 30f), 0f, 0f, true));
        matrixStack.translate(0, Math.abs(max) * 0.35f, max * 0.25f);
        main_body.render(matrixStack, buffer, packedLight, packedOverlay);
        main_pelvis.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.translate(0, -Math.abs(max) * 0.35f, -max * 0.25f);
        matrixStack.rotate(new Quaternion((float) -(max * 30f), 0f, 0f, true));

        matrixStack.translate(0, MathHelper.clamp(max, .0, 1.0) * 0.35f, 0);
        neck_and_head.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.translate(0, -MathHelper.clamp(max, .0, 1.0) * 0.35f, 0);

        if (Math.max(fl, fr) != Math.max(bl, br)) {
            matrixStack.translate(0, Math.max(fl, fr), 0);
            leg_fl.render(matrixStack, buffer, packedLight, packedOverlay);
            leg_fr.render(matrixStack, buffer, packedLight, packedOverlay);
            matrixStack.translate(0, -Math.max(fl, fr), 0);

            matrixStack.translate(0, Math.max(bl, br), 0);
            leg_bl.render(matrixStack, buffer, packedLight, packedOverlay);
            leg_br.render(matrixStack, buffer, packedLight, packedOverlay);
            matrixStack.translate(0, -Math.max(bl, br), 0);
        } else {
            leg_fl.render(matrixStack, buffer, packedLight, packedOverlay);
            leg_fr.render(matrixStack, buffer, packedLight, packedOverlay);
            leg_bl.render(matrixStack, buffer, packedLight, packedOverlay);
            leg_br.render(matrixStack, buffer, packedLight, packedOverlay);
        }

        matrixStack.pop();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}