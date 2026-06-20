package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;
import org.jetbrains.annotations.NotNull;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Objects;

public class F3DebugKeybinding implements INotEKKeybinding, Comparable<F3DebugKeybinding> {
    public final String translationKey;
    public final String category;
    public InputConstants.Key boundKey;
    public final InputConstants.Key defaultKey;

    public F3DebugKeybinding(String translationKey, int code) {
        this.translationKey = NotEKKeyBindings.KEY_BINDING_PREFIX + translationKey;
        this.boundKey = InputConstants.getKey(new KeyEvent(code, 0, 0));
        this.defaultKey = this.boundKey;
        this.category = F3DebugKeys.F3_DEBUG_KEYS_CATEGORY_KEY;
    }

    @Override
    public Component getBoundKeyLocalizedText() {
        return Component.literal("F3 + " + boundKey.getDisplayName().getString());
    }

    @Override
    public boolean isDefault() {
        return this.boundKey.equals(this.defaultKey);
    }

    @Override
    public String getBoundKeyTranslationKey() {
        return this.boundKey.getName();
    }

    @Override
    public void tick(Minecraft client) {
    }

    @Override
    public boolean matchesKey(KeyEvent key) {
        if (key.key() == InputConstants.UNKNOWN.getValue()) {
            return this.boundKey.getType() == InputConstants.Type.SCANCODE && this.boundKey.getValue() == key.scancode();
        }
        return this.boundKey.getType() == InputConstants.Type.KEYSYM && this.boundKey.getValue() == key.key();
    }

    @Override
    public void setAndSaveKeyBinding(InputConstants.Key key) {
        setBoundKey(key);
    }

    @Override
    public void setBoundKey(InputConstants.Key boundKey) {
        this.boundKey = boundKey;
        if (NotEnoughKeybinds.DEBUG_KEYS_CONFIG != null) {
            NotEnoughKeybinds.DEBUG_KEYS_CONFIG.saveBoundKey(getId(), boundKey.getName());
        }
    }

    @Override
    public KeyMapping getBinding() {
        return null;
    }

    @Override
    public boolean isUnbound() {
        return boundKey.equals(InputConstants.UNKNOWN);
    }

    @Override
    public InputConstants.Key getDefaultKey() {
        return defaultKey;
    }

    @Override
    public String getId() {
        return translationKey;
    }

    @Override
    public KeyMapping.Category getCategory() {
        return null;
    }

    @Override
    public int compareTo(@NotNull F3DebugKeybinding keyBinding) {
        if (Objects.equals(this.category, keyBinding.category)) {
            return I18n.get(this.translationKey).compareTo(I18n.get(keyBinding.translationKey));
        }
        return 0; //Integer.compare(KeyBinding.Category.CATEGORIES.indexOf(this.category), KeyBinding.Category.CATEGORIES.indexOf(keyBinding.category));
    }

    /*@Override
    public int compareTo(@NotNull F3DebugKeybinding keyBinding) {
        return this.category.equals(keyBinding.category) ?
                I18n.translate(this.translationKey).compareTo(I18n.translate(keyBinding.translationKey))
                : (KeyBindingAccessor.fabric_getCategoryMap().get(this.category)).compareTo(KeyBindingAccessor.fabric_getCategoryMap().get(keyBinding.category));
    }*/
}
