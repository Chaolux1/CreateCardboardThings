package net.chaolux.createcardboardthings.common.mixin;

import net.chaolux.createcardboardthings.common.item.CardboardElytraItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "dev.kikugie.elytratrims.client.render.ETItemRenderer",remap = false)
public abstract class ETItemRendererMixin {
    @Inject(method = "shouldRender",at = @At("HEAD"),cancellable = true,remap = false,require = 0)
    private static void createcardboardthings$shouldRender(ItemStack itemStack, CallbackInfoReturnable<Boolean> booleanCallbackInfoReturnable) {
        if(itemStack.getItem() instanceof CardboardElytraItem) booleanCallbackInfoReturnable.setReturnValue(false);
    }

}
