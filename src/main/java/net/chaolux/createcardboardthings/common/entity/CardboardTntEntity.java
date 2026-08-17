package net.chaolux.createcardboardthings.common.entity;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CardboardTntEntity extends PrimedTnt {
    private static final int FUSE=80;
    private static final double FLASH_RADIUS=12;
    private static final int DURATION=20*10;
    private static final double MAX_KNOCKBACK=0.45;
    private static final double NEARBY_RADIUS=4;

    @Nullable
    private LivingEntity livingEntity;
    public CardboardTntEntity(EntityType<? extends CardboardTntEntity> entityType, Level level) {
        super(entityType,level);
    }

    public CardboardTntEntity(Level level,double x,double y,double z,@Nullable LivingEntity livingEntity) {
        this(ModEntities.CARDBOARD_TNT.get(),level);
        this.setPos(x,y,z);
        double angle=level.random.nextDouble() * Math.PI * 2;
        this.setDeltaMovement(-Math.sin(angle) * 0.02,0.2,-Math.cos(angle) * 0.02);
        this.setFuse(FUSE);
        this.xo=x;
        this.yo=y;
        this.zo=z;
        this.livingEntity=livingEntity;
    }

    @Override
    protected void explode() {
        if(!Config.cardboardTnt()) return;
        if(!(this.level() instanceof ServerLevel serverLevel)) return;
        explosionEffects(serverLevel);
        apply(serverLevel);
        activateNearbyTnt(serverLevel);
    }

    private void explosionEffects(ServerLevel serverLevel) {
        serverLevel.playSound(null,this.getX(),this.getY(),this.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS,4.0f,(1.0f + (serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.2f) * 0.7f);
        serverLevel.sendParticles(ParticleTypes.FLASH,this.getX(),this.getY() + 0.5,this.getZ(),1,0.0,0.0,0.0,0.0);
        serverLevel.sendParticles(ParticleTypes.LARGE_SMOKE,this.getX(),this.getY() + 0.4,this.getZ(),20,0.9,0.6,0.9,0.035);
        serverLevel.sendParticles(ParticleTypes.SMOKE,this.getX(),this.getY() + 0.35,this.getZ(),12,0.65,0.4,0.65,0.025);

    }

    private void apply(ServerLevel serverLevel) {
        List<LivingEntity> livingEntityList=serverLevel.getEntitiesOfClass(LivingEntity.class,this.getBoundingBox().inflate(FLASH_RADIUS),entity -> entity.isAlive() && !entity.isSpectator());
        Vec3 vec3=this.position();
        for(LivingEntity living : livingEntityList) {
            double sqrt=living.position().distanceToSqr(vec3);
            if(sqrt > FLASH_RADIUS * FLASH_RADIUS) continue;
            if(!living.hasLineOfSight(this)) continue;
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS,DURATION,0));
            applyKnockback(living,vec3,sqrt);
        }
    }

    private void applyKnockback(LivingEntity livingEntity,Vec3 vec3,double sqrt) {
        Vec3 position=livingEntity.position().subtract(vec3);
        double horizontalSqrt=Math.sqrt(position.x * position.x + position.z * position.z);
        if(horizontalSqrt < 1.0E-6) return;
        double distance=Math.sqrt(sqrt);
        double distanceSqrt=1.0 - Math.min(distance / FLASH_RADIUS,1.0);
        double strength=MAX_KNOCKBACK * (0.35 + 0.65 * distanceSqrt);
        livingEntity.push(position.x / horizontalSqrt * strength, 0.08 + 0.08 * distanceSqrt,position.z / horizontalSqrt * strength);
    }

    private void activateNearbyTnt(ServerLevel serverLevel) {
        BlockPos blockPos=this.blockPosition();
        int radius=(int) Math.ceil(NEARBY_RADIUS);
        BlockPos min=blockPos.offset(-radius,-radius,-radius);
        BlockPos max=blockPos.offset(radius,radius,radius);
        for(BlockPos pos : BlockPos.betweenClosed(min,max)) {
            BlockPos position=pos.immutable();
            if(position.equals(blockPos)) continue;
            if(position.distSqr(blockPos) > NEARBY_RADIUS * NEARBY_RADIUS) continue;
            BlockState blockState=serverLevel.getBlockState(position);
            if(!blockState.is(Blocks.TNT) && !blockState.is(ModBlocks.CARDBOARD_TNT.get())) continue;
            if(!(blockState.getBlock() instanceof TntBlock tntBlock)) continue;
            tntBlock.onCaughtFire(blockState,serverLevel,position,null,this.getOwner());
            serverLevel.removeBlock(position,false);
        }
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        if(this.livingEntity != null) return this.livingEntity;
        return super.getOwner();
    }
}
