// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
package by.jackraidenph.dragonsurvival.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
@Deprecated
public class Wings extends EntityModel<Entity> {
    private final ModelRenderer Leg1;
    private final ModelRenderer Forearm1;
    private final ModelRenderer Elbow1;
    private final ModelRenderer hand1;
    private final ModelRenderer Leg2;
    private final ModelRenderer Forearm2;
    private final ModelRenderer Elbow2;
    private final ModelRenderer hand2;
    private final ModelRenderer Leg3;
    private final ModelRenderer Forearm3;
    private final ModelRenderer Elbow3;
    private final ModelRenderer hand3;
    private final ModelRenderer Leg4;
    private final ModelRenderer Forearm4;
    private final ModelRenderer Elbow4;
    private final ModelRenderer hand4;
    private final ModelRenderer main;
    private final ModelRenderer main_body;
    private final ModelRenderer maim_pelvis;
    private final ModelRenderer Tail;
    private final ModelRenderer Tail_5;
    private final ModelRenderer Tail_4;
    private final ModelRenderer Tail_3;
    private final ModelRenderer Tail_2;
    private final ModelRenderer Tail_1;
    private final ModelRenderer Tail_0;
    private final ModelRenderer NeckandHead;
    private final ModelRenderer NeckandMain;
    private final ModelRenderer Neckand_3;
    private final ModelRenderer Neckand_2;
    private final ModelRenderer Neckand_1;
    private final ModelRenderer Head;
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
    private final ModelRenderer leftWing;
    private final ModelRenderer all_wing;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer wings_shoulder;
    private final ModelRenderer cube_r3;
    private final ModelRenderer wing_edge;
    private final ModelRenderer rightWing;
    private final ModelRenderer all_wing2;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer wings_shoulder2;
    private final ModelRenderer cube_r6;
    private final ModelRenderer wing_edge2;

