package net.chaolux.createcardboardthings.registry.item;

import net.chaolux.createcardboardthings.common.item.*;
import net.chaolux.createcardboardthings.common.item.tier.CardboardTier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS;

    public static final Supplier<Item> CARDBOARD_ARROW;
    public static final Supplier<Item> CARDBOARD_ELYTRA;
    public static final Supplier<Item> CARDBOARD_PICKAXE;
    public static final Supplier<Item> CARDBOARD_SHEARS;
    public static final Supplier<Item> CARDBOARD_BALL;
    public static final Supplier<Item> CARDBOARD_INGOT;
    public static final Supplier<Item> CARDBOARD_ROCKET;
    public static final Supplier<Item> CARDBOARD_SADDLE;
    public static final Supplier<Item> CARDBOARD_TOTEM;

    public static Supplier<Item> registerWithTab(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }

    public static Item.Properties basicItem() {
        return new Item.Properties();
    }

    static {
        ITEMS = DeferredRegister.create(Registries.ITEM, "createcardboardthings");

        CARDBOARD_ARROW = registerWithTab("cardboard_arrow", () -> new CardboardArrowItem(basicItem()));
        CARDBOARD_ELYTRA = registerWithTab("cardboard_elytra", () -> new CardboardElytraItem(new Item.Properties().stacksTo(1)));
        CARDBOARD_PICKAXE = registerWithTab("cardboard_pickaxe", () -> new CardboardPickaxeItem(CardboardTier.CARDBOARD, 1,-2.8F,basicItem().stacksTo(1)));
        CARDBOARD_SHEARS = registerWithTab("cardboard_shears", () -> new CardboardShearsItem(basicItem().stacksTo(1).durability(1)));
        CARDBOARD_BALL = registerWithTab("cardboard_ball", () -> new CardboardBallItem(basicItem().stacksTo(16)));
        CARDBOARD_INGOT = registerWithTab("cardboard_ingot", () -> new CardboardIngotItem(basicItem()));
        CARDBOARD_ROCKET = registerWithTab("cardboard_rocket", () -> new CardboardRocketItem(basicItem()));
        CARDBOARD_SADDLE = registerWithTab("cardboard_saddle", () -> new CardboardSaddleItem(basicItem().stacksTo(1)));
        CARDBOARD_TOTEM = registerWithTab("cardboard_totem", () -> new CardboardTotemItem(basicItem().stacksTo(1)));

    }
}
