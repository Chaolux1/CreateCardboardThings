package net.chaolux.createcardboardthings.registry.block;

import net.chaolux.createcardboardthings.common.block.CardboardJukeboxBlock;
import net.chaolux.createcardboardthings.common.block.CardboardTntBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS;
    public static final RegistryObject<Block> CARDBOARD_TNT;
    public static final RegistryObject<Block> CARDBOARD_JUKEBOX;

    static {
        BLOCKS=DeferredRegister.create(ForgeRegistries.BLOCKS,"createcardboardthings");
        CARDBOARD_TNT=BLOCKS.register("cardboard_tnt",() -> new CardboardTntBlock(BlockBehaviour.Properties.copy(Blocks.TNT)));
        CARDBOARD_JUKEBOX=BLOCKS.register("cardboard_jukebox",() -> new CardboardJukeboxBlock(BlockBehaviour.Properties.copy(Blocks.JUKEBOX)));
    }
}
