package net.sn0wix_.keybinds;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class ModKeyBinding extends KeyBinding {
    public ModKeyBinding(String translationKey, int code, String category) {
        super(translationKey, code, category);
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, int code, String category) {
        super(translationKey, type, code, category);
    }
}
