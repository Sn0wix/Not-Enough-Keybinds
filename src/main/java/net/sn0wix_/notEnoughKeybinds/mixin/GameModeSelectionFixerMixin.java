package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.gui.screen.GameModeSwitcherScreen;
import net.minecraft.client.input.KeyInput;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSelectionFixerMixin {
    @ModifyVariable(method = "keyPressed", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public KeyInput modifyF4(KeyInput input) {
        if (F3DebugKeys.GAMEMODES.boundKey.getCode() == input.key()) {
            return new KeyInput(GLFW.GLFW_KEY_F4, input.scancode(), input.modifiers());
        } else if (input.key() == GLFW.GLFW_KEY_F4) {
            return new KeyInput(F3DebugKeys.GAMEMODES.boundKey.getCode(), input.scancode(), input.modifiers());
        }

        return input;
    }
}
