package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

import java.util.Arrays;
import java.util.HashMap;

public class InventoryUtils {
    public static void switchInvHandSlot(MinecraftClient client, Hand hand, int slot) {
        assert client.player != null;
        ScreenHandler handler = new InventoryScreen(client.player).getScreenHandler();
        int button = hand.equals(Hand.OFF_HAND) ? 40 : client.player.getInventory().selectedSlot;

        //For some stupid reason, the hotbar slots are different
        if (PlayerInventory.isValidHotbarIndex(slot)) {
            //https://wiki.vg/File:Inventory-slots.png
            slot += 36;
        }

        assert client.interactionManager != null;
        client.interactionManager.clickSlot(handler.syncId, slot, button, SlotActionType.SWAP, client.player);

        //Maybe more legit?
        //client.setScreen(new InventoryScreen(client.player));
        //client.player.currentScreenHandler.setCursorStack(client.player.getInventory().getStack(slot).copy());
        //client.setScreen(null);
    }

    public static int chooseBestBreakableItem(Inventory inventory, Item item, int mendingScore) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (stack.isOf(item)) {
                int unbreakingLevel = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
                int calculatedMendingScore = EnchantmentHelper.getLevel(Enchantments.MENDING, stack) > 0 ? mendingScore : 0;
                int damageScore = (stack.getMaxDamage() - stack.getDamage())* (unbreakingLevel + 1);

                map.put(damageScore + calculatedMendingScore, i);
            }
        }

        if (!map.isEmpty()) {
            Integer[] scores = map.keySet().toArray(new Integer[0]);
            Arrays.sort(scores);
            return map.get(scores[scores.length - 1]);
        }

        return -1;
    }

    public static void interactItem(Hand hand, MinecraftClient client) throws NullPointerException {
        ActionResult actionResult3 = client.interactionManager.interactItem(client.player, hand);
        if (actionResult3.isAccepted()) {
            if (actionResult3.shouldSwingHand()) {
                client.player.swingHand(hand);
            }
        }
    }

    public static int getShieldSwapSlot(MinecraftClient client) {
        return NotEnoughKeybinds.COMMON_CONFIG.chooseBestShield ?
                InventoryUtils.chooseBestBreakableItem(client.player.getInventory(), Items.SHIELD, NotEnoughKeybinds.COMMON_CONFIG.swapMendingPoints)
                : client.player.getInventory().getSlotWithStack(Items.SHIELD.getDefaultStack());
    }

    public static int getTotemSwapSlot(MinecraftClient client, int lastShieldSlot) {
        return lastShieldSlot > -1 ? lastShieldSlot : client.player.getInventory().getSlotWithStack(Items.TOTEM_OF_UNDYING.getDefaultStack());
    }
}
