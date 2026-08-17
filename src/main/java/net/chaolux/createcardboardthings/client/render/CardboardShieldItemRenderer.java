package net.chaolux.createcardboardthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.chaolux.createcardboardthings.registry.client.render.ModClientModels;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CardboardShieldItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE=new ResourceLocation("createcardboardthings","textures/entity/cardboard_shield.png");
    private static CardboardShieldItemRenderer cardboardShieldItemRenderer;
    private final ShieldModel shieldModel;
    private CardboardShieldItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),Minecraft.getInstance().getEntityModels());
        this.shieldModel=new ShieldModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.SHIELD));
    }

    public static CardboardShieldItemRenderer getInstance() {
        if(cardboardShieldItemRenderer == null) cardboardShieldItemRenderer=new CardboardShieldItemRenderer();
        return cardboardShieldItemRenderer;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource,int light,int overlay) {
        if(!itemStack.is(ModItems.CARDBOARD_SHIELD.get())) {
            super.renderByItem(itemStack,itemDisplayContext,poseStack,multiBufferSource,light,overlay);
            return;
        }
        poseStack.pushPose();
        poseStack.scale(1.0f,-1.0f,-1.0f);
        VertexConsumer vertexConsumer= ItemRenderer.getFoilBufferDirect(multiBufferSource,this.shieldModel.renderType(TEXTURE),true,itemStack.hasFoil());
        this.shieldModel.renderToBuffer(poseStack,vertexConsumer,light,overlay,1.0f,1.0f,1.0f,1.0f);
        poseStack.popPose();
    }
}
