package net.chaolux.createcardboardthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

public class CardboardGogglesModel extends BakedModelWrapper<BakedModel> {
    private final BakedModel bakedModel;
    public CardboardGogglesModel(BakedModel model,BakedModel bakedModel) {
        super(model);
        this.bakedModel=bakedModel;
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext itemDisplayContext, PoseStack poseStack,boolean hand) {
        if(itemDisplayContext == ItemDisplayContext.HEAD) return bakedModel.applyTransform(itemDisplayContext,poseStack,hand);
        return super.applyTransform(itemDisplayContext,poseStack,hand);
    }
}
