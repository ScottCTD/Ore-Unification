package xyz.scottc.oreunification;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

@Mod(OreUnification.MODID)
public class OreUnification {

    public static final String MODID = "oreunification";
    public static final Logger LOGGER = LogManager.getLogger();

    public static boolean itemsWhiteList, tagsWhiteList;

    public static List<? extends String> itemsConfig, tagsConfig = Config.tagsWhiteList.get();
    public static List<? extends String> modsPriority = Config.modsPriority.get();

    public OreUnification() {
        // Init Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    public static Item replace(Item target) {
        Set<ResourceLocation> targetTags = target.getTags();
        ResourceLocation validTag = getValidTag(targetTags);
        if (validTag != null) {
            LOGGER.info(validTag.toString());
            ITag<Item> itemITag = ItemTags.getCollection().get(validTag);
            if (itemITag != null) {
                List<Item> allItems = itemITag.getAllElements();
                for (String s : modsPriority) {
                    for (Item resultItem : allItems) {
                        ResourceLocation resultName = resultItem.getRegistryName();
                        if (resultName != null) {
                            if (resultName.getNamespace().equals(s)) {
                                return resultItem;
                            }
                        }
                    }
                }
            }
        }
        return target;
    }

    // forge:ingots/* forge:ingots/copper
    public static ResourceLocation getValidTag(Set<ResourceLocation> tags) {
        // 物品有的所有tag
        for (ResourceLocation tag : tags) {
            // 配置里有的tag
            for (String config : tagsConfig) {
                String nameSpace = tag.getNamespace();
                String path = tag.getPath();
                String configNameSpace = config.substring(0, config.indexOf(":"));
                String configPath = config.substring(config.indexOf(":") + 1);
                // 如果当前配置的tag的命名空间和当前物品的命名空间不同，那就看看下个是否匹配
                if (!nameSpace.equals(configNameSpace)) continue;
                // 如果当前的tag的path里没有/，那就不是，看下个
                if (!path.contains("/")) continue;
                String identifier = path.substring(0, path.indexOf("/"));
                // 如果configPath里有*
                if (configPath.contains("*") && configPath.contains(identifier)) return tag;
                if (!configPath.contains("*") && config.equals(tag.toString())) return tag;
            }
        }
        return null;
    }

}
