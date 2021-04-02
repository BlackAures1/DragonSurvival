package by.jackraidenph.dragonsurvival.models;

import by.jackraidenph.dragonsurvival.Functions;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
@Deprecated
public class DragonModel2 extends EntityModel<Entity> {
    public final ModelRenderer LeftFrontLeg;
    public final ModelRenderer Forearm1;
    public final ModelRenderer Elbow1;
    private final ModelRenderer hand1;
    public final ModelRenderer RightFrontLeg;
    public final ModelRenderer Forearm2;
    public final ModelRenderer Elbow2;
    private final ModelRenderer hand2;
    public final ModelRenderer Leg3;
    public final ModelRenderer Forearm3;
    public final ModelRenderer Elbow3;
    private final ModelRenderer hand3;
    public final ModelRenderer Leg4;
    public final ModelRenderer Forearm4;
    public final ModelRenderer Elbow4;
    private final ModelRenderer hand4;
    public final ModelRenderer main;
    public final ModelRenderer main_body;
    private final ModelRenderer main_pelvis;
    private final ModelRenderer Tail1;
    private final ModelRenderer Tail_5;
    private final ModelRenderer Tail2;
    private final ModelRenderer Tail3;
    private final ModelRenderer Tail4;
    private final ModelRenderer Tail5;
    private final ModelRenderer Tail_0;
    public final ModelRenderer NeckandHead;
    public final ModelRenderer NeckandMain;
    public final ModelRenderer Neckand_3;
    public final ModelRenderer Neckand_2;
    public final ModelRenderer Neckand_1;
    public final ModelRenderer Head;
    private final ModelRenderer central_horn;
    private final ModelRenderer Horn_left;
    private final ModelRenderer bone25;
    private final ModelRenderer Horn_right;
    private final ModelRenderer bone27;
    private final ModelRenderer nose;
    private final ModelRenderer nasalhorn;
    private final ModelRenderer bone20;
    private final ModelRenderer teeth;
    private final ModelRenderer lower_jaw;
    private final ModelRenderer lower_jaw1;
    private final ModelRenderer teeth2;
    private final ModelRenderer muscles;
    public boolean firstPerson;

