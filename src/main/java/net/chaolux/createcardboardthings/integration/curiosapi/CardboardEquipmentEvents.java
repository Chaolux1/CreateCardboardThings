package net.chaolux.createcardboardthings.integration.curiosapi;

import com.simibubi.create.content.equipment.armor.CardboardArmorHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = "createcardboardthings", bus= Bus.FORGE)
public class CardboardEquipmentEvents {
    private static final Map<Player,Boolean> MAP= Collections.synchronizedMap(new WeakHashMap<>());

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if(playerTickEvent.phase != TickEvent.Phase.END || !CardboardEquipment.isCuriosAPI()) return;
        Player player=playerTickEvent.player;
        boolean value= CardboardArmorHandler.testForStealth(player);
        Boolean data=MAP.put(player,value);
        if(value != Boolean.TRUE.equals(data)) player.refreshDimensions();
        if(CardboardEquipment.isElytraSlot()) CuriosAPIPlugin.set(player);
    }
}
