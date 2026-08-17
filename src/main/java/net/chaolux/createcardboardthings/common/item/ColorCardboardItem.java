package net.chaolux.createcardboardthings.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ColorCardboardItem extends Item {
    private final DyeColor dyeColor;
    public ColorCardboardItem(DyeColor dyeColor,Properties properties) {
        super(properties);
        this.dyeColor=dyeColor;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.createcardboardthings.color_cardboard").withStyle(ChatFormatting.GRAY));
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    public int tint() {
        return tint(this.dyeColor);
    }

    public static int tint(DyeColor dyeColor) {
        return dyeColor.getTextureDiffuseColor();
    }


}