    public Wings() {
        textureWidth = 256;
        textureHeight = 256;

        Leg1 = new ModelRenderer(this);
        Leg1.setRotationPoint(5.0F, 6.0F, -7.0F);
        Leg1.setTextureOffset(99, 123).addBox(-3.75F, -2.0F, -2.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        Leg1.setTextureOffset(103, 71).addBox(0.1F, -9.0F, -2.0F, 0.0F, 11.0F, 9.0F, 0.0F, false);

        Forearm1 = new ModelRenderer(this);
        Forearm1.setRotationPoint(-9.0F, 5.0F, 2.0F);
        Leg1.addChild(Forearm1);
        setRotationAngle(Forearm1, 0.0873F, 0.0F, 0.0F);
        Forearm1.setTextureOffset(58, 98).addBox(5.79F, -0.3073F, -2.9429F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        Elbow1 = new ModelRenderer(this);
        Elbow1.setRotationPoint(0.0F, 5.9962F, -1.0872F);
        Forearm1.addChild(Elbow1);
        setRotationAngle(Elbow1, 1.4835F, 0.0F, 0.0F);
        Elbow1.setTextureOffset(54, 124).addBox(6.3F, -2.2487F, -5.9903F, 2.0F, 3.0F, 7.0F, 0.0F, false);
        Elbow1.setTextureOffset(8, 85).addBox(7.0F, 0.4723F, -1.3366F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        hand1 = new ModelRenderer(this);
        hand1.setRotationPoint(0.0F, -0.5038F, -5.9385F);
        Elbow1.addChild(hand1);
        setRotationAngle(hand1, -1.5708F, 0.0F, 0.0F);
        hand1.setTextureOffset(126, 64).addBox(5.3F, -1.5F, -3.8F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        hand1.setTextureOffset(0, 0).addBox(6.3F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand1.setTextureOffset(0, 0).addBox(8.3F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand1.setTextureOffset(0, 0).addBox(7.2F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);

        Leg2 = new ModelRenderer(this);
        Leg2.setRotationPoint(-5.0F, 6.0F, -7.0F);
        Leg2.setTextureOffset(121, 103).addBox(-0.25F, -2.0F, -2.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        Leg2.setTextureOffset(67, 104).addBox(0.0F, -9.0F, -2.0F, 0.0F, 11.0F, 9.0F, 0.0F, false);

        Forearm2 = new ModelRenderer(this);
        Forearm2.setRotationPoint(1.0F, 5.0F, 2.0F);
        Leg2.addChild(Forearm2);
        setRotationAngle(Forearm2, 0.0873F, 0.0F, 0.0F);
        Forearm2.setTextureOffset(79, 102).addBox(-0.79F, -0.3073F, -2.9429F, 3.0F, 6.0F, 3.0F, 0.0F, false);

        Elbow2 = new ModelRenderer(this);
        Elbow2.setRotationPoint(0.0F, 5.9962F, -1.0872F);
        Forearm2.addChild(Elbow2);
        setRotationAngle(Elbow2, 1.4835F, 0.0F, 0.0F);
        Elbow2.setTextureOffset(65, 128).addBox(-0.3F, -2.2487F, -5.9903F, 2.0F, 3.0F, 7.0F, 0.0F, false);
        Elbow2.setTextureOffset(6, 30).addBox(1.0F, 0.4723F, -1.3366F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        hand2 = new ModelRenderer(this);
        hand2.setRotationPoint(0.0F, -0.5038F, -5.9385F);
        Elbow2.addChild(hand2);
        setRotationAngle(hand2, -1.5708F, 0.0F, 0.0F);
        hand2.setTextureOffset(20, 130).addBox(-1.2F, -1.5F, -3.8F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        hand2.setTextureOffset(0, 0).addBox(-0.3F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand2.setTextureOffset(0, 0).addBox(0.8F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand2.setTextureOffset(0, 0).addBox(1.7F, -3.0F, -7.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);

        Leg3 = new ModelRenderer(this);
        Leg3.setRotationPoint(4.0F, 6.0F, 8.0F);
        Leg3.setTextureOffset(0, 124).addBox(-2.9F, -2.0F, -2.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        Leg3.setTextureOffset(28, 31).addBox(1.0F, -6.0F, -2.0F, 0.0F, 9.0F, 8.0F, 0.0F, false);

        Forearm3 = new ModelRenderer(this);
        Forearm3.setRotationPoint(-1.0F, 7.0F, 0.0F);
        Leg3.addChild(Forearm3);
        setRotationAngle(Forearm3, 0.5236F, 0.0F, 0.0F);
        Forearm3.setTextureOffset(0, 21).addBox(-1.5F, -1.5F, -1.866F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        Elbow3 = new ModelRenderer(this);
        Elbow3.setRotationPoint(0.0F, 4.6025F, -0.2755F);
        Forearm3.addChild(Elbow3);
        setRotationAngle(Elbow3, 0.9599F, 0.0F, 0.0F);
        Elbow3.setTextureOffset(77, 135).addBox(-1.0F, -1.3939F, -5.6084F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        Elbow3.setTextureOffset(90, 91).addBox(0.0F, -0.1341F, -1.1482F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        hand3 = new ModelRenderer(this);
        hand3.setRotationPoint(0.0F, -0.8029F, -5.7943F);
        Elbow3.addChild(hand3);
        setRotationAngle(hand3, -1.4835F, 0.0F, 0.0F);
        hand3.setTextureOffset(33, 133).addBox(-2.0F, -1.2544F, -3.438F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        hand3.setTextureOffset(0, 0).addBox(0.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand3.setTextureOffset(0, 0).addBox(-1.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand3.setTextureOffset(0, 0).addBox(1.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);

        Leg4 = new ModelRenderer(this);
        Leg4.setRotationPoint(-4.0F, 6.0F, 8.0F);
        Leg4.setTextureOffset(79, 120).addBox(-1.1F, -2.0F, -2.75F, 4.0F, 9.0F, 6.0F, 0.0F, false);
        Leg4.setTextureOffset(28, 31).addBox(-1.0F, -6.0F, -2.0F, 0.0F, 9.0F, 8.0F, 0.0F, true);

        Forearm4 = new ModelRenderer(this);
        Forearm4.setRotationPoint(1.0F, 7.0F, 0.0F);
        Leg4.addChild(Forearm4);
        setRotationAngle(Forearm4, 0.5236F, 0.0F, 0.0F);
        Forearm4.setTextureOffset(0, 11).addBox(-1.5F, -1.5F, -1.866F, 3.0F, 7.0F, 3.0F, 0.0F, false);

        Elbow4 = new ModelRenderer(this);
        Elbow4.setRotationPoint(0.0F, 4.6025F, -0.2755F);
        Forearm4.addChild(Elbow4);
        setRotationAngle(Elbow4, 0.9599F, 0.0F, 0.0F);
        Elbow4.setTextureOffset(51, 134).addBox(-1.0F, -1.3939F, -5.6084F, 2.0F, 2.0F, 6.0F, 0.0F, false);
        Elbow4.setTextureOffset(84, 91).addBox(0.0F, -0.1341F, -1.1482F, 0.0F, 3.0F, 3.0F, 0.0F, false);

        hand4 = new ModelRenderer(this);
        hand4.setRotationPoint(0.0F, -0.8029F, -5.7943F);
        Elbow4.addChild(hand4);
        setRotationAngle(hand4, -1.4835F, 0.0F, 0.0F);
        hand4.setTextureOffset(128, 49).addBox(-2.0F, -1.2544F, -3.438F, 4.0F, 3.0F, 5.0F, -0.5F, false);
        hand4.setTextureOffset(0, 0).addBox(1.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand4.setTextureOffset(0, 0).addBox(0.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);
        hand4.setTextureOffset(0, 0).addBox(-1.0F, -2.7534F, -6.5623F, 0.0F, 5.0F, 6.0F, 0.0F, false);

        main = new ModelRenderer(this);
        main.setRotationPoint(0.0F, 24.0F, 0.0F);


        main_body = new ModelRenderer(this);
        main_body.setRotationPoint(0.0F, -17.0F, 1.0F);
        main.addChild(main_body);
        main_body.setTextureOffset(0, 80).addBox(-5.5F, -5.5F, -11.5F, 11.0F, 11.0F, 14.0F, -0.5F, false);
        main_body.setTextureOffset(85, 105).addBox(0.0F, 5.0F, -10.0F, 0.0F, 4.0F, 11.0F, 0.0F, false);
        main_body.setTextureOffset(0, 57).addBox(0.0F, -12.0F, -11.0F, 0.0F, 7.0F, 13.0F, 0.0F, false);

        maim_pelvis = new ModelRenderer(this);
        maim_pelvis.setRotationPoint(0.0F, -21.0F, 1.0F);
        main.addChild(maim_pelvis);
        maim_pelvis.setTextureOffset(0, 39).addBox(-4.5F, -1.4F, 0.5F, 9.0F, 10.0F, 10.0F, -0.5F, false);
        maim_pelvis.setTextureOffset(30, 51).addBox(-0.02F, -7.9F, 2.0F, 0.0F, 7.0F, 8.0F, 0.0F, false);

        Tail = new ModelRenderer(this);
        Tail.setRotationPoint(0.0F, 2.0F, 7.0F);
        maim_pelvis.addChild(Tail);


        Tail_5 = new ModelRenderer(this);
        Tail_5.setRotationPoint(0.0F, 0.0F, 2.0F);
        Tail.addChild(Tail_5);
        Tail_5.setTextureOffset(0, 92).addBox(-0.01F, -6.8F, -1.0F, 0.0F, 4.0F, 13.0F, 0.0F, false);
        Tail_5.setTextureOffset(91, 91).addBox(-3.0F, -2.8F, 0.0F, 6.0F, 6.0F, 12.0F, 0.0F, false);

        Tail_4 = new ModelRenderer(this);
        Tail_4.setRotationPoint(0.0F, -1.9074F, 11.1464F);
        Tail_5.addChild(Tail_4);
        Tail_4.setTextureOffset(37, 94).addBox(-2.0F, -0.7926F, -2.0F, 4.0F, 4.0F, 13.0F, 0.0F, false);
        Tail_4.setTextureOffset(58, 81).addBox(0.0F, -4.7926F, -2.0F, 0.0F, 4.0F, 13.0F, 0.0F, false);

        Tail_3 = new ModelRenderer(this);
        Tail_3.setRotationPoint(0.0F, 1.2957F, 9.604F);
        Tail_4.addChild(Tail_3);
        Tail_3.setTextureOffset(79, 87).addBox(-0.01F, -5.9883F, -0.9397F, 0.0F, 4.0F, 11.0F, 0.0F, false);
        Tail_3.setTextureOffset(103, 109).addBox(-1.0F, -1.9883F, -0.9397F, 2.0F, 3.0F, 11.0F, 0.0F, false);

        Tail_2 = new ModelRenderer(this);
        Tail_2.setRotationPoint(0.0F, -0.2817F, 9.4023F);
        Tail_3.addChild(Tail_2);
        Tail_2.setTextureOffset(18, 118).addBox(-1.0F, -1.0603F, 0.658F, 2.0F, 2.0F, 10.0F, 0.0F, false);
        Tail_2.setTextureOffset(26, 64).addBox(0.0F, -5.0603F, 0.658F, 0.0F, 4.0F, 10.0F, 0.0F, false);
        Tail_2.setTextureOffset(0, 26).addBox(-5.0F, -0.0066F, 0.0F, 10.0F, 0.0F, 13.0F, 0.0F, false);
        Tail_2.setTextureOffset(26, 60).addBox(0.0F, -0.1528F, 0.658F, 0.0F, 4.0F, 10.0F, 0.0F, false);

        Tail_1 = new ModelRenderer(this);
        Tail_1.setRotationPoint(0.0F, 0.0F, 10.0F);
        Tail_2.addChild(Tail_1);
        Tail_1.setTextureOffset(34, 124).addBox(-1.0F, -0.7709F, 0.6473F, 2.0F, 1.0F, 8.0F, 0.0F, false);

        Tail_0 = new ModelRenderer(this);
        Tail_0.setRotationPoint(0.0F, -1.0F, -1.0F);
        Tail_1.addChild(Tail_0);
        setRotationAngle(Tail_0, 0.0F, 0.0F, -1.5708F);
        Tail_0.setTextureOffset(0, 0).addBox(-6.6F, 0.0359F, 1.6473F, 12.0F, 0.0F, 13.0F, 0.0F, false);

        NeckandHead = new ModelRenderer(this);
        NeckandHead.setRotationPoint(0.0F, -19.0F, -9.0F);
        main.addChild(NeckandHead);
        setRotationAngle(NeckandHead, 2.0071F, 0.0F, 0.0F);


        NeckandMain = new ModelRenderer(this);
        NeckandMain.setRotationPoint(0.0F, -2.0F, 1.0F);
        NeckandHead.addChild(NeckandMain);
        setRotationAngle(NeckandMain, -0.4363F, 0.0F, 0.0F);
        NeckandMain.setTextureOffset(103, 49).addBox(-4.0F, -1.0F, -7.0F, 8.0F, 6.0F, 9.0F, 0.0F, false);
        NeckandMain.setTextureOffset(37, 5).addBox(-0.01F, -1.2616F, -11.0284F, 0.0F, 7.0F, 4.0F, 0.0F, false);

        Neckand_3 = new ModelRenderer(this);
        Neckand_3.setRotationPoint(0.0F, 0.7855F, -0.4912F);
        NeckandMain.addChild(Neckand_3);
        setRotationAngle(Neckand_3, -0.5236F, 0.0F, 0.0F);
        Neckand_3.setTextureOffset(0, 109).addBox(-3.0F, -5.2679F, -5.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
        Neckand_3.setTextureOffset(36, 83).addBox(0.0F, -5.2679F, -7.9282F, 0.0F, 7.0F, 3.0F, 0.0F, false);
        Neckand_3.setTextureOffset(36, 75).addBox(-0.01F, -4.9372F, 2.648F, 0.0F, 8.0F, 3.0F, 0.0F, false);

        Neckand_2 = new ModelRenderer(this);
        Neckand_2.setRotationPoint(0.0F, -3.0359F, 0.134F);
        Neckand_3.addChild(Neckand_2);
        setRotationAngle(Neckand_2, -0.5236F, 0.0F, 0.0F);
        Neckand_2.setTextureOffset(122, 122).addBox(-2.0F, -5.5046F, -4.7955F, 4.0F, 6.0F, 7.0F, 0.0F, false);
        Neckand_2.setTextureOffset(38, 44).addBox(-0.01F, -5.3026F, -8.4523F, 0.0F, 6.0F, 4.0F, 0.0F, false);
        Neckand_2.setTextureOffset(37, 18).addBox(0.0F, -6.5038F, 0.3909F, 0.0F, 4.0F, 4.0F, 0.0F, false);

        Neckand_1 = new ModelRenderer(this);
        Neckand_1.setRotationPoint(0.0F, -4.7975F, -0.0202F);
        Neckand_2.addChild(Neckand_1);
        setRotationAngle(Neckand_1, -0.1745F, 0.0F, 0.0F);
        Neckand_1.setTextureOffset(121, 80).addBox(-1.99F, -4.872F, -3.9389F, 4.0F, 5.0F, 6.0F, 0.0F, false);
        Neckand_1.setTextureOffset(0, 38).addBox(0.0F, -4.6562F, -7.4912F, 0.0F, 6.0F, 4.0F, 0.0F, false);
        Neckand_1.setTextureOffset(37, 12).addBox(-0.02F, -7.4997F, 1.523F, 0.0F, 6.0F, 4.0F, 0.0F, false);

        Head = new ModelRenderer(this);
        Head.setRotationPoint(0.0F, -6.632F, -0.795F);
        Neckand_1.addChild(Head);
        setRotationAngle(Head, -0.3491F, 0.0F, 0.0F);
        Head.setTextureOffset(103, 52).addBox(-3.1F, -3.7602F, -3.7807F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        Head.setTextureOffset(103, 49).addBox(2.1F, -3.7602F, -3.7807F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        Head.setTextureOffset(0, 44).addBox(-0.03F, -7.3403F, -9.1667F, 0.0F, 9.0F, 15.0F, 0.0F, false);
        Head.setTextureOffset(103, 64).addBox(-3.5F, -3.6667F, -5.6819F, 7.0F, 7.0F, 9.0F, -0.5F, false);
        Head.setTextureOffset(28, 100).addBox(2.9F, -0.6983F, -4.5513F, 0.0F, 7.0F, 11.0F, 0.0F, false);
        Head.setTextureOffset(92, 98).addBox(-2.9F, -0.6983F, -4.5513F, 0.0F, 7.0F, 11.0F, 0.0F, false);
        Head.setTextureOffset(0, 80).addBox(1.01F, -3.4167F, -4.4319F, 3.0F, 4.0F, 4.0F, -1.0F, false);
        Head.setTextureOffset(33, 29).addBox(-4.01F, -3.4167F, -4.4319F, 3.0F, 4.0F, 4.0F, -1.0F, false);

        central_horn = new ModelRenderer(this);
        central_horn.setRotationPoint(0.0F, -0.6667F, -1.1819F);
        Head.addChild(central_horn);
        setRotationAngle(central_horn, 0.0873F, 0.0F, 0.0F);
        central_horn.setTextureOffset(113, 123).addBox(-0.5F, -2.7309F, 3.1629F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        Horn_left = new ModelRenderer(this);
        Horn_left.setRotationPoint(0.0F, -2.1667F, 1.8181F);
        Head.addChild(Horn_left);
        setRotationAngle(Horn_left, 0.0F, 0.3491F, 0.0F);
        Horn_left.setTextureOffset(0, 88).addBox(0.5F, -0.8264F, 0.9848F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setRotationPoint(1.9397F, -1.1585F, -0.4691F);
        Horn_left.addChild(bone25);
        setRotationAngle(bone25, 0.0F, -0.3491F, 0.0F);
        bone25.setTextureOffset(14, 123).addBox(0.0152F, 0.6265F, 4.6769F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        bone25.setTextureOffset(32, 120).addBox(0.485F, 1.3588F, 7.3602F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        Horn_right = new ModelRenderer(this);
        Horn_right.setRotationPoint(0.0F, -2.1667F, 1.8181F);
        Head.addChild(Horn_right);
        setRotationAngle(Horn_right, 0.0F, -0.3491F, 0.0F);
        Horn_right.setTextureOffset(0, 0).addBox(-2.5F, -0.8264F, 0.9848F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setRotationPoint(-1.9397F, -1.1585F, -0.4691F);
        Horn_right.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.3491F, 0.0F);
        bone27.setTextureOffset(93, 120).addBox(-2.0152F, 0.6265F, 4.6769F, 2.0F, 2.0F, 3.0F, 0.0F, false);
        bone27.setTextureOffset(22, 105).addBox(-1.485F, 1.3588F, 7.3602F, 1.0F, 1.0F, 4.0F, 0.0F, false);

        nose = new ModelRenderer(this);
        nose.setRotationPoint(0.0F, -3.1667F, -2.1819F);
        Head.addChild(nose);
        setRotationAngle(nose, 0.0873F, 0.0F, 0.0F);
        nose.setTextureOffset(41, 111).addBox(-2.0F, 0.118F, -8.7783F, 4.0F, 4.0F, 9.0F, 0.0F, false);
        nose.setTextureOffset(0, 33).addBox(-0.5F, -0.4196F, -8.2258F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        nasalhorn = new ModelRenderer(this);
        nasalhorn.setRotationPoint(0.0F, 0.0F, -9.0F);
        nose.addChild(nasalhorn);
        setRotationAngle(nasalhorn, -0.3491F, 0.0F, 0.0F);
        nasalhorn.setTextureOffset(38, 54).addBox(-0.5F, -0.5067F, -2.222F, 1.0F, 2.0F, 3.0F, 0.0F, false);

        bone20 = new ModelRenderer(this);
        bone20.setRotationPoint(0.0F, 0.9659F, -0.7412F);
        nasalhorn.addChild(bone20);
        setRotationAngle(bone20, -0.3054F, 0.0F, 0.0F);
        bone20.setTextureOffset(40, 91).addBox(-0.5F, -0.9193F, -3.4045F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        teeth = new ModelRenderer(this);
        teeth.setRotationPoint(0.0F, 0.0F, 0.0F);
        nose.addChild(teeth);
        teeth.setTextureOffset(96, 94).addBox(-1.7F, 2.2944F, -7.7783F, 3.0F, 3.0F, 0.0F, 0.0F, false);
        teeth.setTextureOffset(33, 19).addBox(-1.5F, 2.2944F, -8.3455F, 0.0F, 3.0F, 7.0F, 0.0F, false);
        teeth.setTextureOffset(33, 19).addBox(1.5F, 2.2944F, -8.3455F, 0.0F, 3.0F, 7.0F, 0.0F, true);
        teeth.setTextureOffset(8, 0).addBox(-1.99F, 3.2944F, -8.3555F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        teeth.setTextureOffset(0, 0).addBox(0.99F, 3.2944F, -8.3555F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        lower_jaw = new ModelRenderer(this);
        lower_jaw.setRotationPoint(0.0F, 2.1277F, -1.6274F);
        Head.addChild(lower_jaw);
        setRotationAngle(lower_jaw, 0.0F, 0.0F, 0.0F);


        lower_jaw1 = new ModelRenderer(this);
        lower_jaw1.setRotationPoint(0.0F, -2.1005F, -0.2829F);
        lower_jaw.addChild(lower_jaw1);
        setRotationAngle(lower_jaw1, 0.0873F, 0.0F, 0.0F);
        lower_jaw1.setTextureOffset(113, 135).addBox(-1.0F, 0.6019F, -6.4128F, 2.0F, 1.0F, 6.0F, 0.0F, false);
        lower_jaw1.setTextureOffset(0, 65).addBox(-0.1F, 3.0038F, -7.9128F, 0.0F, 3.0F, 12.0F, 0.0F, false);
        lower_jaw1.setTextureOffset(58, 98).addBox(-2.0F, 1.271F, -8.6595F, 4.0F, 2.0F, 13.0F, 0.0F, false);

        teeth2 = new ModelRenderer(this);
        teeth2.setRotationPoint(0.0F, -4.9924F, 0.1743F);
        lower_jaw1.addChild(teeth2);
        teeth2.setTextureOffset(0, 26).addBox(1.6F, 5.2906F, -8.4326F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        teeth2.setTextureOffset(6, 36).addBox(-1.8F, 5.2906F, -7.9371F, 3.0F, 1.0F, 0.0F, 0.0F, false);
        teeth2.setTextureOffset(0, 25).addBox(-1.6F, 5.2906F, -8.6371F, 0.0F, 1.0F, 6.0F, 0.0F, false);
        teeth2.setTextureOffset(9, 21).addBox(-1.9F, 5.2906F, -8.5326F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        teeth2.setTextureOffset(9, 11).addBox(0.88F, 5.2906F, -8.5326F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        muscles = new ModelRenderer(this);
        muscles.setRotationPoint(0.0F, -1.291F, 19.0368F);
        lower_jaw1.addChild(muscles);
        setRotationAngle(muscles, -0.2182F, 0.0F, 0.0F);
        muscles.setTextureOffset(37, 0).addBox(0.91F, 3.3305F, -21.1542F, 1.0F, 5.0F, 4.0F, 0.0F, false);
        muscles.setTextureOffset(0, 33).addBox(-1.91F, 3.3305F, -21.1542F, 1.0F, 5.0F, 4.0F, 0.0F, false);

        leftWing = new ModelRenderer(this);
        leftWing.setRotationPoint(6.0F, 3.0F, -7.0F);
        setRotationAngle(leftWing, 0.0F, -0.48F, 0.6109F);


        all_wing = new ModelRenderer(this);
        all_wing.setRotationPoint(-2.0F, 0.0F, 0.0F);
        leftWing.addChild(all_wing);
        setRotationAngle(all_wing, 0.2618F, -0.1745F, 0.0F);
        all_wing.setTextureOffset(0, 148).addBox(-1.0F, 0.0F, -9.0F, 28.0F, 0.0F, 47.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(12.0F, 0.0F, 1.0F);
        all_wing.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.5672F, 0.0F);
        cube_r1.setTextureOffset(0, 165).addBox(0.0F, -0.9F, 0.0F, 17.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(1.0F, 0.0F, -1.0F);
        all_wing.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, -0.1309F, 0.0F);
        cube_r2.setTextureOffset(0, 160).addBox(0.0F, -1.1F, 0.0F, 12.0F, 2.0F, 3.0F, 0.0F, false);

        wings_shoulder = new ModelRenderer(this);
        wings_shoulder.setRotationPoint(27.0F, 0.0F, -7.0F);
        all_wing.addChild(wings_shoulder);
        setRotationAngle(wings_shoulder, -0.0436F, -0.8727F, 0.6545F);
        wings_shoulder.setTextureOffset(0, 168).addBox(-1.0F, -0.8F, -1.0F, 5.0F, 1.0F, 3.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(0.0F, 0.0F, 19.0F);
        wings_shoulder.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.0F, -0.0436F);
        cube_r3.setTextureOffset(0, 204).addBox(0.0F, -0.1F, -21.0F, 33.0F, 0.0F, 47.0F, 0.0F, false);

        wing_edge = new ModelRenderer(this);
        wing_edge.setRotationPoint(33.0F, 0.0F, 19.0F);
        wings_shoulder.addChild(wing_edge);
        setRotationAngle(wing_edge, 0.0F, 0.0F, 0.4363F);
        wing_edge.setTextureOffset(101, 148).addBox(-0.5637F, -1.5F, -21.0F, 31.0F, 0.0F, 47.0F, 0.0F, false);

        rightWing = new ModelRenderer(this);
        rightWing.setRotationPoint(-6.0F, 3.0F, -7.0F);
        setRotationAngle(rightWing, 0.0F, 0.48F, -0.6109F);


        all_wing2 = new ModelRenderer(this);
        all_wing2.setRotationPoint(2.0F, 0.0F, 0.0F);
        rightWing.addChild(all_wing2);
        setRotationAngle(all_wing2, 0.2618F, 0.1745F, 0.0F);
        all_wing2.setTextureOffset(0, 148).addBox(-27.0F, 0.0F, -9.0F, 28.0F, 0.0F, 47.0F, 0.0F, true);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(-12.0F, 0.0F, 1.0F);
        all_wing2.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, -0.5672F, 0.0F);
        cube_r4.setTextureOffset(0, 165).addBox(-17.0F, -0.9F, 0.0F, 17.0F, 1.0F, 2.0F, 0.0F, true);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(-1.0F, 0.0F, -1.0F);
        all_wing2.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, 0.1309F, 0.0F);
        cube_r5.setTextureOffset(0, 160).addBox(-12.0F, -1.1F, 0.0F, 12.0F, 2.0F, 3.0F, 0.0F, true);

        wings_shoulder2 = new ModelRenderer(this);
        wings_shoulder2.setRotationPoint(-27.0F, 0.0F, -7.0F);
        all_wing2.addChild(wings_shoulder2);
        setRotationAngle(wings_shoulder2, -0.0436F, 0.8727F, -0.6545F);
        wings_shoulder2.setTextureOffset(0, 168).addBox(-4.0F, -0.8F, -1.0F, 5.0F, 1.0F, 3.0F, 0.0F, true);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(0.0F, 0.0F, 19.0F);
        wings_shoulder2.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0F, 0.0F, 0.0436F);
        cube_r6.setTextureOffset(0, 204).addBox(-33.0F, -0.1F, -21.0F, 33.0F, 0.0F, 47.0F, 0.0F, true);

        wing_edge2 = new ModelRenderer(this);
        wing_edge2.setRotationPoint(-33.0F, 0.0F, 19.0F);
        wings_shoulder2.addChild(wing_edge2);
        setRotationAngle(wing_edge2, 0.0F, 0.0F, -0.4363F);
        wing_edge2.setTextureOffset(101, 148).addBox(-30.4363F, -1.5F, -21.0F, 31.0F, 0.0F, 47.0F, 0.0F, true);
    }

    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		Leg1.render(matrixStack, buffer, packedLight, packedOverlay);
//		Leg2.render(matrixStack, buffer, packedLight, packedOverlay);
//		Leg3.render(matrixStack, buffer, packedLight, packedOverlay);
//		Leg4.render(matrixStack, buffer, packedLight, packedOverlay);
//		main.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.translate(0, 1 + 8 / 16d, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(180));
        leftWing.render(matrixStack, buffer, packedLight, packedOverlay);
        rightWing.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}