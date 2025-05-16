package net.chaolux.createcardboardthings.common.event;

import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber
public class CardboardItemEvents {
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if (left.getItem().builtInRegistryHolder().key().location().equals(new ResourceLocation("create", "cardboard_sword")) && right.getItem() == ModItems.CARDBOARD_INGOT.get()) {
            ItemStack output = left.copy();
            Map<Enchantment, Integer> ench = EnchantmentHelper.getEnchantments(output);
            int currentKnockback = ench.getOrDefault(Enchantments.KNOCKBACK, 0);
            ench.put(Enchantments.KNOCKBACK, currentKnockback + 1);
            EnchantmentHelper.setEnchantments(ench, output);
            event.setOutput(output);
            event.setCost(10);
        }
    }
}
