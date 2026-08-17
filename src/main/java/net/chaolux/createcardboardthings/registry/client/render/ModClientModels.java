package net.chaolux.createcardboardthings.registry.client.render;

import net.chaolux.createcardboardthings.CreateCardboardThings;
import net.chaolux.createcardboardthings.client.render.CardboardGogglesModel;
import net.chaolux.createcardboardthings.client.render.CardboardShieldItemRenderer;
import net.chaolux.createcardboardthings.client.render.CardboardTridentItemRenderer;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = "createcardboardthings", value = Dist.CLIENT)
public class ModClientModels {
    public static final ModelResourceLocation CARDBOARD_TRIDENT_ITEM;
    public static final ModelResourceLocation CARDBOARD_SHIELD_ITEM;
    public static final ModelResourceLocation CARDBOARD_GOGGLES_ITEM;
    public static final ModelResourceLocation CARDBOARD_GOGGLES_HEAD;
    public static final ModelResourceLocation CARDBOARD_MARKER_BUTTON;
    public static final ModelResourceLocation CARDBOARD_MARKER_LEVER;

    static {
        CARDBOARD_TRIDENT_ITEM=ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"item/cardboard_trident_item"));
        CARDBOARD_SHIELD_ITEM=ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"item/cardboard_shield_item"));
        CARDBOARD_GOGGLES_ITEM=ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"cardboard_goggles"));
        CARDBOARD_GOGGLES_HEAD=ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"item/cardboard_goggles_head"));
        CARDBOARD_MARKER_BUTTON=ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"block/cardboard_marker_button"));
        CARDBOARD_MARKER_LEVER=ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(CreateCardboardThings.MOD_ID,"block/cardboard_marker_lever"));
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

    @SubscribeEvent
    public static void registerInitializeClient(RegisterClientExtensionsEvent registerClientExtensionsEvent) {
        registerClientExtensionsEvent.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return CardboardTridentItemRenderer.getInstance();
            }
        }, ModItems.CARDBOARD_TRIDENT.get());
        registerClientExtensionsEvent.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return CardboardShieldItemRenderer.getInstance();
            }
        }, ModItems.CARDBOARD_SHIELD.get());
    }
}
