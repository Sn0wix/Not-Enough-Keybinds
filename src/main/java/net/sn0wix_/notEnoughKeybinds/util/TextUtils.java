package net.sn0wix_.notEnoughKeybinds.util;

import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;

public class TextUtils {
    public static Text getCombinedTranslation(Text translation1, Text translation2) {
        return Text.of(translation1.getString() + translation2.getString());
    }

    public static String getTextTranslation(String path) {
        return getTextTranslation(path, false);
    }

    public static String getTextTranslation(String path, boolean tooltip) {
        return "text." + NotEnoughKeybinds.MOD_ID + (tooltip ? ".tooltip" + "." + path : "." + path);
    }

    public static String getSettingsTranslation(String path) {
        return "settings." + NotEnoughKeybinds.MOD_ID + "." + path;
    }
}
