package net.chaolux.createcardboardthings.integration.curiosapi;

import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.fml.ModList;

public class CardboardEquipment {
    public static boolean isCuriosAPI() {
        return ModList.get().isLoaded("curios");
    }

    public static boolean isElytraSlot() {
        return ModList.get().isLoaded("elytraslot");
    }

    public static boolean isEquipment(LivingEntity livingEntity, Item item) {
        return isCuriosAPI() && CuriosAPIPlugin.isItem(livingEntity,item);
    }

    public static boolean isCardboardGoggles(LivingEntity livingEntity) {
        return livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.CARDBOARD_GOGGLES.get()) || isEquipment(livingEntity,ModItems.CARDBOARD_GOGGLES.get());
    }

    public static boolean isCardboardElytra(LivingEntity livingEntity) {
        return livingEntity.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.CARDBOARD_ELYTRA.get()) || (isElytraSlot() && CuriosAPIPlugin.isItem(livingEntity,ModItems.CARDBOARD_ELYTRA.get()));
    }

    public static boolean isCardboardArmor(LivingEntity livingEntity,EquipmentSlot equipmentSlot) {
        return isCuriosAPI() && CuriosAPIPlugin.isArmor(livingEntity,equipmentSlot);
    }
}
