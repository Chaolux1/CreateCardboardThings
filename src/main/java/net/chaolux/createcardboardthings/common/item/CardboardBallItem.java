package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.common.entity.CardboardBallEntity;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

public class CardboardBallItem extends SnowballItem {
    public CardboardBallItem(Properties p_43140_) {
        super(p_43140_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack=player.getItemInHand(hand);
        if(!level.isClientSide) {
            CardboardBallEntity entity=new CardboardBallEntity(level,player);
            entity.setItem(itemStack);
            entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(entity);
        }
        player.getCooldowns().addCooldown(this,10);
        return InteractionResultHolder.sidedSuccess(itemStack,level.isClientSide());
    }
}
