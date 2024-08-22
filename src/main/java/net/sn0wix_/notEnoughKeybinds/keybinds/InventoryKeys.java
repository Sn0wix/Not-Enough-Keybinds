package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings.SwapTotemShieldSettings;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.InventoryUtils;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class InventoryKeys extends NotEKKeyBindings {
    public static final String INVENTORY_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".inventory";
    public static int lastSwitchedTotemShieldSlot = -1;


    public static final NotEKKeyBinding EQUIP_ELYTRA = registerModKeyBinding(new NotEKKeyBinding("equip_elytra", INVENTORY_CATEGORY, (client, keyBinding) -> {
        assert client.player != null;
        int itemSlot = -1;

        if (client.player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId()).isOf(Items.ELYTRA)) {
            itemSlot = InventoryUtils.getBestChestplateSlot(client.player.getInventory());
        } else if (!client.player.getInventory().getArmorStack(EquipmentSlot.CHEST.getEntitySlotId()).isEmpty()) {
            itemSlot = InventoryUtils.getBestBreakableItemSlot(client.player.getInventory(), Items.ELYTRA, 216);
        }

        if (itemSlot > -1) {
            InventoryUtils.switchInvSlot(client, itemSlot, 41);
            NotEnoughKeybinds.LOGGER.info("switched");
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

        if (!NotEnoughKeybinds.COMMON_CONFIG.swapFirst.equals("off")) {
            String string = NotEnoughKeybinds.COMMON_CONFIG.swapFirst;

            for (int i = 0; i < 2; i++) {
                if (string.equals("totem")) {
                    slot = InventoryUtils.getTotemSwapSlot(client, lastSwitchedTotemShieldSlot);
                } else {
                    slot = InventoryUtils.getShieldSwapSlot(client);
                }

                if (slot > -1 && slot != 40) {
                    InventoryUtils.switchInvHandSlot(client, Hand.OFF_HAND, slot);
                    break;
                } else if (NotEnoughKeybinds.COMMON_CONFIG.swapSecond) {
                    string = NotEnoughKeybinds.COMMON_CONFIG.getOppositeSwap();
                }
            }
        }
    }) {
        @Override
        public Text getTooltip() {
            return Text.translatable(TextUtils.getTextTranslation("switch_totem_shield", true));
        }

        @Override
        public Screen getSettingsScreen(Screen parent) {
            return new SwapTotemShieldSettings(parent, MinecraftClient.getInstance().options);
        }
    });


    public static final NotEKKeyBinding THROW_ENDER_PEARL = registerModKeyBinding(new NotEKKeyBinding("throw_ender_pearl", INVENTORY_CATEGORY, (client, keyBinding) -> {
        for (Hand hand : Hand.values()) {
            if (client.player.getStackInHand(hand).isOf(Items.ENDER_PEARL)) {
                InventoryUtils.interactItem(hand, client);
                return;
            }
        }

        int pearlSlot = client.player.getInventory().getSlotWithStack(Items.ENDER_PEARL.getDefaultStack());

        if (pearlSlot > -1 && !PlayerInventory.isValidHotbarIndex(pearlSlot)) {
            InventoryUtils.switchInvHandSlot(client, Hand.MAIN_HAND, pearlSlot);
            InventoryUtils.interactItem(Hand.MAIN_HAND, client);
            InventoryUtils.switchInvHandSlot(client, Hand.MAIN_HAND, pearlSlot);

        } else if (pearlSlot > -1 && PlayerInventory.isValidHotbarIndex(pearlSlot)) {
            int slotBefore = client.player.getInventory().selectedSlot;
            client.player.getInventory().selectedSlot = pearlSlot;
            InventoryUtils.interactItem(Hand.MAIN_HAND, client);
            client.player.getInventory().selectedSlot = slotBefore;
        }
    }) {
        @Override
        public Text getTooltip() {
            return Text.translatable(TextUtils.getTextTranslation("throw_ender_pearl", true));
        }
    });

    @Override
    public KeybindCategory getCategory() {
        return new KeybindCategory(INVENTORY_CATEGORY, 0, THROW_ENDER_PEARL, SWITCH_TOTEM_SHIELD, EQUIP_ELYTRA);
    }
}
