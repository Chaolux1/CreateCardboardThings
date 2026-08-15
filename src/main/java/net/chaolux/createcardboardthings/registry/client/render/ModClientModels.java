package net.chaolux.createcardboardthings.registry.client.render;

import net.chaolux.createcardboardthings.CreateCardboardThings;
import net.chaolux.createcardboardthings.client.render.CardboardGogglesModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
    public static final ResourceLocation CARDBOARD_GOGGLES_ITEM;
    public static final ResourceLocation CARDBOARD_GOGGLES_HEAD;
    public static final ResourceLocation CARDBOARD_MARKER_BUTTON;
    public static final ResourceLocation CARDBOARD_MARKER_LEVER;

    static {
        CARDBOARD_TRIDENT_ITEM=new ResourceLocation(CreateCardboardThings.MOD_ID,"item/cardboard_trident_item");
        CARDBOARD_SHIELD_ITEM=new ResourceLocation(CreateCardboardThings.MOD_ID,"item/cardboard_shield_item");
        CARDBOARD_GOGGLES_ITEM=new ModelResourceLocation(new ResourceLocation(CreateCardboardThings.MOD_ID,"cardboard_goggles"),"inventory");
        CARDBOARD_GOGGLES_HEAD=new ResourceLocation(CreateCardboardThings.MOD_ID,"item/cardboard_goggles_head");
        CARDBOARD_MARKER_BUTTON=new ResourceLocation(CreateCardboardThings.MOD_ID,"block/cardboard_marker_button");
        CARDBOARD_MARKER_LEVER=new ResourceLocation(CreateCardboardThings.MOD_ID,"block/cardboard_marker_lever");
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional registerAdditional) {
        registerAdditional.register(CARDBOARD_TRIDENT_ITEM);
        registerAdditional.register(CARDBOARD_SHIELD_ITEM);
        registerAdditional.register(CARDBOARD_GOGGLES_HEAD);
        registerAdditional.register(CARDBOARD_MARKER_BUTTON);
        registerAdditional.register(CARDBOARD_MARKER_LEVER);
    }

    @SubscribeEvent
    public static void modifyModels(ModelEvent.ModifyBakingResult modifyBakingResult) {
        BakedModel bakedModel=modifyBakingResult.getModels().get(CARDBOARD_GOGGLES_ITEM);
        BakedModel model=modifyBakingResult.getModels().get(CARDBOARD_GOGGLES_HEAD);
        if(bakedModel == null || model == null) return;
        modifyBakingResult.getModels().put(CARDBOARD_GOGGLES_ITEM,new CardboardGogglesModel(bakedModel,model));
    }
}
