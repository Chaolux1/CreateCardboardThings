package net.chaolux.createcardboardthings.integration.curiosapi.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.armor.CardboardArmorItem;
import net.chaolux.createcardboardthings.integration.curiosapi.CuriosAPIPlugin;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosAPIRenderer {
    public static void register() {
        CuriosRendererRegistry.register(ModItems.CARDBOARD_GOGGLES.get(), CardboardGooglesRenderer::new);
        CuriosRendererRegistry.register(AllItems.CARDBOARD_HELMET.get(), CardboardArmorRenderer::new);
        CuriosRendererRegistry.register(AllItems.CARDBOARD_CHESTPLATE.get(), CardboardArmorRenderer::new);
        CuriosRendererRegistry.register(AllItems.CARDBOARD_LEGGINGS.get(), CardboardArmorRenderer::new);
        CuriosRendererRegistry.register(AllItems.CARDBOARD_BOOTS.get(), CardboardArmorRenderer::new);

        MinecraftForge.EVENT_BUS.addListener(CuriosAPIRenderer::onPost);
    }

    private static void onPost(RenderGuiOverlayEvent.Post post) {
        if(!post.getOverlay().id().equals(VanillaGuiOverlay.HELMET.id())) return;
        Minecraft minecraft=Minecraft.getInstance();
        if(minecraft.player == null || !minecraft.options.getCameraType().isFirstPerson()) return;
        if(minecraft.player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof CardboardArmorItem) return;
        ItemStack itemStack=CuriosAPIPlugin.find(minecraft.player,EquipmentSlot.HEAD);
        if(itemStack.isEmpty()) return;
        IClientItemExtensions.of(itemStack).renderHelmetOverlay(itemStack,minecraft.player,post.getWindow().getGuiScaledWidth(),post.getWindow().getGuiScaledHeight(),post.getPartialTick());
    }

    private static final class CardboardGooglesRenderer implements ICurioRenderer {
        @Override
        public <T extends LivingEntity,M extends EntityModel<T>> void render(ItemStack itemStack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T,M> renderLayerParent, MultiBufferSource multiBufferSource,int light,float value,float data,float tick,float values,float yaw,float pitch) {
            if(!(renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel)) return;
            LivingEntity livingEntity=slotContext.entity();
            if(livingEntity.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.CARDBOARD_GOGGLES.get())) return;
            poseStack.pushPose();
            humanoidModel.head.translateAndRotate(poseStack);
            CustomHeadLayer.translateToHead(poseStack,false);
            Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity,itemStack, ItemDisplayContext.HEAD,false,poseStack,multiBufferSource,livingEntity.level(),light, OverlayTexture.NO_OVERLAY,livingEntity.getId());
            poseStack.popPose();
        }
    }

    private static final class CardboardArmorRenderer implements ICurioRenderer {
        Minecraft minecraft=Minecraft.getInstance();
        private final HumanoidModel<LivingEntity> livingEntityHumanoidModel=new HumanoidModel<>(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        private final HumanoidModel<LivingEntity> entityHumanoidModel=new HumanoidModel<>(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR));
        private final HumanoidModel<LivingEntity> humanoidModel=new HumanoidModel<>(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM_INNER_ARMOR));
        private final HumanoidModel<LivingEntity> model=new HumanoidModel<>(minecraft.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM_OUTER_ARMOR));
        @Override
        @SuppressWarnings("unchecked")
        public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack itemStack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T,M> renderLayerParent, MultiBufferSource multiBufferSource,int light,float value,float data,float tick,float values,float yaw,float pitch) {
            if(!(itemStack.getItem() instanceof CardboardArmorItem cardboardArmorItem) || !(renderLayerParent.getModel() instanceof HumanoidModel<?>)) return;
            LivingEntity livingEntity=slotContext.entity();
            EquipmentSlot equipmentSlot=cardboardArmorItem.getEquipmentSlot();
            if(!livingEntity.getItemBySlot(equipmentSlot).isEmpty()) return;
            boolean slim=livingEntity instanceof AbstractClientPlayer abstractClientPlayer && abstractClientPlayer.getModelName().equals("slim");
            HumanoidModel<LivingEntity> livingEntityHumanoidModels=equipmentSlot == EquipmentSlot.LEGS ? (slim ? humanoidModel : livingEntityHumanoidModel) : (slim ? model : entityHumanoidModel);
            ((HumanoidModel<LivingEntity>)renderLayerParent.getModel()).copyPropertiesTo(livingEntityHumanoidModels);
            livingEntityHumanoidModels.setAllVisible(false);
            switch (equipmentSlot) {
                case HEAD -> {
                    livingEntityHumanoidModels.head.visible=true;
                    livingEntityHumanoidModels.hat.visible=true;
                }
                case CHEST -> {
                    livingEntityHumanoidModels.body.visible=true;
                    livingEntityHumanoidModels.leftArm.visible=true;
                    livingEntityHumanoidModels.rightArm.visible=true;
                }
                case LEGS -> {
                    livingEntityHumanoidModels.body.visible=true;
                    livingEntityHumanoidModels.leftLeg.visible=true;
                    livingEntityHumanoidModels.rightLeg.visible=true;
                }
                case FEET -> {
                    livingEntityHumanoidModels.leftLeg.visible=true;
                    livingEntityHumanoidModels.rightLeg.visible=true;
                }
                default -> {
                    return;
                }
            }
            String string=cardboardArmorItem.getArmorTexture(itemStack,livingEntity,equipmentSlot,null);
            if(string == null) return;
            VertexConsumer vertexConsumer= ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(new ResourceLocation(string)),false,itemStack.hasFoil());
            livingEntityHumanoidModels.renderToBuffer(poseStack,vertexConsumer,light,OverlayTexture.NO_OVERLAY,1,1,1,1);

        }
    }
}
