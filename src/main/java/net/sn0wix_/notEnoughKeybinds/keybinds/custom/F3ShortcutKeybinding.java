package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.util.InputUtil;

public class F3ShortcutKeybinding extends NotEKKeyBinding {
    private final int codeToEmulate;

    public F3ShortcutKeybinding(String translationKey, int code, String category, int codeToEmulate) {
        super(translationKey, code, category, null);
        this.codeToEmulate = codeToEmulate;
    }

    public F3ShortcutKeybinding(String translationKey, InputUtil.Type type, int code, String category, int codeToEmulate) {
        super(translationKey, type, code, category, null);
        this.codeToEmulate = codeToEmulate;
    }

    public F3ShortcutKeybinding(String translationKey, String category, int codeToEmulate) {
        this(translationKey, InputUtil.UNKNOWN_KEY.getCode(), category, codeToEmulate);
    }

    public F3ShortcutKeybinding(String translationKey, InputUtil.Type type, String category, int codeToEmulate) {
        this(translationKey, type, InputUtil.UNKNOWN_KEY.getCode(), category, codeToEmulate);
    }


    public int getCodeToEmulate() {
        return codeToEmulate;
    }
}
