package net.sn0wix_.keybinds;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.sn0wix_.NotEnoughKeybinds;
import net.sn0wix_.keybinds.custom.KeybindingCategory;
import net.sn0wix_.keybinds.custom.ModKeyBinding;
import net.sn0wix_.util.Utils;

public class InventoryKeybinds extends ModKeybindings {
    public static final String INVENTORY_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".inventory";

    public static final ModKeyBinding SWITCH_TOTEM_SHIELD = (ModKeyBinding) registerModKeyBinding(new ModKeyBinding("switch_totem_shield", INVENTORY_CATEGORY, (client, keyBinding) -> {
        for (Hand hand : Hand.values()) {
            if (hand.equals(Hand.OFF_HAND)) {
                if (client.player.getStackInHand(hand).isOf(Items.SHIELD)) {
                    int totemSlot = client.player.getInventory().getSlotWithStack(Items.TOTEM_OF_UNDYING.getDefaultStack());

                    if (totemSlot > -1) {
                        client.interactionManager.clickSlot(new InventoryScreen(client.player).getScreenHandler().syncId, totemSlot, 40, SlotActionType.SWAP, client.player);
                    }
                } else if (client.player.getStackInHand(hand).isOf(Items.TOTEM_OF_UNDYING)) {
                    int shieldSlot = client.player.getInventory().getSlotWithStack(Items.SHIELD.getDefaultStack());

                    if (shieldSlot > -1) {
                        client.interactionManager.clickSlot(new InventoryScreen(client.player).getScreenHandler().syncId, shieldSlot, 40, SlotActionType.SWAP, client.player);
                    }
                }
            }
        }
    }));


    public static final ModKeyBinding THROW_ENDER_PEARL = (ModKeyBinding) registerModKeyBinding(new ModKeyBinding("throw_ender_pearl", INVENTORY_CATEGORY, (client, keyBinding) -> {
        for (Hand hand : Hand.values()) {
            int pearlSlot = client.player.getInventory().getSlotWithStack(Items.ENDER_PEARL.getDefaultStack());

            if (pearlSlot > -1 && !PlayerInventory.isValidHotbarIndex(pearlSlot)) {
                client.interactionManager.clickSlot(new InventoryScreen(client.player).getScreenHandler().syncId, pearlSlot, client.player.getInventory().selectedSlot, SlotActionType.SWAP, client.player);
                Utils.interactItem(hand, client);
                client.interactionManager.clickSlot(new InventoryScreen(client.player).getScreenHandler().syncId, pearlSlot, client.player.getInventory().selectedSlot, SlotActionType.SWAP, client.player);

            } else if (pearlSlot > -1 && PlayerInventory.isValidHotbarIndex(pearlSlot)) {
                int slotBefore = client.player.getInventory().selectedSlot;
                client.player.getInventory().selectedSlot = pearlSlot;
                Utils.interactItem(hand, client);
                client.player.getInventory().selectedSlot = slotBefore;
            }
        }
    }));

    @Override
    public KeybindingCategory getCategory() {
        return new KeybindingCategory(INVENTORY_CATEGORY, 0, THROW_ENDER_PEARL, SWITCH_TOTEM_SHIELD);
    }
}
