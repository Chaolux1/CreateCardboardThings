package net.chaolux.createcardboardthings.integration.curiosapi;

import com.illusivesoulworks.elytraslot.ElytraSlotCommonMod;
import com.illusivesoulworks.elytraslot.client.ElytraRenderResult;
import com.illusivesoulworks.elytraslot.common.IElytraProvider;
import net.chaolux.createcardboardthings.common.item.CardboardElytraItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ElytraSlotPlugin {
    private static final ResourceLocation CARDBOARD_TEXTURE=new ResourceLocation("createcardboardthings","textures/entity/elytra/cardboard_elytra.png");
    public static void register() {
        ElytraSlotCommonMod.PROVIDERS.add(new IElytraProvider() {
            @Override
            public boolean matches(ItemStack itemStack) {
                return itemStack.getItem() instanceof CardboardElytraItem;
            }

            @Override
            public ElytraRenderResult getRender(ItemStack itemStack) {
                return new ElytraRenderResult(COLOR,CARDBOARD_TEXTURE,false,itemStack,false);
            }

            @Override
            public boolean hasCapeTexture(ItemStack itemStack) {
                return false;
            }
        });
    }
}
