package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.sn0wix_.notEnoughKeybinds.gui.keybindsScreen.NotEKSettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeybindings;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    protected abstract boolean processF3(int key);


    //missing F1 keybind
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowRenderingChart()Z", shift = At.Shift.BEFORE))
    private void injectOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key == GLFW.GLFW_KEY_F1 && !NotEKKeybindings.TOGGLE_HIDE_HUD.matchesKey(key, 0)) {
            this.client.options.hudHidden = !this.client.options.hudHidden;
        }
        if (key != GLFW.GLFW_KEY_F1 && NotEKKeybindings.TOGGLE_HIDE_HUD.matchesKey(key, 0)) {
            this.client.options.hudHidden = !this.client.options.hudHidden;
        }
    }

    //f3 shortcuts
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void injectShortcuts(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        List<Integer> codes = Utils.checkF3Shortcuts(key, scancode);
        if (client.player != null && !codes.isEmpty() && !(client.currentScreen instanceof NotEKSettingsScreen)) {
            codes.forEach(this::processF3);
        }
    }

    //f3 debug keys
    @ModifyArg(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;processF3(I)Z"))
    private int injectProcessF3(int key) {
        Iterator<NotEKKeybinding> iterator = Arrays.stream(F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()).iterator();
        ArrayList<Integer> pressedF3Keys = new ArrayList<>(1);

        while (iterator.hasNext()) {
            NotEKKeybinding keyBinding = iterator.next();

            if (keyBinding.matchesKey(key, 0)) {
                pressedF3Keys.add(keyBinding.getDefaultKey().getCode());
            }
        }

        boolean bl = false;

        while (!pressedF3Keys.isEmpty()) {
            bl = true;
            key = pressedF3Keys.get(0);
            pressedF3Keys.remove(0);

            if (!pressedF3Keys.isEmpty()) {
                this.processF3(key);
            }

            pressedF3Keys.trimToSize();
        }

        return bl ? key : 0;
    }
}
