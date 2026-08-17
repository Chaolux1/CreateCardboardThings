package net.chaolux.createcardboardthings.client.sound;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EventBusSubscriber(modid = "createcardboardthings",value = Dist.CLIENT)
public class CardboardJukeboxEvents {
    private static final Map<BlockPos,CardboardSoundInstance> MAP=new HashMap<>();
    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent playSoundEvent) {
        if(!Config.cardboardJukebox()) return;
        SoundInstance soundInstance=playSoundEvent.getOriginalSound();
        if(soundInstance instanceof CardboardSoundInstance) return;
        if(soundInstance.getSource() != SoundSource.RECORDS) return;
        Minecraft minecraft=Minecraft.getInstance();
        if(minecraft.level == null) return;
        BlockPos blockPos=BlockPos.containing(soundInstance.getX(),soundInstance.getY(),soundInstance.getZ());
        if(!minecraft.level.getBlockState(blockPos).is(ModBlocks.CARDBOARD_JUKEBOX.get())) return;
        if(!(minecraft.level.getBlockEntity(blockPos) instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity)) return;

        ItemStack itemStack=cardboardJukeboxBlockEntity.getRecord();
        if(itemStack.isEmpty()) return;
        Optional<Holder<JukeboxSong>> jukeboxSongHolder=JukeboxSong.fromStack(minecraft.level.registryAccess(),itemStack);
        if(jukeboxSongHolder.isEmpty()) return;
        if(!jukeboxSongHolder.get().value().soundEvent().value().getLocation().equals(soundInstance.getLocation())) return;
        CardboardSoundProfile cardboardSoundProfile=CardboardSoundProfile.from(itemStack.getItem());
        CardboardSoundInstance cardboardSoundInstance=new CardboardSoundInstance(jukeboxSongHolder.get().value().soundEvent().value(),blockPos,itemStack.getItem(),cardboardSoundProfile);
        CardboardSoundInstance instance=MAP.put(blockPos.immutable(),cardboardSoundInstance);
        if(instance != null) instance.forceStop();
        playSoundEvent.setSound(cardboardSoundInstance);
    }

    @SubscribeEvent
    public static void onLevel(LevelEvent.Unload unload) {
        if(!unload.getLevel().isClientSide()) return;
        for(CardboardSoundInstance cardboardSoundInstance : MAP.values()) {
            cardboardSoundInstance.forceStop();
        }
        MAP.clear();
    }
}
