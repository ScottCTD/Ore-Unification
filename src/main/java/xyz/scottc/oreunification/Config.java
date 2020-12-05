package xyz.scottc.oreunification;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class Config {

    public static ForgeConfigSpec SERVER_CONFIG;
    private static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();

    public static final String ITEMS = "Items";
    public static final String TAGS = "Tags";
    public static final String MODE = "Mode";
    public static final String MODS = "Mods";
    public static final String WHITE_LIST = "White List";
    public static final String BLACK_LIST = "Black List";
    public static final String PRIORITY = "Priority";

    public static ForgeConfigSpec.BooleanValue enableItemsWhiteList;
    public static ForgeConfigSpec.BooleanValue enableTagsWhiteList;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> tagsWhiteList;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> modsPriority;

    static {
        SERVER_BUILDER.comment("The Items Settings (modid:itemRegistryName)").push(ITEMS);
        enableItemsWhiteList = SERVER_BUILDER.comment("True to White List mode, false to Black List Mode")
                .define(MODE, true);
        SERVER_BUILDER.pop();

        SERVER_BUILDER.comment("The Tags Settings (e.g. forge:ingots/*)").push(TAGS);
        enableTagsWhiteList = SERVER_BUILDER.comment("True to White List mode, false to Black List Mode")
                .define(MODE, true);
        tagsWhiteList = SERVER_BUILDER.comment("The White List for tags")
                .defineList(WHITE_LIST, Arrays.asList("forge:ingots/*", "forge:nuggets"), s -> s instanceof String);
        SERVER_BUILDER.pop();

        modsPriority = SERVER_BUILDER.comment("The Mods Priority Settings (e.g. mekanism)")
                .defineList(PRIORITY, Arrays.asList("additionalthings", "mekanism"), s -> s instanceof String);

        SERVER_CONFIG = SERVER_BUILDER.build();
    }

}
