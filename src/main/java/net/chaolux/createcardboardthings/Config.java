package net.chaolux.createcardboardthings;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = CreateCardboardThings.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_ARROW;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_ELYTRA;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_PICKAXE;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_SHEARS;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_BALL;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_INGOT;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_ROCKET;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_TOTEM;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_TRIDENT;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_BUCKET;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_SHIELD;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_TNT;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_GOGGLES;
    private static final ForgeConfigSpec.BooleanValue CARDBOARD_JUKEBOX;
    private static final ForgeConfigSpec.BooleanValue WHITE_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue ORANGE_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue MAGENTA_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue LIGHT_BLUE_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue YELLOW_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue LIME_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue PINK_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue GRAY_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue LIGHT_GRAY_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue CYAN_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue PURPLE_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue BLUE_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue BROWN_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue GREEN_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue RED_CARDBOARD;
    private static final ForgeConfigSpec.BooleanValue BLACK_CARDBOARD;
    static final ForgeConfigSpec SPEC;

    static {
        BUILDER.comment("Enable or disable only a special functions");
        BUILDER.push("feature");
        CARDBOARD_ARROW=BUILDER.define("cardboardArrow",true);
        CARDBOARD_ELYTRA=BUILDER.define("cardboardElytra",true);
        CARDBOARD_PICKAXE=BUILDER.define("cardboardPickaxe",true);
        CARDBOARD_SHEARS=BUILDER.define("cardboardShears",true);
        CARDBOARD_BALL=BUILDER.define("cardboardBall",true);
        CARDBOARD_INGOT=BUILDER.define("cardboardIngot",true);
        CARDBOARD_ROCKET=BUILDER.define("cardboardRocket",true);
        CARDBOARD_TOTEM=BUILDER.define("cardboardTotem",true);
        CARDBOARD_TRIDENT=BUILDER.define("cardboardTrident",true);
        CARDBOARD_BUCKET=BUILDER.define("cardboardBucket",true);
        CARDBOARD_SHIELD=BUILDER.define("cardboardShield",true);
        CARDBOARD_TNT=BUILDER.define("cardboardTnt",true);
        CARDBOARD_GOGGLES=BUILDER.define("cardboardGoggles",true);
        CARDBOARD_JUKEBOX=BUILDER.define("cardboardJukebox",true);
        BUILDER.push("colorCardboard");
        WHITE_CARDBOARD=BUILDER.define("whiteCardboard",true);
        ORANGE_CARDBOARD=BUILDER.define("orangeCardboard",true);
        MAGENTA_CARDBOARD=BUILDER.define("magentaCardboard",true);
        LIGHT_BLUE_CARDBOARD=BUILDER.define("lightBlueCardboard",true);
        YELLOW_CARDBOARD=BUILDER.define("yellowCardboard",true);
        LIME_CARDBOARD=BUILDER.define("limeCardboard",true);
        PINK_CARDBOARD=BUILDER.define("pinkCardboard",true);
        GRAY_CARDBOARD=BUILDER.define("grayCardboard",true);
        LIGHT_GRAY_CARDBOARD=BUILDER.define("lightGrayCardboard",true);
        CYAN_CARDBOARD=BUILDER.define("cyanCardboard",true);
        PURPLE_CARDBOARD=BUILDER.define("purpleCardboard",true);
        BLUE_CARDBOARD=BUILDER.define("blueCardboard",true);
        BROWN_CARDBOARD=BUILDER.define("brownCardboard",true);
        GREEN_CARDBOARD=BUILDER.define("greenCardboard",true);
        RED_CARDBOARD=BUILDER.define("redCardboard",true);
        BLACK_CARDBOARD=BUILDER.define("blackCardboard",true);
        BUILDER.pop();
        BUILDER.pop();
        SPEC=BUILDER.build();
    }

    public static boolean cardboardArrow() {
        return CARDBOARD_ARROW.get();
    }

    public static boolean cardboardElytra() {
        return CARDBOARD_ELYTRA.get();
    }

    public static boolean cardboardPickaxe() {
        return CARDBOARD_PICKAXE.get();
    }

    public static boolean cardboardShears() {
        return CARDBOARD_SHEARS.get();
    }

    public static boolean cardboardBall() {
        return CARDBOARD_BALL.get();
    }

    public static boolean cardboardIngot() {
        return CARDBOARD_INGOT.get();
    }

    public static boolean cardboardRocket() {
        return CARDBOARD_ROCKET.get();
    }

    public static boolean cardboardTotem() {
        return CARDBOARD_TOTEM.get();
    }

    public static boolean cardboardTrident() {
        return CARDBOARD_TRIDENT.get();
    }

    public static boolean cardboardBucket() {
        return CARDBOARD_BUCKET.get();
    }

    public static boolean cardboardShield() {
        return CARDBOARD_SHIELD.get();
    }

    public static boolean cardboardTnt() {
        return CARDBOARD_TNT.get();
    }

    public static boolean cardboardGoggles() {
        return CARDBOARD_GOGGLES.get();
    }

    public static boolean cardboardJukebox() {
        return CARDBOARD_JUKEBOX.get();
    }

    public static boolean colorCardboard(DyeColor dyeColor) {
        return switch (dyeColor) {
            case WHITE -> WHITE_CARDBOARD.get();
            case ORANGE -> ORANGE_CARDBOARD.get();
            case MAGENTA -> MAGENTA_CARDBOARD.get();
            case LIGHT_BLUE -> LIGHT_BLUE_CARDBOARD.get();
            case YELLOW -> YELLOW_CARDBOARD.get();
            case LIME -> LIME_CARDBOARD.get();
            case PINK -> PINK_CARDBOARD.get();
            case GRAY -> GRAY_CARDBOARD.get();
            case LIGHT_GRAY -> LIGHT_GRAY_CARDBOARD.get();
            case CYAN -> CYAN_CARDBOARD.get();
            case PURPLE -> PURPLE_CARDBOARD.get();
            case BLUE -> BLUE_CARDBOARD.get();
            case BROWN -> BROWN_CARDBOARD.get();
            case GREEN -> GREEN_CARDBOARD.get();
            case RED -> RED_CARDBOARD.get();
            case BLACK -> BLACK_CARDBOARD.get();
        };
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

    }
}
