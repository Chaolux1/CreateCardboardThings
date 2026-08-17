package net.chaolux.createcardboardthings.common.event;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = "createcardboardthings")
public class CardboardGogglesEvents {
    private static final String COOLDOWN="CardboardGogglesCooldown";
    private static final int DURATION=20*60;
    private static final double RANGE=12;
    private static final double MIN=0.5;
    private static final double MAX=3;
    private static final double CONE=Math.cos(Math.toRadians(45));

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post playerTickEvent) {
        if(!Config.cardboardGoggles()) return;
        Player player=playerTickEvent.getEntity();
        if(player.level().isClientSide) return;
        if(!isGoggles(player)) {
            player.getPersistentData().remove(COOLDOWN);
            return;
        }
        CompoundTag compoundTag=player.getPersistentData();
        int cooldown=compoundTag.getInt(COOLDOWN);
        if(cooldown < DURATION) {
            cooldown++;
            compoundTag.putInt(COOLDOWN,cooldown);
        }
        if(cooldown < DURATION) return;
        if(push(player)) compoundTag.putInt(COOLDOWN,0);
    }

    private static boolean isGoggles(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.CARDBOARD_GOGGLES.get());
    }

    private static boolean push(Player player) {
        Vec3 vec3=player.getEyePosition();
        Vec3 look=player.getViewVector(1.0f).normalize();
        AABB aabb=player.getBoundingBox().inflate(RANGE);
        List<Entity> list=player.level().getEntities(player,aabb,entity -> entity.isAlive() && !entity.isSpectator() && entity.isPushable());
        boolean push=false;
        for(Entity entity : list) {
            Vec3 position=entity.getBoundingBox().getCenter();
            Vec3 toPosition=position.subtract(vec3);
            double sqr=toPosition.lengthSqr();
            if(sqr > RANGE * RANGE) continue;
            if(sqr < 1.0E-6) continue;
            Vec3 direction=toPosition.normalize();
            double cone=look.dot(direction);
            if(cone < CONE) continue;
            if(!player.hasLineOfSight(entity)) continue;
            double force=MIN + player.getRandom().nextDouble() * (MAX - MIN);
            pushEntity(player,entity,force);
            push=true;
        }
        return push;
    }

    private static void pushEntity(Player player,Entity entity,double force) {
        Vec3 vec3=entity.position().subtract(player.position());
        Vec3 horizontal=new Vec3(vec3.x,0.0,vec3.z);
        if(horizontal.lengthSqr() < 1.0E-6) {
            Vec3 look=player.getViewVector(1.0f);
            horizontal=new Vec3(look.x,0.0,look.z);
        }
        if(horizontal.lengthSqr() < 1.0E-6) horizontal=new Vec3(1.0,0.0,0.0);
        horizontal=horizontal.normalize();
        double horizontalForce=0.1 + force * 0.04;
        entity.push(horizontal.x * force,horizontalForce,horizontal.z * force);
    }
}
