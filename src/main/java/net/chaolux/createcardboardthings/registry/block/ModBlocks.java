package net.chaolux.createcardboardthings.registry.block;

import net.chaolux.createcardboardthings.common.block.CardboardJukeboxBlock;
import net.chaolux.createcardboardthings.common.block.CardboardTntBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS;
    public static final Supplier<Block> CARDBOARD_TNT;
    public static final Supplier<Block> CARDBOARD_JUKEBOX;

    static {
        BLOCKS=DeferredRegister.create(Registries.BLOCK,"createcardboardthings");
        CARDBOARD_TNT=BLOCKS.register("cardboard_tnt",() -> new CardboardTntBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TNT)));
        CARDBOARD_JUKEBOX=BLOCKS.register("cardboard_jukebox",() -> new CardboardJukeboxBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUKEBOX)));
    }
}
