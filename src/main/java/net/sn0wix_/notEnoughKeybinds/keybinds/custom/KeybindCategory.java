package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.gui.screen.Screen;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen.ControlsListWidget;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class KeybindCategory {
    private final String TRANSLATION_KEY;
    private final INotEKKeybinding[] keyBindings;
    private final int priority;

    public KeybindCategory(String translationKey, int priority, boolean sort, INotEKKeybinding... keyBindings) {
        this.TRANSLATION_KEY = translationKey;
        this.keyBindings = keyBindings;
        this.priority = priority;

        if (sort) {
            Arrays.sort(keyBindings);
        }
    }

    public KeybindCategory(String translationKey, int priority, INotEKKeybinding... keyBindings) {
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

    public String getAddNewButtonTranslation() {
        return "";
    }

    @Nullable
    public Screen getAddNewButtonScreen(Screen parent) {
        return null;
    }
}
