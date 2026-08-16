package net.chaolux.createcardboardthings.common.entity;

import net.chaolux.createcardboardthings.registry.block.ModBlockEntityTypes;
import net.chaolux.createcardboardthings.registry.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CardboardJukeboxBlockEntity extends BlockEntity {
    private static final String TAG_RECORD="Record";
    private static final String TAG_PLAYING="Playing";
    private static final String TAG_PLAY_TICK="PlayTick";
    private ItemStack itemStack=ItemStack.EMPTY;
    private boolean playing;
    private int playTick;
    public CardboardJukeboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.CARDBOARD_JUKEBOX.get(),blockPos,blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if(!this.itemStack.isEmpty()) compoundTag.put(TAG_RECORD,this.itemStack.save(new CompoundTag()));
        compoundTag.putBoolean(TAG_PLAYING,this.playing);
        compoundTag.putInt(TAG_PLAY_TICK,this.playTick);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if(compoundTag.contains(TAG_RECORD, Tag.TAG_COMPOUND)) {
            this.itemStack=ItemStack.of(compoundTag.getCompound(TAG_RECORD));
        } else {
            this.itemStack=ItemStack.EMPTY;
        }
        this.playing=compoundTag.getBoolean(TAG_PLAYING) && !this.itemStack.isEmpty();
        this.playTick=Math.max(0,compoundTag.getInt(TAG_PLAY_TICK));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.level == null || !this.level.isClientSide || !this.playing || !(this.itemStack.getItem() instanceof RecordItem)) return;
        this.level.levelEvent(null,1010,this.worldPosition,Item.getId(this.itemStack.getItem()));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void sync() {
        this.setChanged();
        if(this.level != null) this.level.sendBlockUpdated(this.worldPosition,this.getBlockState(),this.getBlockState(),3);
    }

    public static void tick(Level level,BlockPos blockPos,BlockState blockState,CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity) {
        if(level.isClientSide || !cardboardJukeboxBlockEntity.playing) return;
        if(!(cardboardJukeboxBlockEntity.itemStack.getItem() instanceof RecordItem recordItem)) {
            cardboardJukeboxBlockEntity.playing=false;
            cardboardJukeboxBlockEntity.playTick=0;
            cardboardJukeboxBlockEntity.sync();
            return;
        }
        cardboardJukeboxBlockEntity.playTick++;
        if(cardboardJukeboxBlockEntity.playTick >= recordItem.getLengthInTicks()) {
            cardboardJukeboxBlockEntity.playing=false;
            cardboardJukeboxBlockEntity.playTick=0;
            cardboardJukeboxBlockEntity.sync();
            level.levelEvent(null,1011,blockPos,0);
            return;
        }
        if(cardboardJukeboxBlockEntity.playTick % 20 == 0) cardboardJukeboxBlockEntity.setChanged();
    }

    public boolean hasRecord() {
        return !this.itemStack.isEmpty();
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public int getPlayTick() {
        return this.playTick;
    }

    public ItemStack getRecord() {
        return this.itemStack.copy();
    }

    public boolean isPlayingRecordItem(Item item) {
        return this.playing && !this.itemStack.isEmpty() && this.itemStack.getItem() == item;
    }

    public void setRecord(ItemStack itemStack) {
        ItemStack stack=itemStack.copy();
        stack.setCount(1);
        this.itemStack=stack;
        this.playing=!stack.isEmpty();
        this.playTick=0;
        this.sync();
    }

    public ItemStack removeRecord() {
        ItemStack stack=this.itemStack.copy();
        this.itemStack=ItemStack.EMPTY;
        this.playing=false;
        this.playTick=0;
        this.sync();
        return stack;
    }
}
