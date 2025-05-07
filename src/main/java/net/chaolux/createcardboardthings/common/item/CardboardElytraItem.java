package net.chaolux.createcardboardthings.common.item;

import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

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
        return true;
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
        if(!(entity instanceof Player player)) return;
        if(level.isClientSide) return;
        ItemStack chest=player.getItemBySlot(EquipmentSlot.CHEST);
        if(!chest.is(ModItems.CARDBOARD_ELYTRA.get())) return;
        boolean isFlying=player.isFallFlying();
        var tag=stack.getOrCreateTag();
        boolean flightStart=tag.getBoolean("flightStart");

        if(isFlying && !flightStart) {
            tag.putBoolean("flightStart", true);
        }
        else if(!isFlying && flightStart) {
            player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            player.inventoryMenu.broadcastChanges();
        }
    }
}