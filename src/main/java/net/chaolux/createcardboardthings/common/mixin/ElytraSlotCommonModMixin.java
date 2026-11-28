package net.chaolux.createcardboardthings.common.mixin;

import com.illusivesoulworks.elytraslot.common.IElytraProvider;
import net.chaolux.createcardboardthings.integration.curiosapi.ElytraSlotPlugin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(targets = "com.illusivesoulworks.elytraslot.ElytraSlotCommonMod",remap = false)
public class ElytraSlotCommonModMixin {
    @Shadow
    @Final
    private static List<IElytraProvider> PROVIDERS;

    @Inject(method = "init", at = @At("RETURN"),remap = false)
    private static void createcardboardthings$registerProvider(CallbackInfo callbackInfo) {
        ElytraSlotPlugin.register(PROVIDERS);
    }
}
