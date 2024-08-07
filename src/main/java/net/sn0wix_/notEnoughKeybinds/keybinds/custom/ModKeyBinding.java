package net.sn0wix_.notEnoughKeybinds.keybinds.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.keybinds.ModKeybindings;

public class ModKeyBinding extends KeyBinding {
    private final KeybindingTicker onWasPressed;


    public ModKeyBinding(String translationKey, int code, String category, KeybindingTicker onTick) {
        super(ModKeybindings.KEY_BINDING_PREFIX + translationKey, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, int code, String category, KeybindingTicker onTick) {
        super(ModKeybindings.KEY_BINDING_PREFIX + translationKey, type, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, int code, String category, KeybindingTicker onTick, boolean useCustomTranslation) {
        this(useCustomTranslation ? translationKey : ModKeybindings.KEY_BINDING_PREFIX + translationKey, code, category, onTick);
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, int code, String category, KeybindingTicker onTick, boolean useCustomTranslation) {
        this(useCustomTranslation ? translationKey : ModKeybindings.KEY_BINDING_PREFIX + translationKey, type, code, category, onTick);
    }

    public ModKeyBinding(String translationKey, String category, KeybindingTicker onTick) {
        this(translationKey, InputUtil.UNKNOWN_KEY.getCode(), category, onTick);
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, String category, KeybindingTicker onTick) {
        this(translationKey, type, InputUtil.UNKNOWN_KEY.getCode(), category, onTick);
    }

    public Text getTooltip() {
        return Text.empty();
    }

    public Screen getSettingsScreen() {
        return null;
    }


    public void tick(MinecraftClient client) {
        if (onWasPressed != null) {
            onWasPressed.onTick(client, this);
        }
    }

    @FunctionalInterface
    public interface KeybindingTicker {
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
