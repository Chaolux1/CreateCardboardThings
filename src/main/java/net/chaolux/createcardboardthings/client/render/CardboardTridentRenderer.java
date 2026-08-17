package net.chaolux.createcardboardthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.chaolux.createcardboardthings.common.entity.CardboardTridentEntity;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CardboardTridentRenderer extends EntityRenderer<CardboardTridentEntity> {
    private static final ResourceLocation TEXTURE=ResourceLocation.fromNamespaceAndPath("createcardboardthings","textures/entity/cardboard_trident_entity.png");
    private final TridentModel tridentModel;
    public CardboardTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.tridentModel=new TridentModel(context.bakeLayer(ModelLayers.TRIDENT));
    }

    @Override
    public void render(CardboardTridentEntity cardboardTridentEntity, float yaw, float particleTick, PoseStack poseStack, MultiBufferSource multiBufferSource,int light) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(particleTick,cardboardTridentEntity.yRotO,cardboardTridentEntity.getYRot()) - 90.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(particleTick,cardboardTridentEntity.xRotO,cardboardTridentEntity.getXRot()) + 90.0f));
        VertexConsumer vertexConsumer= ItemRenderer.getFoilBufferDirect(multiBufferSource,this.tridentModel.renderType(this.getTextureLocation(cardboardTridentEntity)),false,cardboardTridentEntity.isFoil());
        this.tridentModel.renderToBuffer(poseStack,vertexConsumer,light, OverlayTexture.NO_OVERLAY,0xFFFFFFFF);
        poseStack.popPose();
        super.render(cardboardTridentEntity,yaw,particleTick,poseStack,multiBufferSource,light);
    }

    @Override
    public ResourceLocation getTextureLocation(CardboardTridentEntity cardboardTridentEntity) {
        return TEXTURE;
    }
}
