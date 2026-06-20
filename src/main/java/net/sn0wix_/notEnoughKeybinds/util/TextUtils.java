package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

public class TextUtils {
    public static Text getCombinedTranslation(Text translation1, Text translation2) {
        return Text.of(translation1.getString() + translation2.getString());
    }

    public static String getTranslationKey(String path) {
        return getTranslationKey(path, false);
    }

    public static Text getText(String path) {
        return Text.translatable(getTranslationKey(path));
    }

    public static Text getText(String path, Object... args) {
        return Text.translatable(getTranslationKey(path), args);
    }

    public static Text getText(String path, boolean tooltip) {
        return Text.translatable(getTranslationKey(path, tooltip));
    }

    public static Tooltip getTooltip(String path) {
        return Tooltip.of(getText(path, true));
    }

    public static Tooltip getTooltip(String path, Object... args) {
        return Tooltip.of(Text.translatable(getTranslationKey(path, true), args));
    }

    public static String getTranslationKey(String path, boolean tooltip) {
        return "text." + NotEnoughKeybinds.MOD_ID + (tooltip ? ".tooltip" + "." + path : "." + path);
    }

    public static String getSettingsTranslationKey(String path) {
        return "settings." + NotEnoughKeybinds.MOD_ID + "." + path;
    }

    public static String trimText(String text, TextRenderer textRenderer, int maxWidth) {
        String textToRender = text;
        try {
            if (maxWidth < textRenderer.getWidth(text)) {
                StringBuilder builder = new StringBuilder();
                int i = 0;
                while (textRenderer.getWidth(builder + "...") < maxWidth) {
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
