package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindingCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.Utils;

public class InventoryKeys extends NotEKKeybindings {
    public static final String INVENTORY_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".inventory";

    public static final NotEKKeyBinding SWITCH_TOTEM_SHIELD = (NotEKKeyBinding) registerModKeyBinding(new NotEKKeyBinding("switch_totem_shield", INVENTORY_CATEGORY, (client, keyBinding) -> {
        for (Hand hand : Hand.values()) {
            int slot = -1;

            if (client.player.getStackInHand(hand).isOf(Items.SHIELD)) {
                slot = client.player.getInventory().getSlotWithStack(Items.TOTEM_OF_UNDYING.getDefaultStack());
            } else if (client.player.getStackInHand(hand).isOf(Items.TOTEM_OF_UNDYING)) {
                slot = client.player.getInventory().getSlotWithStack(Items.SHIELD.getDefaultStack());
            }

            if (slot > -1) {
                ScreenHandler handler = new InventoryScreen(client.player).getScreenHandler();
                int button = hand.equals(Hand.OFF_HAND) ? 40 : client.player.getInventory().selectedSlot;

                //For some stupid reason, the hotbar slots are different
                if (PlayerInventory.isValidHotbarIndex(slot)) {
                    //https://wiki.vg/File:Inventory-slots.png
                    slot = slot + 36;
                }

                client.interactionManager.clickSlot(handler.syncId, slot, button, SlotActionType.SWAP, client.player);

                //Maybe more legit?
                //client.setScreen(new InventoryScreen(client.player));
                //client.player.currentScreenHandler.setCursorStack(client.player.getInventory().getStack(slot).copy());
                //client.setScreen(null);
            }
        }
    }) {
        @Override
        public Text getTooltip() {
            return Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".tooltip.switch_totem_shield");
        }
    });


    public static final NotEKKeyBinding THROW_ENDER_PEARL = (NotEKKeyBinding) registerModKeyBinding(new NotEKKeyBinding("throw_ender_pearl", INVENTORY_CATEGORY, (client, keyBinding) -> {
        for (Hand hand : Hand.values()) {
            if (client.player.getStackInHand(hand).isOf(Items.ENDER_PEARL)) {
                Utils.interactItem(hand, client);
                return;
            }
        }

        int pearlSlot = client.player.getInventory().getSlotWithStack(Items.ENDER_PEARL.getDefaultStack());

        if (pearlSlot > -1 && !PlayerInventory.isValidHotbarIndex(pearlSlot)) {
            int syncId = new InventoryScreen(client.player).getScreenHandler().syncId;

            client.interactionManager.clickSlot(syncId, pearlSlot, client.player.getInventory().selectedSlot, SlotActionType.SWAP, client.player);
            Utils.interactItem(Hand.MAIN_HAND, client);
            client.interactionManager.clickSlot(syncId, pearlSlot, client.player.getInventory().selectedSlot, SlotActionType.SWAP, client.player);

        } else if (pearlSlot > -1 && PlayerInventory.isValidHotbarIndex(pearlSlot)) {
            int slotBefore = client.player.getInventory().selectedSlot;
            client.player.getInventory().selectedSlot = pearlSlot;
            Utils.interactItem(Hand.MAIN_HAND, client);
            client.player.getInventory().selectedSlot = slotBefore;
        }
    }){
        @Override
        public Text getTooltip() {
            return Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".tooltip.throw_ender_pearl");
        }
    });

    @Override
    public KeybindingCategory getCategory() {
        return new KeybindingCategory(INVENTORY_CATEGORY, 0, THROW_ENDER_PEARL, SWITCH_TOTEM_SHIELD);
    }
}
