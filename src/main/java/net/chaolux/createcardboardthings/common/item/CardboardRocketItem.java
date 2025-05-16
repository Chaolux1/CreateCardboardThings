package net.chaolux.createcardboardthings.common.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;


public class CardboardRocketItem extends FireworkRocketItem {
    public CardboardRocketItem(Properties p_41209_) {
        super(p_41209_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack=player.getItemInHand(hand);
        if(!player.isFallFlying()) {
            return InteractionResultHolder.fail(stack);
        }
        if(!level.isClientSide) {
            FireworkRocketEntity rocket = new FireworkRocketEntity(level, stack, player);
            level.addFreshEntity(rocket);
            if(!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack,level.isClientSide());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }
}
