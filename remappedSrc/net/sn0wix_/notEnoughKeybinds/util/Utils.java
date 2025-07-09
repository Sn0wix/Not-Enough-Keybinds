package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.AdvancedConfirmScreen;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3ShortcutsKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Utils {

    public static Screen getModConfirmScreen(ParentScreenBlConsumer consumer, Text text) {
        return new AdvancedConfirmScreen(consumer, Text.empty(), text,
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

    public static String nextValue(String errorRegex, BufferedReader reader) {
        try {
            return getValue(reader.readLine());
        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            NotEnoughKeybinds.LOGGER.error("Incorrect " + errorRegex + " format");
        }

        return "";
    }

    public static String getValue(String string) {
        String[] list = string.split(":");
        //make sure that even if the last char is ':' it still gets through
        if (string.charAt(string.length() - 1) == ':')
            list[list.length - 1] = list[list.length - 1] + ":";

        StringBuilder builder = new StringBuilder();
        //remove the initial entry name
        list[0] = "";

        for (int i = 0; i < list.length; i++) {
            if (i > 1)
                builder.append(":");
            builder.append(list[i]);
        }

        return builder.toString();
    }

    public static List<String> bindingsToList(boolean defaultBindings) {
        ArrayList<String> bindingsList = new ArrayList<>();

        Stream.of(MinecraftClient.getInstance().options.allKeys, ChatKeys.CHAT_KEYS_CATEGORY.getKeyBindings(), F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()).toList().forEach(bindings -> {
            if (bindings instanceof INotEKKeybinding[] newBindings) {
                for (INotEKKeybinding binding : newBindings) {
                    bindingsList.add(binding.getTranslationKey() + ":" + (defaultBindings ? binding.getDefaultKey().getTranslationKey() : binding.getBoundKeyTranslation()));
                }
            } else if (bindings instanceof KeyBinding[] newBindings) {
                for (KeyBinding binding : newBindings) {
                    bindingsList.add(binding.getTranslationKey() + ":" + (defaultBindings ? binding.getDefaultKey().getTranslationKey() : binding.getBoundKeyTranslationKey()));
                }
            }
        });

        return bindingsList;
    }

    public static void showToastNotification(Text description) {
        MinecraftClient.getInstance().getToastManager().add(new SystemToast(new SystemToast.Type(2769), Text.literal(NotEnoughKeybinds.MOD_NAME), description));
    }

    public static void showToastNotification(Text description, long displayDuration) {
        MinecraftClient.getInstance().getToastManager().add(new SystemToast(new SystemToast.Type(displayDuration), Text.literal(NotEnoughKeybinds.MOD_NAME), description));
    }
}
