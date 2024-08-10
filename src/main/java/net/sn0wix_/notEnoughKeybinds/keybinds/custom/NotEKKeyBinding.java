package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeybindings;

public class NotEKKeyBinding extends KeyBinding implements INotEKKeybinding {
    private final KeybindingTicker onWasPressed;


    public NotEKKeyBinding(String translationKey, int code, String category, KeybindingTicker onTick) {
        super(NotEKKeybindings.KEY_BINDING_PREFIX + translationKey, code, category);
        this.onWasPressed = onTick;
    }

    public NotEKKeyBinding(String translationKey, InputUtil.Type type, int code, String category, KeybindingTicker onTick) {
        super(NotEKKeybindings.KEY_BINDING_PREFIX + translationKey, type, code, category);
        this.onWasPressed = onTick;
    }

    public NotEKKeyBinding(String translationKey, int code, String category, KeybindingTicker onTick, boolean useCustomTranslation) {
        this(useCustomTranslation ? translationKey : NotEKKeybindings.KEY_BINDING_PREFIX + translationKey, code, category, onTick);
    }

    public NotEKKeyBinding(String translationKey, InputUtil.Type type, int code, String category, KeybindingTicker onTick, boolean useCustomTranslation) {
        this(useCustomTranslation ? translationKey : NotEKKeybindings.KEY_BINDING_PREFIX + translationKey, type, code, category, onTick);
    }

    public NotEKKeyBinding(String translationKey, String category, KeybindingTicker onTick) {
        this(translationKey, InputUtil.UNKNOWN_KEY.getCode(), category, onTick);
    }

    public NotEKKeyBinding(String translationKey, InputUtil.Type type, String category, KeybindingTicker onTick) {
        this(translationKey, type, InputUtil.UNKNOWN_KEY.getCode(), category, onTick);
    }

    @Override
    public KeyBinding getBinding() {
        return this;
    }

    @Override
    public void setAndSaveKeyBinding(InputUtil.Key key) {
        MinecraftClient.getInstance().options.setKeyCode(this, key);
    }

    public void tick(MinecraftClient client) {
        if (onWasPressed != null) {
            onWasPressed.onTick(client, this);
        }
    }
}
