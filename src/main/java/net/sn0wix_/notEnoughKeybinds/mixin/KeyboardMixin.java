package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
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
    protected abstract boolean processF3(KeyInput keyInput);


    //missing F1 keybind
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;shouldShowRenderingChart()Z", shift = At.Shift.BEFORE))
    private void injectOnKey(long window, int action, KeyInput input, CallbackInfo ci) {
        if (input.key() == GLFW.GLFW_KEY_F1 && !NotEKKeyBindings.TOGGLE_HIDE_HUD.matchesKey(new KeyInput(input.key(), GLFW.glfwGetKeyScancode(input.key()),0 ))) {
            this.client.options.hudHidden = !this.client.options.hudHidden;
        }
        if (input.key() != GLFW.GLFW_KEY_F1 && NotEKKeyBindings.TOGGLE_HIDE_HUD.matchesKey(new KeyInput(input.key(), GLFW.glfwGetKeyScancode(input.key()), 0))) {
            this.client.options.hudHidden = !this.client.options.hudHidden;
        }
    }

    //f3 shortcuts
    @Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V", ordinal = 1, shift = At.Shift.BEFORE))
    private void injectShortcuts(long window, int action, KeyInput input, CallbackInfo ci) {
        List<Integer> codes = Utils.checkF3Shortcuts(input);

        if (client.player != null && !codes.isEmpty() && !(client.currentScreen instanceof NotEKSettingsScreen)) {
            codes.forEach(scanCode -> this.processF3(new KeyInput(scanCode, input.scancode(), 0))); //Will only codes work?
        }
    }

    //f3 debug keys
    @ModifyArg(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;processF3(Lnet/minecraft/client/input/KeyInput;)Z"))
    private KeyInput injectProcessF3(KeyInput input) {
        int key;

        Iterator<INotEKKeybinding> iterator = Arrays.stream(F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()).iterator();
        ArrayList<Integer> pressedF3Keys = new ArrayList<>(1);

        while (iterator.hasNext()) {
            INotEKKeybinding keyBinding = iterator.next();

            if (keyBinding.matchesKey(input)) {
                pressedF3Keys.add(keyBinding.getDefaultKey().getCode());
            }
        }

        KeyInput finalInput = new KeyInput(0, 0, 0);

        while (!pressedF3Keys.isEmpty()) {
            key = pressedF3Keys.getFirst();
            pressedF3Keys.removeFirst();

            finalInput = new KeyInput(key, input.scancode(), 0);

            if (!pressedF3Keys.isEmpty()) {
                this.processF3(finalInput);
            }

            pressedF3Keys.trimToSize();
        }

        return finalInput;
    }


    @ModifyArg(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/InputUtil;isKeyPressed(Lnet/minecraft/client/util/Window;I)Z"), index = 1)
    public int fixF3C(int code) {
        if (code == GLFW.GLFW_KEY_C) {
            code = F3DebugKeys.COPY_LOCATION.boundKey.getCode();
        }

        return code;
    }

    @ModifyArg(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;sendMessage(Lnet/minecraft/text/Text;)V"))
    public Text fixHelpMessage(Text message) {
        return Utils.correctF3DebugMessage(message);
    }


    @ModifyArg(method = "debugLog(Lnet/minecraft/text/Text;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;getDebugMessage(Lnet/minecraft/util/Formatting;Lnet/minecraft/text/Text;)Lnet/minecraft/text/Text;"), index = 1)
    public Text fixF3CMessage(Text message) {
        return message.getContent().toString().contains("debug.crash.message") ? Text.translatable("debug.crash.message", F3DebugKeys.COPY_LOCATION.boundKey.getLocalizedText()) : message;
    }
}
