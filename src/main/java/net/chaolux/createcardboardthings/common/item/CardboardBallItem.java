package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.common.entity.CardboardBallEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

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
            itemStack.shrink(1);
        }
        player.getCooldowns().addCooldown(this,10);
        return InteractionResultHolder.sidedSuccess(itemStack,level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.createcardboardthings.cardboard_ball").withStyle(ChatFormatting.GRAY));
    }
}
