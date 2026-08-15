package net.chaolux.createcardboardthings.common.entity;

import net.chaolux.createcardboardthings.common.utility.CardboardMarkerUtils;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.network.NetworkHooks;

public class CardboardMarkerEntity extends Entity {
    private static final EntityDataAccessor<Integer> COLOR= SynchedEntityData.defineId(CardboardMarkerEntity.class, EntityDataSerializers.INT);
    public CardboardMarkerEntity(EntityType<? extends CardboardMarkerEntity> entityType, Level level) {
        super(entityType,level);
        this.noPhysics=true;
        this.setNoGravity(true);
    }

    public CardboardMarkerEntity(Level level, BlockPos blockPos, DyeColor dyeColor) {
        this(ModEntities.CARDBOARD_MARKER.get(),level);
        this.setPos(blockPos.getX() + 0.5,blockPos.getY() + 0.5,blockPos.getZ() + 0.5);
        this.setColor(dyeColor);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLOR,DyeColor.WHITE.getId());
    }

    @Override
    public void tick() {
        super.tick();
        this.noPhysics=true;
        this.setDeltaMovement(0.0,0.0,0.0);
        if(!this.level().isClientSide && this.tickCount % 20 == 0) {
            if(!CardboardMarkerUtils.isMark(this.level().getBlockState(this.blockPosition()))) this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.setColor(DyeColor.byId(compoundTag.getInt("Color")));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("Color",this.getDyeColor().getId());
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public DyeColor getDyeColor() {
        return DyeColor.byId(this.entityData.get(COLOR));
    }

    public void setColor(DyeColor dyeColor) {
        this.entityData.set(COLOR,dyeColor.getId());
    }
}
