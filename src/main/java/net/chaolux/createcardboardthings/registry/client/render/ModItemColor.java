package net.chaolux.createcardboardthings.registry.client.render;

import net.chaolux.createcardboardthings.common.item.ColorCardboardItem;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "createcardboardthings", bus= Bus.MOD, value = Dist.CLIENT)
public class ModItemColor {
    @SubscribeEvent
    public static void register(RegisterColorHandlersEvent.Item item) {
        ItemLike[] itemLikes=new ItemLike[ModItems.COLOR_CARDBOARD.size()];
        for(int index=0;index < ModItems.COLOR_CARDBOARD.size();index++) {
            itemLikes[index] = ModItems.COLOR_CARDBOARD.get(index).get();
        }
        item.register((itemStack, tint) -> {
            if(tint != 0) return 0xFFFFFF;
            if(itemStack.getItem() instanceof ColorCardboardItem colorCardboardItem) return colorCardboardItem.tint();
            return 0xFFFFFF;
            },itemLikes);
    }
}
