package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3ShortcutsKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;

import java.util.*;

public class Utils {

    public static Screen getModConfirmScreen(ParentScreenBlConsumer consumer, Text text) {
        return new ConfirmScreen(consumer, Text.empty(), text,
                Text.translatable("text.not-enough-keybinds.confirm." + new Random().nextInt(4)),
                Text.translatable("text.not-enough-keybinds.cancel." + new Random().nextInt(4)));
    }


    public static KeyBinding[] filterModKeybindings(KeyBinding[] keyBindings) {
        ArrayList<KeyBinding> newBindings = new ArrayList<>();

        for (KeyBinding keyBinding : keyBindings) {
            if (keyBinding instanceof NotEKKeyBinding) {
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


    public static Text correctF3DebugMessage(Text message) {
        String translatedMessage = message.getString();
        String f3String = "F3 + ";
        for (int i = 0; i < F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings().length; i++) {
            String newKey = F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i].getBoundKeyLocalizedText().getString().replace(f3String, "");
            String oldKey = F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i].getDefaultKey().getLocalizedText().getString();

            if (translatedMessage.contains(f3String + oldKey)) {
                translatedMessage = translatedMessage.replace(f3String + oldKey, f3String + newKey);
            }
        }

        return Text.of(translatedMessage);
    }

    public static Object[] addArgToEnd(Object[] args, Object addedArg) {
        Object[] newArgs = new Object[args.length + 1];
        System.arraycopy(args, 0, newArgs, 0, args.length);

        newArgs[args.length] = addedArg;
        return newArgs;
    }
}
