package net.chaolux.createcardboardthings.client.render;

import net.chaolux.createcardboardthings.common.entity.CardboardArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CardboardArrowRenderer extends ArrowRenderer<CardboardArrowEntity> {
    public CardboardArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CardboardArrowEntity cardboardArrowEntity) {
        return ResourceLocation.fromNamespaceAndPath("createcardboardthings", "textures/entity/projectiles/cardboard_arrow.png");
    }
}
