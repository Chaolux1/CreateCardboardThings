package net.chaolux.createcardboardthings.registry.item;

import net.chaolux.createcardboardthings.common.item.*;
import net.chaolux.createcardboardthings.common.item.tier.CardboardTier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS;

    public static final RegistryObject<Item> CARDBOARD_ARROW;
    public static final RegistryObject<Item> CARDBOARD_ELYTRA;
    public static final RegistryObject<Item> CARDBOARD_PICKAXE;
    public static final RegistryObject<Item> CARDBOARD_SHEARS;
    public static final RegistryObject<Item> CARDBOARD_BALL;
    public static final RegistryObject<Item> CARDBOARD_INGOT;
    public static final RegistryObject<Item> CARDBOARD_ROCKET;
    public static final RegistryObject<Item> CARDBOARD_SADDLE;
    public static final RegistryObject<Item> CARDBOARD_TOTEM;

    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }

    public static Item.Properties basicItem() {
        return new Item.Properties();
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "createcardboardthings");

        CARDBOARD_ARROW = registerWithTab("cardboard_arrow", () -> new CardboardArrowItem(basicItem()));
        CARDBOARD_ELYTRA = registerWithTab("cardboard_elytra", () -> new CardboardElytraItem(new Item.Properties().stacksTo(1)));
        CARDBOARD_PICKAXE = registerWithTab("cardboard_pickaxe", () -> new CardboardPickaxeItem(CardboardTier.CARDBOARD, 1,-2.8F,basicItem().stacksTo(1)));
        CARDBOARD_SHEARS = registerWithTab("cardboard_shears", () -> new CardboardShearsItem(basicItem().stacksTo(1).durability(1)));
        CARDBOARD_BALL = registerWithTab("cardboard_ball", () -> new CardboardBallItem(basicItem().stacksTo(16)));
        CARDBOARD_INGOT = registerWithTab("cardboard_ingot", () -> new Item(basicItem()));
        CARDBOARD_ROCKET = registerWithTab("cardboard_rocket", () -> new CardboardRocketItem(basicItem()));
        CARDBOARD_SADDLE = registerWithTab("cardboard_saddle", () -> new Item(basicItem().stacksTo(1)));
        CARDBOARD_TOTEM = registerWithTab("cardboard_totem", () -> new Item(basicItem().stacksTo(1)));

    }
}
