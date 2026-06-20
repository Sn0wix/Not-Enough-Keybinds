package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import com.mojang.blaze3d.platform.InputConstants;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3ShortcutsKeys;

public class F3ShortcutKeybinding extends NotEKKeyBinding {
    private final int codeToEmulate;

    public F3ShortcutKeybinding(String translationKey, int code, int codeToEmulate) {
        super(translationKey, code, F3ShortcutsKeys.F3_SHORTCUTS_CATEGORY, null);
        this.codeToEmulate = codeToEmulate;
    }

    public F3ShortcutKeybinding(String translationKey, InputConstants.Type type, int code, int codeToEmulate) {
        super(translationKey, type, code, F3ShortcutsKeys.F3_SHORTCUTS_CATEGORY, null);
        this.codeToEmulate = codeToEmulate;
    }

    public F3ShortcutKeybinding(String translationKey,  int codeToEmulate) {
        this(translationKey, InputConstants.UNKNOWN.getValue(), codeToEmulate);
    }

    public F3ShortcutKeybinding(String translationKey, InputConstants.Type type, int codeToEmulate) {
        this(translationKey, type, InputConstants.UNKNOWN.getValue(), codeToEmulate);
    }


    public int getCodeToEmulate() {
        return codeToEmulate;
    }
}
