package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.List;

public class CardboardBucketItem extends Item {
    private static final int DURATION=20*10;
    private static final int LAVA_FIRE=5;
    private static final MobEffect[] EFFECTS= {
            MobEffects.POISON,MobEffects.BLINDNESS,MobEffects.WEAKNESS,MobEffects.MOVEMENT_SLOWDOWN
    };
    public CardboardBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack=player.getItemInHand(interactionHand);
        if(!Config.cardboardBucket()) return InteractionResultHolder.pass(itemStack);
        BlockHitResult blockHitResult=getPlayerPOVHitResult(level,player, ClipContext.Fluid.SOURCE_ONLY);
        if(blockHitResult.getType() == HitResult.Type.MISS) return InteractionResultHolder.pass(itemStack);
        if(blockHitResult.getType() != HitResult.Type.BLOCK) return InteractionResultHolder.pass(itemStack);
        BlockPos blockPos=blockHitResult.getBlockPos();
        if(!level.mayInteract(player,blockPos) || !player.mayUseItemAt(blockPos,blockHitResult.getDirection(),itemStack)) return InteractionResultHolder.fail(itemStack);
        BlockState blockState=level.getBlockState(blockPos);
        FluidState fluidState=level.getFluidState(blockPos);
        if(!fluidState.isSource() || !(blockState.getBlock() instanceof BucketPickup bucketPickup)) return InteractionResultHolder.fail(itemStack);
        ItemStack stack=bucketPickup.pickupBlock(level,blockPos,blockState);
        if(stack.isEmpty()) return InteractionResultHolder.fail(itemStack);
        player.awardStat(Stats.ITEM_USED.get(this));
        bucketPickup.getPickupSound(blockState).ifPresent(soundEvent -> level.playSound(null,blockPos,soundEvent, SoundSource.BLOCKS,1.0f,1.0f));
        level.gameEvent(player, GameEvent.FLUID_PICKUP,blockPos);
        if(!level.isClientSide) apply(player,fluidState,level);
        return InteractionResultHolder.sidedSuccess(itemStack,level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.createcardboardthings.cardboard_bucket").withStyle(ChatFormatting.GRAY));
    }

    private static void apply(Player player,FluidState fluidState,Level level) {
        if(fluidState.is(FluidTags.LAVA)) {
            player.setSecondsOnFire(LAVA_FIRE);
            return;
        }
        if(fluidState.is(FluidTags.WATER)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,DURATION,0));
            return;
        }
        MobEffect mobEffect=EFFECTS[level.random.nextInt(EFFECTS.length)];
        player.addEffect(new MobEffectInstance(mobEffect,DURATION,0));
    }
}
