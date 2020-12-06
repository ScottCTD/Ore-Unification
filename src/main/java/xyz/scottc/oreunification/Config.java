package xyz.scottc.oreunification;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {

    public static ForgeConfigSpec SERVER_CONFIG;
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

    public static final String ITEMS = "Items";
    public static final String TAGS = "Tags";
    public static final String MODE = "Mode";
    public static final String MODS = "Mods";
    public static final String WHITE_LIST = "WhiteList";
    public static final String BLACK_LIST = "BlackList";
    public static final String PRIORITY = "Priority";
    public static final String EVENT = "EventSettings";
    public static final String PLAYER_TICK_EVENT_GAP = "PlayerTickEventGap";

    public static ForgeConfigSpec.BooleanValue isEnableItemsWhiteList;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> itemsWhiteList, itemsBlackList, tagsWhiteList, tagsBlackList, modsPriority;

    public static ForgeConfigSpec.IntValue playerTickEventGap;

    static {
        // Items settings
        SERVER_BUILDER.comment("The Items Settings (modid:itemRegistryName)").push(ITEMS);
        isEnableItemsWhiteList = SERVER_BUILDER
                .comment("True to White List mode, false to Black List Mode")
                .define(MODE, false);
        itemsWhiteList = SERVER_BUILDER
                .comment("The whitelist of items to be replaced, not commonly used. E.g. \"minecraft:iron_ingot\"")
                .define(WHITE_LIST, Collections.emptyList(), Config::isItemOrTag);
        itemsBlackList = SERVER_BUILDER
                .comment("The blacklist of items to be replaced")
                .define(BLACK_LIST, Collections.emptyList(), Config::isItemOrTag);
        SERVER_BUILDER.pop();

        // Tags settings
        SERVER_BUILDER.comment("The Tags Settings (e.g. \"forge:ingots/*\")").push(TAGS);
        tagsWhiteList = SERVER_BUILDER
                .comment("The whitelist for tags")
                .defineList(WHITE_LIST, Arrays.asList("forge:ingots/*", "forge:nuggets/*", "forge:dusts/*", "forge:gems/*", "forge:ores/"), Config::isItemOrTag);
        tagsBlackList = SERVER_BUILDER
                .comment("The blacklist for tags")
                .defineList(BLACK_LIST, Collections.emptyList(), Config::isItemOrTag);
        SERVER_BUILDER.pop();

        // Mod Priority settings
        SERVER_BUILDER.comment("The Mods Priority Settings (e.g. mekanism)").push(PRIORITY);
        modsPriority = SERVER_BUILDER
                .comment("The first one is the highest priority mod").defineList(MODS,
                Arrays.asList("minecraft", "thermal", "mekanism", "additionalthings"), s -> s instanceof String);
        SERVER_BUILDER.pop();

        // Events settings
        SERVER_BUILDER.comment("The settings for listened event to replace the items").push(EVENT);
        playerTickEventGap = SERVER_BUILDER
                .comment("The gap of replacing the entire player inventory. 20 tick (1 seconds) by default")
                .defineInRange(PLAYER_TICK_EVENT_GAP, 20, 1, Integer.MAX_VALUE);
        SERVER_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    private static boolean isItemOrTag(Object s) {
        if (s instanceof String) {
            return ((String) s).contains(":");
        }
        return false;
    }

}
