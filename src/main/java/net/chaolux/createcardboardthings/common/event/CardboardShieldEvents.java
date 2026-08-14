package net.chaolux.createcardboardthings.common.event;

import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "createcardboardthings", bus= Bus.FORGE)
public class CardboardShieldEvents {
    private static final double MIN_SPEED=0.5;
    private static final double OFFSET=0.75;

    @SubscribeEvent
    public static void onProjectile(ProjectileImpactEvent projectileImpactEvent) {
        if(!(projectileImpactEvent.getRayTraceResult() instanceof EntityHitResult entityHitResult)) return;
        if(!(entityHitResult.getEntity() instanceof Player player)) return;
        if(!isUsedCardboardShield(player)) return;
        Projectile projectile=projectileImpactEvent.getProjectile();
        if(!isProjectile(player,projectile)) return;
        reflectProjectile(player,projectile);
        getDamage(player);
        projectileImpactEvent.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
        if(!projectile.level().isClientSide) projectile.level().playSound(null,player.getX(),player.getY(),player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS,1.0f,1.0f);
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent shieldBlockEvent) {
        if(!(shieldBlockEvent.getEntity() instanceof Player player)) return;
        if(!player.getUseItem().is(ModItems.CARDBOARD_SHIELD.get())) return;
        Entity entity=shieldBlockEvent.getDamageSource().getDirectEntity();
        if(entity instanceof Projectile projectile) {
            reflectProjectile(player,projectile);
            getDamage(player);
            shieldBlockEvent.setBlockedDamage(shieldBlockEvent.getOriginalBlockedDamage());
            shieldBlockEvent.setShieldTakesDamage(false);
            if(!projectile.level().isClientSide) projectile.level().playSound(null,player.getX(),player.getY(),player.getZ(),SoundEvents.SHIELD_BLOCK,SoundSource.PLAYERS,1.0f,1.0f);
            return;
        }
        shieldBlockEvent.setCanceled(true);
    }

    private static boolean isUsedCardboardShield(Player player) {
        return player.isUsingItem() && player.getUseItem().is(ModItems.CARDBOARD_SHIELD.get());
    }

    private static boolean isProjectile(Player player,Projectile projectile) {
        Vec3 look=player.getViewVector(1.0f);
        Vec3 toProjectile=projectile.position().subtract(player.getEyePosition());
        Vec3 horizontalLook=new Vec3(look.x,0.0,look.z);
        Vec3 horizontalProjectile=new Vec3(toProjectile.x,0.0,toProjectile.z);
        if(horizontalLook.lengthSqr() < 1.0E-6 || horizontalProjectile.lengthSqr() < 1.0E-6) return true;
        return horizontalLook.normalize().dot(horizontalProjectile.normalize()) > 0.0;
    }

    private static void reflectProjectile(Player player,Projectile projectile) {
        Vec3 current=projectile.getDeltaMovement();
        double speed=Math.max(current.length(),MIN_SPEED);
        Vec3 position=getReturn(player,projectile,current);
        projectile.setDeltaMovement(position.scale(speed));
        Vec3 newPosition=projectile.position().add(position.scale(OFFSET));
        projectile.setPos(newPosition.x,newPosition.y,newPosition.z);
        projectile.hasImpulse=true;
    }

    private static Vec3 getReturn(Player player,Projectile projectile,Vec3 vec3) {
        Entity entity=projectile.getOwner();
        if(entity != null && entity != player && entity.isAlive()) {
            Vec3 position=getPosition(entity);
            Vec3 owner=position.subtract(projectile.position());
            if(owner.lengthSqr() > 1.0E-6) return owner.normalize();
        }
        if(vec3.lengthSqr() > 1.0E-6) return vec3.scale(-1.0).normalize();
        return player.getViewVector(1.0f).normalize();
    }

    private static Vec3 getPosition(Entity entity) {
        if(entity instanceof LivingEntity livingEntity) return livingEntity.getEyePosition();
        return entity.getBoundingBox().getCenter();
    }

    private static void getDamage(Player player) {
        if(player.level().isClientSide) return;
        ItemStack itemStack=player.getUseItem();
        if(!itemStack.is(ModItems.CARDBOARD_SHIELD.get())) return;
        InteractionHand interactionHand=player.getUsedItemHand();
        itemStack.hurtAndBreak(1,player,entity -> entity.broadcastBreakEvent(interactionHand));
    }
}