    public DragonModel2(boolean firstPerson) {
        this.firstPerson = firstPerson;
        textureWidth = 128;
        textureHeight = 128;

        LeftFrontLeg = new ModelRenderer(this);
        LeftFrontLeg.setRotationPoint(5.0F, 6.0F, -7.0F);
        LeftFrontLeg.setTextureOffset(91, 42).addBox(-3.75F, -2.0F, -2.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        LeftFrontLeg.setTextureOffset(22, 69).addBox(0.1F, -9.0F, -2.0F, 0.0F, 11.0F, 9.0F, 0.0F, false);

        Forearm1 = new ModelRenderer(this);
        Forearm1.setRotationPoint(-9.0F, 5.0F, 2.0F);
        LeftFrontLeg.addChild(Forearm1);
        setRotationAngle(Forearm1, 0.0873F, 0.0F, 0.0F);
        Forearm1.setTextureOffset(30, 97).addBox(5.79F, -0.3073F, -2.9429F, 3.0F, 6.0F, 3.0F, 0.0F, true);

        Elbow1 = new ModelRenderer(this);
        Elbow1.setRotationPoint(0.0F, 5.9962F, -1.0872F);
        Forearm1.addChild(Elbow1);
        setRotationAngle(Elbow1, 1.4835F, 0.0F, 0.0F);
        Elbow1.setTextureOffset(0, 104).addBox(6.3F, -2.2487F, -5.9903F, 2.0F, 3.0F, 7.0F, 0.0F, true);
        Elbow1.setTextureOffset(10, 41).addBox(7.0F, 0.4723F, -1.3366F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        hand1 = new ModelRenderer(this);
        hand1.setRotationPoint(0.0F, -0.5038F, -5.9385F);
        Elbow1.addChild(hand1);
        setRotationAngle(hand1, -1.5708F, 0.0F, 0.0F);
        hand1.setTextureOffset(105, 35).addBox(5.3F, -1.5F, -3.8F, 4.0F, 3.0F, 5.0F, -0.5F, true);
        hand1.setTextureOffset(78, 111).addBox(6.3F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand1.setTextureOffset(78, 111).addBox(8.3F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand1.setTextureOffset(78, 111).addBox(7.2F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);

        RightFrontLeg = new ModelRenderer(this);
        RightFrontLeg.setRotationPoint(-5.0F, 6.0F, -7.0F);
        RightFrontLeg.setTextureOffset(0, 89).addBox(-0.25F, -2.0F, -2.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        RightFrontLeg.setTextureOffset(72, 73).addBox(0.0F, -9.0F, -2.0F, 0.0F, 11.0F, 9.0F, 0.0F, false);

        Forearm2 = new ModelRenderer(this);
        Forearm2.setRotationPoint(1.0F, 5.0F, 2.0F);
        RightFrontLeg.addChild(Forearm2);
        setRotationAngle(Forearm2, 0.0873F, 0.0F, 0.0F);
        Forearm2.setTextureOffset(51, 95).addBox(-0.79F, -0.3073F, -2.9429F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        Elbow2 = new ModelRenderer(this);
        Elbow2.setRotationPoint(0.0F, 5.9962F, -1.0872F);
        Forearm2.addChild(Elbow2);
        setRotationAngle(Elbow2, 1.4835F, 0.0F, 0.0F);
        Elbow2.setTextureOffset(103, 57).addBox(-0.3F, -2.2487F, -5.9903F, 2.0F, 3.0F, 7.0F, 0.0F, false);
        Elbow2.setTextureOffset(6, 38).addBox(1.0F, 0.4723F, -1.3366F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        hand2 = new ModelRenderer(this);
        hand2.setRotationPoint(0.0F, -0.5038F, -5.9385F);
        Elbow2.addChild(hand2);
        setRotationAngle(hand2, -1.5708F, 0.0F, 0.0F);
        hand2.setTextureOffset(104, 92).addBox(-1.2F, -1.5F, -3.8F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        hand2.setTextureOffset(78, 111).addBox(-0.3F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand2.setTextureOffset(78, 111).addBox(0.8F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand2.setTextureOffset(78, 111).addBox(1.7F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);

        Leg3 = new ModelRenderer(this);
        Leg3.setRotationPoint(4.0F, 6.0F, 8.0F);
        Leg3.setTextureOffset(68, 96).addBox(-2.9F, -2.0F, -2.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        Leg3.setTextureOffset(88, 94).addBox(1.0F, -6.0F, -2.0F, 0.0F, 9.0F, 8.0F, 0.0F, false);

        Forearm3 = new ModelRenderer(this);
        Forearm3.setRotationPoint(-1.0F, 7.0F, 0.0F);
        Leg3.addChild(Forearm3);
        setRotationAngle(Forearm3, 0.5236F, 0.0F, 0.0F);
        Forearm3.setTextureOffset(88, 64).addBox(-1.5F, -1.5F, -1.866F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        Elbow3 = new ModelRenderer(this);
        Elbow3.setRotationPoint(0.0F, 4.6025F, -0.2755F);
        Forearm3.addChild(Elbow3);
        setRotationAngle(Elbow3, 0.9599F, 0.0F, 0.0F);
        Elbow3.setTextureOffset(56, 105).addBox(-1.0F, -1.3939F, -5.6084F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        Elbow3.setTextureOffset(10, 45).addBox(0.0F, -0.1341F, -1.1482F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        hand3 = new ModelRenderer(this);
        hand3.setRotationPoint(0.0F, -0.8029F, -5.7943F);
        Elbow3.addChild(hand3);
        setRotationAngle(hand3, -1.4835F, 0.0F, 0.0F);
        hand3.setTextureOffset(18, 106).addBox(-2.0F, -1.2544F, -3.438F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        hand3.setTextureOffset(78, 111).addBox(0.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand3.setTextureOffset(78, 111).addBox(-1.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand3.setTextureOffset(78, 111).addBox(1.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);

        Leg4 = new ModelRenderer(this);
        Leg4.setRotationPoint(-4.0F, 6.0F, 8.0F);
        Leg4.setTextureOffset(84, 87).addBox(-1.1F, -2.0F, -2.75F, 4.0F, 9.0F, 6.0F, 0.0F, true);
        Leg4.setTextureOffset(88, 94).addBox(-1.0F, -6.0F, -2.0F, 0.0F, 9.0F, 8.0F, 0.0F, true);

        Forearm4 = new ModelRenderer(this);
        Forearm4.setRotationPoint(1.0F, 7.0F, 0.0F);
        Leg4.addChild(Forearm4);
        setRotationAngle(Forearm4, 0.5236F, 0.0F, 0.0F);
        Forearm4.setTextureOffset(82, 15).addBox(-1.5F, -1.5F, -1.866F, 3.0F, 7.0F, 3.0F, 0.0F, true);

        Elbow4 = new ModelRenderer(this);
        Elbow4.setRotationPoint(0.0F, 4.6025F, -0.2755F);
        Forearm4.addChild(Elbow4);
        setRotationAngle(Elbow4, 0.9599F, 0.0F, 0.0F);
        Elbow4.setTextureOffset(88, 0).addBox(-1.0F, -1.3939F, -5.6084F, 2.0F, 2.0F, 6.0F, 0.0F, true);
        Elbow4.setTextureOffset(10, 48).addBox(0.0F, -0.1341F, -1.1482F, 0.0F, 3.0F, 3.0F, 0.0F, true);

        hand4 = new ModelRenderer(this);
        hand4.setRotationPoint(0.0F, -0.8029F, -5.7943F);
        Elbow4.addChild(hand4);
        setRotationAngle(hand4, -1.4835F, 0.0F, 0.0F);
        hand4.setTextureOffset(104, 104).addBox(-2.0F, -1.2544F, -3.438F, 4.0F, 3.0F, 5.0F, -0.5F, true);
        hand4.setTextureOffset(78, 111).addBox(1.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand4.setTextureOffset(78, 111).addBox(0.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand4.setTextureOffset(78, 111).addBox(-1.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);


        main_body = new ModelRenderer(this);
        main_body.setRotationPoint(0.0F, -17.0F, 1.0F);
        main.addChild(main_body);
        main_body.setTextureOffset(0, 0).addBox(-5.5F, -5.5F, -11.5F, 11.0F, 11.0F, 14.0F, -0.5F, false);
        main_body.setTextureOffset(0, 74).addBox(0.0F, 5.0F, -10.0F, 0.0F, 4.0F, 11.0F, 0.0F, false);
        main_body.setTextureOffset(46, 35).addBox(0.0F, -12.0F, -11.0F, 0.0F, 7.0F, 13.0F, 0.0F, false);

        main_pelvis = new ModelRenderer(this);
        main_pelvis.setRotationPoint(0.0F, -21.0F, 1.0F);
        main.addChild(main_pelvis);
        main_pelvis.setTextureOffset(27, 28).addBox(-4.5F, -1.4F, 0.5F, 9.0F, 10.0F, 10.0F, -0.5F, false);
        main_pelvis.setTextureOffset(90, 72).addBox(-0.02F, -7.9F, 2.0F, 0.0F, 7.0F, 8.0F, 0.0F, false);

        Tail1 = new ModelRenderer(this);
        Tail1.setRotationPoint(0.0F, 2.0F, 7.0F);
        main_pelvis.addChild(Tail1);


        Tail_5 = new ModelRenderer(this);
        Tail_5.setRotationPoint(0.0F, 0.0F, 2.0F);
        Tail1.addChild(Tail_5);
        Tail_5.setTextureOffset(26, 61).addBox(-0.01F, -6.8F, -1.0F, 0.0F, 4.0F, 13.0F, 0.0F, false);
        Tail_5.setTextureOffset(22, 48).addBox(-3.0F, -2.8F, 0.0F, 6.0F, 6.0F, 12.0F, 0.0F, false);

        Tail2 = new ModelRenderer(this);
        Tail2.setRotationPoint(0.0F, -1.9074F, 11.1464F);
        Tail_5.addChild(Tail2);
        Tail2.setTextureOffset(50, 0).addBox(-2.0F, -0.7926F, -2.0F, 4.0F, 4.0F, 13.0F, 0.0F, false);
        Tail2.setTextureOffset(15, 53).addBox(0.0F, -4.7926F, -2.0F, 0.0F, 4.0F, 13.0F, 0.0F, false);

        Tail3 = new ModelRenderer(this);
        Tail3.setRotationPoint(0.0F, 1.2957F, 9.604F);
        Tail2.addChild(Tail3);
        Tail3.setTextureOffset(26, 59).addBox(-0.01F, -5.9883F, -0.9397F, 0.0F, 4.0F, 11.0F, 0.0F, false);
        Tail3.setTextureOffset(74, 48).addBox(-1.0F, -1.9883F, -0.9397F, 2.0F, 3.0F, 11.0F, 0.0F, false);

        Tail4 = new ModelRenderer(this);
        Tail4.setRotationPoint(0.0F, -0.2817F, 9.4023F);
        Tail3.addChild(Tail4);
        Tail4.setTextureOffset(30, 79).addBox(-1.0F, -1.0603F, 0.658F, 2.0F, 2.0F, 10.0F, 0.0F, false);
        Tail4.setTextureOffset(86, 66).addBox(0.0F, -5.0603F, 0.658F, 0.0F, 4.0F, 10.0F, 0.0F, false);
        Tail4.setTextureOffset(84, 112).addBox(-5.0F, -0.0066F, 0.0F, 10.0F, 0.0F, 13.0F, 0.0F, false);
        Tail4.setTextureOffset(92, 114).addBox(0.0F, -0.1528F, 0.658F, 0.0F, 4.0F, 10.0F, 0.0F, false);

        Tail5 = new ModelRenderer(this);
        Tail5.setRotationPoint(0.0F, 0.0F, 10.0F);
        Tail4.addChild(Tail5);
        Tail5.setTextureOffset(101, 18).addBox(-1.0F, -0.7709F, 0.6473F, 2.0F, 1.0F, 8.0F, 0.0F, false);
        Tail5.setTextureOffset(0, 25).addBox(-5.75F, -0.1066F, 1.0622F, 12.0F, 0.0F, 13.0F, 0.0F, false);

        Tail_0 = new ModelRenderer(this);
        Tail_0.setRotationPoint(0.0F, -1.0F, -1.0F);
        Tail5.addChild(Tail_0);
        setRotationAngle(Tail_0, 0.0F, 0.0F, -1.5708F);
        Tail_0.setTextureOffset(23, 0).addBox(-6.6F, 0.0359F, 1.6473F, 12.0F, 0.0F, 13.0F, 0.0F, false);

        NeckandHead = new ModelRenderer(this);
        NeckandHead.setRotationPoint(0.0F, -19.0F, -9.0F);
        main.addChild(NeckandHead);
        setRotationAngle(NeckandHead, 2.0071F, 0.0F, 0.0F);

        NeckandMain = new ModelRenderer(this);
        NeckandMain.setRotationPoint(0.0F, -2.0F, 1.0F);
        NeckandHead.addChild(NeckandMain);
        setRotationAngle(NeckandMain, -0.4363F, 0.0F, 0.0F);
        NeckandMain.setTextureOffset(49, 59).addBox(-4.0F, -1.0F, -7.0F, 8.0F, 6.0F, 9.0F, 0.0F, false);
        NeckandMain.setTextureOffset(0, 110).addBox(-0.01F, -1.2616F, -11.0284F, 0.0F, 7.0F, 4.0F, 0.0F, false);

        Neckand_3 = new ModelRenderer(this);
        Neckand_3.setRotationPoint(0.0F, 0.7855F, -0.4912F);
        NeckandMain.addChild(Neckand_3);
        setRotationAngle(Neckand_3, -0.5236F, 0.0F, 0.0F);
        Neckand_3.setTextureOffset(44, 74).addBox(-3.0F, -5.2679F, -5.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
        Neckand_3.setTextureOffset(15, 56).addBox(0.0F, -5.2679F, -7.9282F, 0.0F, 7.0F, 3.0F, 0.0F, false);
        Neckand_3.setTextureOffset(21, 35).addBox(-0.01F, -4.9372F, 2.648F, 0.0F, 8.0F, 3.0F, 0.0F, false);

        Neckand_2 = new ModelRenderer(this);
        Neckand_2.setRotationPoint(0.0F, -3.0359F, 0.134F);
        Neckand_3.addChild(Neckand_2);
        setRotationAngle(Neckand_2, -0.5236F, 0.0F, 0.0F);
        Neckand_2.setTextureOffset(88, 27).addBox(-2.0F, -5.5046F, -4.7955F, 4.0F, 6.0F, 7.0F, 0.0F, false);
        Neckand_2.setTextureOffset(74, 58).addBox(-0.01F, -5.3026F, -8.4523F, 0.0F, 6.0F, 4.0F, 0.0F, false);
        Neckand_2.setTextureOffset(0, 61).addBox(0.0F, -6.5038F, 0.3909F, 0.0F, 4.0F, 4.0F, 0.0F, false);

        Neckand_1 = new ModelRenderer(this);
        Neckand_1.setRotationPoint(0.0F, -4.7975F, -0.0202F);
        Neckand_2.addChild(Neckand_1);
        setRotationAngle(Neckand_1, -0.1745F, 0.0F, 0.0F);
        Neckand_1.setTextureOffset(100, 81).addBox(-1.99F, -4.872F, -3.9389F, 4.0F, 5.0F, 6.0F, 0.0F, false);
        Neckand_1.setTextureOffset(72, 44).addBox(0.0F, -4.6562F, -7.4912F, 0.0F, 6.0F, 4.0F, 0.0F, false);
        Neckand_1.setTextureOffset(0, 55).addBox(-0.02F, -7.4997F, 1.523F, 0.0F, 6.0F, 4.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, -6.632F, -0.795F);
        Neckand_1.addChild(Head);
        setRotationAngle(Head, -0.3491F, 0.0F, 0.0F);
        Head.setTextureOffset(71, 0).addBox(-3.1F, -3.7602F, -3.7807F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        Head.setTextureOffset(65, 36).addBox(2.1F, -3.7602F, -3.7807F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        Head.setTextureOffset(13, 99).addBox(-0.03F, -7.3403F, -9.1667F, 0.0F, 9.0F, 15.0F, 0.0F, false);
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

        Horn_right = new ModelRenderer(this);
        Horn_right.setRotationPoint(0.0F, -2.1667F, 1.8181F);
        Head.addChild(Horn_right);
        setRotationAngle(Horn_right, 0.0F, -0.3491F, 0.0F);
        Horn_right.setTextureOffset(0, 0).addBox(-2.5F, -0.8264F, 0.9848F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(-1.9397F, -1.1585F, -0.4691F);
        Horn_right.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.3491F, 0.0F);
        bone27.setTextureOffset(62, 88).addBox(-2.0152F, 0.6265F, 4.6769F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        bone27.setTextureOffset(72, 54).addBox(-1.485F, 1.3588F, 7.3602F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        nose = new ModelRenderer(this);
        nose.setRotationPoint(0.0F, -3.1667F, -2.1819F);
        Head.addChild(nose);
        setRotationAngle(nose, 0.0873F, 0.0F, 0.0F);
        nose.setTextureOffset(71, 0).addBox(-2.0F, 0.118F, -8.7783F, 4.0F, 4.0F, 9.0F, 0.0F, false);
        nose.setTextureOffset(0, 41).addBox(-0.5F, -0.4196F, -8.2258F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        nasalhorn = new ModelRenderer(this);
        nasalhorn.setRotationPoint(0.0F, 0.0F, -9.0F);
        nose.addChild(nasalhorn);
        setRotationAngle(nasalhorn, -0.3491F, 0.0F, 0.0F);
        nasalhorn.setTextureOffset(43, 95).addBox(-0.5F, -0.5067F, -2.222F, 1.0F, 2.0F, 3.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.9659F, -0.7412F);
        nasalhorn.addChild(bone20);
        setRotationAngle(bone20, -0.3054F, 0.0F, 0.0F);
        bone20.setTextureOffset(12, 108).addBox(-0.5F, -0.9193F, -3.4045F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        teeth = new ModelRenderer(this);
        teeth.setRotationPoint(0.0F, 0.0F, 0.0F);
        nose.addChild(teeth);
        teeth.setTextureOffset(3, 11).addBox(-1.7F, 2.2944F, -7.7783F, 3.0F, 3.0F, 0.0F, 0.0F, false);
        teeth.setTextureOffset(0, 0).addBox(-1.5F, 2.2944F, -8.3455F, 0.0F, 3.0F, 7.0F, 0.0F, false);
        teeth.setTextureOffset(0, 0).addBox(1.5F, 2.2944F, -8.3455F, 0.0F, 3.0F, 7.0F, 0.0F, true);
        teeth.setTextureOffset(8, 36).addBox(-1.99F, 3.2944F, -8.3555F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        teeth.setTextureOffset(9, 33).addBox(0.99F, 3.2944F, -8.3555F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lower_jaw = new ModelRenderer(this);
        lower_jaw.setRotationPoint(0.0F, 2.1277F, -1.6274F);
        Head.addChild(lower_jaw);
        setRotationAngle(lower_jaw, 0.0F, 0.0F, 0.0F);


        lower_jaw1 = new ModelRenderer(this);
        lower_jaw1.setRotationPoint(0.0F, -2.1005F, -0.2829F);
        lower_jaw.addChild(lower_jaw1);
        setRotationAngle(lower_jaw1, 0.0873F, 0.0F, 0.0F);
        lower_jaw1.setTextureOffset(103, 27).addBox(-1.0F, 0.6019F, -6.4128F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        lower_jaw1.setTextureOffset(37, 13).addBox(-0.1F, 3.0038F, -7.9128F, 0.0F, 3.0F, 12.0F, 0.0F, false);
        lower_jaw1.setTextureOffset(55, 17).addBox(-2.0F, 1.271F, -8.6595F, 4.0F, 2.0F, 13.0F, 0.0F, false);

        teeth2 = new ModelRenderer(this);
        teeth2.setRotationPoint(0.0F, -4.9924F, 0.1743F);
        lower_jaw1.addChild(teeth2);
        teeth2.setTextureOffset(0, 7).addBox(1.6F, 5.2906F, -8.4326F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        teeth2.setTextureOffset(8, 3).addBox(-1.8F, 5.2906F, -7.9371F, 3.0F, 1.0F, 0.0F, 0.0F, false);
        teeth2.setTextureOffset(0, 0).addBox(-1.6F, 5.2906F, -8.6371F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        teeth2.setTextureOffset(8, 39).addBox(-1.9F, 5.2906F, -8.5326F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        teeth2.setTextureOffset(0, 0).addBox(0.88F, 5.2906F, -8.5326F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        muscles = new ModelRenderer(this);
        muscles.setRotationPoint(0.0F, -1.291F, 19.0368F);
        lower_jaw1.addChild(muscles);
        setRotationAngle(muscles, -0.2182F, 0.0F, 0.0F);
        muscles.setTextureOffset(0, 25).addBox(0.91F, 3.3305F, -21.1542F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        muscles.setTextureOffset(0, 40).addBox(-1.91F, 3.3305F, -21.1542F, 1.0F, 5.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.onGround || entity.isSwimming()) {
            LeftFrontLeg.rotateAngleX = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount) / 2;

            RightFrontLeg.rotateAngleX = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount) / 2;

            Leg3.rotateAngleX = Functions.getDefaultXLeftLimbRotation(limbSwing, limbSwingAmount) / 2;

            Leg4.rotateAngleX = Functions.getDefaultXRightLimbRotation(limbSwing, limbSwingAmount) / 2;

            float speed = ageInTicks / 20;
            //start
            Tail1.rotateAngleX = MathHelper.cos(speed) / 6;
            Tail2.rotateAngleX = MathHelper.sin(speed) / 12;
            Tail3.rotateAngleX = MathHelper.cos(speed) / 12;
            Tail4.rotateAngleX = MathHelper.sin(speed) / 12;
            Tail5.rotateAngleX = MathHelper.cos(speed) / 12;

            lower_jaw.rotateAngleX = MathHelper.sin(speed * 1.5f) / 12 + Functions.degreesToRadians(5);
        }
        if(entity.getMotion().x!=0 || entity.getMotion().z!=0)
            Neckand_3.rotateAngleZ=0;
//        else
//            Neckand_3.rotateAngleZ = -(float) ((netHeadYaw) * Math.PI / 180.0F);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.push();
        matrixStack.translate(0, 1.5, 0);
        matrixStack.rotate(new Quaternion(180, 0, 0, true));
        LeftFrontLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        RightFrontLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        Leg3.render(matrixStack, buffer, packedLight, packedOverlay);
        Leg4.render(matrixStack, buffer, packedLight, packedOverlay);
        main.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.pop();
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}