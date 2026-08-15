package net.chaolux.createcardboardthings.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.chaolux.createcardboardthings.common.entity.CardboardMarkerEntity;
import net.chaolux.createcardboardthings.common.utility.CardboardMarkerUtils;
import net.chaolux.createcardboardthings.registry.client.render.ModClientModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelData;
import org.checkerframework.checker.units.qual.A;

public class CardboardMarkerRenderer extends EntityRenderer<CardboardMarkerEntity> {
    private static final double OFFSET=0.0005;
    private static final double LEVER=3.0 / 16.0;
    private static final double BUTTON=2.0 / 16.0;
    private static final double PRESSED_BUTTON=1.0 / 16.0;
    public CardboardMarkerRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius=0.0f;
    }

    @Override
    public void render(CardboardMarkerEntity cardboardMarkerEntity, float yaw, float tick, PoseStack poseStack, MultiBufferSource multiBufferSource,int light) {
        BlockPos blockPos=cardboardMarkerEntity.blockPosition();
        BlockState blockState=cardboardMarkerEntity.level().getBlockState(blockPos);
        if(!CardboardMarkerUtils.isMark(blockState)) return;
        Direction direction=getDirection(blockState);
        Vec3 vec3=getSurface(blockState,direction);
        ResourceLocation resourceLocation=blockState.getBlock() instanceof LeverBlock ? ModClientModels.CARDBOARD_MARKER_LEVER : ModClientModels.CARDBOARD_MARKER_BUTTON;
        BakedModel bakedModel= Minecraft.getInstance().getModelManager().getModel(resourceLocation);
        DyeColor dyeColor=cardboardMarkerEntity.getDyeColor();
        float[] rgb=dyeColor.getTextureDiffuseColors();
        poseStack.pushPose();
        poseStack.translate(vec3.x - 0.5 + direction.getStepX() * OFFSET,vec3.y - 0.5 + direction.getStepY() * OFFSET,vec3.z - 0.5 + direction.getStepZ() * OFFSET);
        rotate(poseStack,direction);
        poseStack.translate(-0.5,-0.5,-0.5);
        RenderType renderType=RenderType.cutout();
        VertexConsumer vertexConsumer=multiBufferSource.getBuffer(renderType);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(),vertexConsumer,null,bakedModel,rgb[0],rgb[1],rgb[2],light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY,renderType);
        poseStack.popPose();
        super.render(cardboardMarkerEntity,yaw,tick,poseStack,multiBufferSource,light);
    }

    @Override
    public boolean shouldRender(CardboardMarkerEntity cardboardMarkerEntity, Frustum frustum,double x,double y,double z) {
        AABB aabb=new AABB(cardboardMarkerEntity.blockPosition()).inflate(1.0);
        return frustum.isVisible(aabb);
    }


    private static Direction getDirection(BlockState blockState) {
        AttachFace attachFace=blockState.getValue(FaceAttachedHorizontalDirectionalBlock.FACE);
        Direction direction=blockState.getValue(FaceAttachedHorizontalDirectionalBlock.FACING);
        return switch (attachFace) {
            case FLOOR -> Direction.UP;
            case CEILING -> Direction.DOWN;
            case WALL -> direction;
        };
    }

    private static Vec3 getSurface(BlockState blockState,Direction direction) {
        double marker=getMarker(blockState);
        return switch (direction) {
            case UP -> new Vec3(0.5,marker,0.5);
            case DOWN -> new Vec3(0.5,1.0 - marker,0.5);
            case EAST -> new Vec3(marker,0.5,0.5);
            case WEST -> new Vec3(1.0 - marker,0.5,0.5);
            case SOUTH -> new Vec3(0.5,0.5,marker);
            case NORTH -> new Vec3(0.5,0.5,1.0 - marker);
        };
    }

    private static double getMarker(BlockState blockState) {
        if(blockState.getBlock() instanceof ButtonBlock) {
            if(blockState.hasProperty(ButtonBlock.POWERED) && blockState.getValue(ButtonBlock.POWERED)) return PRESSED_BUTTON;
            return BUTTON;
        }
        return LEVER;
    }

    private static void rotate(PoseStack poseStack,Direction direction) {
        switch (direction) {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90.0f));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(-90.0f));
            case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            case SOUTH -> {

            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CardboardMarkerEntity cardboardMarkerEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
