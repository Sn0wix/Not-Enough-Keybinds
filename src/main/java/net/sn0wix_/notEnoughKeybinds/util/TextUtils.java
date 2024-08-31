package net.sn0wix_.notEnoughKeybinds.util;

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

    public static Text getText(String path, boolean tooltip) {
        return Text.translatable(getTranslationKey(path, tooltip));
    }

    public static String getTranslationKey(String path, boolean tooltip) {
        return "text." + NotEnoughKeybinds.MOD_ID + (tooltip ? ".tooltip" + "." + path : "." + path);
    }

    public static String getSettingsTranslationKey(String path) {
        return "settings." + NotEnoughKeybinds.MOD_ID + "." + path;
    }
}
