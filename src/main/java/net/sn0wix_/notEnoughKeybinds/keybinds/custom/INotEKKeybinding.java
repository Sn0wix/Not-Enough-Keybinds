package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public interface INotEKKeybinding {
    void setBoundKey(InputConstants.Key key);

    KeyMapping getBinding();

    boolean isUnbound();

    InputConstants.Key getDefaultKey();

    String getId();

    KeyMapping.Category getCategory();

    Component getBoundKeyLocalizedText();

    boolean isDefault();

    default Component getTooltip() {
        return Component.empty();
    }

    default Screen getSettingsScreen(Screen parent) {
        return null;
    }

    String getBoundKeyTranslationKey();

    void tick(Minecraft client);

    boolean matchesKey(KeyEvent key);

    void setAndSaveKeyBinding(InputConstants.Key key);

    default Component getSettingsDisplayName() {
        return Component.translatable(getId());
    }

    default String getBoundKeyTranslation() {
        return this.getBoundKeyTranslationKey();
    }


    @FunctionalInterface
    interface KeybindingTicker {
        /**
         * Executes if the keybind is pressed while a world is loaded
         */
        void onWasPressed(Minecraft client, NotEKKeyBinding keyBinding);

        /**
         * Executes every tick
         */
        default void onTick(Minecraft client, NotEKKeyBinding keyBinding) {
            while (keyBinding.consumeClick() && isInGame(client)) {
                onWasPressed(client, keyBinding);
            }
        }

        default boolean isInGame(Minecraft client) {
            return client.player != null;
        }
    }
}
