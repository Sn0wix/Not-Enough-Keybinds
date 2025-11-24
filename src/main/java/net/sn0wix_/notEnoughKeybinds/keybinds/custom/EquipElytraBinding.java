package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.config.EquipElytraConfig;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings.EquipElytraSettings;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class EquipElytraBinding extends NotEKKeyBinding {
    public EquipElytraBinding(String translationKey, Category category, KeybindingTicker onTick) {
        super(translationKey, category, onTick);
    }


    @Override
    public boolean isDefault() {
        if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect) {
            return false;
        }

        return super.isDefault();
    }

    @Override
    public String getBoundKeyTranslation() {
        if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect) {
            return "auto-detect";
        }

        return super.getBoundKeyTranslationKey();
    }

    @Override
    public Screen getSettingsScreen(Screen parent) {
        return new EquipElytraSettings(parent);
    }

    @Override
    public Text getTooltip() {
        return TextUtils.getText("switch_elytra_chestplate", true);
    }

    @Override
    public void setAndSaveKeyBinding(InputUtil.Key key) {
        if (key.equals(getDefaultKey()) && NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect) {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect = false;
            EquipElytraConfig.saveConfig();
            return;
        }

        if (MinecraftClient.getInstance().options.jumpKey.matchesKey(new KeyInput(key.getCode(), 0, 0))) { //        if (MinecraftClient.getInstance().options.jumpKey.matchesKey(key.getCode(), 0)) {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect = true;
            super.setAndSaveKeyBinding(InputUtil.UNKNOWN_KEY);
        } else {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect = false;
            super.setAndSaveKeyBinding(key);
        }

        EquipElytraConfig.saveConfig();
    }
    @Override
    public Text getBoundKeyLocalizedText() {
        return NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect ? TextUtils.getText("elytra_auto_detect") : super.getBoundKeyLocalizedText();
    }
}
