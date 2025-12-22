package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeybindsScreen.class)
public abstract class ResetAllKeybindsMixin {
    @Shadow private ControlsListWidget controlsList;

    //confirmation dialog
    @Inject(method = "method_60342", at = @At("HEAD"), cancellable = true)
    private void injectUpdate(ButtonWidget button, CallbackInfo ci) {
        MinecraftClient.getInstance().setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(((KeybindsScreen) (Object) this), client -> {
            for (KeyBinding keyBinding : client.options.allKeys) {
                keyBinding.setBoundKey(keyBinding.getDefaultKey());
            }

            for (int i = 0; i < ChatKeys.CHAT_KEYS_MOD_CATEGORY.getKeyBindings().length; i++) {
                INotEKKeybinding keybinding = ChatKeys.CHAT_KEYS_MOD_CATEGORY.getKeyBindings()[i];
                keybinding.setBoundKey(keybinding.getDefaultKey());
            }

            this.controlsList.update();
        }, true), Text.translatable("text.not-enough-keybinds.resetAllKeys")));
        ci.cancel();
    }
}