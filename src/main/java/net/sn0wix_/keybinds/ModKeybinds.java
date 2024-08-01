package net.sn0wix_.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.sn0wix_.NotEnoughKeybinds;
import net.sn0wix_.keybinds.custom.F3ShortcutKeybinding;
import net.sn0wix_.keybinds.custom.ModKeyBinding;
import net.sn0wix_.util.Utils;

import java.util.ArrayList;

public class ModKeybinds {
    public static final ArrayList<ModKeyBinding> MOD_KEY_BINDINGS = new ArrayList<>();
    public static final ArrayList<F3ShortcutKeybinding> F3_SHORTCUT_KEYS = new ArrayList<>(8);


    public static final String INVENTORY_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".inventory";
    public static final String F3_SHORTCUTS_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".f3_shortcuts";
    public static final String SKIN_LAYERS_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".skin_layers";
    public static final String BUILDING_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".building";

    public static final String KEY_BINDING_PREFIX = "key." + NotEnoughKeybinds.MOD_ID + ".";


    //inventory
    public static final KeyBinding SWITCH_TOTEM_SHIELD = registerModKeyBinding(new ModKeyBinding("switch_totem_shield", INVENTORY_CATEGORY, (client, keyBinding) -> {
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


    public static final KeyBinding THROW_ENDER_PEARL = registerModKeyBinding(new ModKeyBinding("throw_ender_pearl", INVENTORY_CATEGORY, (client, keyBinding) -> {
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


    //building
    public static final KeyBinding FAST_BUILDING = registerModKeyBinding(new ModKeyBinding("fast_building", BUILDING_CATEGORY, new ModKeyBinding.OnClientTick() {
        @Override
        public void onWasPressedInGame(MinecraftClient client, KeyBinding keyBinding) {
        }

        @Override
        public void onGameTick(MinecraftClient client) {
            for (Hand hand : Hand.values()) {
                ItemStack itemStack = client.player.getStackInHand(hand);

                if (ModKeybinds.FAST_BUILDING.isPressed() || ModKeybinds.FAST_BUILDING.wasPressed()) {
                    if (client.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
                        BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                        int i = itemStack.getCount();
                        ActionResult actionResult2 = client.interactionManager.interactBlock(client.player, hand, blockHitResult);
                        if (actionResult2.isAccepted()) {
                            if (actionResult2.shouldSwingHand()) {
                                client.player.swingHand(hand);
                                if (!itemStack.isEmpty() && (itemStack.getCount() != i || client.interactionManager.hasCreativeInventory())) {
                                    client.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                                }
                            }
                            return;
                        }
                        if (actionResult2 == ActionResult.FAIL) {
                            return;
                        }
                    }
                }
            }
        }
    }));
    public static final KeyBinding FAST_BLOCK_BREAKING = registerModKeyBinding(new ModKeyBinding("fast_block_breaking", BUILDING_CATEGORY, new ModKeyBinding.OnClientTick() {
        @Override
        public void onWasPressedInGame(MinecraftClient client, KeyBinding keyBinding) {
        }

        @Override
        public void onGameTick(MinecraftClient client) {
            for (Hand hand : Hand.values()) {
                if (ModKeybinds.FAST_BLOCK_BREAKING.isPressed() || ModKeybinds.FAST_BLOCK_BREAKING.wasPressed()) {
                    if (client.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
                        BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                        BlockPos blockPos = blockHitResult.getBlockPos();
                        if (!client.world.getBlockState(blockPos).isAir()) {
                            client.interactionManager.attackBlock(blockPos, blockHitResult.getSide());
                            client.player.swingHand(hand);
                            break;
                        }
                    }
                }
            }
        }
    }));

    public static final KeyBinding ALWAYS_PLACE_ITEM = registerModKeyBinding(new ModKeyBinding("always_place_item", BUILDING_CATEGORY, (client, keyBinding) -> {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = client.player.getStackInHand(hand);

            if (!client.crosshairTarget.getType().equals(HitResult.Type.ENTITY)) {
                BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                int i = itemStack.getCount();
                ActionResult actionResult2 = client.interactionManager.interactBlock(client.player, hand, blockHitResult);
                if (actionResult2.isAccepted()) {
                    if (actionResult2.shouldSwingHand()) {
                        client.player.swingHand(hand);
                        if (!itemStack.isEmpty() && (itemStack.getCount() != i || client.interactionManager.hasCreativeInventory())) {
                            client.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                        }
                    }
                    return;
                }
                if (actionResult2 == ActionResult.FAIL) {
                    return;
                }
            }
        }
    }));


    //why this isn't a thing in vanilla
    public static final KeyBinding TOGGLE_HIDE_HUD = registerKeyBinding(new KeyBinding(KEY_BINDING_PREFIX + "toggle_hide_hud", InputUtil.GLFW_KEY_F1, KeyBinding.MISC_CATEGORY));



    public static void registerModKeybinds() {
        NotEnoughKeybinds.LOGGER.info("Registering key bindings for " + NotEnoughKeybinds.MOD_ID);
        F3ShortcutsKeybinds.registerKeybinds();
        SkinLayersKeybinds.registerSkinLayerKeybinds();
    }

    public static KeyBinding registerModKeyBinding(ModKeyBinding keyBinding) {
        if (keyBinding instanceof F3ShortcutKeybinding f3ShortcutKeybinding) {
            F3_SHORTCUT_KEYS.add(f3ShortcutKeybinding);
        }

        MOD_KEY_BINDINGS.add(keyBinding);
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    public static KeyBinding registerKeyBinding(KeyBinding keyBinding) {
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }
}
