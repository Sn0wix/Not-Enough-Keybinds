package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;

public class NotEKKeyBinding extends KeyBinding implements INotEKKeybinding {
    private final KeybindingTicker onWasPressed;


    public NotEKKeyBinding(String translationKey, int code, Category category, KeybindingTicker onTick, boolean useCustomTranslation) {
        super(useCustomTranslation ? NotEKKeyBindings.KEY_BINDING_PREFIX + translationKey : translationKey, code, category);
        this.onWasPressed = onTick;
    }

    public NotEKKeyBinding(String translationKey, InputUtil.Type type, int code, Category category, KeybindingTicker onTick, boolean useCustomTranslation) {
        super(useCustomTranslation ? NotEKKeyBindings.KEY_BINDING_PREFIX + translationKey : translationKey, type, code, category);
        this.onWasPressed = onTick;
    }

    public NotEKKeyBinding(String translationKey, int code, Category category, KeybindingTicker onTick) {
        this(translationKey, code, category, onTick, true);
    }

    public NotEKKeyBinding(String translationKey, InputUtil.Type type, int code, Category category, KeybindingTicker onTick) {
        this(translationKey, type, code, category, onTick, true);
    }

    public NotEKKeyBinding(String translationKey, Category category, KeybindingTicker onTick) {
        this(translationKey, InputUtil.UNKNOWN_KEY.getCode(), category, onTick, true);
    }

    public NotEKKeyBinding(String translationKey, Category category, KeybindingTicker onTick, boolean useCustomTranslation) {
        this(translationKey, InputUtil.UNKNOWN_KEY.getCode(), category, onTick, useCustomTranslation);
    }


    @Override
    public KeyBinding getBinding() {
        return this;
    }

    @Override
    public void setAndSaveKeyBinding(InputUtil.Key key) {
        this.setBoundKey(key);
    }

    public void tick(MinecraftClient client) {
        if (onWasPressed != null) {
            onWasPressed.onTick(client, this);
        }
    }

    public void onWasPressed(MinecraftClient client) {
        if (onWasPressed != null) {
            onWasPressed.onWasPressed(client, this);
        }
    }

    //Idk why this has to be there, but it doesn't work without it
    @Override
    public void setBoundKey(InputUtil.Key boundKey) {
        super.setBoundKey(boundKey);
    }

    @Override
    public boolean isUnbound() {
        return super.isUnbound();
    }

    @Override
    public InputUtil.Key getDefaultKey() {
        return super.getDefaultKey();
    }

    @Override
    public String getId() {
        return super.getId(); //?
    }

    @Override
    public Category getCategory() {
        return super.getCategory();
    }

    @Override
    public Text getBoundKeyLocalizedText() {
        return super.getBoundKeyLocalizedText();
    }

    @Override
    public boolean isDefault() {
        return super.isDefault();
    }

    @Override
    public String getBoundKeyTranslationKey() {
        return super.getBoundKeyTranslationKey();
    }

    @Override
    public boolean matchesKey(KeyInput key) {
        return super.matchesKey(key);
    }
}
