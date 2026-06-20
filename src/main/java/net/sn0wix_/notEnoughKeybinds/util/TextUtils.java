package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

public class TextUtils {
    public static Component getCombinedTranslation(Component translation1, Component translation2) {
        return Component.nullToEmpty(translation1.getString() + translation2.getString());
    }

    public static String getTranslationKey(String path) {
        return getTranslationKey(path, false);
    }

    public static Component getText(String path) {
        return Component.translatable(getTranslationKey(path));
    }

    public static Component getText(String path, Object... args) {
        return Component.translatable(getTranslationKey(path), args);
    }

    public static Component getText(String path, boolean tooltip) {
        return Component.translatable(getTranslationKey(path, tooltip));
    }

    public static Tooltip getTooltip(String path) {
        return Tooltip.create(getText(path, true));
    }

    public static Tooltip getTooltip(String path, Object... args) {
        return Tooltip.create(Component.translatable(getTranslationKey(path, true), args));
    }

    public static String getTranslationKey(String path, boolean tooltip) {
        return "text." + NotEnoughKeybinds.MOD_ID + (tooltip ? ".tooltip" + "." + path : "." + path);
    }

    public static String getSettingsTranslationKey(String path) {
        return "settings." + NotEnoughKeybinds.MOD_ID + "." + path;
    }

    public static String trimText(String text, Font textRenderer, int maxWidth) {
        String textToRender = text;
        try {
            if (maxWidth < textRenderer.width(text)) {
                StringBuilder builder = new StringBuilder();
                int i = 0;
                while (textRenderer.width(builder + "...") < maxWidth) {
                    builder.append(textToRender.charAt(i));
                    i++;
                }

                textToRender = builder + "...";
            }
        } catch (IndexOutOfBoundsException ignored) {
        }

        return textToRender;
    }
}
