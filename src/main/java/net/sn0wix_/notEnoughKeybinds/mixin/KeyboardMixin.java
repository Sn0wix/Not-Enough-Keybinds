package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
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

@Mixin(KeyboardHandler.class)
public abstract class KeyboardMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract boolean handleDebugKeys(KeyEvent keyInput);


    //missing F1 keybind
    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/DebugScreenOverlay;showProfilerChart()Z", shift = At.Shift.BEFORE))
    private void injectOnKey(long window, int action, KeyEvent input, CallbackInfo ci) {
        if (input.key() == GLFW.GLFW_KEY_F1 && !NotEKKeyBindings.TOGGLE_HIDE_HUD.matches(new KeyEvent(input.key(), GLFW.glfwGetKeyScancode(input.key()),0 ))) {
            this.minecraft.options.hideGui = !this.minecraft.options.hideGui;
        }
        if (input.key() != GLFW.GLFW_KEY_F1 && NotEKKeyBindings.TOGGLE_HIDE_HUD.matches(new KeyEvent(input.key(), GLFW.glfwGetKeyScancode(input.key()), 0))) {
            this.minecraft.options.hideGui = !this.minecraft.options.hideGui;
        }
    }

    //f3 shortcuts
    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V", ordinal = 1, shift = At.Shift.BEFORE))
    private void injectShortcuts(long window, int action, KeyEvent input, CallbackInfo ci) {
        List<Integer> codes = Utils.checkF3Shortcuts(input);

        if (minecraft.player != null && !codes.isEmpty() && !(minecraft.screen instanceof NotEKSettingsScreen)) {
            codes.forEach(scanCode -> this.handleDebugKeys(new KeyEvent(scanCode, input.scancode(), 0))); //Will only codes work?
        }
    }

    //f3 debug keys
    @ModifyArg(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyboardHandler;handleDebugKeys(Lnet/minecraft/client/input/KeyEvent;)Z"))
    private KeyEvent injectProcessF3(KeyEvent input) {
        int key;

        Iterator<INotEKKeybinding> iterator = Arrays.stream(F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()).iterator();
        ArrayList<Integer> pressedF3Keys = new ArrayList<>(1);

        while (iterator.hasNext()) {
            INotEKKeybinding keyBinding = iterator.next();

            if (keyBinding.matchesKey(input)) {
                pressedF3Keys.add(keyBinding.getDefaultKey().getValue());
            }
        }

        KeyEvent finalInput = new KeyEvent(0, 0, 0);

        while (!pressedF3Keys.isEmpty()) {
            key = pressedF3Keys.getFirst();
            pressedF3Keys.removeFirst();

            finalInput = new KeyEvent(key, input.scancode(), 0);

            if (!pressedF3Keys.isEmpty()) {
                this.handleDebugKeys(finalInput);
            }

            pressedF3Keys.trimToSize();
        }

        return finalInput;
    }


    @ModifyArg(method = "keyPress", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/InputConstants;isKeyDown(Lcom/mojang/blaze3d/platform/Window;I)Z"), index = 1)
    public int fixF3C(int code) {
        if (code == GLFW.GLFW_KEY_C) {
            code = F3DebugKeys.COPY_LOCATION.boundKey.getValue();
        }

        return code;
    }

    @ModifyArg(method = "handleDebugKeys", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyboardHandler;showDebugChat(Lnet/minecraft/network/chat/Component;)V"), require = 0)
    public Component fixHelpMessage(Component message) {
        return Utils.correctF3DebugMessage(message);
    }


    @ModifyArg(method = "debugFeedbackComponent(Lnet/minecraft/network/chat/Component;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyboardHandler;decorateDebugComponent(Lnet/minecraft/ChatFormatting;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/Component;"), index = 1)
    public Component fixF3CMessage(Component message) {
        return message.getContents().toString().contains("debug.crash.message") ? Component.translatable("debug.crash.message", F3DebugKeys.COPY_LOCATION.boundKey.getDisplayName()) : message;
    }
}
