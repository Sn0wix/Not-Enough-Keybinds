package net.sn0wix_.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.sn0wix_.keybinds.custom.ModKeyBinding;

import java.util.ArrayList;
import java.util.List;

import static net.sn0wix_.keybinds.ModKeybinds.F3_SHORTCUT_KEYS;

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

        F3_SHORTCUT_KEYS.forEach(f3ShortcutKeybinding -> {
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
            client.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
        }
    }
}
