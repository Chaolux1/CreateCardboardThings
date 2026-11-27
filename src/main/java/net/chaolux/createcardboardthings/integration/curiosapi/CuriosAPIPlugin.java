package net.chaolux.createcardboardthings.integration.curiosapi;

import com.simibubi.create.content.equipment.armor.CardboardArmorItem;
import net.chaolux.createcardboardthings.common.item.CardboardArrowItem;
import net.chaolux.createcardboardthings.common.item.CardboardElytraItem;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

public class CuriosAPIPlugin {
    public static boolean isItem(LivingEntity livingEntity, Item item) {
        return CuriosApi.getCuriosInventory(livingEntity).map(iCuriosItemHandler -> iCuriosItemHandler.isEquipped(item)).orElse(false);
    }

    public static boolean isArmor(LivingEntity livingEntity, EquipmentSlot equipmentSlot) {
        return !find(livingEntity,equipmentSlot).isEmpty();
    }

    public static ItemStack find(LivingEntity livingEntity,EquipmentSlot equipmentSlot) {
        return CuriosApi.getCuriosInventory(livingEntity).map(iCuriosItemHandler -> iCuriosItemHandler.findFirstCurio(itemStack -> itemStack.getItem() instanceof CardboardArmorItem cardboardArmorItem && cardboardArmorItem.getEquipmentSlot() == equipmentSlot).map(SlotResult::stack).orElse(ItemStack.EMPTY)).orElse(ItemStack.EMPTY);
    }

    public static void set(Player player) {
        if(player.level().isClientSide || player.isFallFlying()) return;
        CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> {
            for(var value : iCuriosItemHandler.findCurios(ModItems.CARDBOARD_ELYTRA.get())) {
                if(!CardboardElytraItem.isUse(value.stack())) continue;
                var data=value.slotContext();
                iCuriosItemHandler.setEquippedCurio(data.identifier(),data.index(),ItemStack.EMPTY);
            }
        });
    }
}
