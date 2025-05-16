package net.chaolux.createcardboardthings.registry.client.render;

import net.chaolux.createcardboardthings.client.render.CardboardBallRenderer;
import net.chaolux.createcardboardthings.client.render.CardboardElytraLayer;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.chaolux.createcardboardthings.client.render.CardboardArrowRenderer;

@Mod.EventBusSubscriber(modid = "createcardboardthings", bus= Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenders {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CARDBOARD_ARROW.get(), CardboardArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.CARDBOARD_BALL.get(), CardboardBallRenderer::new);
    }

    @SubscribeEvent
    public static void onAddLayer(EntityRenderersEvent.AddLayers event) {
        for(String skin:event.getSkins()) {
            EntityRenderer<? extends Player> rendererRaw=event.getSkin(skin);
            if(rendererRaw instanceof LivingEntityRenderer<?,?> genericRenderer) {
                LivingEntityRenderer<?,?> rawRenderer=genericRenderer;
                if(rawRenderer.getModel() instanceof PlayerModel<?> playerModel) {
                    @SuppressWarnings("unchecked")
                    LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> castedRenderer =
                            (LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) rawRenderer;
                    castedRenderer.addLayer(new CardboardElytraLayer<>(castedRenderer, event.getEntityModels()));
                }
            }
        }
    }
}
