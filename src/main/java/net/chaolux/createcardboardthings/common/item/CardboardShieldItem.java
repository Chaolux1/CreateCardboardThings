package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.client.render.CardboardShieldItemRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class CardboardShieldItem extends ShieldItem {
    public CardboardShieldItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.createcardboardthings.cardboard_shield").withStyle(ChatFormatting.GRAY));
    }
}
