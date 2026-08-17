package net.chaolux.createcardboardthings.registry.item;

import net.chaolux.createcardboardthings.common.item.*;
import net.chaolux.createcardboardthings.common.item.tier.CardboardTier;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
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
    public static final Supplier<Item> CARDBOARD_TRIDENT;
    public static final Supplier<Item> CARDBOARD_BUCKET;
    public static final Supplier<Item> CARDBOARD_SHIELD;
    public static final Supplier<Item> CARDBOARD_TNT;
    public static final Supplier<Item> CARDBOARD_GOGGLES;
    public static final Supplier<Item> WHITE_CARDBOARD;
    public static final Supplier<Item> ORANGE_CARDBOARD;
    public static final Supplier<Item> MAGENTA_CARDBOARD;
    public static final Supplier<Item> LIGHT_BLUE_CARDBOARD;
    public static final Supplier<Item> YELLOW_CARDBOARD;
    public static final Supplier<Item> LIME_CARDBOARD;
    public static final Supplier<Item> PINK_CARDBOARD;
    public static final Supplier<Item> GRAY_CARDBOARD;
    public static final Supplier<Item> LIGHT_GRAY_CARDBOARD;
    public static final Supplier<Item> CYAN_CARDBOARD;
    public static final Supplier<Item> PURPLE_CARDBOARD;
    public static final Supplier<Item> BLUE_CARDBOARD;
    public static final Supplier<Item> BROWN_CARDBOARD;
    public static final Supplier<Item> GREEN_CARDBOARD;
    public static final Supplier<Item> RED_CARDBOARD;
    public static final Supplier<Item> BLACK_CARDBOARD;
    public static final List<Supplier<Item>> COLOR_CARDBOARD;
    public static final Supplier<Item> CARDBOARD_JUKEBOX;

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
        CARDBOARD_SHEARS = registerWithTab("cardboard_shears", () -> new CardboardShearsItem(basicItem().stacksTo(1).durability(32)));
        CARDBOARD_BALL = registerWithTab("cardboard_ball", () -> new CardboardBallItem(basicItem().stacksTo(16)));
        CARDBOARD_INGOT = registerWithTab("cardboard_ingot", () -> new CardboardIngotItem(basicItem()));
        CARDBOARD_ROCKET = registerWithTab("cardboard_rocket", () -> new CardboardRocketItem(basicItem()));
        CARDBOARD_SADDLE = registerWithTab("cardboard_saddle", () -> new CardboardSaddleItem(basicItem().stacksTo(1)));
        CARDBOARD_TOTEM = registerWithTab("cardboard_totem", () -> new CardboardTotemItem(basicItem().stacksTo(1)));
        CARDBOARD_TRIDENT = registerWithTab("cardboard_trident", () -> new CardboardTridentItem(basicItem().stacksTo(1).durability(64).attributes(CardboardTridentItem.createItemAttribute())));
        CARDBOARD_BUCKET = registerWithTab("cardboard_bucket", () -> new CardboardBucketItem(basicItem().stacksTo(1)));
        CARDBOARD_SHIELD = registerWithTab("cardboard_shield", () -> new CardboardShieldItem(basicItem().stacksTo(1).durability(124)));
        CARDBOARD_TNT = registerWithTab("cardboard_tnt", () -> new CardboardTntItem(ModBlocks.CARDBOARD_TNT.get(),basicItem()));
        CARDBOARD_GOGGLES = registerWithTab("cardboard_goggles", () -> new CardboardGogglesItem(basicItem().stacksTo(1)));
        WHITE_CARDBOARD = registerColorCardboard("white_cardboard", DyeColor.WHITE);
        ORANGE_CARDBOARD = registerColorCardboard("orange_cardboard",DyeColor.ORANGE);
        MAGENTA_CARDBOARD = registerColorCardboard("magenta_cardboard",DyeColor.MAGENTA);
        LIGHT_BLUE_CARDBOARD = registerColorCardboard("light_blue_cardboard",DyeColor.LIGHT_BLUE);
        YELLOW_CARDBOARD = registerColorCardboard("yellow_cardboard",DyeColor.YELLOW);
        LIME_CARDBOARD = registerColorCardboard("lime_cardboard",DyeColor.LIME);
        PINK_CARDBOARD = registerColorCardboard("pink_cardboard",DyeColor.PINK);
        GRAY_CARDBOARD = registerColorCardboard("gray_cardboard",DyeColor.GRAY);
        LIGHT_GRAY_CARDBOARD = registerColorCardboard("light_gray_cardboard",DyeColor.LIGHT_GRAY);
        CYAN_CARDBOARD = registerColorCardboard("cyan_cardboard",DyeColor.CYAN);
        PURPLE_CARDBOARD = registerColorCardboard("purple_cardboard",DyeColor.PURPLE);
        BLUE_CARDBOARD = registerColorCardboard("blue_cardboard",DyeColor.BLUE);
        BROWN_CARDBOARD = registerColorCardboard("brown_cardboard",DyeColor.BROWN);
        GREEN_CARDBOARD = registerColorCardboard("green_cardboard",DyeColor.GREEN);
        RED_CARDBOARD = registerColorCardboard("red_cardboard",DyeColor.RED);
        BLACK_CARDBOARD = registerColorCardboard("black_cardboard",DyeColor.BLACK);
        COLOR_CARDBOARD=List.of(WHITE_CARDBOARD,ORANGE_CARDBOARD,MAGENTA_CARDBOARD,LIGHT_BLUE_CARDBOARD,YELLOW_CARDBOARD,LIME_CARDBOARD,PINK_CARDBOARD,GRAY_CARDBOARD,LIGHT_GRAY_CARDBOARD,CYAN_CARDBOARD,PURPLE_CARDBOARD,BLUE_CARDBOARD,BROWN_CARDBOARD,GREEN_CARDBOARD,RED_CARDBOARD,BLACK_CARDBOARD);
        CARDBOARD_JUKEBOX = registerWithTab("cardboard_jukebox", () -> new CardboardJukeboxItem(ModBlocks.CARDBOARD_JUKEBOX.get(),basicItem()));
    }

    private static Supplier<Item> registerColorCardboard(String string,DyeColor dyeColor) {
        return registerWithTab(string,() -> new ColorCardboardItem(dyeColor,basicItem()));
    }

    public static Item getColorCardboard(DyeColor dyeColor) {
        return switch (dyeColor) {
            case WHITE -> WHITE_CARDBOARD.get();
            case ORANGE -> ORANGE_CARDBOARD.get();
            case MAGENTA -> MAGENTA_CARDBOARD.get();
            case LIGHT_BLUE -> LIGHT_BLUE_CARDBOARD.get();
            case YELLOW -> YELLOW_CARDBOARD.get();
            case LIME -> LIME_CARDBOARD.get();
            case PINK -> PINK_CARDBOARD.get();
            case GRAY -> GRAY_CARDBOARD.get();
            case LIGHT_GRAY -> LIGHT_GRAY_CARDBOARD.get();
            case CYAN -> CYAN_CARDBOARD.get();
            case PURPLE -> PURPLE_CARDBOARD.get();
            case BLUE -> BLUE_CARDBOARD.get();
            case BROWN -> BROWN_CARDBOARD.get();
            case GREEN -> GREEN_CARDBOARD.get();
            case RED -> RED_CARDBOARD.get();
            case BLACK -> BLACK_CARDBOARD.get();
        };
    }
}
