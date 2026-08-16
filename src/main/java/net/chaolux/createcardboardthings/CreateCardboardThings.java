package net.chaolux.createcardboardthings;

import com.mojang.logging.LogUtils;
import net.chaolux.createcardboardthings.registry.block.ModBlockEntityTypes;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(CreateCardboardThings.MOD_ID)
public class CreateCardboardThings
{
    public static final String MOD_ID = "createcardboardthings";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateCardboardThings()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntityTypes.TILES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.CARDBOARD_ELYTRA);
            event.accept(ModItems.CARDBOARD_PICKAXE);
            event.accept(ModItems.CARDBOARD_SHEARS);
            event.accept(ModItems.CARDBOARD_SADDLE);
            event.accept(ModItems.CARDBOARD_BUCKET);
            event.accept(ModItems.CARDBOARD_GOGGLES);
        }

        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.CARDBOARD_ARROW);
            event.accept(ModItems.CARDBOARD_TOTEM);
            event.accept(ModItems.CARDBOARD_BALL);
            event.accept(ModItems.CARDBOARD_ROCKET);
            event.accept(ModItems.CARDBOARD_TRIDENT);
            event.accept(ModItems.CARDBOARD_SHIELD);
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.CARDBOARD_INGOT);
            event.accept(ModItems.WHITE_CARDBOARD);
            event.accept(ModItems.ORANGE_CARDBOARD);
            event.accept(ModItems.MAGENTA_CARDBOARD);
            event.accept(ModItems.LIGHT_BLUE_CARDBOARD);
            event.accept(ModItems.YELLOW_CARDBOARD);
            event.accept(ModItems.LIME_CARDBOARD);
            event.accept(ModItems.PINK_CARDBOARD);
            event.accept(ModItems.GRAY_CARDBOARD);
            event.accept(ModItems.LIGHT_GRAY_CARDBOARD);
            event.accept(ModItems.CYAN_CARDBOARD);
            event.accept(ModItems.PURPLE_CARDBOARD);
            event.accept(ModItems.BLUE_CARDBOARD);
            event.accept(ModItems.BROWN_CARDBOARD);
            event.accept(ModItems.GREEN_CARDBOARD);
            event.accept(ModItems.RED_CARDBOARD);
            event.accept(ModItems.BLACK_CARDBOARD);
        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModItems.CARDBOARD_TNT);
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.CARDBOARD_JUKEBOX);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            event.enqueueWork(() -> {
               ItemProperties.register(ModItems.CARDBOARD_SHIELD.get(),new ResourceLocation("minecraft","blocking"),((itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f));
            });
        }
    }
}
