package net.chaolux.createcardboardthings.common.block;

import com.mojang.serialization.MapCodec;
import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.chaolux.createcardboardthings.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CardboardJukeboxBlock extends BaseEntityBlock {
    public static final MapCodec<CardboardJukeboxBlock> CODEC=simpleCodec(CardboardJukeboxBlock::new);
    public CardboardJukeboxBlock(Properties properties) {
        super(properties);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CardboardJukeboxBlockEntity(blockPos,blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity>BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntityTypes.CARDBOARD_JUKEBOX.get(),CardboardJukeboxBlockEntity::tick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack,BlockState blockState,Level level,BlockPos blockPos,Player player,InteractionHand interactionHand,BlockHitResult blockHitResult) {
        BlockEntity blockEntity=level.getBlockEntity(blockPos);
        if(!(blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(cardboardJukeboxBlockEntity.hasRecord()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        Optional<Holder<JukeboxSong>> jukeboxSongHolder=JukeboxSong.fromStack(level.registryAccess(),itemStack);
        if(jukeboxSongHolder.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(!level.isClientSide) {
            ItemStack stack=itemStack.copyWithCount(1);
            cardboardJukeboxBlockEntity.setRecord(stack);
            if(!player.getAbilities().instabuild) itemStack.shrink(1);
            level.playSound(null,blockPos,jukeboxSongHolder.get().value().soundEvent().value(), SoundSource.RECORDS,4.0f,1.0f);
            level.updateNeighbourForOutputSignal(blockPos,this);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState,Level level,BlockPos blockPos,Player player,BlockHitResult blockHitResult) {
        BlockEntity blockEntity=level.getBlockEntity(blockPos);
        if(!(blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity)) return InteractionResult.PASS;
        if(!cardboardJukeboxBlockEntity.hasRecord()) return InteractionResult.PASS;
        if(!level.isClientSide) {
            ItemStack itemStack=cardboardJukeboxBlockEntity.removeRecord();
            if(!itemStack.isEmpty()) Containers.dropItemStack(level,blockPos.getX() + 0.5,blockPos.getY() + 1.0,blockPos.getZ() + 0.5,itemStack);
            level.updateNeighbourForOutputSignal(blockPos,this);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void onRemove(BlockState blockState,Level level,BlockPos blockPos,BlockState state,boolean piston) {
        if(blockState.getBlock() != state.getBlock()) {
            BlockEntity blockEntity=level.getBlockEntity(blockPos);
            if(blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity && cardboardJukeboxBlockEntity.hasRecord()) {
                ItemStack itemStack=cardboardJukeboxBlockEntity.removeRecord();
                if(!level.isClientSide && !itemStack.isEmpty()) Containers.dropItemStack(level,blockPos.getX() + 0.5,blockPos.getY() + 0.5,blockPos.getZ() + 0.5,itemStack);
            }
        }
        super.onRemove(blockState,level,blockPos,state,piston);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState,Level level,BlockPos blockPos) {
        BlockEntity blockEntity=level.getBlockEntity(blockPos);
        return blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity ? cardboardJukeboxBlockEntity.getComparator() : 0;
    }

    @Override
    public MapCodec<CardboardJukeboxBlock> codec() {
        return CODEC;
    }
}
