package net.chaolux.createcardboardthings.common.event;

import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CardboardTotemEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
            ItemStack totem = currentCardboardTotem(entity);
            if (!totem.isEmpty()) {
                event.setCanceled(true);
                entity.setHealth(1.0F);
                entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 320, 1));
                entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 640, 1));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 640, 1));
                entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 160, 1));

                if (entity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundEntityEventPacket(serverPlayer, (byte) 35));
                }

                InteractionHand hand = entity.getMainHandItem() == totem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                entity.setItemInHand(hand, ItemStack.EMPTY);
            }
    }

    private static ItemStack currentCardboardTotem(LivingEntity entity) {
        if(entity.getMainHandItem().is(ModItems.CARDBOARD_TOTEM.get())) {
            return entity.getMainHandItem();
        }
        if(entity.getOffhandItem().is(ModItems.CARDBOARD_TOTEM.get())) {
            return entity.getOffhandItem();
        }
        return ItemStack.EMPTY;
    }
}
