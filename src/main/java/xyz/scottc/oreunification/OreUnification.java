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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Mod(OreUnification.MODID)
public class OreUnification {

    public static final String MODID = "oreunification";
    public static final Logger LOGGER = LogManager.getLogger();

    public static boolean isEnableItemsWhiteList;

    public static List<? extends String> itemsWhiteList, tagsWhiteList, itemsBlackList, tagsBlackList;
    public static List<? extends String> modsPriority;

    public OreUnification() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Init Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
    }

    public static @NotNull Item replace(Item target) {
        if (isNeedToReplace(target)) {
            Set<ResourceLocation> targetTags = target.getTags();
            ResourceLocation validTag = getValidTag(targetTags);
            if (validTag != null) {
                ITag<Item> itemITag = ItemTags.getCollection().get(validTag);
                if (itemITag != null) {
                    List<Item> allItems = itemITag.getAllElements();
                    for (String s : modsPriority) {
                        for (Item resultItem : allItems) {
                            ResourceLocation resultName = resultItem.getRegistryName();
                            if (resultName != null) {
                                if (resultName.getNamespace().equals(s)) return resultItem;
                            }
                        }
                    }
                }
            }
        }
        return target;
    }

    /**
     * Get the valid tag of given item because item always have many tags.
     * @param tags The Set<ResourceLocation> of a item.
     * @return The ResouceLocation of a item if this item is not in the blacklist.
     *         null for in blocklist and cannot find.
     */
    public static @Nullable ResourceLocation getValidTag(Set<ResourceLocation> tags) {
        if (tags.size() == 0) return null;
        // 物品有的所有tag
        for (ResourceLocation tag : tags) {
            String path = tag.getPath();
            // 如果当前的tag的path里没有/，那就不是，看下个
            if (!path.contains("/")) continue;
            // 黑名单
            for (String blackTag : tagsBlackList) {
                if (isIncludeIn(blackTag, tag)) return null;
            }
            // 白名单
            for (String whiteTag : tagsWhiteList) {
                if (isIncludeIn(whiteTag, tag)) return tag;
            }
        }
        return null;
    }

    /**
     * Return if the target include or equal to the candidate according to specific algorithm
     * @param target The target which may contain the candidate.
     * @param candidate The candidate.
     * @return If the target include or equal to the candidate
     */
    public static boolean isIncludeIn(String target, ResourceLocation candidate) {
        String resourceLocationC = candidate.toString();
        if (target.equals(resourceLocationC)) return true;

        String nameSpaceT = target.substring(0, target.indexOf(":"));
        String nameSpaceC = candidate.getNamespace();
        if (!nameSpaceT.equals(nameSpaceC)) return false;

        String pathT = target.substring(nameSpaceT.length());
        String identifierT = pathT.contains("/") ? pathT.substring(0, pathT.indexOf("/")) : pathT;
        String pathC = candidate.getPath();
        String identifierC = pathC.contains("/") ? pathC.substring(0, pathC.indexOf("/")) : pathC;
        if (!pathT.contains("*")) return false;
        if (identifierT.equals(identifierC)) return true;

        return false;
    }

    // If the target Item is in the itemBlackList, not replace it.
    private static boolean isNeedToReplace(Item target) {
        ResourceLocation registryName = target.getRegistryName();
        if (registryName != null) {
            if (OreUnification.isEnableItemsWhiteList) {
                int result = Collections.binarySearch(itemsWhiteList, registryName.toString());
                return result >= 0;
            } else {
                // Blacklist -> false if exist
                int result = Collections.binarySearch(itemsBlackList, registryName.toString());
                return !(result >= 0);
            }
        }

        return false;
    }

}
