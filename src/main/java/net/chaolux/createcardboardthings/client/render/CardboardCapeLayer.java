package net.chaolux.createcardboardthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.chaolux.createcardboardthings.integration.curiosapi.CardboardEquipment;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.entity.EquipmentSlot;

public class CardboardCapeLayer extends CapeLayer {
    public CardboardCapeLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> abstractClientPlayerPlayerModelRenderLayerParent) {
        super(abstractClientPlayerPlayerModelRenderLayerParent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource,int light,AbstractClientPlayer abstractClientPlayer,float value,float data,float tick,float values,float yaw,float pitch) {
        if(CardboardEquipment.isCardboardElytra(abstractClientPlayer)) return;
        super.render(poseStack,multiBufferSource,light,abstractClientPlayer,value,data,tick,values,yaw,pitch);
    }
}
