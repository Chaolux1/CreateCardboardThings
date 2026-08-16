package net.chaolux.createcardboardthings.common.event;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.common.entity.CardboardMarkerEntity;
import net.chaolux.createcardboardthings.common.item.ColorCardboardItem;
import net.chaolux.createcardboardthings.common.utility.CardboardMarkerUtils;
import net.chaolux.createcardboardthings.registry.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "createcardboardthings", bus= Bus.FORGE)
public class CardboardMarkerEvents {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        Level level=rightClickBlock.getLevel();
        BlockPos blockPos=rightClickBlock.getPos();
        BlockState blockState=level.getBlockState(blockPos);
        if(!CardboardMarkerUtils.isMark(blockState)) return;
        ItemStack itemStack=rightClickBlock.getItemStack();
        if(itemStack.getItem() instanceof ColorCardboardItem colorCardboardItem) {
            DyeColor dyeColor=colorCardboardItem.getDyeColor();
            if(!Config.colorCardboard(dyeColor)) return;
            rightClickBlock.setCanceled(true);
            rightClickBlock.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
            if(level.isClientSide) return;
            Player player=rightClickBlock.getEntity();
            CardboardMarkerEntity cardboardMarkerEntity=CardboardMarkerUtils.findMark(level,blockPos);
            if(cardboardMarkerEntity != null && cardboardMarkerEntity.getDyeColor() == dyeColor) return;
            if(cardboardMarkerEntity == null) {
                cardboardMarkerEntity=new CardboardMarkerEntity(level,blockPos,dyeColor);
                level.addFreshEntity(cardboardMarkerEntity);
            } else {
                cardboardMarkerEntity.setColor(dyeColor);
            }
            if(!player.getAbilities().instabuild) itemStack.shrink(1);
            level.playSound(null,blockPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS,0.7f,1.2f);
            return;
        }
        if(!itemStack.is(ModItems.CARDBOARD_SHEARS.get())) return;
        if(!Config.cardboardShears()) return;
        CardboardMarkerEntity cardboardMarkerEntity=CardboardMarkerUtils.findMark(level,blockPos);
        if(cardboardMarkerEntity == null) return;
        rightClickBlock.setCanceled(true);
        rightClickBlock.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        if(level.isClientSide) return;
        Player player=rightClickBlock.getEntity();
        DyeColor dyeColor=cardboardMarkerEntity.getDyeColor();
        cardboardMarkerEntity.discard();
        ItemStack stack=new ItemStack(ModItems.getColorCardboard(dyeColor));
        if(!player.addItem(stack)) player.drop(stack,false);
        if(!player.getAbilities().instabuild) itemStack.hurtAndBreak(1,player, livingEntity -> livingEntity.broadcastBreakEvent(rightClickBlock.getHand()));
        level.playSound(null,blockPos,SoundEvents.SHEEP_SHEAR,SoundSource.PLAYERS,0.8f,1.1f);
    }
}
