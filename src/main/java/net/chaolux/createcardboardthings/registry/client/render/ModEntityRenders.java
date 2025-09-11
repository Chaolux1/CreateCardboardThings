package net.chaolux.createcardboardthings.registry.client.render;

import net.chaolux.createcardboardthings.client.render.CardboardArrowRenderer;
import net.chaolux.createcardboardthings.client.render.CardboardBallRenderer;
import net.chaolux.createcardboardthings.client.render.CardboardElytraLayer;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static net.chaolux.createcardboardthings.CreateCardboardThings.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = net.neoforged.api.distmarker.Dist.CLIENT)
public class ModEntityRenders {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CARDBOARD_ARROW.get(), CardboardArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.CARDBOARD_BALL.get(), CardboardBallRenderer::new);
    }

    @SubscribeEvent
    public static void onAddLayer(EntityRenderersEvent.AddLayers event) {
        for(PlayerSkin.Model skin:event.getSkins()) {
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
