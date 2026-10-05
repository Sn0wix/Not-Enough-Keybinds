package net.sn0wix_.notEnoughKeybinds.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.input.KeyEvent;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSwitcherScreenMixin {
    @ModifyVariable(method = "keyPressed", at = @At("HEAD"), argsOnly = true, name = "event")
    public KeyEvent notenoughkeybinds$modifyF4(KeyEvent event) {
        if (F3DebugKeys.GAMEMODES.boundKey.getValue() == event.key()) {
            return new KeyEvent(InputConstants.KEY_F4, event.keycode(), event.modifiers());
        }
        if (event.key() == InputConstants.KEY_F4) {
            return new KeyEvent(F3DebugKeys.GAMEMODES.boundKey.getValue(), event.keycode(), event.modifiers());
        }
        return event;
    }
}
