package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public interface NotEKKeybinding {
    void setBoundKey(InputUtil.Key key);

    KeyBinding getBinding();

    boolean isUnbound();

    InputUtil.Key getDefaultKey();

    String getTranslationKey();

    String getCategory();

    Text getBoundKeyLocalizedText();

    boolean isDefault();

    default Text getTooltip() {
        return Text.empty();
    }

    default Screen getSettingsScreen() {
        return null;
    }

    String getBoundKeyTranslationKey();

    void tick(MinecraftClient client);

    boolean matchesKey(int key, int i);

    void setAndSaveKeyBinding(InputUtil.Key key);


    @FunctionalInterface
    interface KeybindingTicker {
        /**
         * Executes if the keybind is pressed while a world is loaded
         */
        void onWasPressed(MinecraftClient client, KeyBinding keyBinding);

        /**
         * Executes every tick
         */
        default void onTick(MinecraftClient client, KeyBinding keyBinding) {
            while (keyBinding.wasPressed() && isInGame(client)) {
                onWasPressed(client, keyBinding);
            }
        }

        default boolean isInGame(MinecraftClient client) {
            return client.player != null;
        }
    }
}
