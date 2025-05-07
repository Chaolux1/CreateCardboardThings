package net.chaolux.createcardboardthings.common.entity;

import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class CardboardArrowEntity extends AbstractArrow {

    public CardboardArrowEntity(EntityType<? extends CardboardArrowEntity> type, Level level) {
        super(type, level);
    }

    public CardboardArrowEntity(Level level, LivingEntity shooter) {
        super(ModEntities.CARDBOARD_ARROW.get(), shooter, level);
    }

    public CardboardArrowEntity(EntityType<? extends CardboardArrowEntity> type, Level level, FriendlyByteBuf buf) {
        super(type, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.CARDBOARD_ARROW.get());
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if(!this.level().isClientSide()) {
            if(hitResult.getEntity() instanceof LivingEntity target) {
                var knockbackarrow = 2.5;

                double dx = target.getX() - this.getX();
                double dz = target.getZ() - this.getZ();
                double radius = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));

                target.push((dx/radius)*knockbackarrow, 0.2, (dz/radius)*knockbackarrow);

                this.discard();
            }
        }
    }

    @Override
    public double getBaseDamage() {
        return 0.0;
    }
}
