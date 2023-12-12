package com.chromanyan.chromaticarsenal.client.renderer;

import com.chromanyan.chromaticarsenal.ChromaticArsenal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@OnlyIn(Dist.CLIENT)
public class ChromaNyanRenderer implements ICurioRenderer {
    // i didn't want to make a second renderer to do literally the same thing as the blahaj one but ModelLayerLocation is a public static final and idk if i can change that
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(new ResourceLocation(ChromaticArsenal.MODID, "chromanyan"), "chromanyan");

    private final HumanoidModel<LivingEntity> model;

    public ChromaNyanRenderer(ModelPart part) {
        this.model = new HumanoidModel<>(part);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        model.setupAnim(slotContext.entity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        model.prepareMobModel(slotContext.entity(), limbSwing, limbSwingAmount, partialTicks);
        ICurioRenderer.followHeadRotations(slotContext.entity(), model.head);

        // Translate and rotate with our head
        matrixStack.pushPose();
        matrixStack.translate(model.head.x / 16.0, model.head.y / 16.0, model.head.z / 16.0);
        matrixStack.mulPose(Vector3f.YP.rotation(model.head.yRot));
        matrixStack.mulPose(Vector3f.XP.rotation(model.head.xRot));

        // Translate and scale to our head
        matrixStack.translate(0, -0.25, 0);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        matrixStack.scale(0.625f, 0.625f, 0.625f);

        // Translate slightly higher if wearing a head item
        if(!slotContext.entity().getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            matrixStack.translate(0, 0.12, 0);
        }

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.HEAD, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer, 0);
        matrixStack.popPose();
    }

    public static MeshDefinition mesh() {
        CubeListBuilder builder = new CubeListBuilder();
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
        mesh.getRoot().addOrReplaceChild("head", builder, PartPose.ZERO);
        return mesh;
    }
}
