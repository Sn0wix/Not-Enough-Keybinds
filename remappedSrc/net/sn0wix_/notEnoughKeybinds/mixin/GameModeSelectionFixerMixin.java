package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.gui.screen.GameModeSwitcherScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSelectionFixerMixin {
    @ModifyVariable(method = "keyPressed", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public int modifyF4(int value) {
        if (F3DebugKeys.GAMEMODES.boundKey.getCode() == value) {
            return GLFW.GLFW_KEY_F4;
        } else if (value == GLFW.GLFW_KEY_F4) {
            return F3DebugKeys.GAMEMODES.boundKey.getCode();
        }

        return value;
    }
}
