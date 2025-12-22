package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen.NotEKSettingsScreen;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract boolean processF3(KeyInput keyInput);

    //f3 shortcuts
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V", ordinal = 1, shift = At.Shift.BEFORE))
    private void injectShortcuts(long window, int action, KeyInput input, CallbackInfo ci) {
        List<Integer> codes = Utils.checkF3Shortcuts(input);

        if (client.player != null && !codes.isEmpty() && !(client.currentScreen instanceof NotEKSettingsScreen)) {
            codes.forEach(scanCode -> this.processF3(new KeyInput(scanCode, input.scancode(), 0))); //Will only codes work?
        }
    }
}
