package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;

public class NotEKKeyBinding extends KeyMapping implements INotEKKeybinding {
    private final KeybindingTicker onWasPressed;


    public NotEKKeyBinding(String translationKey, int code, Category category, KeybindingTicker onTick, boolean useCustomTranslation) {
        super(useCustomTranslation ? NotEKKeyBindings.KEY_BINDING_PREFIX + translationKey : translationKey, code, category);
        this.onWasPressed = onTick;
    }

    public NotEKKeyBinding(String translationKey, InputConstants.Type type, int code, Category category, KeybindingTicker onTick, boolean useCustomTranslation) {
        super(useCustomTranslation ? NotEKKeyBindings.KEY_BINDING_PREFIX + translationKey : translationKey, type, code, category);
        this.onWasPressed = onTick;
    }

    public NotEKKeyBinding(String translationKey, int code, Category category, KeybindingTicker onTick) {
        this(translationKey, code, category, onTick, true);
    }

    public NotEKKeyBinding(String translationKey, InputConstants.Type type, int code, Category category, KeybindingTicker onTick) {
        this(translationKey, type, code, category, onTick, true);
    }

    public NotEKKeyBinding(String translationKey, Category category, KeybindingTicker onTick) {
        this(translationKey, InputConstants.UNKNOWN.getValue(), category, onTick, true);
    }

    public NotEKKeyBinding(String translationKey, Category category, KeybindingTicker onTick, boolean useCustomTranslation) {
        this(translationKey, InputConstants.UNKNOWN.getValue(), category, onTick, useCustomTranslation);
    }


    @Override
    public KeyMapping getBinding() {
        return this;
    }

    @Override
    public void setAndSaveKeyBinding(InputConstants.Key key) {
        this.setKey(key);
    }

    @Override
    public void setBoundKey(InputConstants.Key key) {
        this.setKey(key);
    }

    public void tick(Minecraft client) {
        if (onWasPressed != null) {
            onWasPressed.onTick(client, this);
        }
    }

    public void onWasPressed(Minecraft client) {
        if (onWasPressed != null) {
            onWasPressed.onWasPressed(client, this);
        }
    }

    //Idk why this has to be there, but it doesn't work without it
    @Override
    public void setKey(InputConstants.Key boundKey) {
        super.setKey(boundKey);
    }

    @Override
    public boolean isUnbound() {
        return super.isUnbound();
    }

    @Override
    public InputConstants.Key getDefaultKey() {
        return super.getDefaultKey();
    }

    @Override
    public String getName() {
        return super.getName(); //?
    }

    @Override
    public String getId() {
        return this.getName();
    }

    @Override
    public Category getCategory() {
        return super.getCategory();
    }

    @Override
    public Component getTranslatedKeyMessage() {
        return super.getTranslatedKeyMessage();
    }

    @Override
    public Component getBoundKeyLocalizedText() {
        return this.getTranslatedKeyMessage();
    }

    @Override
    public boolean isDefault() {
        return super.isDefault();
    }

    @Override
    public String saveString() {
        return super.saveString();
    }

    @Override
    public String getBoundKeyTranslationKey() {
        return this.saveString();
    }

    @Override
    public boolean matchesKey(KeyEvent key) {
        return super.matches(key);
    }
}
