package net.chaolux.createcardboardthings.common.entity;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CardboardTridentEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID= SynchedEntityData.defineId(CardboardTridentEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL=SynchedEntityData.defineId(CardboardTridentEntity.class,EntityDataSerializers.BOOLEAN);
    private static final Logger log = LoggerFactory.getLogger(CardboardTridentEntity.class);
    private ItemStack itemStack=ItemStack.EMPTY;
    private boolean damage;
    public int client;
    public CardboardTridentEntity(EntityType<? extends CardboardTridentEntity> type, Level level) {
        super(type,level);
        this.itemStack=new ItemStack(ModItems.CARDBOARD_TRIDENT.get());
    }

    public CardboardTridentEntity(Level level, LivingEntity livingEntity,ItemStack stack) {
        super(ModEntities.CARDBOARD_TRIDENT.get(),livingEntity,level,stack.copyWithCount(1),stack);
        this.itemStack=stack.copyWithCount(1);
        this.refreshEnchantment();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID,(byte) 0);
        builder.define(ID_FOIL,false);
    }

    @Override
    public void tick() {
        if(!Config.cardboardTrident()) {
            this.discard();
            return;
        }
        if(touchFluid()) {
            destroyFluid();
            return;
        }
        if(this.inGroundTime > 4) this.damage=true;
        Entity entity=this.getOwner();
        int loyalty=this.entityData.get(ID);
        if(loyalty > 0 && (this.damage || this.isNoPhysics()) && entity != null) {
            if(!isReturn()) {
                if(!this.level().isClientSide && this.pickup == Pickup.ALLOWED) this.spawnAtLocation(this.itemStack.copy(),0.1f);
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3=entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(),this.getY() + vec3.y * 0.015 * loyalty,this.getZ());
                if(this.level().isClientSide) this.yOld=this.getY();
                double speed=0.05 * loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(speed)));
                if(this.client == 0) this.playSound(SoundEvents.TRIDENT_RETURN,10.0f,1.0f);
                ++this.client;
            }
        }
        super.tick();
        if(touchFluid()) destroyFluid();
    }

    @Override
    protected EntityHitResult findHitEntity(Vec3 start,Vec3 end) {
        return this.damage ? null : super.findHitEntity(start,end);
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity=entityHitResult.getEntity();
        float damage=1.0f;
        int impaling= enchantment(Enchantments.IMPALING);
        if(impaling > 0 && entity.getType().is(EntityTypeTags.SENSITIVE_TO_IMPALING)) damage += 2.5f * impaling;
        Entity owner=this.getOwner();
        Entity damageOwner=owner == null ? this : owner;
        this.damage=true;
        SoundEvent soundEvent=SoundEvents.TRIDENT_HIT;
        float sound=1.0f;
        if(entity.hurt(this.damageSources().trident(this,damageOwner),damage)) {
            if(entity.getType() == EntityType.ENDERMAN) return;
            if(entity instanceof LivingEntity livingEntity) {
                this.doPostHurtEffects(livingEntity);
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01,-0.1,-0.01));
        if(this.level() instanceof ServerLevel serverLevel && serverLevel.isThundering() && this.isChanneling()) {
            BlockPos blockPos=entity.blockPosition();
            if(serverLevel.canSeeSky(blockPos)) {
                LightningBolt lightningBolt=EntityType.LIGHTNING_BOLT.create(serverLevel);
                if(lightningBolt != null) {
                    lightningBolt.moveTo(Vec3.atBottomCenterOf(blockPos));
                    lightningBolt.setCause(owner instanceof ServerPlayer serverPlayer ? serverPlayer : null);
                    serverLevel.addFreshEntity(lightningBolt);
                    soundEvent=SoundEvents.TRIDENT_THUNDER.value();
                    sound=5.0f;
                }
            }
        }
        this.playSound(soundEvent,sound,1.0f);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        Entity entity=this.getOwner();
        if(entity == null || entity.getUUID().equals(player.getUUID())) super.playerTouch(player);
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return this.itemStack.copy();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains("Trident", Tag.TAG_COMPOUND)) this.itemStack=ItemStack.parseOptional(this.level().registryAccess(),compoundTag.getCompound("Trident"));
        this.damage=compoundTag.getBoolean("DeltaDamage");
        this.refreshEnchantment();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if(!this.itemStack.isEmpty()) compoundTag.put("Trident",this.itemStack.save(this.level().registryAccess()));
        compoundTag.putBoolean("DeltaDamage",this.damage);
    }

    @Override
    public void tickDespawn() {
        int loyalty=this.entityData.get(ID);
        if(this.pickup != Pickup.ALLOWED || loyalty <= 0) super.tickDespawn();
    }

    @Override
    protected float getWaterInertia() {
        return 0.99f;
    }

    @Override
    public boolean shouldRender(double x,double y,double z) {
        return true;
    }

    private boolean isReturn() {
        Entity entity=this.getOwner();
        if(entity == null || !entity.isAlive()) return false;
        return !(entity instanceof ServerPlayer serverPlayer) || !serverPlayer.isSpectator();
    }

    public boolean isChanneling() {
        return enchantment(Enchantments.CHANNELING) > 0;
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    private boolean touchFluid() {
        if(this.isInWater() || this.isInLava()) return true;
        Vec3 start=this.position();
        Vec3 end=start.add(this.getDeltaMovement());
        BlockHitResult blockHitResult=this.level().clip(new ClipContext(start,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.ANY,this));
        if(blockHitResult.getType() != BlockHitResult.Type.BLOCK) return false;
        return this.level().getFluidState(blockHitResult.getBlockPos()).is(FluidTags.WATER) || this.level().getFluidState(blockHitResult.getBlockPos()).is(FluidTags.LAVA);
    }

    private void destroyFluid() {
        if(!this.level().isClientSide) {
            this.level().playSound(null,this.getX(),this.getY(),this.getZ(),SoundEvents.ITEM_BREAK, SoundSource.PLAYERS,0.9f,0.85f + this.random.nextFloat() * 0.3f);
            this.discard();
        }
    }

    private int enchantment(ResourceKey<Enchantment> resourceKey) {
        HolderLookup.RegistryLookup<Enchantment> holderLookup=this.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> enchantmentHolder=holderLookup.getOrThrow(resourceKey);
        return EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder,this.itemStack);
    }

    private void refreshEnchantment() {
        this.entityData.set(ID,(byte) enchantment(Enchantments.LOYALTY));
        this.entityData.set(ID_FOIL,this.itemStack.hasFoil());
    }
}
