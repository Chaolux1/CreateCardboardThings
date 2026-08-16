package net.chaolux.createcardboardthings.common.block;

import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.chaolux.createcardboardthings.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CardboardJukeboxBlock extends BaseEntityBlock {
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
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity=level.getBlockEntity(blockPos);
        if(!(blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity)) return InteractionResult.PASS;
        if(cardboardJukeboxBlockEntity.hasRecord()) {
            if (!level.isClientSide) {
                ItemStack itemStack = cardboardJukeboxBlockEntity.removeRecord();
                level.levelEvent(null, 1011, blockPos, 0);
                if (!itemStack.isEmpty()) Containers.dropItemStack(level, blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, itemStack);
                level.updateNeighbourForOutputSignal(blockPos, this);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        ItemStack itemStack=player.getItemInHand(interactionHand);
        if(!(itemStack.getItem() instanceof RecordItem)) return InteractionResult.PASS;
        if(!level.isClientSide) {
            ItemStack stack=itemStack.copy();
            stack.setCount(1);
            cardboardJukeboxBlockEntity.setRecord(stack);
            if(!player.getAbilities().instabuild) itemStack.shrink(1);
            level.levelEvent(null,1010,blockPos, Item.getId(stack.getItem()));
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
                if(!level.isClientSide) level.levelEvent(null,1011,blockPos,0);
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
        if(blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity) {
            ItemStack itemStack=cardboardJukeboxBlockEntity.getRecord();
            if(itemStack.getItem() instanceof RecordItem recordItem) return recordItem.getAnalogOutput();
        }
        return 0;
    }
}
