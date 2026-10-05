package net.sn0wix_.notEnoughKeybinds.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.input.KeyEvent;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSelectionFixerMixin {
    @ModifyVariable(method = "keyPressed", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public KeyEvent modifyF4(KeyEvent input) {
        if (F3DebugKeys.GAMEMODES.boundKey.getValue() == input.key()) {
            return new KeyEvent(InputConstants.KEY_F4, input.keycode(), input.modifiers());
        } else if (input.key() == InputConstants.KEY_F4) {
            return new KeyEvent(F3DebugKeys.GAMEMODES.boundKey.getValue(), input.keycode(), input.modifiers());
        }

        return input;
    }
}
