package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import java.util.Arrays;

public class KeybindingCategory {
    private final String TRANSLATION_KEY;
    private final INotEKKeybinding[] keyBindings;
    private final int priority;

    public KeybindingCategory(String translationKey, int priority, boolean sort, INotEKKeybinding... keyBindings) {
        this.TRANSLATION_KEY = translationKey;
        this.keyBindings = keyBindings;
        this.priority = priority;

        if (sort) {
            Arrays.sort(keyBindings);
        }
    }

    public KeybindingCategory(String translationKey, int priority, INotEKKeybinding... keyBindings) {
        this(translationKey, priority, true, keyBindings);
    }

    public String getTranslationKey() {
        return TRANSLATION_KEY;
    }

    public INotEKKeybinding[] getKeyBindings() {
        return keyBindings;
    }

    public int getPriority() {
        return priority;
    }
}
