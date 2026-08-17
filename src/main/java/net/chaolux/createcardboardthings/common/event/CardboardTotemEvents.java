package net.chaolux.createcardboardthings.common.event;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = "createcardboardthings")
public class CardboardTotemEvents {
    private static final double FORCE=2.6;
    private static final double UP_FORCE=0.85;
    private static final int DURATION=20*10;

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if(!Config.cardboardTotem()) return;
        LivingEntity livingEntity=event.getEntity();
        if(livingEntity.level().isClientSide) return;
        InteractionHand interactionHand=currentCardboardTotem(livingEntity);
        if(interactionHand == null) return;
        event.setCanceled(true);
        livingEntity.setHealth(1.0f);
        livingEntity.fallDistance=0.0f;
        livingEntity.clearFire();
        Entity entity=event.getSource().getEntity();
        if(entity == null) entity=event.getSource().getDirectEntity();
        if(entity != null && entity != livingEntity) {
            force(livingEntity,entity);
        } else {
            panic(livingEntity);
        }
        livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,DURATION,0));
        livingEntity.level().broadcastEntityEvent(livingEntity,(byte) 35);
        ItemStack itemStack=livingEntity.getItemInHand(interactionHand);
        itemStack.shrink(1);
    }

    private static void force(LivingEntity livingEntity,Entity entity) {
        Vec3 vec3=livingEntity.position().subtract(entity.position());
        Vec3 horizontal=new Vec3(vec3.x,0.0,vec3.z);
        if(horizontal.lengthSqr() < 1.0E-6) {
            panic(livingEntity);
            return;
        }
        horizontal=horizontal.normalize();
        livingEntity.setDeltaMovement(horizontal.x * FORCE,UP_FORCE,horizontal.z * FORCE);
        livingEntity.hurtMarked=true;
    }

    private static void panic(LivingEntity livingEntity) {
        livingEntity.setDeltaMovement(0.0,UP_FORCE,0.0);
        livingEntity.hurtMarked=true;
    }

    @Nullable
    private static InteractionHand currentCardboardTotem(LivingEntity entity) {
        if(entity.getMainHandItem().is(ModItems.CARDBOARD_TOTEM.get())) {
            return InteractionHand.MAIN_HAND;
        }
        if(entity.getOffhandItem().is(ModItems.CARDBOARD_TOTEM.get())) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }
}