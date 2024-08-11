package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.util.InputUtil;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3ShortcutsKeys;

public class F3ShortcutKeybinding extends NotEKKeyBinding {
    private final int codeToEmulate;

    public F3ShortcutKeybinding(String translationKey, int code, int codeToEmulate) {
        super(translationKey, code, F3ShortcutsKeys.F3_SHORTCUTS_CATEGORY_STRING, null);
        this.codeToEmulate = codeToEmulate;
    }

    public F3ShortcutKeybinding(String translationKey, InputUtil.Type type, int code, int codeToEmulate) {
        super(translationKey, type, code, F3ShortcutsKeys.F3_SHORTCUTS_CATEGORY_STRING, null);
        this.codeToEmulate = codeToEmulate;
    }

    public F3ShortcutKeybinding(String translationKey,  int codeToEmulate) {
        this(translationKey, InputUtil.UNKNOWN_KEY.getCode(), codeToEmulate);
    }

    public F3ShortcutKeybinding(String translationKey, InputUtil.Type type, int codeToEmulate) {
        this(translationKey, type, InputUtil.UNKNOWN_KEY.getCode(), codeToEmulate);
    }


    public int getCodeToEmulate() {
        return codeToEmulate;
    }
}
