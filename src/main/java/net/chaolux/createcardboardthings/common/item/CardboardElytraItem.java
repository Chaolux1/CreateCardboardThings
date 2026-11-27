package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

public class CardboardElytraItem extends ElytraItem {
    public CardboardElytraItem(Properties p_41132_) {
        super(p_41132_);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return false;
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return Config.cardboardElytra();
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if(level.isClientSide || !(entity instanceof Player player)) return;
        if(!player.isFallFlying() && isUse(stack)) {
            stack.shrink(1);
            player.inventoryMenu.broadcastChanges();
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.createcardboardthings.cardboard_elytra").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean elytraFlightTick(ItemStack itemStack,LivingEntity livingEntity,int value) {
        if(!canElytraFly(itemStack,livingEntity)) return false;
        if(!livingEntity.level().isClientSide && livingEntity instanceof Player) {
            ItemStack stack=livingEntity.getItemBySlot(EquipmentSlot.CHEST);
            if(stack == itemStack || !stack.canElytraFly(livingEntity)) {
                if(!isUse(itemStack)) itemStack.getOrCreateTag().putBoolean("flightStart",true);
            }
        }
        return true;
    }

    public static boolean isUse(ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().getBoolean("flightStart");
    }
}
