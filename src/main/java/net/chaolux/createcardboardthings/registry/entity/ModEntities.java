package net.chaolux.createcardboardthings.registry.entity;


import net.chaolux.createcardboardthings.common.entity.CardboardArrowEntity;
import net.chaolux.createcardboardthings.common.entity.CardboardBallEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES;

    public static final Supplier<EntityType<CardboardArrowEntity>> CARDBOARD_ARROW;
    public static final Supplier<EntityType<CardboardBallEntity>> CARDBOARD_BALL;

    static {
        ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, "createcardboardthings");

        CARDBOARD_ARROW = ENTITIES.register("cardboard_arrow", () -> EntityType.Builder.<CardboardArrowEntity>of(CardboardArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("cardboard_arrow"));
        CARDBOARD_BALL = ENTITIES.register("cardboard_ball", () -> EntityType.Builder.<CardboardBallEntity>of(CardboardBallEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(20).build("cardboard_ball"));

    }
}