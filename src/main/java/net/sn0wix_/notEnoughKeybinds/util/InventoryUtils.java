package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class InventoryUtils {
    public static int getFireworkSlot(PlayerInventory inventory, boolean longestDuration, boolean canExplode) {
        int bestSlot = -1;
        int bestFlight = longestDuration ? -1 : Integer.MAX_VALUE;


        for (int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);

            if (stack.getItem() instanceof FireworkRocketItem) {
                FireworksComponent component = stack.getComponents().get(DataComponentTypes.FIREWORKS);
                if (!canExplode && !component.explosions().isEmpty()) continue;


                int newFlight = component.flightDuration();
                if (longestDuration ? newFlight > bestFlight : bestFlight != -1 && newFlight < bestFlight) {
                    bestFlight = newFlight;
                    bestSlot = slot;
                }
            }
        }

        return bestSlot;
    }

    public static void switchInvHandSlot(MinecraftClient client, Hand hand, int clickedSlot) {
        switchInvHotbarSlot(client, hand.equals(Hand.OFF_HAND) ? 40 : client.player.getInventory().getSelectedSlot(), clickedSlot);
    }

    public static void switchInvHotbarSlot(MinecraftClient client, int hotbarSlot, int clickedSlot) {
        if (hotbarSlot > -1 && clickedSlot > -1) {
            assert client.player != null;
            ScreenHandler handler = new InventoryScreen(client.player).getScreenHandler();
            assert client.interactionManager != null;
            client.interactionManager.clickSlot(handler.syncId, convertSlotIds(clickedSlot), hotbarSlot, SlotActionType.SWAP, client.player);
        }

        /*Maybe more legit?
        client.setScreen(new InventoryScreen(client.player));
        client.player.currentScreenHandler.setCursorStack(client.player.getInventory().getStack(clickedSlot).copy());
        client.setScreen(null);*/
    }

    public static void equipChestplate(MinecraftClient client, int slot) {
        //6 for chestplate and 0 for left mouse button click
        switchInvSlot(client, convertSlotIds(slot), 6, 0);
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
                Registry<Enchantment> registryManager = MinecraftClient.getInstance().world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);

                int unbreakingLevel = EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.UNBREAKING), stack);
                int calculatedMendingScore = EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.MENDING), stack) > 0 ? mendingScore : 0;
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
            int slotBefore = client.player.getInventory().getSelectedSlot();
            client.player.getInventory().setSelectedSlot(slot);
            InventoryUtils.interactItem(Hand.MAIN_HAND, client);
            client.player.getInventory().setSelectedSlot(slotBefore);
        }
    }


    /**
     * {//@link MinecraftClient#doItemUse()}
     */
    public static void interactItem(Hand hand, MinecraftClient client) throws NullPointerException {
        if (client.interactionManager.interactItem(client.player, hand) instanceof ActionResult.Success swingSource) {
            if (swingSource.swingSource() == ActionResult.SwingSource.CLIENT) {
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
                try {
                    if (isChestplate(client.player.getInventory().getStack(i))) {
                        slot = i;
                        break;
                    }
                } catch (NullPointerException ignored) {
                }
            }
        }

        return slot;
    }

    public static int getSlotWithElytra(MinecraftClient client) {
        return NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestElytra ?
                InventoryUtils.getBestBreakableItemSlot(client.player.getInventory(), Items.ELYTRA, 216)
                : InventoryUtils.getSlotWithItem(Items.ELYTRA, client.player.getInventory());

    }

    public static int getShieldSwapSlot(MinecraftClient client) {
        return NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield ?
                InventoryUtils.getBestBreakableItemSlot(client.player.getInventory(), Items.SHIELD, NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints)
                : InventoryUtils.getSlotWithItem(Items.SHIELD, client.player.getInventory());
    }

    public static int getTotemSwapSlot(MinecraftClient client, int lastShieldSlot) {
        return lastShieldSlot > -1 ? lastShieldSlot : InventoryUtils.getSlotWithItem(Items.TOTEM_OF_UNDYING, client.player.getInventory());
    }

    //snagged from https://github.com/YumeGod/TheresaModules/blob/6f8fd374924ba8c7c4c2dd45153231b204e5eb73/player/AutoArmor.java
    public static int getBestChestplateSlot(Inventory inventory, boolean acceptVanishing, boolean acceptBinding) {
        int bestArmorSlot = -1;
        float bestArmorStrength = -1;
        int bestArmorDamage = Integer.MAX_VALUE;

        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) {
                final ItemStack stack = inventory.getStack(i);

                if ((EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE) && !acceptBinding) ||
                        (EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP) && !acceptVanishing))
                    continue;


                try {
                    if (isChestplate(stack)) {
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
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }
        return bestArmorSlot;
    }

    public static float getFinalArmorStrength(ItemStack itemStack) {
        float rating = getArmorRating(itemStack);
        Registry<Enchantment> registryManager = MinecraftClient.getInstance().world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);

        rating += EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.PROTECTION), itemStack) * 1.25F;
        rating += EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.FIRE_ASPECT), itemStack) * 1.20F;
        rating += EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.BLAST_PROTECTION), itemStack) * 1.20F;
        rating += EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.PROTECTION), itemStack) * 1.20F;
        rating += EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.FEATHER_FALLING), itemStack) * 0.33F;
        rating += EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.THORNS), itemStack) * 0.10F;
        rating += EnchantmentHelper.getLevel(registryManager.getOrThrow(Enchantments.UNBREAKING), itemStack) * 0.05F;
        return rating;
    }

    public static float getArmorRating(ItemStack itemStack) {
        float rating = 0;

        try {
            Optional<TagKey<Item>> tagKey = itemStack.get(DataComponentTypes.REPAIRABLE).items().getTagKey();

            if (tagKey.isPresent()) {
                if (tagKey.get().id().equals(ArmorMaterials.LEATHER.repairIngredient().id())) {
                    rating = 1;
                } else if (tagKey.get().id().equals(ArmorMaterials.GOLD.repairIngredient().id())) {
                    rating = 2;
                } else if (tagKey.get().id().equals(ArmorMaterials.CHAIN.repairIngredient().id())) {
                    rating = 3;
                } else if (tagKey.get().id().equals(ArmorMaterials.IRON.repairIngredient().id())) {
                    rating = 4;
                } else if (tagKey.get().id().equals(ArmorMaterials.DIAMOND.repairIngredient().id())) {
                    rating = 5;
                } else if (tagKey.get().id().equals(ArmorMaterials.NETHERITE.repairIngredient().id())) {
                    rating = 6;
                }
            }
        } catch (NullPointerException ignored) {
        }

        return rating;
    }

    public static int convertSlotIds(int slot) {
        //For some stupid reason, the hotbar slots are different
        //https://wiki.vg/File:Inventory-slots.png
        if (PlayerInventory.isValidHotbarIndex(slot)) {
            slot += 36;
        } else if (slot == 40) {
            slot = 45;
        }

        return slot;
    }

    public static int getSlotWithItem(Item item, PlayerInventory inventory) {
        for (int i = 0; i < inventory.getMainStacks().size(); i++) {
            if (!inventory.getMainStacks().get(i).isEmpty() && inventory.getMainStacks().get(i).isOf(item)) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isChestplate(ItemStack itemStack) {
        AtomicBoolean bl = new AtomicBoolean(false);

        try {
            itemStack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS).modifiers().forEach(modifier -> {
                if (modifier.modifier().id().equals(Identifier.ofVanilla("armor.chestplate"))) {
                    bl.set(true);
                }
            });
        } catch (NullPointerException ignored) {
        }

        return bl.get();
    }
}
