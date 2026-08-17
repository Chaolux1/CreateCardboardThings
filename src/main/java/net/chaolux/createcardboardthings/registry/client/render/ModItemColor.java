package net.chaolux.createcardboardthings.registry.client.render;

import net.chaolux.createcardboardthings.common.item.ColorCardboardItem;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = "createcardboardthings", value = Dist.CLIENT)
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
