package net.chaolux.createcardboardthings.integration.curiosapi;

import com.simibubi.create.content.equipment.armor.CardboardArmorHandler;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;


import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

@EventBusSubscriber(modid = "createcardboardthings")
public class CardboardEquipmentEvents {
    private static final Map<Player,Boolean> MAP= Collections.synchronizedMap(new WeakHashMap<>());

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post playerTickEvent) {
        if(!CardboardEquipment.isCuriosAPI()) return;
        Player player=playerTickEvent.getEntity();
        boolean value= CardboardArmorHandler.testForStealth(player);
        Boolean data=MAP.put(player,value);
        if(value != Boolean.TRUE.equals(data)) player.refreshDimensions();
        if(CardboardEquipment.isElytraSlot()) CuriosAPIPlugin.set(player);
    }
}
