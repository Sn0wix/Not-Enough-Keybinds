package net.sn0wix_.notEnoughKeybinds.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.input.KeyEvent;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameModeSwitcherScreen.class)
public abstract class GameModeSwitcherScreenMixin {
    @WrapMethod(method = "keyPressed")
    public boolean notenoughkeybinds$modifyF4(KeyEvent event, Operation<Boolean> original) {
        if (F3DebugKeys.GAMEMODES.boundKey.getValue() == event.key()) {
            event = new KeyEvent(InputConstants.KEY_F4, event.keycode(), event.modifiers());
        } else if (event.key() == InputConstants.KEY_F4) {
            event = new KeyEvent(F3DebugKeys.GAMEMODES.boundKey.getValue(), event.keycode(), event.modifiers());
        }
        return original.call(event);
    }
}