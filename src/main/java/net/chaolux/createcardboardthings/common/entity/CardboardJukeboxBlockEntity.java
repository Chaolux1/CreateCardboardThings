package net.chaolux.createcardboardthings.common.entity;

import net.chaolux.createcardboardthings.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag,provider);
        if(!this.itemStack.isEmpty()) compoundTag.put(TAG_RECORD,this.itemStack.save(provider));
        compoundTag.putBoolean(TAG_PLAYING,this.playing);
        compoundTag.putInt(TAG_PLAY_TICK,this.playTick);
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag,HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag,provider);
        this.itemStack=compoundTag.contains(TAG_RECORD,Tag.TAG_COMPOUND) ? ItemStack.parseOptional(provider,compoundTag.getCompound(TAG_RECORD)) : ItemStack.EMPTY;
        this.playing=compoundTag.getBoolean(TAG_PLAYING) && !this.itemStack.isEmpty();
        this.playTick=Math.max(0,compoundTag.getInt(TAG_PLAY_TICK));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.level == null || !this.level.isClientSide || !this.playing || this.itemStack.isEmpty()) return;
        JukeboxSong.fromStack(this.level.registryAccess(),this.itemStack).ifPresent(jukeboxSongHolder -> this.level.playLocalSound(this.worldPosition.getX() + 0.5,this.worldPosition.getY() + 0.5,this.worldPosition.getZ() + 0.5,jukeboxSongHolder.value().soundEvent().value(), SoundSource.RECORDS,4.0f,1.0f,false));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
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
        Optional<Holder<JukeboxSong>> jukeboxSongHolder=JukeboxSong.fromStack(level.registryAccess(),cardboardJukeboxBlockEntity.itemStack);
        if(jukeboxSongHolder.isEmpty()) {
            cardboardJukeboxBlockEntity.playing=false;
            cardboardJukeboxBlockEntity.playTick=0;
            cardboardJukeboxBlockEntity.sync();
            return;
        }
        cardboardJukeboxBlockEntity.playTick++;
        int tick=Math.max(1,(int) Math.ceil(jukeboxSongHolder.get().value().lengthInSeconds() * 20.0f));
        if(cardboardJukeboxBlockEntity.playTick >= tick) {
            cardboardJukeboxBlockEntity.playing=false;
            cardboardJukeboxBlockEntity.playTick=0;
            cardboardJukeboxBlockEntity.sync();
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

    public int getComparator() {
        if(this.level == null || this.itemStack.isEmpty()) return 0;
        return JukeboxSong.fromStack(this.level.registryAccess(),this.itemStack).map(jukeboxSongHolder -> jukeboxSongHolder.value().comparatorOutput()).orElse(0);
    }
}
