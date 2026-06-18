package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
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

    public static Screen getModConfirmScreen(ParentScreenBlConsumer consumer, Component text) {
        return new AdvancedConfirmScreen(consumer, Component.empty(), text,
                Component.translatable("text.not-enough-keybinds.confirm." + new Random().nextInt(4)),
                Component.translatable("text.not-enough-keybinds.cancel." + new Random().nextInt(4)));
    }


    public static KeyMapping[] filterModKeybindings(KeyMapping[] keyBindings) {
        ArrayList<KeyMapping> newBindings = new ArrayList<>();

        for (KeyMapping keyBinding : keyBindings) {
            if (keyBinding instanceof NotEKKeyBinding) {
                continue;
            }

            newBindings.add(keyBinding);
        }

        KeyMapping[] finalBindings = new KeyMapping[newBindings.size()];

        for (int i = 0; i < newBindings.size(); i++) {
            finalBindings[i] = newBindings.get(i);
        }

        return finalBindings;
    }

    public static List<Integer> checkF3Shortcuts(KeyEvent input) {
        ArrayList<Integer> codes = new ArrayList<>();

        F3ShortcutsKeys.getF3ShortcutKeys().forEach(f3ShortcutKeybinding -> {
            if (f3ShortcutKeybinding.matches(input)) {
                codes.add(f3ShortcutKeybinding.getCodeToEmulate());
            }
        });

        return codes;
    }


    public static Component correctF3DebugMessage(Component message) {
        String translatedMessage = message.getString();
        String f3String = "F3 + ";
        for (int i = 0; i < F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings().length; i++) {
            String newKey = F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i].getBoundKeyLocalizedText().getString().replace(f3String, "");
            String oldKey = F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i].getDefaultKey().getDisplayName().getString();

            if (translatedMessage.contains(f3String + oldKey)) {
                translatedMessage = translatedMessage.replace(f3String + oldKey, f3String + newKey);
            }
        }

        return Component.nullToEmpty(translatedMessage);
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

        Stream.of(Minecraft.getInstance().options.keyMappings, ChatKeys.CHAT_KEYS_MOD_CATEGORY.getKeyBindings(), F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()).toList().forEach(bindings -> {
            if (bindings instanceof INotEKKeybinding[] newBindings) {
                for (INotEKKeybinding binding : newBindings) {
                    bindingsList.add(binding.getId() + ":" + (defaultBindings ? binding.getDefaultKey().getName() : binding.getBoundKeyTranslation()));
                }
            } else if (bindings instanceof KeyMapping[] newBindings) {
                for (KeyMapping binding : newBindings) {
                    bindingsList.add(binding.getName() + ":" + (defaultBindings ? binding.getDefaultKey().getName() : binding.saveString()));
                }
            }
        });

        return bindingsList;
    }

    public static void showToastNotification(Component description) {
        Minecraft.getInstance().getToastManager().addToast(new SystemToast(new SystemToast.SystemToastId(2769), Component.literal(NotEnoughKeybinds.MOD_NAME), description));
    }

    public static void showToastNotification(Component description, long displayDuration) {
        Minecraft.getInstance().getToastManager().addToast(new SystemToast(new SystemToast.SystemToastId(displayDuration), Component.literal(NotEnoughKeybinds.MOD_NAME), description));
    }
}
