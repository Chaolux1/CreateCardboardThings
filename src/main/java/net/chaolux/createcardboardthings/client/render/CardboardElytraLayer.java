package net.chaolux.createcardboardthings.client.render;

import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CardboardElytraLayer<T extends AbstractClientPlayer, M extends net.minecraft.client.model.PlayerModel<T>>
        extends ElytraLayer<T, M> {
    public CardboardElytraLayer(RenderLayerParent<T, M> p_174493_, EntityModelSet p_174494_) {
        super(p_174493_, p_174494_);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation("createcardboardthings" , "textures/entity/elytra/cardboard_elytra.png");

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
            return TEXTURE;
    }

    @Override
    public boolean shouldRender(ItemStack stack, T entity) {
        return stack.is(ModItems.CARDBOARD_ELYTRA.get());
    }
}
