package net.chaolux.createcardboardthings.registry.block;

import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILES;
    public static final RegistryObject<BlockEntityType<CardboardJukeboxBlockEntity>> CARDBOARD_JUKEBOX;

    static {
        TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "createcardboardthings");
        CARDBOARD_JUKEBOX=TILES.register("cardboard_jukebox", () -> BlockEntityType.Builder.of(CardboardJukeboxBlockEntity::new, ModBlocks.CARDBOARD_JUKEBOX.get()).build(null));

    }
}
