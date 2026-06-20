package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class InventoryUtils {
    public static int getFireworkSlot(Inventory inventory, boolean longestDuration, boolean canExplode) {
        int bestSlot = -1;
        int bestFlight = longestDuration ? -1 : Integer.MAX_VALUE;


        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack stack = inventory.getItem(slot);

            if (stack.getItem() instanceof FireworkRocketItem) {
                Fireworks component = stack.getComponents().get(DataComponents.FIREWORKS);
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

    public static void switchInvHandSlot(Minecraft client, InteractionHand hand, int clickedSlot) {
        switchInvHotbarSlot(client, hand.equals(InteractionHand.OFF_HAND) ? 40 : client.player.getInventory().getSelectedSlot(), clickedSlot);
    }

    public static void switchInvHotbarSlot(Minecraft client, int hotbarSlot, int clickedSlot) {
        if (hotbarSlot > -1 && clickedSlot > -1) {
            assert client.player != null;
            AbstractContainerMenu handler = new InventoryScreen(client.player).getMenu();
            assert client.gameMode != null;
            client.gameMode.handleContainerInput(handler.containerId, convertSlotIds(clickedSlot), hotbarSlot, ContainerInput.SWAP, client.player);
        }

        /*Maybe more legit?
        client.setScreen(new InventoryScreen(client.player));
        client.player.currentScreenHandler.setCursorStack(client.player.getInventory().getStack(clickedSlot).copy());
        client.setScreen(null);*/
    }

    public static void equipChestplate(Minecraft client, int slot) {
        //6 for chestplate and 0 for left mouse button click
        switchInvSlot(client, convertSlotIds(slot), 6, 0);
    }

    public static void switchInvSlot(Minecraft client, int slot1, int slot2, int button) {
        assert client.player != null;
        AbstractContainerMenu handler = new InventoryScreen(client.player).getMenu();

        assert client.gameMode != null;
        client.gameMode.handleContainerInput(handler.containerId, slot1, button, ContainerInput.PICKUP, client.player);
        client.gameMode.handleContainerInput(handler.containerId, slot2, button, ContainerInput.PICKUP, client.player);
        client.gameMode.handleContainerInput(handler.containerId, slot1, button, ContainerInput.PICKUP, client.player);
    }

    public static int getBestBreakableItemSlot(Container inventory, Item item, int mendingScore) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);

            if (stack.is(item)) {
                Registry<Enchantment> registryManager = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

                int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.UNBREAKING), stack);
                int calculatedMendingScore = EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.MENDING), stack) > 0 ? mendingScore : 0;
                int damageScore = (stack.getMaxDamage() - stack.getDamageValue()) * (unbreakingLevel + 1);

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

    public static void quickUseItem(Minecraft client, int slot) {
        if (!Inventory.isHotbarSlot(slot)) {
            InventoryUtils.switchInvHandSlot(client, InteractionHand.MAIN_HAND, slot);
            InventoryUtils.interactItem(InteractionHand.MAIN_HAND, client);
            InventoryUtils.switchInvHandSlot(client, InteractionHand.MAIN_HAND, slot);

        } else {
            int slotBefore = client.player.getInventory().getSelectedSlot();
            client.player.getInventory().setSelectedSlot(slot);
            InventoryUtils.interactItem(InteractionHand.MAIN_HAND, client);
            client.player.getInventory().setSelectedSlot(slotBefore);
        }
    }


    /**
     * {//@link MinecraftClient#doItemUse()}
     */
    public static void interactItem(InteractionHand hand, Minecraft client) throws NullPointerException {
        if (client.gameMode.useItem(client.player, hand) instanceof InteractionResult.Success swingSource) {
            if (swingSource.swingSource() == InteractionResult.SwingSource.CLIENT) {
                client.player.swing(hand);
            }
        }
    }

    public static int getSlotWithChestplate(Minecraft client) {
        int slot = -1;

        if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestChestplate) {
            slot = getBestChestplateSlot(client.player.getInventory(), NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfVanishing, NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfBinding);
        } else {
            for (int i = 0; i < client.player.getInventory().getContainerSize(); i++) {
                try {
                    if (isChestplate(client.player.getInventory().getItem(i))) {
                        slot = i;
                        break;
                    }
                } catch (NullPointerException ignored) {
                }
            }
        }

        return slot;
    }

    public static int getSlotWithElytra(Minecraft client) {
        return NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestElytra ?
                InventoryUtils.getBestBreakableItemSlot(client.player.getInventory(), Items.ELYTRA, 216)
                : InventoryUtils.getSlotWithItem(Items.ELYTRA, client.player.getInventory());

    }

    public static int getShieldSwapSlot(Minecraft client) {
        return NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield ?
                InventoryUtils.getBestBreakableItemSlot(client.player.getInventory(), Items.SHIELD, NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints)
                : InventoryUtils.getSlotWithItem(Items.SHIELD, client.player.getInventory());
    }

    public static int getTotemSwapSlot(Minecraft client, int lastShieldSlot) {
        return lastShieldSlot > -1 ? lastShieldSlot : InventoryUtils.getSlotWithItem(Items.TOTEM_OF_UNDYING, client.player.getInventory());
    }

    //snagged from https://github.com/YumeGod/TheresaModules/blob/6f8fd374924ba8c7c4c2dd45153231b204e5eb73/player/AutoArmor.java
    public static int getBestChestplateSlot(Container inventory, boolean acceptVanishing, boolean acceptBinding) {
        int bestArmorSlot = -1;
        float bestArmorStrength = -1;
        int bestArmorDamage = Integer.MAX_VALUE;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!inventory.getItem(i).isEmpty()) {
                final ItemStack stack = inventory.getItem(i);

                if ((EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) && !acceptBinding) ||
                        (EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP) && !acceptVanishing))
                    continue;


                try {
                    if (isChestplate(stack)) {
                        float armorStrength = getFinalArmorStrength(stack);

                        if (armorStrength > bestArmorStrength) {
                            bestArmorStrength = armorStrength;
                            bestArmorSlot = i;
                            bestArmorDamage = stack.getDamageValue();

                        } else if (armorStrength == bestArmorStrength && stack.getDamageValue() < bestArmorDamage) {
                            bestArmorDamage = stack.getDamageValue();
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
        Registry<Enchantment> registryManager = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        rating += EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.PROTECTION), itemStack) * 1.25F;
        rating += EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.FIRE_ASPECT), itemStack) * 1.20F;
        rating += EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.BLAST_PROTECTION), itemStack) * 1.20F;
        rating += EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.PROTECTION), itemStack) * 1.20F;
        rating += EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.FEATHER_FALLING), itemStack) * 0.33F;
        rating += EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.THORNS), itemStack) * 0.10F;
        rating += EnchantmentHelper.getItemEnchantmentLevel(registryManager.getOrThrow(Enchantments.UNBREAKING), itemStack) * 0.05F;
        return rating;
    }

    public static float getArmorRating(ItemStack itemStack) {
        float rating = 1;

        try {
            Optional<TagKey<Item>> tagKey = itemStack.get(DataComponents.REPAIRABLE).items().unwrapKey();

            if (tagKey.isPresent()) {
                 if (tagKey.get().location().equals(ArmorMaterials.COPPER.repairIngredient().location())) {
                    rating = 2;
                } else if (tagKey.get().location().equals(ArmorMaterials.GOLD.repairIngredient().location())) {
                    rating = 2;
                } else if (tagKey.get().location().equals(ArmorMaterials.CHAINMAIL.repairIngredient().location())) {
                    rating = 3;
                } else if (tagKey.get().location().equals(ArmorMaterials.IRON.repairIngredient().location())) {
                    rating = 4;
                } else if (tagKey.get().location().equals(ArmorMaterials.DIAMOND.repairIngredient().location())) {
                    rating = 5;
                } else if (tagKey.get().location().equals(ArmorMaterials.NETHERITE.repairIngredient().location())) {
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
        if (Inventory.isHotbarSlot(slot)) {
            slot += 36;
        } else if (slot == 40) {
            slot = 45;
        }

        return slot;
    }

    public static int getSlotWithItem(Item item, Inventory inventory) {
        for (int i = 0; i < inventory.getNonEquipmentItems().size(); i++) {
            if (!inventory.getNonEquipmentItems().get(i).isEmpty() && inventory.getNonEquipmentItems().get(i).is(item)) {
                return i;
            }
        }

        return -1;
    }

    public static boolean isChestplate(ItemStack itemStack) {
        AtomicBoolean bl = new AtomicBoolean(false);

        try {
            itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS).modifiers().forEach(modifier -> {
                if (modifier.modifier().id().equals(Identifier.withDefaultNamespace("armor.chestplate"))) {
                    bl.set(true);
                }
            });
        } catch (NullPointerException ignored) {
        }

        return bl.get();
    }
}
