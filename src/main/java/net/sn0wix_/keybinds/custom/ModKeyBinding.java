package net.sn0wix_.keybinds.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sn0wix_.keybinds.ModKeybindings;

public class ModKeyBinding extends KeyBinding {
    private final OnClientTick onWasPressed;


    public ModKeyBinding(String translationKey, int code, String category, OnClientTick onTick) {
        super(ModKeybindings.KEY_BINDING_PREFIX + translationKey, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, int code, String category, OnClientTick onTick) {
        super(ModKeybindings.KEY_BINDING_PREFIX + translationKey, type, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, int code, String category, OnClientTick onTick, boolean useCustomTranslation) {
        super(useCustomTranslation ? translationKey : ModKeybindings.KEY_BINDING_PREFIX + translationKey, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, int code, String category, OnClientTick onTick, boolean useCustomTranslation) {
        super(useCustomTranslation ? translationKey : ModKeybindings.KEY_BINDING_PREFIX + translationKey, type, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, String category, OnClientTick onTick) {
        this(translationKey, InputUtil.UNKNOWN_KEY.getCode(), category, onTick);
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, String category, OnClientTick onTick) {
        this(translationKey, type, InputUtil.UNKNOWN_KEY.getCode(), category, onTick);
    }



    public void tick(MinecraftClient client) {
        if (onWasPressed != null && wasPressed()) {
            onWasPressed.onTick(client, this);
        }
    }

    @FunctionalInterface
    public interface OnClientTick {
        /**
         * Executes if the keybind is pressed while a world is loaded
         * */
        void onWasPressed(MinecraftClient client, KeyBinding keyBinding);

        /**
         * Executes every tick
         * */
        default void onTick(MinecraftClient client, KeyBinding keyBinding) {
            if (keyBinding.wasPressed() && isInGame(client)) {
                onWasPressed(client, keyBinding);
            }
        }

        default boolean isInGame(MinecraftClient client) {
            return client.player != null;
        }
    }
}
