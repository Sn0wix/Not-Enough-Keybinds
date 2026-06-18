package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.input.KeyEvent;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSelectionFixerMixin {
    @ModifyVariable(method = "keyPressed", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public KeyEvent modifyF4(KeyEvent input) {
        if (F3DebugKeys.GAMEMODES.boundKey.getValue() == input.key()) {
            return new KeyEvent(GLFW.GLFW_KEY_F4, input.scancode(), input.modifiers());
        } else if (input.key() == GLFW.GLFW_KEY_F4) {
            return new KeyEvent(F3DebugKeys.GAMEMODES.boundKey.getValue(), input.scancode(), input.modifiers());
        }

        return input;
    }
}
