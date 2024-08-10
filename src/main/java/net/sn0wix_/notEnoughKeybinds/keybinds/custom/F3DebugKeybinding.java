package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.fabricmc.fabric.mixin.client.keybinding.KeyBindingAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeybindings;
import org.jetbrains.annotations.NotNull;

public class F3DebugKeybinding implements INotEKKeybinding, Comparable<F3DebugKeybinding> {
    public final String translationKey;
    public final String category;
    public InputUtil.Key boundKey;
    public final InputUtil.Key defaultKey;

    public F3DebugKeybinding(String translationKey, int code) {
        this.translationKey = NotEKKeybindings.KEY_BINDING_PREFIX + translationKey;
        this.boundKey = InputUtil.fromKeyCode(code, 0);
        this.defaultKey = this.boundKey;
        this.category = F3DebugKeys.F3_DEBUG_KEYS_CATEGORY_STRING;
    }

    @Override
    public Text getBoundKeyLocalizedText() {
        return Text.literal("F3 + " + boundKey.getLocalizedText().getString());
    }

    @Override
    public boolean isDefault() {
        return this.boundKey.equals(this.defaultKey);
    }

    @Override
    public String getBoundKeyTranslationKey() {
        return this.boundKey.getTranslationKey();
    }

    @Override
    public void tick(MinecraftClient client) {
    }

    @Override
    public boolean matchesKey(int keyCode, int scanCode) {
        return keyCode == InputUtil.UNKNOWN_KEY.getCode()
                ? this.boundKey.getCategory() == InputUtil.Type.SCANCODE && this.boundKey.getCode() == scanCode
                : this.boundKey.getCategory() == InputUtil.Type.KEYSYM && this.boundKey.getCode() == keyCode;
    }

    @Override
    public void setAndSaveKeyBinding(InputUtil.Key key) {
        setBoundKey(key);
    }

    @Override
    public void setBoundKey(InputUtil.Key boundKey) {
        this.boundKey = boundKey;
        if (NotEnoughKeybinds.DEBUG_KEYS_CONFIG != null) {
            NotEnoughKeybinds.DEBUG_KEYS_CONFIG.saveBoundKey(getTranslationKey(), boundKey.getTranslationKey());
        }
    }

    @Override
    public KeyBinding getBinding() {
        return null;
    }

    @Override
    public boolean isUnbound() {
        return boundKey.equals(InputUtil.UNKNOWN_KEY);
    }

    @Override
    public InputUtil.Key getDefaultKey() {
        return defaultKey;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public String getCategory() {
        return category;
    }


    @Override
    public int compareTo(@NotNull F3DebugKeybinding keyBinding) {
        return this.category.equals(keyBinding.category) ? I18n.translate(this.translationKey).compareTo(I18n.translate(keyBinding.translationKey)) : (KeyBindingAccessor.fabric_getCategoryMap().get(this.category)).compareTo(KeyBindingAccessor.fabric_getCategoryMap().get(keyBinding.category));
    }
}
