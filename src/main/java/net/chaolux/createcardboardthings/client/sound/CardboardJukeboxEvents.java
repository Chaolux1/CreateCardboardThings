package net.chaolux.createcardboardthings.client.sound;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.neoforged.neoforge.event.level.LevelEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@EventBusSubscriber(modid = "createcardboardthings",value = Dist.CLIENT)
public class CardboardJukeboxEvents {
    private static final Map<BlockPos,CardboardSoundInstance> MAP=new HashMap<>();
    private static final Queue<QueueSound> QUEUE=new ConcurrentLinkedQueue<>();

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent playSoundEvent) {
        SoundInstance soundInstance=playSoundEvent.getSound();
        if(soundInstance == null) return;
        if(soundInstance instanceof CardboardSoundInstance) return;
        if(soundInstance.getSource() != SoundSource.RECORDS) return;
        Minecraft minecraft=Minecraft.getInstance();
        if(minecraft.level == null) return;
        if(!Config.cardboardJukebox()) return;
        BlockPos blockPos=BlockPos.containing(soundInstance.getX(),soundInstance.getY(),soundInstance.getZ());
        if(!minecraft.level.hasChunkAt(blockPos)) return;
        if(!minecraft.level.getBlockState(blockPos).is(ModBlocks.CARDBOARD_JUKEBOX.get())) return;
        ResourceLocation resourceLocation=soundInstance.getLocation();
        BlockEntity blockEntity=minecraft.level.getBlockEntity(blockPos);
        if(blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity ) {
            if(cardboardJukeboxBlockEntity.isPlaying() && cardboardJukeboxBlockEntity.hasRecord()) {
                var value=JukeboxSong.fromStack(minecraft.level.registryAccess(),cardboardJukeboxBlockEntity.getRecord());
                if(value.isPresent() && !value.get().value().soundEvent().value().getLocation().equals(resourceLocation)) return;
            }
        }
        CardboardSoundProfile cardboardSoundProfile=CardboardSoundProfile.from(resourceLocation);
        CardboardSoundInstance cardboardSoundInstance=new CardboardSoundInstance(resourceLocation,blockPos,cardboardSoundProfile);
        CardboardSoundInstance previous=MAP.put(blockPos.immutable(),cardboardSoundInstance);
        if(previous != null) previous.forceStop();
        playSoundEvent.setSound(cardboardSoundInstance);
    }

    @SubscribeEvent
    public static void onLevel(LevelEvent.Unload unload) {
        if(!unload.getLevel().isClientSide()) return;
        for(CardboardSoundInstance cardboardSoundInstance : MAP.values()) {
            cardboardSoundInstance.forceStop();
        }
        MAP.clear();
        QUEUE.clear();
    }

    private record QueueSound(BlockPos blockPos, SoundEvent soundEvent, float value, float data) {

    }

    public static void setQueue(BlockPos blockPos,SoundEvent soundEvent,float value,float data) {
        QUEUE.add(new QueueSound(blockPos.immutable(),soundEvent,value,data));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post clientTickEvent) {
        Minecraft minecraft=Minecraft.getInstance();
        if(minecraft.level == null) {
            QUEUE.clear();
            return;
        }
        if(minecraft.isPaused()) return;
        MAP.entrySet().removeIf(blockPosCardboardSoundInstanceEntry -> blockPosCardboardSoundInstanceEntry.getValue().isStopped());
        if(!Config.cardboardJukebox()) {
            QUEUE.clear();
            return;
        }
        QueueSound queueSound;
        while ((queueSound=QUEUE.poll()) != null) {
            BlockPos blockPos=queueSound.blockPos();
            CardboardSoundInstance cardboardSoundInstance=MAP.get(blockPos);
            if(cardboardSoundInstance == null || cardboardSoundInstance.isStopped()) continue;
            if(!minecraft.level.hasChunkAt(blockPos)) continue;
            if(!minecraft.level.getBlockState(blockPos).is(ModBlocks.CARDBOARD_JUKEBOX.get())) continue;
            minecraft.level.playLocalSound(blockPos.getX() + 0.5,blockPos.getY() + 0.5,blockPos.getZ() + 0.5,queueSound.soundEvent(),SoundSource.BLOCKS,queueSound.value(),queueSound.data(),false);
        }
    }
}
