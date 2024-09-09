package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

import java.util.Arrays;
import java.util.HashMap;

public class InventoryUtils {
    public static int getFireworkSlot(PlayerInventory inventory, boolean longestDuration, boolean canExplode) {
        int bestSlot = -1;
        byte bestFlight = longestDuration ? -1 : Byte.MAX_VALUE;


        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);

            if (stack.getItem() instanceof FireworkRocketItem) {
                NbtCompound nbtCompound = stack.getSubNbt("Fireworks");
                if (!canExplode && !nbtCompound.getList("Explosions", NbtElement.COMPOUND_TYPE).isEmpty()) continue;


                byte newFlight = nbtCompound.getByte("Flight");
                if (longestDuration ? newFlight > bestFlight : bestFlight != -1 && newFlight < bestFlight) {
                    bestFlight = newFlight;
                    bestSlot = slot;
                }
            }
        }

        return bestSlot;
    }

    public static void switchInvHandSlot(MinecraftClient client, Hand hand, int clickedSlot) {
        switchInvHotbarSlot(client, hand.equals(Hand.OFF_HAND) ? 40 : client.player.getInventory().selectedSlot, clickedSlot);
    }

    public static void switchInvHotbarSlot(MinecraftClient client, int hotbarSlot, int clickedSlot) {
        assert client.player != null;
        ScreenHandler handler = new InventoryScreen(client.player).getScreenHandler();
        assert client.interactionManager != null;
        client.interactionManager.clickSlot(handler.syncId, convertSlotIds(clickedSlot), hotbarSlot, SlotActionType.SWAP, client.player);

        //Maybe more legit?
        //client.setScreen(new InventoryScreen(client.player));
        //client.player.currentScreenHandler.setCursorStack(client.player.getInventory().getStack(clickedSlot).copy());
        //client.setScreen(null);
    }

    public static void equipChestplate(MinecraftClient client, int slot1) {
        //6 for chestplate and 0 for left mouse button click
        switchInvSlot(client, convertSlotIds(slot1), 6, 0);
    }

    public static void switchInvSlot(MinecraftClient client, int slot1, int slot2, int button) {
        assert client.player != null;
        ScreenHandler handler = new InventoryScreen(client.player).getScreenHandler();

        assert client.interactionManager != null;
        client.interactionManager.clickSlot(handler.syncId, slot1, button, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(handler.syncId, slot2, button, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(handler.syncId, slot1, button, SlotActionType.PICKUP, client.player);
    }

    public static int getBestBreakableItemSlot(Inventory inventory, Item item, int mendingScore) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (stack.isOf(item)) {
                int unbreakingLevel = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
                int calculatedMendingScore = EnchantmentHelper.getLevel(Enchantments.MENDING, stack) > 0 ? mendingScore : 0;
                int damageScore = (stack.getMaxDamage() - stack.getDamage()) * (unbreakingLevel + 1);

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

    public static void quickUseItem(MinecraftClient client, int slot) {
        if (!PlayerInventory.isValidHotbarIndex(slot)) {
            InventoryUtils.switchInvHandSlot(client, Hand.MAIN_HAND, slot);
            InventoryUtils.interactItem(Hand.MAIN_HAND, client);
            InventoryUtils.switchInvHandSlot(client, Hand.MAIN_HAND, slot);

        } else {
            int slotBefore = client.player.getInventory().selectedSlot;
            client.player.getInventory().selectedSlot = slot;
            InventoryUtils.interactItem(Hand.MAIN_HAND, client);
            client.player.getInventory().selectedSlot = slotBefore;
        }
    }

    public static void interactItem(Hand hand, MinecraftClient client) throws NullPointerException {
        ActionResult actionResult3 = client.interactionManager.interactItem(client.player, hand);
        if (actionResult3.isAccepted()) {
            if (actionResult3.shouldSwingHand()) {
                client.player.swingHand(hand);
            }
        }
    }

    public static int getSlotWithChestplate(MinecraftClient client) {
        int slot = -1;

        if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestChestplate) {
            slot = getBestChestplateSlot(client.player.getInventory(), NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfVanishing, NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfBinding);
        } else {
            for (int i = 0; i < client.player.getInventory().size(); i++) {
                if (client.player.getInventory().getStack(i).getItem() instanceof ArmorItem armorItem) {
                    if (armorItem.getType().equals(ArmorItem.Type.CHESTPLATE)) {
                        slot = i;
                        break;
                    }
                }
            }
        }

        return slot;
    }

    public static int getSlotWithElytra(MinecraftClient client) {
        return NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestElytra ?
                InventoryUtils.getBestBreakableItemSlot(client.player.getInventory(), Items.ELYTRA, 216)
                : client.player.getInventory().getSlotWithStack(Items.ELYTRA.getDefaultStack());

    }

    public static int getShieldSwapSlot(MinecraftClient client) {
        return NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield ?
                InventoryUtils.getBestBreakableItemSlot(client.player.getInventory(), Items.SHIELD, NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints)
                : client.player.getInventory().getSlotWithStack(Items.SHIELD.getDefaultStack());
    }

    public static int getTotemSwapSlot(MinecraftClient client, int lastShieldSlot) {
        return lastShieldSlot > -1 ? lastShieldSlot : client.player.getInventory().getSlotWithStack(Items.TOTEM_OF_UNDYING.getDefaultStack());
    }

    //snagged from https://github.com/YumeGod/TheresaModules/blob/6f8fd374924ba8c7c4c2dd45153231b204e5eb73/player/AutoArmor.java
    public static int getBestChestplateSlot(Inventory inventory, boolean acceptVanishing, boolean acceptBinding) {
        int bestArmorSlot = -1;
        float bestArmorStrength = -1;
        int bestArmorDamage = Integer.MAX_VALUE;

        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) {
                final ItemStack stack = inventory.getStack(i);

                if ((EnchantmentHelper.hasBindingCurse(stack) && !acceptBinding) || (EnchantmentHelper.hasVanishingCurse(stack) && !acceptVanishing))
                    continue;

                if (stack.getItem() instanceof ArmorItem armorItem) {
                    if (armorItem.getType().equals(ArmorItem.Type.CHESTPLATE)) {
                        float armorStrength = getFinalArmorStrength(stack);

                        if (armorStrength > bestArmorStrength) {
                            bestArmorStrength = armorStrength;
                            bestArmorSlot = i;
                            bestArmorDamage = stack.getDamage();

                        } else if (armorStrength == bestArmorStrength && stack.getDamage() < bestArmorDamage) {
                            bestArmorDamage = stack.getDamage();
                            bestArmorSlot = i;
                        }
                    }
                }
            }
        }
        return bestArmorSlot;
    }

    public static float getFinalArmorStrength(ItemStack itemStack) {
        float rating = getArmorRating(itemStack);
        rating += EnchantmentHelper.getLevel(Enchantments.PROTECTION, itemStack) * 1.25F;
        rating += EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, itemStack) * 1.20F;
        rating += EnchantmentHelper.getLevel(Enchantments.BLAST_PROTECTION, itemStack) * 1.20F;
        rating += EnchantmentHelper.getLevel(Enchantments.PROTECTION, itemStack) * 1.20F;
        rating += EnchantmentHelper.getLevel(Enchantments.FEATHER_FALLING, itemStack) * 0.33F;
        rating += EnchantmentHelper.getLevel(Enchantments.THORNS, itemStack) * 0.10F;
        rating += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, itemStack) * 0.05F;
        return rating;
    }

    public static float getArmorRating(ItemStack itemStack) {
        float rating = 0;

        if (itemStack.getItem() instanceof ArmorItem armor) {
            ArmorMaterial material = armor.getMaterial();
            if (material.equals(ArmorMaterials.LEATHER)) {
                rating = 1;
            } else if (material.equals(ArmorMaterials.GOLD)) {
                rating = 2;
            } else if (material.equals(ArmorMaterials.CHAIN)) {
                rating = 3;
            } else if (material.equals(ArmorMaterials.IRON)) {
                rating = 4;
            } else if (material.equals(ArmorMaterials.DIAMOND)) {
                rating = 5;
            } else if (material.equals(ArmorMaterials.NETHERITE)) {
                rating = 6;
            }
        }
        return rating;
    }

    public static int convertSlotIds(int slot) {
        //For some stupid reason, the hotbar slots are different
        //https://wiki.vg/File:Inventory-slots.png
        if (PlayerInventory.isValidHotbarIndex(slot)) {
            slot += 36;
        } else if (slot >= 36 && slot < 45) {
            slot -= 36;
        }

        return slot;
    }
}
