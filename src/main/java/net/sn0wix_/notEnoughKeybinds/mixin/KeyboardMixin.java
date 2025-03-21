package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen.NotEKSettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
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
        if (key == GLFW.GLFW_KEY_F1 && !NotEKKeyBindings.TOGGLE_HIDE_HUD.matchesKey(key, GLFW.glfwGetKeyScancode(key))) {
            this.client.options.hudHidden = !this.client.options.hudHidden;
        }
        if (key != GLFW.GLFW_KEY_F1 && NotEKKeyBindings.TOGGLE_HIDE_HUD.matchesKey(key, GLFW.glfwGetKeyScancode(key))) {
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
        Iterator<INotEKKeybinding> iterator = Arrays.stream(F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()).iterator();
        ArrayList<Integer> pressedF3Keys = new ArrayList<>(1);

        while (iterator.hasNext()) {
            INotEKKeybinding keyBinding = iterator.next();

            if (keyBinding.matchesKey(key, GLFW.glfwGetKeyScancode(key))) {
                pressedF3Keys.add(keyBinding.getDefaultKey().getCode());
            }
        }

        boolean bl = false;

        while (!pressedF3Keys.isEmpty()) {
            bl = true;
            key = pressedF3Keys.getFirst();
            pressedF3Keys.removeFirst();

            if (!pressedF3Keys.isEmpty()) {
                this.processF3(key);
            }

            pressedF3Keys.trimToSize();
        }

        return bl ? key : 0;
    }


    @ModifyArg(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/InputUtil;isKeyPressed(JI)Z"), index = 1)
    public int fixF3C(int code) {
        if (code == GLFW.GLFW_KEY_C) {
            code = F3DebugKeys.COPY_LOCATION.boundKey.getCode();
        }

        return code;
    }

    @ModifyArg(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V"))
    public Text fixHelpMessage(Text message) {
        return Utils.correctF3DebugMessage(message);
    }


    @ModifyArg(method = "pollDebugCrash", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;debugLog(Ljava/lang/String;[Ljava/lang/Object;)V"), index = 1)
    public Object[] fixF3CMessage(Object[] args) {
        return Utils.addArgToEnd(args, F3DebugKeys.COPY_LOCATION.boundKey.getLocalizedText());
    }
}
