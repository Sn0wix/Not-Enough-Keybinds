package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ElytraController {
    private static Runnable postFlight;
    private static int ticksToStartFlying = Integer.MIN_VALUE;
    private static boolean shouldJump;
    private static int swapBackItemSlot = -1;
    private static int swapBackRocketSlot = -1;
    private static Item swapBackItem = Items.AIR.asItem();

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
        if (ticksToStartFlying > -1) {
            ticksToStartFlying--;

            try {
                if (MinecraftClient.getInstance().player.isFallFlying()) {
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

    public static boolean shouldSimulateJump() {
        tick();

        if (shouldJump) {
            shouldJump = false;
            return true;
        }

        return false;
    }
}
