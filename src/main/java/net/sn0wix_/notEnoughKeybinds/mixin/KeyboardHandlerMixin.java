package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen.NotEKSettingsScreen;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow protected abstract boolean handleDebugKeys(KeyEvent keyInput);

    //f3 shortcuts
    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V", ordinal = 1, shift = At.Shift.BEFORE))
    private void notenoughkeybinds$injectShortcuts(long handle, int action, KeyEvent event, CallbackInfo ci) {
        List<Integer> codes = Utils.checkF3Shortcuts(event);

        if (minecraft.player != null && !codes.isEmpty() && !(minecraft.gui.screen() instanceof NotEKSettingsScreen)) {
            codes.forEach(scanCode -> {
                this.handleDebugKeys(new KeyEvent(scanCode, event.keycode(), 0));
            }); //Will only codes work?
        }
    }
}