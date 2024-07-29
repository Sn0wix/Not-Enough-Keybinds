package net.sn0wix_.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.sn0wix_.keybinds.ModKeybinds;
import org.lwjgl.glfw.GLFW;
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
    @Shadow protected abstract boolean processF3(int key);

    //missing F1 keybind
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowRenderingChart()Z", shift = At.Shift.BEFORE))
    private void injectOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (key == GLFW.GLFW_KEY_F1 && !ModKeybinds.TOGGLE_HIDE_HUD.matchesKey(key, 0)) {
            this.client.options.hudHidden = !this.client.options.hudHidden;
        }
        if (key != GLFW.GLFW_KEY_F1 && ModKeybinds.TOGGLE_HIDE_HUD.matchesKey(key, 0)) {
            this.client.options.hudHidden = !this.client.options.hudHidden;
        }
    }

    //f3 shortcuts
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V", ordinal = 0, shift = At.Shift.BEFORE))
    private void injectProcessF3(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        List<Integer> codes = ModKeybinds.checkF3Shortcuts(key, scancode);
        if (client.world != null && !codes.isEmpty()) {
            codes.forEach(this::processF3);
        }
    }
}
