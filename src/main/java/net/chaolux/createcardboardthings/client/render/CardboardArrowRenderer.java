package net.chaolux.createcardboardthings.client.render;

import net.chaolux.createcardboardthings.common.entity.CardboardArrowEntity;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CardboardArrowRenderer extends ArrowRenderer<CardboardArrowEntity> {
    public CardboardArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(CardboardArrowEntity cardboardArrowEntity) {
        return new ResourceLocation("createcardboardthings", "textures/entity/projectiles/cardboard_arrow.png");
    }
}
