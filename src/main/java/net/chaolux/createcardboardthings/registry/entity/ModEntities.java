package net.chaolux.createcardboardthings.registry.entity;

import net.chaolux.createcardboardthings.common.entity.CardboardArrowEntity;
import net.chaolux.createcardboardthings.common.item.CardboardArrowItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES;

    public static final RegistryObject<EntityType<CardboardArrowEntity>> CARDBOARD_ARROW;

    static {
        ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, "createcardboardthings");

        CARDBOARD_ARROW = ENTITIES.register("cardboard_arrow", () -> EntityType.Builder.<CardboardArrowEntity>of(CardboardArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("cardboard_arrow"));
    }
}