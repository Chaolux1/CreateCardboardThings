package net.chaolux.createcardboardthings;

import com.mojang.logging.LogUtils;
import net.chaolux.createcardboardthings.registry.block.ModBlockEntityTypes;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.chaolux.createcardboardthings.registry.data.ModDataComponents;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(CreateCardboardThings.MOD_ID)
public class CreateCardboardThings {
    public static final String MOD_ID = "createcardboardthings";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateCardboardThings(IEventBus modEventBus, ModContainer modContainer) {
        ModDataComponents.COMPONENT.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntityTypes.TILES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.CARDBOARD_ELYTRA.get());
            event.accept(ModItems.CARDBOARD_PICKAXE.get());
            event.accept(ModItems.CARDBOARD_SHEARS.get());
            event.accept(ModItems.CARDBOARD_SADDLE.get());
            event.accept(ModItems.CARDBOARD_BUCKET.get());
            event.accept(ModItems.CARDBOARD_GOGGLES.get());
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.CARDBOARD_ARROW.get());
            event.accept(ModItems.CARDBOARD_TOTEM.get());
            event.accept(ModItems.CARDBOARD_BALL.get());
            event.accept(ModItems.CARDBOARD_ROCKET.get());
            event.accept(ModItems.CARDBOARD_TRIDENT.get());
            event.accept(ModItems.CARDBOARD_SHIELD.get());
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.CARDBOARD_INGOT.get());
            event.accept(ModItems.WHITE_CARDBOARD.get());
            event.accept(ModItems.ORANGE_CARDBOARD.get());
            event.accept(ModItems.MAGENTA_CARDBOARD.get());
            event.accept(ModItems.LIGHT_BLUE_CARDBOARD.get());
            event.accept(ModItems.YELLOW_CARDBOARD.get());
            event.accept(ModItems.LIME_CARDBOARD.get());
            event.accept(ModItems.PINK_CARDBOARD.get());
            event.accept(ModItems.GRAY_CARDBOARD.get());
            event.accept(ModItems.LIGHT_GRAY_CARDBOARD.get());
            event.accept(ModItems.CYAN_CARDBOARD.get());
            event.accept(ModItems.PURPLE_CARDBOARD.get());
            event.accept(ModItems.BLUE_CARDBOARD.get());
            event.accept(ModItems.BROWN_CARDBOARD.get());
            event.accept(ModItems.GREEN_CARDBOARD.get());
            event.accept(ModItems.RED_CARDBOARD.get());
            event.accept(ModItems.BLACK_CARDBOARD.get());
        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModItems.CARDBOARD_TNT.get());
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.CARDBOARD_JUKEBOX.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemProperties.register(ModItems.CARDBOARD_SHIELD.get(),ResourceLocation.fromNamespaceAndPath("minecraft","blocking"),((itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f));
            });
        }
    }
}
