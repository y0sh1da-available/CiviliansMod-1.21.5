package net.asian.civiliansmod.model;

import net.asian.civiliansmod.renderer.NPCRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import org.joml.Vector3f;

import java.util.List;

public class NPCModel extends BipedEntityModel<NPCRenderState> {

    private static final String LEFT_SLEEVE = "left_sleeve";
    private static final String RIGHT_SLEEVE = "right_sleeve";
    private static final String LEFT_PANTS = "left_pants";
    private static final String RIGHT_PANTS = "right_pants";
    private final List<ModelPart> parts;
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;
    private final boolean thinArms;

    public NPCModel(ModelPart modelPart, boolean bl) {
        super(modelPart, RenderLayer::getEntityTranslucent);
        this.thinArms = bl;
        this.leftSleeve = this.leftArm.getChild(LEFT_SLEEVE);
        this.rightSleeve = this.rightArm.getChild(RIGHT_SLEEVE);
        this.leftPants = this.leftLeg.getChild(LEFT_PANTS);
        this.rightPants = this.rightLeg.getChild(RIGHT_PANTS);
        this.jacket = this.body.getChild("jacket");
        this.parts = List.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
    }

    public static ModelData getTexturedModelData(Dilation dilation, boolean bl) {
        ModelData modelData = BipedEntityModel.getModelData(dilation, 0.0F);
        ModelPartData modelPartData = modelData.getRoot();

        // Get existing parts and add children to them
        ModelPartData leftArm = modelPartData.getChild("left_arm");
        ModelPartData rightArm = modelPartData.getChild("right_arm");
        ModelPartData leftLeg = modelPartData.getChild("left_leg");
        ModelPartData rightLeg = modelPartData.getChild("right_leg");
        ModelPartData body = modelPartData.getChild("body");

        if (bl) {
            // Thin arms - update existing parts
            leftArm.addChild(LEFT_SLEEVE,
                    ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)),
                    ModelTransform.NONE);
            rightArm.addChild(RIGHT_SLEEVE,
                    ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)),
                    ModelTransform.NONE);
        } else {
            // Normal arms
            leftArm.addChild(LEFT_SLEEVE,
                    ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)),
                    ModelTransform.NONE);
            rightArm.addChild(RIGHT_SLEEVE,
                    ModelPartBuilder.create().uv(40, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)),
                    ModelTransform.NONE);
        }

        // Add pants to legs
        leftLeg.addChild(LEFT_PANTS,
                ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)),
                ModelTransform.NONE);
        rightLeg.addChild(RIGHT_PANTS,
                ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)),
                ModelTransform.NONE);

        // Add jacket to body
        body.addChild("jacket",
                ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.25F)),
                ModelTransform.NONE);

        return modelData;
    }

    public void setAngles(NPCRenderState playerEntityRenderState) {
        boolean bl = !playerEntityRenderState.spectator;
        this.body.visible = bl;
        this.rightArm.visible = bl;
        this.leftArm.visible = bl;
        this.rightLeg.visible = bl;
        this.leftLeg.visible = bl;
        this.hat.visible = playerEntityRenderState.hatVisible;
        this.jacket.visible = playerEntityRenderState.jacketVisible;
        this.leftPants.visible = playerEntityRenderState.leftPantsLegVisible;
        this.rightPants.visible = playerEntityRenderState.rightPantsLegVisible;
        this.leftSleeve.visible = playerEntityRenderState.leftSleeveVisible;
        this.rightSleeve.visible = playerEntityRenderState.rightSleeveVisible;
        super.setAngles(playerEntityRenderState);
    }

    public void setVisible(boolean bl) {
        super.setVisible(bl);
        this.leftSleeve.visible = bl;
        this.rightSleeve.visible = bl;
        this.leftPants.visible = bl;
        this.rightPants.visible = bl;
        this.jacket.visible = bl;
    }

    public void setArmAngle(Arm arm, Vector3f matrixStack) {
        this.getRootPart().rotate(matrixStack);
        ModelPart modelPart = this.getArm(arm);
        if (this.thinArms) {
            float f = 0.5F * (float)(arm == Arm.RIGHT ? 1 : -1);
            modelPart.originX += f;
            modelPart.rotate(matrixStack);
            modelPart.originX -= f;
        } else {
            modelPart.rotate(matrixStack);
        }
    }

    public ModelPart getRandomPart(Random random) {
        return (ModelPart) Util.getRandom(this.parts, random);
    }
}