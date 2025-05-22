package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.common.entity.CardboardArrowEntity;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;


public class CardboardArrowItem extends ArrowItem {
    public CardboardArrowItem(Properties p_40512_) {
        super(p_40512_);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        return new CardboardArrowEntity(level, shooter);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.createcardboardthings.cardboard_arrow").withStyle(ChatFormatting.GRAY));
    }
}
