package net.sn0wix_.keybinds.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sn0wix_.keybinds.ModKeybinds;

public class ModKeyBinding extends KeyBinding {
    private final OnClientTick onWasPressed;


    public ModKeyBinding(String translationKey, int code, String category, OnClientTick onTick) {
        super(ModKeybinds.KEY_BINDING_PREFIX + translationKey, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, int code, String category, OnClientTick onTick) {
        super(ModKeybinds.KEY_BINDING_PREFIX + translationKey, type, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, int code, String category, OnClientTick onTick, boolean useCustomTranslation) {
        super(useCustomTranslation ? translationKey : ModKeybinds.KEY_BINDING_PREFIX + translationKey, code, category);
        this.onWasPressed = onTick;
    }

    public ModKeyBinding(String translationKey, InputUtil.Type type, int code, String category, OnClientTick onTick, boolean useCustomTranslation) {
        super(useCustomTranslation ? translationKey : ModKeybinds.KEY_BINDING_PREFIX + translationKey, type, code, category);
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
            if (client.world != null) {
                onWasPressed.onWasPressedInGame(client, this);
            } else {
                onWasPressed.onWasPressed(client, this);
            }
        } else if (onWasPressed != null){
            if (client.world != null) {
                onWasPressed.onGameTick(client);
            } else {
                onWasPressed.onTick(client);
            }
        }
    }

    @FunctionalInterface
    public interface OnClientTick {
        /**
         * Executes if the keybind is pressed while a world is loaded
         * */
        void onWasPressedInGame(MinecraftClient client, KeyBinding keyBinding);

        /**
         * Executes every tick while a world is loaded
         * */
        default void onGameTick(MinecraftClient client) {}

        /**
         * Executes every tick
         * */
        default void onTick(MinecraftClient client) {}

        /**
         * Executes if the keybind is pressed, world doesn't have to be loaded
         * */
        default void onWasPressed(MinecraftClient client, KeyBinding keyBinding) {}
    }
}
