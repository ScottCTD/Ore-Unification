package xyz.scottc.oreunification;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OreUnification.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    static {
        init();
    }

    public static boolean isEnableTickEventListener, isEnableEntityJoinWorldListener;
    public static int playerTickEventGap;

    // Lowest priority - execute after everything else completed
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (isEnableTickEventListener) {
            if (!event.player.getEntityWorld().isRemote && event.phase == TickEvent.Phase.END && event.player.world.getGameTime() % playerTickEventGap == 0) {
                init();
                PlayerEntity player = event.player;
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    ItemStack stack = player.inventory.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        player.inventory.setInventorySlotContents(i, new ItemStack(OreUnification.replace(stack.getItem()), stack.getCount()));
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (isEnableEntityJoinWorldListener) {
            if (!event.getWorld().isRemote && event.getEntity() instanceof ItemEntity) {
                init();
                ItemEntity itemEntity = (ItemEntity) event.getEntity();
                ItemStack itemStack = itemEntity.getItem();
                Item replacement = OreUnification.replace(itemStack.getItem());
                itemEntity.setItem(new ItemStack(replacement, itemStack.getCount()));
            }
        }
    }



    public static void init() {
        OreUnification.isEnableItemsWhiteList = Config.isEnableItemsWhiteList.get();
        OreUnification.itemsWhiteList = Config.itemsWhiteList.get();
        OreUnification.tagsWhiteList = Config.tagsWhiteList.get();
        OreUnification.itemsBlackList = Config.itemsBlackList.get();
        OreUnification.tagsBlackList = Config.tagsBlackList.get();
        OreUnification.modsPriority = Config.modsPriority.get();

        isEnableTickEventListener = Config.isEnableTickEventListener.get();
        isEnableEntityJoinWorldListener = Config.isEnableEntityJoinWorldListener.get();
        playerTickEventGap = Config.playerTickEventGap.get();
    }

}
