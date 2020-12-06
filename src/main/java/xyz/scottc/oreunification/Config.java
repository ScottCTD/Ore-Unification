package xyz.scottc.oreunification;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {

    public static ForgeConfigSpec SERVER_CONFIG;
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

    public static final String MATCHING_SETTINGS = "MatchingSettings";

    public static final String EVENTS_SETTINGS = "EventSettings";

    public static ForgeConfigSpec.BooleanValue isEnableItemsWhiteList;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> itemsWhiteList, itemsBlackList, tagsWhiteList, tagsBlackList, modsPriority;

    public static ForgeConfigSpec.BooleanValue isEnableTickEventListener, isEnableEntityJoinWorldListener;
    public static ForgeConfigSpec.IntValue playerTickEventGap;

    static {
        initMatchingSettings();

        initEventsSettings();

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

    private static void initMatchingSettings() {
        SERVER_BUILDER.push(MATCHING_SETTINGS);

        // Items settings
        SERVER_BUILDER.push("ItemsRegistryNames");

        isEnableItemsWhiteList = SERVER_BUILDER
                .comment("True to enable whitelist, and false to enable blacklist.\nIf the whitelist was enabled, the blacklist won't do anything.")
                .translation("config.oreunification.item_registryname_mode_comment")
                .define("Mode", false);

        itemsWhiteList = SERVER_BUILDER
                .comment("Whitelist of items to be replaced, not commonly used. E.g. \"minecraft:iron_ingot\"")
                .translation("config.oreunification.item_registryname_whitelist_comment")
                .define("WhiteList", Collections.emptyList(), Config::isItemOrTag);

        itemsBlackList = SERVER_BUILDER
                .comment("Blacklist of items to be replaced. E.g. \"minecraft:iron_ingot\"")
                .translation("config.oreunification.item_registryname_blacklist_comment")
                .define("BlackList", Collections.emptyList(), Config::isItemOrTag);

        SERVER_BUILDER.pop();

        // Tags settings
        SERVER_BUILDER.push("Tags");

        tagsWhiteList = SERVER_BUILDER
                .comment("Whitelist for tags to be replaced.\n[Notice]If you want to match all the occurrences, you have to add \"*\"!\n(e.g. \"forge:ingots/*\")\nYou can use * but you cannot use other regex.")
                .translation("config.oreunification.tags_whitelist_comment")
                .defineList("WhiteList", Arrays.asList("forge:ingots/*", "forge:nuggets/*", "forge:dusts/*", "forge:gems/*", "forge:ores/*"), Config::isItemOrTag);

        tagsBlackList = SERVER_BUILDER
                .comment("Blacklist for tags to be replaced.\nThe priority of blacklist is higher than that of whitelist. So if you have both \"forge:ingots/*\" in the whitelist and blacklist, \"forge:ingots/*\" will not be replaced.")
                .translation("config.oreunification.tags_blacklist_comment")
                .defineList("BlackList", Collections.emptyList(), Config::isItemOrTag);

        SERVER_BUILDER.pop();

        // Mod Priority settings
        SERVER_BUILDER.push("Priority");

        modsPriority = SERVER_BUILDER
                .comment("Mods Priority Settings (e.g. mekanism)\nFrom highest to lowest.\nUse mod id here (For \"mekanism:copper_ingot\", the mod id is \"mekanism\"\nThe first one should always be \"minecraft\"")
                .translation("config.oreunification.mods_priority_comment")
                .defineList("Mods", Arrays.asList("minecraft", "mekanism", "thermal", "immersiveengineering"), s -> s instanceof String);

        SERVER_BUILDER.pop();

        SERVER_BUILDER.pop();
    }

    private static void initEventsSettings() {
        // Events settings
        SERVER_BUILDER.push(EVENTS_SETTINGS);

        isEnableTickEventListener = SERVER_BUILDER
                .comment("True to enable replacing items in players' inventory every configurable period.")
                .translation("config.oreunification.is_enable_player_tick_comment")
                .define("EnablePlayerTickListener", true);

        playerTickEventGap = SERVER_BUILDER
                .comment("The period of ticks to replace the entire player inventory if \"EnablePlayerTickListener\" is true. 20 tick (= 1 seconds) by default")
                .translation("config.oreunification.player_tick_gap_comment")
                .defineInRange("PlayerTickEventGap", 20, 1, Integer.MAX_VALUE);

        isEnableEntityJoinWorldListener = SERVER_BUILDER
                .comment("True to enable replacing items when items drop.")
                .translation("config.oreunification.is_enable_entity_join_world_comment")
                .define("EnableItemDropListener", true);

        SERVER_BUILDER.pop();
    }

    private static boolean isItemOrTag(Object s) {
        if (s instanceof String) {
            return ((String) s).contains(":");
        }
        return false;
    }

}
