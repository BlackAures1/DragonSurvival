package by.jackraidenph.dragonsurvival.models;// Made with Blockbench 3.5.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class DragonModel<T extends Entity> extends EntityModel<T> {
    private final ModelRenderer main;
    private final ModelRenderer main_body;
    private final ModelRenderer maim_pelvis;
    private final ModelRenderer Tail;
    private final ModelRenderer bone;
    private final ModelRenderer bone11;
    private final ModelRenderer bone12;
    private final ModelRenderer bone13;
    private final ModelRenderer bone16;
    private final ModelRenderer bone14;
    private final ModelRenderer NeckandHead;
    private final ModelRenderer Neckand;
    private final ModelRenderer Neckand4;
    private final ModelRenderer Neckand8;
    private final ModelRenderer Neckand12;
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
    private final ModelRenderer Legs_all;
    private final ModelRenderer Leg1;
    private final ModelRenderer bone6;
    private final ModelRenderer bone9;
    private final ModelRenderer bone10;
    private final ModelRenderer Leg2;
    private final ModelRenderer bone2;
    private final ModelRenderer bone3;
    private final ModelRenderer bone7;
    private final ModelRenderer Leg3;
    private final ModelRenderer bone8;
    private final ModelRenderer bone5;
    private final ModelRenderer bone4;
    private final ModelRenderer Leg4;
    private final ModelRenderer bone19;
    private final ModelRenderer bone20;
    private final ModelRenderer bone21;

    public DragonModel() {
        textureWidth = 128;
        textureHeight = 128;

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);


        main_body = new ModelRenderer(this);
        main_body.setRotationPoint(0.0F, -17.0F, 1.0F);
        main.addChild(main_body);
        main_body.setTextureOffset(0, 0).addBox(-5.5F, -5.5F, -11.5F, 11.0F, 11.0F, 14.0F, -0.5F, false);
        main_body.setTextureOffset(0, 38).addBox(-2.0F, -7.0F, -11.0F, 4.0F, 8.0F, 13.0F, 0.0F, false);

        maim_pelvis = new ModelRenderer(this);
        maim_pelvis.setRotationPoint(0.0F, -21.0F, 1.0F);
        main.addChild(maim_pelvis);
        maim_pelvis.setTextureOffset(27, 28).addBox(-4.5F, -1.4F, 0.5F, 9.0F, 10.0F, 10.0F, -0.5F, false);
        maim_pelvis.setTextureOffset(20, 95).addBox(-1.0F, -3.0F, 2.0F, 2.0F, 5.0F, 8.0F, 0.0F, false);

        Tail = new ModelRenderer(this);
        Tail.setRotationPoint(0.0F, 2.0F, 7.0F);
        maim_pelvis.addChild(Tail);


        bone = new ModelRenderer(this);
        bone.setRotationPoint(0.0F, 0.0F, 2.0F);
        Tail.addChild(bone);
        setRotationAngle(bone, -0.3491F, 0.0F, 0.0F);
        bone.setTextureOffset(22, 48).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 12.0F, 0.0F, false);

        bone11 = new ModelRenderer(this);
        bone11.setRotationPoint(0.0F, -1.9074F, 11.1464F);
        bone.addChild(bone11);
        bone11.setTextureOffset(50, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 4.0F, 13.0F, 0.0F, false);

        bone12 = new ModelRenderer(this);
        bone12.setRotationPoint(0.0F, 1.2957F, 9.604F);
        bone11.addChild(bone12);
        setRotationAngle(bone12, 0.3491F, 0.0F, 0.0F);
        bone12.setTextureOffset(74, 48).addBox(-1.0F, -2.342F, -0.9397F, 2.0F, 3.0F, 11.0F, 0.0F, false);

        bone13 = new ModelRenderer(this);
        bone13.setRotationPoint(0.0F, -0.2817F, 9.4023F);
        bone12.addChild(bone13);
        setRotationAngle(bone13, 0.2618F, 0.0F, 0.0F);
        bone13.setTextureOffset(71, 0).addBox(-1.0F, -2.0603F, -0.342F, 2.0F, 2.0F, 10.0F, 0.0F, false);

        bone16 = new ModelRenderer(this);
        bone16.setRotationPoint(0.0F, -1.0F, 9.0F);
        bone13.addChild(bone16);
        setRotationAngle(bone16, 0.2618F, 0.0F, 0.0F);
        bone16.setTextureOffset(101, 15).addBox(-1.0F, -0.7709F, 0.2067F, 2.0F, 1.0F, 8.0F, 0.0F, false);
        bone16.setTextureOffset(0, 25).addBox(-6.0F, -0.1066F, 1.0622F, 12.0F, 0.0F, 13.0F, 0.0F, false);

        bone14 = new ModelRenderer(this);
        bone14.setRotationPoint(0.0F, 0.0F, 0.0F);
        bone16.addChild(bone14);
        setRotationAngle(bone14, 0.0F, 0.0F, -1.5708F);


        NeckandHead = new ModelRenderer(this);
        NeckandHead.setRotationPoint(0.0F, -19.0F, -9.0F);
        main.addChild(NeckandHead);
        setRotationAngle(NeckandHead, 1.8326F, 0.0F, 0.0F);


        Neckand = new ModelRenderer(this);
        Neckand.setRotationPoint(0.0F, -2.0F, 1.0F);
        NeckandHead.addChild(Neckand);
        setRotationAngle(Neckand, -0.2618F, 0.0F, 0.0F);
        Neckand.setTextureOffset(49, 59).addBox(-4.0F, -1.0F, -7.0F, 8.0F, 6.0F, 9.0F, 0.0F, false);

        Neckand4 = new ModelRenderer(this);
        Neckand4.setRotationPoint(0.0F, 2.7855F, -0.4912F);
        Neckand.addChild(Neckand4);
        setRotationAngle(Neckand4, -0.4363F, 0.0F, 0.0F);
        Neckand4.setTextureOffset(44, 74).addBox(-3.0F, -7.0F, -6.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
        Neckand4.setTextureOffset(21, 35).addBox(0.0F, -5.6693F, 1.648F, 0.0F, 8.0F, 3.0F, 0.0F, false);

        Neckand8 = new ModelRenderer(this);
        Neckand8.setRotationPoint(0.0F, 1.2321F, -1.866F);
        Neckand4.addChild(Neckand8);
        setRotationAngle(Neckand8, -0.2618F, 0.0F, 0.0F);
        Neckand8.setTextureOffset(90, 41).addBox(-2.0F, -12.0F, -5.0F, 4.0F, 6.0F, 7.0F, 0.0F, false);
        Neckand8.setTextureOffset(41, 62).addBox(-0.01F, -11.6331F, 0.5524F, 0.0F, 4.0F, 4.0F, 0.0F, false);

        Neckand12 = new ModelRenderer(this);
        Neckand12.setRotationPoint(0.0F, 0.7071F, -1.2247F);
        Neckand8.addChild(Neckand12);
        setRotationAngle(Neckand12, -0.0873F, 0.0F, 0.0F);
        Neckand12.setTextureOffset(100, 79).addBox(-1.99F, -17.0F, -4.0F, 4.0F, 5.0F, 6.0F, 0.0F, false);
        Neckand12.setTextureOffset(0, 55).addBox(0.0F, -17.7483F, 0.7779F, 0.0F, 6.0F, 4.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, -9.2944F, 8.4455F);
        Neckand.addChild(Head);
        setRotationAngle(Head, -1.3963F, 0.0F, 0.0F);
        Head.setTextureOffset(67, 34).addBox(-3.1F, -3.7602F, -3.7807F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        Head.setTextureOffset(4, 67).addBox(2.1F, -3.7602F, -3.7807F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        Head.setTextureOffset(65, 32).addBox(-3.5F, -3.6667F, -5.6819F, 7.0F, 7.0F, 9.0F, -0.5F, false);
        Head.setTextureOffset(64, 64).addBox(-2.9F, -0.6983F, -4.5513F, 0.0F, 7.0F, 11.0F, 0.0F, false);
        Head.setTextureOffset(14, 108).addBox(1.01F, -3.4167F, -4.4319F, 3.0F, 4.0F, 4.0F, -1.0F, false);
        Head.setTextureOffset(0, 0).addBox(-4.01F, -3.4167F, -4.4319F, 3.0F, 4.0F, 4.0F, -1.0F, false);

        central_horn = new ModelRenderer(this);
        central_horn.setRotationPoint(0.0F, -0.6667F, -1.1819F);
        Head.addChild(central_horn);
        setRotationAngle(central_horn, 0.0873F, 0.0F, 0.0F);
        central_horn.setTextureOffset(30, 89).addBox(-0.5F, -2.7309F, 3.1629F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        Horn_left = new ModelRenderer(this);
        Horn_left.setRotationPoint(0.0F, -2.1667F, 1.8181F);
        Head.addChild(Horn_left);
        setRotationAngle(Horn_left, 0.0F, 0.3491F, 0.0F);
        Horn_left.setTextureOffset(72, 48).addBox(0.5F, -0.8264F, 0.9848F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(1.9397F, -1.1585F, -0.4691F);
        Horn_left.addChild(bone25);
        setRotationAngle(bone25, 0.0F, -0.3491F, 0.0F);
        bone25.setTextureOffset(48, 88).addBox(0.0152F, 0.6265F, 4.6769F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        bone25.setTextureOffset(85, 5).addBox(0.485F, 1.3588F, 7.3602F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        Hprn_right = new ModelRenderer(this);
        Hprn_right.setRotationPoint(0.0F, -2.1667F, 1.8181F);
        Head.addChild(Hprn_right);
        setRotationAngle(Hprn_right, 0.0F, -0.3491F, 0.0F);
        Hprn_right.setTextureOffset(55, 32).addBox(-2.5F, -0.8264F, 0.9848F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(-1.9397F, -1.1585F, -0.4691F);
        Hprn_right.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.3491F, 0.0F);
        bone27.setTextureOffset(85, 0).addBox(-2.0152F, 0.6265F, 4.6769F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        bone27.setTextureOffset(72, 54).addBox(-1.485F, 1.3588F, 7.3602F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        nose = new ModelRenderer(this);
        nose.setRotationPoint(0.0F, -3.1667F, -4.1819F);
        Head.addChild(nose);
        nose.setTextureOffset(96, 96).addBox(-1.5F, 1.2944F, -7.4455F, 3.0F, 2.0F, 8.0F, 1.0F, false);
        nose.setTextureOffset(0, 29).addBox(-0.5F, -0.4196F, -8.2258F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        teeth = new ModelRenderer(this);
        teeth.setRotationPoint(0.0F, 0.0F, 0.0F);
        nose.addChild(teeth);
        teeth.setTextureOffset(0, 5).addBox(1.92F, 2.2944F, -8.3455F, 0.0F, 3.0F, 7.0F, 0.0F, false);
        teeth.setTextureOffset(65, 38).addBox(-1.5F, 2.2944F, -8.3455F, 3.0F, 3.0F, 0.0F, 0.0F, false);
        teeth.setTextureOffset(0, 2).addBox(-1.91F, 2.2944F, -8.3455F, 0.0F, 3.0F, 7.0F, 0.0F, false);
        teeth.setTextureOffset(8, 38).addBox(-2.0F, 3.2944F, -8.3555F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        teeth.setTextureOffset(9, 35).addBox(1.0F, 3.2944F, -8.3555F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lower_jaw = new ModelRenderer(this);
        lower_jaw.setRotationPoint(0.0F, 3.1277F, -2.6274F);
        Head.addChild(lower_jaw);


        bone18 = new ModelRenderer(this);
        bone18.setRotationPoint(0.0F, -3.1005F, -1.2829F);
        lower_jaw.addChild(bone18);
        setRotationAngle(bone18, 0.5236F, 0.0F, 0.0F);
        bone18.setTextureOffset(61, 107).addBox(-1.0F, 1.5981F, -6.5F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        bone18.setTextureOffset(55, 17).addBox(-2.0F, 2.2672F, -8.7467F, 4.0F, 2.0F, 13.0F, 0.0F, false);

        muscles = new ModelRenderer(this);
        muscles.setRotationPoint(0.0F, -1.9128F, 0.9962F);
        bone18.addChild(muscles);
        muscles.setTextureOffset(0, 27).addBox(0.91F, 0.2944F, -3.4455F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        muscles.setTextureOffset(0, 43).addBox(-1.91F, 1.2944F, -3.4455F, 1.0F, 4.0F, 4.0F, 0.0F, false);

        teeth2 = new ModelRenderer(this);
        teeth2.setRotationPoint(0.0F, -1.0872F, -0.9962F);
        muscles.addChild(teeth2);
        teeth2.setTextureOffset(0, 20).addBox(1.9F, 4.2944F, -8.3455F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        teeth2.setTextureOffset(6, 30).addBox(-1.5F, 4.2944F, -8.3455F, 3.0F, 1.0F, 0.0F, 0.0F, false);
        teeth2.setTextureOffset(0, 19).addBox(-1.9F, 4.2944F, -8.3455F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        teeth2.setTextureOffset(10, 0).addBox(-2.0F, 4.2944F, -8.4455F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        teeth2.setTextureOffset(0, 0).addBox(0.88F, 4.2944F, -8.4455F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        Legs_all = new ModelRenderer(this);
        Legs_all.setRotationPoint(0.0F, 9.0F, -2.0F);
        main.addChild(Legs_all);


        Leg1 = new ModelRenderer(this);
        Leg1.setRotationPoint(5.0F, -28.0F, -3.0F);
        Legs_all.addChild(Leg1);
        Leg1.setTextureOffset(0, 89).addBox(-3.75F, -2.0F, -3.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setRotationPoint(-1.0F, 6.0F, 0.0F);
        Leg1.addChild(bone6);
        setRotationAngle(bone6, 0.0873F, 0.0F, 0.0F);
        bone6.setTextureOffset(88, 26).addBox(-3.0F, -2.1419F, -4.1245F, 6.0F, 9.0F, 6.0F, -1.0F, false);

        bone9 = new ModelRenderer(this);
        bone9.setRotationPoint(0.0F, 5.9962F, 0.9128F);
        bone6.addChild(bone9);
        setRotationAngle(bone9, 1.4835F, 0.0F, 0.0F);
        bone9.setTextureOffset(103, 54).addBox(-0.9F, -3.2411F, -6.1646F, 2.0F, 3.0F, 7.0F, 0.0F, false);

        bone10 = new ModelRenderer(this);
        bone10.setRotationPoint(0.0F, -2.4962F, -6.1128F);
        bone9.addChild(bone10);
        setRotationAngle(bone10, -1.5708F, 0.0F, 0.0F);
        bone10.setTextureOffset(105, 36).addBox(-2.0F, -1.5F, -2.8F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        bone10.setTextureOffset(0, 60).addBox(-1.0F, -1.0F, -4.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        bone10.setTextureOffset(55, 23).addBox(0.0F, -1.0F, -4.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);
        bone10.setTextureOffset(21, 43).addBox(1.0F, -1.0F, -4.0F, 0.0F, 2.0F, 5.0F, 0.0F, false);

        Leg2 = new ModelRenderer(this);
        Leg2.setRotationPoint(-5.0F, -28.0F, -3.0F);
        Legs_all.addChild(Leg2);
        Leg2.setTextureOffset(54, 88).addBox(-0.25F, -2.0F, -3.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setRotationPoint(1.0F, 6.0F, 0.0F);
        Leg2.addChild(bone2);
        setRotationAngle(bone2, 0.0873F, 0.0F, 0.0F);
        bone2.setTextureOffset(34, 88).addBox(-3.0F, -2.1419F, -4.1245F, 6.0F, 9.0F, 6.0F, -1.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setRotationPoint(0.0F, 5.9962F, 0.9128F);
        bone2.addChild(bone3);
        setRotationAngle(bone3, 1.4835F, 0.0F, 0.0F);
        bone3.setTextureOffset(49, 103).addBox(-1.1F, -3.2411F, -6.1646F, 2.0F, 3.0F, 7.0F, 0.0F, false);

        bone7 = new ModelRenderer(this);
        bone7.setRotationPoint(0.0F, -2.4962F, -6.1128F);
        bone3.addChild(bone7);
        setRotationAngle(bone7, -1.5708F, 0.0F, 0.0F);
        bone7.setTextureOffset(0, 104).addBox(-2.0F, -1.5F, -2.8F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        bone7.setTextureOffset(0, 25).addBox(-1.0F, -1.0F, -4.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        bone7.setTextureOffset(10, 0).addBox(0.0F, -1.0F, -4.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);
        bone7.setTextureOffset(0, 0).addBox(1.0F, -1.0F, -4.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        Leg3 = new ModelRenderer(this);
        Leg3.setRotationPoint(5.0F, -27.0F, 12.0F);
        Legs_all.addChild(Leg3);
        Leg3.setTextureOffset(96, 0).addBox(-3.9F, -2.0F, -3.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);

        bone8 = new ModelRenderer(this);
        bone8.setRotationPoint(-2.0F, 7.0F, -1.0F);
        Leg3.addChild(bone8);
        setRotationAngle(bone8, 0.5236F, 0.0F, 0.0F);
        bone8.setTextureOffset(86, 62).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 9.0F, 5.0F, -1.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setRotationPoint(0.0F, 4.1025F, -5.1415F);
        bone8.addChild(bone5);
        setRotationAngle(bone5, 0.9599F, 0.0F, 0.0F);
        bone5.setTextureOffset(106, 70).addBox(-1.0F, 2.8789F, -3.2269F, 2.0F, 2.0F, 6.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setRotationPoint(0.0F, 3.4737F, -3.5F);
        bone5.addChild(bone4);
        setRotationAngle(bone4, -1.4835F, 0.0F, 0.0F);
        bone4.setTextureOffset(85, 106).addBox(-2.0F, -1.3415F, -3.4342F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        bone4.setTextureOffset(49, 63).addBox(-1.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        bone4.setTextureOffset(61, 22).addBox(0.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        bone4.setTextureOffset(21, 43).addBox(1.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        Leg4 = new ModelRenderer(this);
        Leg4.setRotationPoint(-5.0F, -27.0F, 12.0F);
        Legs_all.addChild(Leg4);
        Leg4.setTextureOffset(84, 87).addBox(-0.1F, -2.0F, -3.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);

        bone19 = new ModelRenderer(this);
        bone19.setRotationPoint(2.0F, 7.0F, -1.0F);
        Leg4.addChild(bone19);
        setRotationAngle(bone19, 0.5236F, 0.0F, 0.0F);
        bone19.setTextureOffset(79, 12).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 9.0F, 5.0F, -1.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 4.1025F, -5.1415F);
        bone19.addChild(bone20);
        setRotationAngle(bone20, 0.9599F, 0.0F, 0.0F);
        bone20.setTextureOffset(103, 106).addBox(-1.0F, 2.8789F, -3.2269F, 2.0F, 2.0F, 6.0F, 0.0F, false);

        bone21 = new ModelRenderer(this);
        bone21.setRotationPoint(0.0F, 3.4737F, -3.5F);
        bone20.addChild(bone21);
        setRotationAngle(bone21, -1.4835F, 0.0F, 0.0F);
        bone21.setTextureOffset(102, 24).addBox(-2.0F, -1.3415F, -3.4342F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        bone21.setTextureOffset(0, 64).addBox(-1.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        bone21.setTextureOffset(63, 31).addBox(0.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
        bone21.setTextureOffset(63, 29).addBox(1.0F, -0.8363F, -5.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float partialTicks, float headYaw, float headPitch, float scale) {
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0F, 1.5F, 0F);
        matrixStack.rotate(new Quaternion(180f, 0f, 0f, true));
        setRotationAngle(main, 0, headYaw * 0.017453292F, 0);
        main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}