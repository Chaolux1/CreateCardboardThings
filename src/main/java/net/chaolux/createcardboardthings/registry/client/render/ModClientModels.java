package net.chaolux.createcardboardthings.registry.client.render;

import net.chaolux.createcardboardthings.CreateCardboardThings;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "createcardboardthings", bus= Bus.MOD, value = Dist.CLIENT)
public class ModClientModels {
    public static final ResourceLocation CARDBOARD_TRIDENT_ITEM;
    public static final ResourceLocation CARDBOARD_SHIELD_ITEM;

    static {
        CARDBOARD_TRIDENT_ITEM=new ResourceLocation(CreateCardboardThings.MOD_ID,"item/cardboard_trident_item");
        CARDBOARD_SHIELD_ITEM=new ResourceLocation(CreateCardboardThings.MOD_ID,"item/cardboard_shield_item");
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional registerAdditional) {
        registerAdditional.register(CARDBOARD_TRIDENT_ITEM);
        registerAdditional.register(CARDBOARD_SHIELD_ITEM);
    }
}
