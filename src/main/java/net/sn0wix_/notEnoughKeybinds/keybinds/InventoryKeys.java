package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings.SwapTotemShieldSettings;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.EquipElytraBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.ElytraController;
import net.sn0wix_.notEnoughKeybinds.util.InventoryUtils;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryKeys extends NotEKKeyBindings {
    public static final String INVENTORY_CATEGORY_KEY = "key.category." + NotEnoughKeybinds.MOD_ID + ".inventory";
    public static final KeyMapping.Category INVENTORY_CATEGORY = KeyMapping.Category.register(NotEnoughKeybinds.getIdentifier(INVENTORY_CATEGORY_KEY));


    public static int lastSwitchedTotemShieldSlot = -1;


    public static final NotEKKeyBinding EQUIP_ELYTRA = registerModKeyBinding(new EquipElytraBinding("equip_elytra", INVENTORY_CATEGORY, (client, keyBinding) -> {
        assert client.player != null;
        AtomicInteger itemSlot = new AtomicInteger(-1);
        boolean elytra = false;
        boolean swapFirstOrSecond = false;

        if (client.player.getInventory().getItem(EquipmentSlot.CHEST.getIndex(Inventory.INVENTORY_SIZE)).is(Items.ELYTRA)) {
            itemSlot.set(InventoryUtils.getSlotWithChestplate(client));
        } else if (!client.player.getInventory().getItem(EquipmentSlot.CHEST.getIndex(Inventory.INVENTORY_SIZE)).isEmpty()) {
            itemSlot.set(InventoryUtils.getSlotWithElytra(client));
            elytra = true;
        }

        if (!NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst.equals("off") && itemSlot.get() == -1) {
            String string = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst;

            for (int i = 0; i < 2; i++) {
                if (string.equals("chestplate")) {
                    itemSlot.set(InventoryUtils.getSlotWithChestplate(client));
                } else {
                    itemSlot.set(InventoryUtils.getSlotWithElytra(client));
                    elytra = true;
                }

                if (itemSlot.get() == 38)//found chest slotIn
                    itemSlot.set(-1);

                if (itemSlot.get() != -1) {
                    swapFirstOrSecond = true;
                    break;
                } else if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond) {
                    string = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.getOppositeSwap();
                    continue;
                }

                break;
            }
        }

        if (itemSlot.get() > -1) {
            InventoryUtils.equipChestplate(client, itemSlot.get());

            if (elytra) {
                itemSlot.set(InventoryUtils.getFireworkSlot(client.player.getInventory(), NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.longestDuration, NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.canExplode));

                if (itemSlot.get() > -1) {
                    int swapBackSlot = -1;
                    switch (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode) {
                        case 1 -> {
                            //hotbar
                            InventoryUtils.switchInvHotbarSlot(client, NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.fireworkSwapSlot, itemSlot.get());
                            if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.selectRocket)
                                client.player.getInventory().setSelectedSlot(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.fireworkSwapSlot);

                            swapBackSlot = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.fireworkSwapSlot;
                        }
                        case 2 -> {
                            //current slotIn
                            InventoryUtils.switchInvHandSlot(client, InteractionHand.MAIN_HAND, itemSlot.get());
                            swapBackSlot = client.player.getInventory().getSelectedSlot();
                        }
                        case 3 -> {
                            //offhand
                            InventoryUtils.switchInvHandSlot(client, InteractionHand.OFF_HAND, itemSlot.get());
                            swapBackSlot = 40;
                        }
                    }
                    if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapBackOldItem && swapBackSlot != -1 && NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode != 4) {
                        //write swap back
                        ElytraController.setSwapBackItem(itemSlot.get(), swapBackSlot, client.player.getInventory().getItem(itemSlot.get()).getItem());
                    }
                }

                Runnable useRocket = () -> {
                    if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode == 4) {
                        //quick use
                        InventoryUtils.quickUseItem(client, itemSlot.get());
                        return;
                    }

                    if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.useRocket && client.player.getItemInHand(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode == 3 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND).is(Items.FIREWORK_ROCKET))
                        InventoryUtils.interactItem(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode == 3 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND, client);
                };

                if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode) {
                    ElytraController.startFlying(ElytraController.getTicksToStartFlying(client), useRocket);
                } else {
                    useRocket.run();
                }

            } else if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapBackOldItem && !swapFirstOrSecond) {
                //execute swap back

                if (ElytraController.hasSwapBack() && ElytraController.shouldSwapBack(client.player.getInventory().getItem(ElytraController.getSwapBackItemSlot()))) {
                    InventoryUtils.switchInvHotbarSlot(client, ElytraController.getSwapBackRocketSlot(), ElytraController.getSwapBackItemSlot());
                    ElytraController.clearSwapBackData();
                }
            }
        }
    }));

    public static final NotEKKeyBinding SWITCH_TOTEM_SHIELD = registerModKeyBinding(new NotEKKeyBinding("switch_totem_shield", INVENTORY_CATEGORY, (client, keyBinding) -> {
        assert client.player != null;
        InteractionHand hand = client.player.getItemInHand(InteractionHand.OFF_HAND).isEmpty() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

        if (lastSwitchedTotemShieldSlot > -1)
            lastSwitchedTotemShieldSlot = client.player.getInventory().getItem(lastSwitchedTotemShieldSlot).is(client.player.getItemInHand(hand).getItem()) ?
                    -1 : client.player.getInventory().getItem(lastSwitchedTotemShieldSlot).isEmpty() ? -1 : lastSwitchedTotemShieldSlot;

        int slot = -1;

        if (client.player.getItemInHand(hand).is(Items.SHIELD)) {
            slot = InventoryUtils.getTotemSwapSlot(client, lastSwitchedTotemShieldSlot);
        } else if (client.player.getItemInHand(hand).is(Items.TOTEM_OF_UNDYING)) {
            slot = InventoryUtils.getShieldSwapSlot(client);
        }

        if (slot > -1 && slot != 40) {
            lastSwitchedTotemShieldSlot = slot;
            InventoryUtils.switchInvHandSlot(client, hand, slot);
            return;
        }

        if (!NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapFirst.equals("off")) {
            String string = NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapFirst;

            for (int i = 0; i < 2; i++) {
                if (string.equals("totem")) {
                    slot = InventoryUtils.getTotemSwapSlot(client, lastSwitchedTotemShieldSlot);
                } else {
                    slot = InventoryUtils.getShieldSwapSlot(client);
                }

                if (slot > -1 && slot != 40) {
                    InventoryUtils.switchInvHandSlot(client, InteractionHand.OFF_HAND, slot);
                    break;
                } else if (NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapSecond) {
                    string = NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getOppositeSwap();
                }
            }
        }
    }) {
        @Override
        public Component getTooltip() {
            return TextUtils.getText("switch_totem_shield", true);
        }

        @Override
        public Screen getSettingsScreen(Screen parent) {
            return new SwapTotemShieldSettings(parent);
        }
    });


    public static final NotEKKeyBinding THROW_ENDER_PEARL = registerModKeyBinding(new NotEKKeyBinding("throw_ender_pearl", INVENTORY_CATEGORY, (client, keyBinding) -> {
        for (InteractionHand hand : InteractionHand.values()) {
            if (client.player.getItemInHand(hand).is(Items.ENDER_PEARL)) {
                InventoryUtils.interactItem(hand, client);
                return;
            }
        }

        int pearlSlot = InventoryUtils.getSlotWithItem(Items.ENDER_PEARL, client.player.getInventory());

        if (pearlSlot > -1) {
            InventoryUtils.quickUseItem(client, pearlSlot);
        }
    }) {
        @Override
        public Component getTooltip() {
            return TextUtils.getText("throw_ender_pearl", true);
        }
    });


    public static final NotEKKeyBinding THROW_WIND_CHARGE = registerModKeyBinding(new NotEKKeyBinding("throw_wind_charge", INVENTORY_CATEGORY, (client, keyBinding) -> {
        for (InteractionHand hand : InteractionHand.values()) {
            if (client.player.getItemInHand(hand).is(Items.WIND_CHARGE)) {
                InventoryUtils.interactItem(hand, client);
                return;
            }
        }
        int chargeSlot = InventoryUtils.getSlotWithItem(Items.WIND_CHARGE, client.player.getInventory());

        if (chargeSlot > -1) {
            InventoryUtils.quickUseItem(client, chargeSlot);
        }
    }) {
        @Override
        public Component getTooltip() {
            return TextUtils.getText("throw_wind_charge", true);
        }
    });


    @Override
    public KeybindCategory getModCategory() {
        return new KeybindCategory(INVENTORY_CATEGORY_KEY, 0, THROW_WIND_CHARGE,THROW_ENDER_PEARL, SWITCH_TOTEM_SHIELD, EQUIP_ELYTRA);
    }
}
