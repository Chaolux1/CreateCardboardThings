package net.chaolux.createcardboardthings.registry.entity;

import net.chaolux.createcardboardthings.common.entity.*;
import net.chaolux.createcardboardthings.common.item.CardboardArrowItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES;

    public static final RegistryObject<EntityType<CardboardArrowEntity>> CARDBOARD_ARROW;
    public static final RegistryObject<EntityType<CardboardBallEntity>> CARDBOARD_BALL;
    public static final RegistryObject<EntityType<CardboardTridentEntity>> CARDBOARD_TRIDENT;
    public static final RegistryObject<EntityType<CardboardTntEntity>> CARDBOARD_TNT;
    public static final RegistryObject<EntityType<CardboardMarkerEntity>> CARDBOARD_MARKER;

    static {
        ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "createcardboardthings");

        CARDBOARD_ARROW = ENTITIES.register("cardboard_arrow", () -> EntityType.Builder.<CardboardArrowEntity>of(CardboardArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("cardboard_arrow"));
        CARDBOARD_BALL = ENTITIES.register("cardboard_ball", () -> EntityType.Builder.<CardboardBallEntity>of(CardboardBallEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(20).build("cardboard_ball"));
        CARDBOARD_TRIDENT = ENTITIES.register("cardboard_trident", () -> EntityType.Builder.<CardboardTridentEntity>of(CardboardTridentEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("cardboard_trident"));
        CARDBOARD_TNT = ENTITIES.register("cardboard_tnt", () -> EntityType.Builder.<CardboardTntEntity>of(CardboardTntEntity::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10).build("cardboard_tnt"));
        CARDBOARD_MARKER = ENTITIES.register("cardboard_marker", () -> EntityType.Builder.<CardboardMarkerEntity>of(CardboardMarkerEntity::new,MobCategory.MISC).sized(0.01f,0.01f).clientTrackingRange(16).updateInterval(20).build("cardboard_marker"));
    }
}