package net.chaolux.createcardboardthings.client.sound;

import com.mojang.logging.LogUtils;
import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@EventBusSubscriber(modid = "createcardboardthings", bus= Bus.FORGE,value = Dist.CLIENT)
public class CardboardJukeboxEvents {
    private static final Map<BlockPos,CardboardSoundInstance> MAP=new ConcurrentHashMap<>();
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
        RecordItem recordItem=find(soundInstance.getLocation());
        if(recordItem == null) return;
        BlockEntity blockEntity=minecraft.level.getBlockEntity(blockPos);
        if(blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity) {
            if(cardboardJukeboxBlockEntity.hasRecord() && !cardboardJukeboxBlockEntity.isPlayingRecordItem(recordItem)) return;
        }
        CardboardSoundProfile cardboardSoundProfile=CardboardSoundProfile.from(recordItem);
        CardboardSoundInstance cardboardSoundInstance=new CardboardSoundInstance(recordItem.getSound(),blockPos,recordItem,cardboardSoundProfile);
        CardboardSoundInstance instance=MAP.put(blockPos.immutable(),cardboardSoundInstance);
        if(instance != null) instance.forceStop();
        playSoundEvent.setSound(cardboardSoundInstance);
    }

    private static RecordItem find(ResourceLocation resourceLocation) {
        for(Item item : ForgeRegistries.ITEMS.getValues()) {
            if(!(item instanceof RecordItem recordItem)) continue;
            SoundEvent soundEvent=recordItem.getSound();
            if(soundEvent == null) continue;
            if(soundEvent.getLocation().equals(resourceLocation)) return recordItem;
        }
        return null;
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

    private record QueueSound(BlockPos blockPos, SoundEvent soundEvent,float value,float data) {

    }

    public static void setQueue(BlockPos blockPos,SoundEvent soundEvent,float value,float data) {
        QUEUE.add(new QueueSound(blockPos.immutable(),soundEvent,value,data));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent clientTickEvent) {
        if(clientTickEvent.phase != TickEvent.Phase.END) return;
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
