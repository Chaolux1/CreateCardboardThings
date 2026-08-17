package net.chaolux.createcardboardthings.registry.block;

import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILES;
    public static final Supplier<BlockEntityType<CardboardJukeboxBlockEntity>> CARDBOARD_JUKEBOX;

    static {
        TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "createcardboardthings");
        CARDBOARD_JUKEBOX=TILES.register("cardboard_jukebox", () -> BlockEntityType.Builder.of(CardboardJukeboxBlockEntity::new, ModBlocks.CARDBOARD_JUKEBOX.get()).build(null));

    }
}
