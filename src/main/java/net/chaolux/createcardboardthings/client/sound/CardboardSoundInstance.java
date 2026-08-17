package net.chaolux.createcardboardthings.client.sound;

import net.chaolux.createcardboardthings.Config;
import net.chaolux.createcardboardthings.common.entity.CardboardJukeboxBlockEntity;
import net.chaolux.createcardboardthings.registry.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CardboardSoundInstance extends AbstractTickableSoundInstance {
    private static final long CRACKLE=0x3B7A6C2D51F9E2A1L;
    private static final long RATTLE=0x6D19AF8372C44B11L;
    private static final long DROPOUT=0x74F52A91D8E36C07L;
    private static final long DROPOUT_LENGTH=0x27B8415CE9D063F2L;
    private static final long CRACKLE_PITCH=0x19D7A4C268BEF315L;
    private static final long RATTLE_PITCH=0x5D0E3B92A74FC816L;
    private final BlockPos blockPos;
    private final CardboardSoundProfile cardboardSoundProfile;
    private int value;
    private int tick;
    private boolean clientRecord;
    public CardboardSoundInstance(ResourceLocation resourceLocation, BlockPos blockPos, CardboardSoundProfile cardboardSoundProfile) {
        super(SoundEvent.createVariableRangeEvent(resourceLocation), SoundSource.RECORDS, RandomSource.create(cardboardSoundProfile.seed()));
        this.blockPos=blockPos.immutable();
        this.cardboardSoundProfile=cardboardSoundProfile;
        this.x=blockPos.getX() + 0.5;
        this.y=blockPos.getY() + 0.5;
        this.z=blockPos.getZ() + 0.5;
        this.volume=cardboardSoundProfile.volume();
        this.pitch=cardboardSoundProfile.pitch();
        this.looping=false;
        this.delay=0;
        this.attenuation= Attenuation.LINEAR;
        this.relative=false;
    }

    @Override
    public void tick() {
        if(!Config.cardboardJukebox()) {
            this.stop();
            return;
        }
        Minecraft minecraft=Minecraft.getInstance();
        if(minecraft.level == null) {
            this.stop();
            return;
        }
        if(!minecraft.level.getBlockState(this.blockPos).is(ModBlocks.CARDBOARD_JUKEBOX.get())) {
            this.stop();
            return;
        }
        BlockEntity blockEntity=minecraft.level.getBlockEntity(this.blockPos);
        if(blockEntity instanceof CardboardJukeboxBlockEntity cardboardJukeboxBlockEntity) {
            if(cardboardJukeboxBlockEntity.isPlaying()) {
                this.clientRecord=true;
            } else if(this.clientRecord) {
                this.stop();
                return;
            } else if(this.value >= 20) {
                this.stop();
                return;
            }
        } else if(this.value >= 20) {
            this.stop();
            return;
        }
        this.value++;
        updatePitch();
        updateDropout();
        playStart();
        playCrackle();
        playRattle();
    }

    public void forceStop() {
        this.stop();
    }

    private void updatePitch() {
        double slowWave=Math.sin(this.value * this.cardboardSoundProfile.wowSpeed() + this.cardboardSoundProfile.wowPhase());
        double flutterWave=Math.sin(this.value * 0.38 + this.cardboardSoundProfile.phase());
        float wow=(float) slowWave * this.cardboardSoundProfile.wowAmount() * 2.6f;
        float flutter=(float) flutterWave * this.cardboardSoundProfile.amount() * 2.8f;
        this.pitch= Mth.clamp(this.cardboardSoundProfile.pitch() + wow + flutter,0.45f,1.75f);
    }

    private void updateDropout() {
        if(this.tick > 0) {
            this.tick--;
            this.volume=this.cardboardSoundProfile.volume() * 0.015f;
            return;
        }
        this.volume=this.cardboardSoundProfile.volume();
        if(this.value % this.cardboardSoundProfile.dropoutInterval() != 0) return;
        float roll=this.cardboardSoundProfile.random(this.value,DROPOUT);
        if(roll >= this.cardboardSoundProfile.dropout()) return;
        this.tick=4 + (int) (this.cardboardSoundProfile.random(this.value,DROPOUT_LENGTH) * 12.0f);
    }

    private void playStart() {
        if(this.value == 1) {
            playNote('C');
            return;
        }
        if(this.value == 4) {
            playNote('H');
            return;
        }
        if(this.value == 7) {
            playNote('A');
            return;
        }
        if(this.value == 10) {
            playNote('O');
            return;
        }
        if(this.value == 13) {
            playNote('L');
            return;
        }
        if(this.value == 16) {
            playNote('U');
            return;
        }
        if(this.value == 19) {
            playNote('X');
        }
    }

    private void playNote(char letter) {
        int note=letter - 'A';
        play(SoundEvents.NOTE_BLOCK_HARP,0.42f,notePitch(note));
    }

    private void playCrackle() {
        float roll=this.cardboardSoundProfile.random(this.value,CRACKLE);
        if(roll >= this.cardboardSoundProfile.crackle()) return;
        float pitch=1.0f + this.cardboardSoundProfile.random(this.value,CRACKLE_PITCH) * 1.0f;
        float volume=this.cardboardSoundProfile.style() == CardboardSoundProfile.Style.BAD_RADIO ? 0.32f : 0.2f;
        play(SoundEvents.NOTE_BLOCK_HAT,volume,pitch);
    }

    private void playRattle() {
        float roll=this.cardboardSoundProfile.random(this.value,RATTLE);
        if(roll >= this.cardboardSoundProfile.rattle()) return;
        float pitch=0.3f + this.cardboardSoundProfile.random(this.value,RATTLE_PITCH) * 0.45f;
        float volume=this.cardboardSoundProfile.style() == CardboardSoundProfile.Style.LOOSE_SPEAKER ? 0.32f : 0.2f;
        play(SoundEvents.NOTE_BLOCK_BASEDRUM,volume,pitch);
    }

    private void play(Holder<SoundEvent> soundEvent, float volume, float pitch) {
        Minecraft minecraft=Minecraft.getInstance();
        if(minecraft.level == null) return;
        minecraft.level.playLocalSound(this.x,this.y,this.z,soundEvent.value(),SoundSource.BLOCKS,volume,pitch,false);
    }

    private static float notePitch(int note) {
        return (float) Math.pow(2.0,(note - 12) / 12.0);
    }
}


