package net.chaolux.createcardboardthings.client.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class CardboardSoundProfile {
    public enum Style {
        WORN_TAPE,BAD_RADIO,LOOSE_SPEAKER,DYING_CASSETTE
    }
    private final long seed;
    private final Style style;
    private final float pitch;
    private final float volume;
    private final float wowAmount;
    private final float wowSpeed;
    private final float amount;
    private final float crackle;
    private final float rattle;
    private final float dropout;
    private final int dropoutInterval;
    private final int transpose;
    private final double wowPhase;
    private final double phase;
    private CardboardSoundProfile(long seed,Style style,float pitch,float volume,float wowAmount,float wowSpeed,float amount,float crackle,float rattle,float dropout,int dropoutInterval,int transpose,double wowPhase,double phase) {
        this.seed=seed;
        this.style=style;
        this.pitch=pitch;
        this.volume=volume;
        this.wowAmount=wowAmount;
        this.wowSpeed=wowSpeed;
        this.amount=amount;
        this.crackle=crackle;
        this.rattle=rattle;
        this.dropout=dropout;
        this.dropoutInterval=dropoutInterval;
        this.transpose=transpose;
        this.wowPhase=wowPhase;
        this.phase=phase;
    }

    public static CardboardSoundProfile from(Item item) {
        ResourceLocation resourceLocation= ForgeRegistries.ITEMS.getKey(item);
        String string=resourceLocation != null ? resourceLocation.toString() : item.getDescriptionId();
        long seed=fnv(string);
        Style style=Style.values()[(int) Math.floorMod(seed,Style.values().length)];
        float pitch=lerp(sample(seed,1),0.72f,1.28f);
        float volume=lerp(sample(seed,2),0.82f,0.94f);
        float wowAmount;
        float wowSpeed;
        float amount;
        float crackle;
        float rattle;
        float dropout;
        int dropoutInterval;
        switch (style) {
            case WORN_TAPE -> {
                wowAmount=lerp(sample(seed,3),0.08f,0.16f);
                wowSpeed=lerp(sample(seed,4),0.018f,0.035f);
                amount=lerp(sample(seed,5),0.025f,0.06f);
                crackle=0.045f;
                rattle=0.0025f;
                dropout=0.06f;
                dropoutInterval=40;
            }
            case BAD_RADIO -> {
                wowAmount=lerp(sample(seed,3),0.05f,0.1f);
                wowSpeed=lerp(sample(seed,4),0.025f,0.045f);
                amount=lerp(sample(seed,5),0.035f,0.075f);
                crackle=0.11f;
                rattle=0.003f;
                dropout=0.09f;
                dropoutInterval=34;
            }
            case LOOSE_SPEAKER -> {
                wowAmount=lerp(sample(seed,3),0.045f,0.09f);
                wowSpeed=lerp(sample(seed,4),0.018f,0.035f);
                amount=lerp(sample(seed,5),0.02f,0.055f);
                crackle=0.035f;
                rattle=0.008f;
                dropout=0.04f;
                dropoutInterval=46;
            }
            case DYING_CASSETTE -> {
                wowAmount=lerp(sample(seed,3),0.14f,0.26f);
                wowSpeed=lerp(sample(seed,4),0.03f,0.06f);
                amount=lerp(sample(seed,5),0.06f,0.13f);
                crackle=0.085f;
                rattle=0.005f;
                dropout=0.12f;
                dropoutInterval=28;
            }
            default -> throw new IllegalStateException();
        }
        int transpose=(int) Math.floor(sample(seed,6) * 3.0f) - 1;
        double wowPhase=sample(seed,7) * Math.PI * 2;
        double phase=sample(seed,8) * Math.PI * 2;
        return new CardboardSoundProfile(seed,style,pitch,volume,wowAmount,wowSpeed,amount,crackle,rattle,dropout,dropoutInterval,transpose,wowPhase,phase);
    }

    public long seed() {
        return this.seed;
    }

    public Style style() {
        return this.style;
    }

    public float pitch() {
        return this.pitch;
    }

    public float volume() {
        return this.volume;
    }

    public float wowAmount() {
        return this.wowAmount;
    }

    public float wowSpeed() {
        return this.wowSpeed;
    }

    public float amount() {
        return this.amount;
    }

    public float crackle() {
        return this.crackle;
    }

    public float rattle() {
        return this.rattle;
    }

    public float dropout() {
        return this.dropout;
    }

    public int dropoutInterval() {
        return this.dropoutInterval;
    }

    public int transpose() {
        return this.transpose;
    }

    public double wowPhase() {
        return this.wowPhase;
    }

    public double phase() {
        return this.phase;
    }

    public float random(int tick,long seed) {
        long value=fmix(this.seed ^ ((long) tick * 0x9E3779B97F4A7C15L ^ seed));
        return (float) ((value >>> 40) & 0xFFFFFFL) / (float) 0x1000000;
    }

    private static float sample(long seed,long currentSeed) {
        long value=fmix(seed ^ (currentSeed * 0x9E3779B97F4A7C15L));
        return (float) ((value >>> 40) & 0xFFFFFFL) / (float) 0x1000000;
    }

    private static float lerp(float value,float min,float max) {
        return Mth.lerp(value,min,max);
    }

    private static long fnv(String string) {
        long hash=0xcbf29ce484222325L;
        for(int index=0;index < string.length();index++) {
            hash ^= string.charAt(index);
            hash *= 0x100000001b3L;
        }
        return hash;
    }

    private static long fmix(long value) {
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= value >>> 33;
        return value;
    }
}
