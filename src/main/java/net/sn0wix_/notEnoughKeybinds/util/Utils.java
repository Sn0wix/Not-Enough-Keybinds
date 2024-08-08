package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3ShortcutsKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ModKeyBinding;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static KeyBinding[] filterModKeybindings(KeyBinding[] keyBindings) {
        ArrayList<KeyBinding> newBindings = new ArrayList<>();

        for (KeyBinding keyBinding : keyBindings) {
            if (keyBinding instanceof ModKeyBinding) {
                continue;
            }

            newBindings.add(keyBinding);
        }

        KeyBinding[] finalBindings = new KeyBinding[newBindings.size()];

        for (int i = 0; i < newBindings.size(); i++) {
            finalBindings[i] = newBindings.get(i);
        }

        return finalBindings;
    }

    public static List<Integer> checkF3Shortcuts(int key, int scanCode) {
        ArrayList<Integer> codes = new ArrayList<>();

        F3ShortcutsKeys.getF3ShortcutKeys().forEach(f3ShortcutKeybinding -> {
            if (f3ShortcutKeybinding.matchesKey(key, scanCode)) {
                codes.add(f3ShortcutKeybinding.getCodeToEmulate());
            }
        });

        return codes;
    }

    public static void interactItem(Hand hand, MinecraftClient client) throws NullPointerException {
        ActionResult actionResult3 = client.interactionManager.interactItem(client.player, hand);
        if (actionResult3.isAccepted()) {
            if (actionResult3.shouldSwingHand()) {
                client.player.swingHand(hand);
            }
        }
    }
}
