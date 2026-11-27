package net.chaolux.createcardboardthings.common.mixin;

import com.simibubi.create.content.equipment.armor.CardboardArmorHandler;
import com.simibubi.create.content.equipment.armor.CardboardArmorItem;
import net.chaolux.createcardboardthings.integration.curiosapi.CardboardEquipment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CardboardArmorHandler.class,remap = false)
public abstract class CardboardArmorHandlerMixin {
    @Unique
    private static final EquipmentSlot[] EQUIPMENT_SLOT= {
            EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS,EquipmentSlot.FEET
    };

    @Inject(method = "testForStealth",at = @At("HEAD"),cancellable = true,remap = false)
    private static void createcardboardthings$setCuriosAPI(Entity entity, CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable) {
        if(!CardboardEquipment.isCuriosAPI() || !(entity instanceof Player player)) return;
        if(player.getPose() != Pose.CROUCHING || player.getAbilities().flying) return;
        for(EquipmentSlot equipmentSlot : EQUIPMENT_SLOT) {
            if(!(player.getItemBySlot(equipmentSlot).getItem() instanceof CardboardArmorItem) && !CardboardEquipment.isCardboardArmor(player,equipmentSlot)) return;
        }
        booleanCallbackInfoReturnable.setReturnValue(true);
    }
}
