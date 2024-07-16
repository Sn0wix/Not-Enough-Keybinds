package net.sn0wix_.keybinds;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class F3ShortcutKeybinding extends KeyBinding {
    private final int codeToEmulate;
    public F3ShortcutKeybinding(String translationKey, int code, String category, int codeToEmulate) {
        super(translationKey, code, category);
        this.codeToEmulate = codeToEmulate;
    }

    public F3ShortcutKeybinding(String translationKey, InputUtil.Type type, int code, String category, int codeToEmulate) {
        super(translationKey, type, code, category);
        this.codeToEmulate = codeToEmulate;
    }

    public int getCodeToEmulate() {
        return codeToEmulate;
    }
}
