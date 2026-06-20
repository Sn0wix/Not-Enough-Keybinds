package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.InventoryKeys;

public class ElytraController {
    private static Runnable postFlight;
    private static Runnable nextTick = null;
    private static int ticksToStartFlying = Integer.MIN_VALUE;
    private static boolean shouldJump;
    private static int swapBackItemSlot = -1;
    private static int swapBackRocketSlot = -1;
    private static Item swapBackItem = Items.AIR.asItem();

    private static boolean previousFallFlying = false;

    public static void setSwapBackItem(int slot, int rocketSlot, Item item) {
        swapBackItem = item;
        swapBackItemSlot = slot;
        swapBackRocketSlot = rocketSlot;
    }

    public static boolean shouldSwapBack(ItemStack stack) {
        return stack.isOf(swapBackItem);
    }

    public static int getSwapBackItemSlot() {
        return swapBackItemSlot;
    }

    public static int getSwapBackRocketSlot() {
        return swapBackRocketSlot;
    }

    public static void clearSwapBackData() {
        swapBackItemSlot = -1;
        swapBackRocketSlot = -1;
        swapBackItem = Items.AIR.asItem();
    }

    public static boolean hasSwapBack() {
        return swapBackItemSlot > -1;
    }

    public static void startFlying(int ticksToStartFlying, Runnable postFlight) {
        shouldJump = true;
        ElytraController.ticksToStartFlying = ticksToStartFlying;
        ElytraController.postFlight = postFlight;
    }

    public static void tick() {
        //executing nextTick
        if (nextTick != null) {
            nextTick.run();
            nextTick = null;
        }

        //player landed with auto-detect on
        try {
            if (!MinecraftClient.getInstance().player.isGliding() && previousFallFlying && NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect) {
                String swapFirstBefore = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst;
                boolean swapSecondBefore = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond;

                //tweaking the parameters needed
                NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst = "chestplate";
                NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond = false;

                InventoryKeys.EQUIP_ELYTRA.onWasPressed(MinecraftClient.getInstance());

                //setting back the old parameters
                NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst = swapFirstBefore;
                NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond = swapSecondBefore;
            }
            previousFallFlying = MinecraftClient.getInstance().player.isGliding();
        }catch (NullPointerException ignored) {}

        //Enter flight mode logic
        if (ticksToStartFlying > -1) {
            ticksToStartFlying--;

            try {
                if (MinecraftClient.getInstance().player.isGliding()) {
                    postFlight.run();
                    ticksToStartFlying = Integer.MIN_VALUE;
                }
            } catch (NullPointerException ignored) {
            }

            if (ticksToStartFlying == 0) {
                shouldJump = true;
            } else if (ticksToStartFlying == -1) {
                postFlight.run();
            }
        }
    }

    public static boolean shouldStimulateJump() {
        tick();

        if (shouldJump) {
            shouldJump = false;
            return true;
        }

        return false;
    }

    public static void nextTick(Runnable runnable) {
        nextTick = runnable;
    }

    public static int getTicksToStartFlying(MinecraftClient client) throws NullPointerException {
        return NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect ? 1 : client.player.isCreative() ? 8 : 3;
    }
}
