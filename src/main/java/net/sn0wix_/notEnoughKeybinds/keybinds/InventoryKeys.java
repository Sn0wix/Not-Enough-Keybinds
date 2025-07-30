package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.config.EquipElytraConfig;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings.EquipElytraSettings;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings.SwapTotemShieldSettings;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.EquipElytraBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.ElytraController;
import net.sn0wix_.notEnoughKeybinds.util.InventoryUtils;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class InventoryKeys extends NotEKKeyBindings {
    public static final String INVENTORY_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".inventory";
    public static int lastSwitchedTotemShieldSlot = -1;


    public static final NotEKKeyBinding EQUIP_ELYTRA = registerModKeyBinding(new EquipElytraBinding("equip_elytra", INVENTORY_CATEGORY, (client, keyBinding) -> {
        assert client.player != null;
        AtomicInteger itemSlot = new AtomicInteger(-1);
        boolean elytra = false;
        boolean swapFirstOrSecond = false;

        if (client.player.getInventory().getStack(EquipmentSlot.CHEST.getOffsetEntitySlotId(PlayerInventory.MAIN_SIZE)).isOf(Items.ELYTRA)) {
            itemSlot.set(InventoryUtils.getSlotWithChestplate(client));
        } else if (!client.player.getInventory().getStack(EquipmentSlot.CHEST.getOffsetEntitySlotId(PlayerInventory.MAIN_SIZE)).isEmpty()) {
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
                            InventoryUtils.switchInvHandSlot(client, Hand.MAIN_HAND, itemSlot.get());
                            swapBackSlot = client.player.getInventory().getSelectedSlot();
                        }
                        case 3 -> {
                            //offhand
                            InventoryUtils.switchInvHandSlot(client, Hand.OFF_HAND, itemSlot.get());
                            swapBackSlot = 40;
                        }
                    }
                    if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapBackOldItem && swapBackSlot != -1 && NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode != 4) {
                        //write swap back
                        ElytraController.setSwapBackItem(itemSlot.get(), swapBackSlot, client.player.getInventory().getStack(itemSlot.get()).getItem());
                    }
                }

                Runnable useRocket = () -> {
                    if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode == 4) {
                        //quick use
                        InventoryUtils.quickUseItem(client, itemSlot.get());
                        return;
                    }

                    if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.useRocket && client.player.getStackInHand(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode == 3 ? Hand.OFF_HAND : Hand.MAIN_HAND).isOf(Items.FIREWORK_ROCKET))
                        InventoryUtils.interactItem(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode == 3 ? Hand.OFF_HAND : Hand.MAIN_HAND, client);
                };

                if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode) {
                    ElytraController.startFlying(ElytraController.getTicksToStartFlying(client), useRocket);
                } else {
                    useRocket.run();
                }

            } else if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapBackOldItem && !swapFirstOrSecond) {
                //execute swap back

                if (ElytraController.hasSwapBack() && ElytraController.shouldSwapBack(client.player.getInventory().getStack(ElytraController.getSwapBackItemSlot()))) {
                    InventoryUtils.switchInvHotbarSlot(client, ElytraController.getSwapBackRocketSlot(), ElytraController.getSwapBackItemSlot());
                    ElytraController.clearSwapBackData();
                }
            }
        }
    }));

    public static final NotEKKeyBinding SWITCH_TOTEM_SHIELD = registerModKeyBinding(new NotEKKeyBinding("switch_totem_shield", INVENTORY_CATEGORY, (client, keyBinding) -> {
        assert client.player != null;
        Hand hand = client.player.getStackInHand(Hand.OFF_HAND).isEmpty() ? Hand.MAIN_HAND : Hand.OFF_HAND;

        if (lastSwitchedTotemShieldSlot > -1)
            lastSwitchedTotemShieldSlot = client.player.getInventory().getStack(lastSwitchedTotemShieldSlot).isOf(client.player.getStackInHand(hand).getItem()) ?
                    -1 : client.player.getInventory().getStack(lastSwitchedTotemShieldSlot).isEmpty() ? -1 : lastSwitchedTotemShieldSlot;

        int slot = -1;

        if (client.player.getStackInHand(hand).isOf(Items.SHIELD)) {
            slot = InventoryUtils.getTotemSwapSlot(client, lastSwitchedTotemShieldSlot);
        } else if (client.player.getStackInHand(hand).isOf(Items.TOTEM_OF_UNDYING)) {
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
                    InventoryUtils.switchInvHandSlot(client, Hand.OFF_HAND, slot);
                    break;
                } else if (NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapSecond) {
                    string = NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getOppositeSwap();
                }
            }
        }
    }) {
        @Override
        public Text getTooltip() {
            return TextUtils.getText("switch_totem_shield", true);
        }

        @Override
        public Screen getSettingsScreen(Screen parent) {
            return new SwapTotemShieldSettings(parent);
        }
    });


    public static final NotEKKeyBinding THROW_ENDER_PEARL = registerModKeyBinding(new NotEKKeyBinding("throw_ender_pearl", INVENTORY_CATEGORY, (client, keyBinding) -> {
        for (Hand hand : Hand.values()) {
            if (client.player.getStackInHand(hand).isOf(Items.ENDER_PEARL)) {
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
        public Text getTooltip() {
            return TextUtils.getText("throw_ender_pearl", true);
        }
    });

    @Override
    public KeybindCategory getCategory() {
        return new KeybindCategory(INVENTORY_CATEGORY, 0, THROW_ENDER_PEARL, SWITCH_TOTEM_SHIELD, EQUIP_ELYTRA);
    }
}
