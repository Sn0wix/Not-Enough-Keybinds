package net.sn0wix_.notEnoughKeybinds.util;

public class ElytraController {
    private static int simulateJumpFor = 0;
    public static void startFlying() {
        simulateJumpFor = 5;
    }

    public static boolean shouldSimulateJump() {
        return true;
    }
}
