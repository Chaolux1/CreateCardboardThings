package net.chaolux.createcardboardthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.chaolux.createcardboardthings.common.item.CardboardTridentItem;
import net.chaolux.createcardboardthings.registry.client.render.ModClientModels;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;

public class CardboardTridentItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation TEXTURE=new ResourceLocation("createcardboardthings","textures/entity/cardboard_trident_entity.png");
    private static CardboardTridentItemRenderer cardboardTridentItemRenderer;
    private final TridentModel tridentModel;
    private CardboardTridentItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(),Minecraft.getInstance().getEntityModels());
        this.tridentModel=new TridentModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.TRIDENT));
    }

    public static CardboardTridentItemRenderer getInstance() {
        if(cardboardTridentItemRenderer == null) cardboardTridentItemRenderer=new CardboardTridentItemRenderer();
        return cardboardTridentItemRenderer;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource,int light,int overlay) {
        if(!itemStack.is(ModItems.CARDBOARD_TRIDENT.get())) {
            super.renderByItem(itemStack,itemDisplayContext,poseStack,multiBufferSource,light,overlay);
            return;
        }
        if(usedModel(itemDisplayContext)) {
            renderModel(itemStack,itemDisplayContext,poseStack,multiBufferSource,light,overlay);
            return;
        }
        poseStack.pushPose();
        poseStack.scale(1.0f,-1.0f,-1.0f);
        VertexConsumer vertexConsumer= ItemRenderer.getFoilBufferDirect(multiBufferSource,this.tridentModel.renderType(TEXTURE),false,itemStack.hasFoil());
        this.tridentModel.renderToBuffer(poseStack,vertexConsumer,light,overlay,1.0f,1.0f,1.0f,1.0f);
        poseStack.popPose();
    }

    private static boolean usedModel(ItemDisplayContext itemDisplayContext) {
        return itemDisplayContext == ItemDisplayContext.GUI || itemDisplayContext == ItemDisplayContext.GROUND || itemDisplayContext == ItemDisplayContext.FIXED;
    }

    private static void renderModel(ItemStack itemStack,ItemDisplayContext itemDisplayContext,PoseStack poseStack,MultiBufferSource multiBufferSource,int light,int overlay) {
        Minecraft minecraft=Minecraft.getInstance();
        BakedModel bakedModel=minecraft.getModelManager().getModel(ModClientModels.CARDBOARD_TRIDENT_ITEM);
        poseStack.pushPose();
        poseStack.translate(0.5,0.5,0.5);
        minecraft.getItemRenderer().render(itemStack,ItemDisplayContext.NONE,false,poseStack,multiBufferSource,light,overlay,bakedModel);
        poseStack.popPose();
    }
}
