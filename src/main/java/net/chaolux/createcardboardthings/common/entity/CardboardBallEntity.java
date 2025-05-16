package net.chaolux.createcardboardthings.common.entity;

import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class CardboardBallEntity extends ThrowableItemProjectile {
    public CardboardBallEntity(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
    }

    public CardboardBallEntity(EntityType<? extends ThrowableItemProjectile> p_37432_, double p_37433_, double p_37434_, double p_37435_, Level p_37436_) {
        super(p_37432_, p_37433_, p_37434_, p_37435_, p_37436_);
    }

    public CardboardBallEntity(EntityType<? extends ThrowableItemProjectile> p_37438_, LivingEntity p_37439_, Level p_37440_) {
        super(p_37438_, p_37439_, p_37440_);
    }

    public CardboardBallEntity(Level level, LivingEntity thrower) {
        super(ModEntities.CARDBOARD_BALL.get(), thrower, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if(!this.level().isClientSide) {
            if(result.getEntity() instanceof LivingEntity target) {
                target.hurt(this.damageSources().thrown(this, this.getOwner()), 1.0F);
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if(!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.CARDBOARD_BALL.get();
    }
}
