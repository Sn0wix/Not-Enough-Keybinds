package net.sn0wix_.notEnoughKeybinds.util;

public class ElytraController {
    private static Runnable postFlight;
    private static int ticksToStartFlying = Integer.MIN_VALUE;
    private static boolean shouldJump;

    public static void startFlying(int ticksToStartFlying, Runnable postFlight) {
        shouldJump = true;
        ElytraController.ticksToStartFlying = ticksToStartFlying;
        ElytraController.postFlight = postFlight;
    }

    public static void tick() {
        if (ticksToStartFlying > -1) {
            ticksToStartFlying--;

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
