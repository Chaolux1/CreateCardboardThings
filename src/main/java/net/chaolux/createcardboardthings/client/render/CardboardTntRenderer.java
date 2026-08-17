package net.chaolux.createcardboardthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.chaolux.createcardboardthings.common.entity.CardboardTntEntity;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CardboardTntRenderer extends EntityRenderer<CardboardTntEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;
    public CardboardTntRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius=0.5f;
        this.blockRenderDispatcher=context.getBlockRenderDispatcher();
    }

    @Override
    public void render(CardboardTntEntity cardboardTntEntity, float yaw, float tick, PoseStack poseStack, MultiBufferSource multiBufferSource,int light) {
        poseStack.pushPose();
        poseStack.translate(0.0f,0.5f,0.0f);
        int fuse=cardboardTntEntity.getFuse();
        if((float) fuse - tick + 1.0f < 10.0f) {
            float scale=1.0f - ((float) fuse - tick + 1.0f) / 10.0f;
            scale= Mth.clamp(scale,0.0f,1.0f);
            scale *=scale;
            scale *=scale;
            float endScale=1.0f + scale * 0.3f;
            poseStack.scale(endScale,endScale,endScale);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        poseStack.translate(-0.5f,-0.5f,0.5f);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderDispatcher, ModBlocks.CARDBOARD_TNT.get().defaultBlockState(),poseStack,multiBufferSource,light,isFuse(fuse));
        poseStack.popPose();
        super.render(cardboardTntEntity,yaw,tick,poseStack,multiBufferSource,light);
    }

    private static boolean isFuse(int fuse) {
        int tick=80 - fuse;
        return (tick >=0 && tick < 3) || (tick >=6 && tick < 9) || (tick >=12 && tick < 15) || (tick >=24 && tick < 33) || (tick >=36 && tick < 45) || (tick >=48 && tick < 57) || (tick >=66 && tick < 69) || (tick >=72 && tick < 75) || (tick >=78 && tick < 81);
    }

    @Override
    public ResourceLocation getTextureLocation(CardboardTntEntity cardboardTntEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
