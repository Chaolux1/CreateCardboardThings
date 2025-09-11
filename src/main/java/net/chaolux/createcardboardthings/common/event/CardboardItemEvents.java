package net.chaolux.createcardboardthings.common.event;

import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

import java.util.Map;

@EventBusSubscriber
public class CardboardItemEvents {
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        boolean isCreateCardboardSword=left.getItem().builtInRegistryHolder().key().location().equals(ResourceLocation.fromNamespaceAndPath("create","cardboard_sword"));
        boolean isCardboardIngot=right.getItem() == ModItems.CARDBOARD_INGOT.get();
        if(!isCreateCardboardSword || !isCardboardIngot) {
            return;
        }
        ItemStack output=left.copy();
        HolderLookup.RegistryLookup<Enchantment> ench=event.getPlayer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> knockback=ench.getOrThrow(Enchantments.KNOCKBACK);

        int current=EnchantmentHelper.getItemEnchantmentLevel(knockback,output);
        int next=Math.min(current + 1,255);
        if(next > 0) {
            output.enchant(knockback,next);
        }
        event.setOutput(output);
        event.setCost(10);
        event.setMaterialCost(1);
    }
}
