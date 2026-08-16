package net.chaolux.createcardboardthings.client.sound;

import com.mojang.logging.LogUtils;
import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = "createcardboardthings", bus= Bus.FORGE,value = Dist.CLIENT)
public class CardboardJukeboxEvents {
    private static final Map<BlockPos,CardboardSoundInstance> MAP=new HashMap<>();
    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent playSoundEvent) {
        SoundInstance soundInstance=playSoundEvent.getOriginalSound();
        if(soundInstance instanceof CardboardSoundInstance) return;
        if(soundInstance.getSource() != SoundSource.RECORDS) return;
        Minecraft minecraft=Minecraft.getInstance();
        if(minecraft.level == null) return;
        BlockPos blockPos=BlockPos.containing(soundInstance.getX(),soundInstance.getY(),soundInstance.getZ());
        if(!minecraft.level.getBlockState(blockPos).is(ModBlocks.CARDBOARD_JUKEBOX.get())) return;
        ResourceLocation resourceLocation=soundInstance.getLocation();
        RecordItem recordItem=find(resourceLocation);
        if(recordItem == null) return;
        CardboardSoundProfile cardboardSoundProfile=CardboardSoundProfile.from(recordItem);
        CardboardSoundInstance cardboardSoundInstance=new CardboardSoundInstance(recordItem.getSound(),blockPos,recordItem,cardboardSoundProfile);
        CardboardSoundInstance instance=MAP.put(blockPos.immutable(),cardboardSoundInstance);
        if(instance != null) instance.forceStop();
        playSoundEvent.setSound(cardboardSoundInstance);
    }

    private static RecordItem find(ResourceLocation resourceLocation) {
        for(Item item : ForgeRegistries.ITEMS.getValues()) {
            if(!(item instanceof RecordItem recordItem)) continue;
            if(recordItem.getSound().getLocation().equals(resourceLocation)) return recordItem;
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
    }
}
