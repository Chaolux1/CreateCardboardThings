package net.chaolux.createcardboardthings.registry.item;

import net.chaolux.createcardboardthings.common.item.CardboardArrowItem;
import net.chaolux.createcardboardthings.common.item.CardboardElytraItem;
import net.chaolux.createcardboardthings.common.item.CardboardPickaxeItem;
import net.chaolux.createcardboardthings.common.item.CardboardShearsItem;
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

    }
}
