package xyz.scottc.oreunification;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OreUnification.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    //@SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemPickup(EntityItemPickupEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity) {
            ItemStack target = event.getItem().getItem();
            Item replacement = OreUnification.replace(target.getItem());
            PlayerEntity player = (PlayerEntity) entityLiving;
            // TODO 被替换物不会消失
            event.getItem().remove();
            player.addItemStackToInventory(new ItemStack(replacement, target.getCount()));
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.getEntityWorld().isRemote && event.phase == TickEvent.Phase.END && event.player.world.getGameTime() % 20 == 18) {
            PlayerEntity player = event.player;
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                player.inventory.setInventorySlotContents(i, new ItemStack(OreUnification.replace(stack.getItem()), stack.getCount()));
            }
        }
    }

}
