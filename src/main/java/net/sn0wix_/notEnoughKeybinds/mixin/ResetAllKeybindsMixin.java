package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsScreen.class)
public abstract class ResetAllKeybindsMixin {
    @Shadow private KeyBindsList keyBindsList;

    //confirmation dialog
    @Inject(method = "lambda$addFooter$0", at = @At("HEAD"), cancellable = true)
    private void injectUpdate(Button button, CallbackInfo ci) {
        Minecraft.getInstance().setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(((KeyBindsScreen) (Object) this), client -> {
            for (KeyMapping keyBinding : client.options.keyMappings) {
                keyBinding.setKey(keyBinding.getDefaultKey());
            }

            for (int i = 0; i < F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings().length; i++) {
                INotEKKeybinding keybinding = F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()[i];
                keybinding.setBoundKey(keybinding.getDefaultKey());
            }

            for (int i = 0; i < ChatKeys.CHAT_KEYS_MOD_CATEGORY.getKeyBindings().length; i++) {
                INotEKKeybinding keybinding = ChatKeys.CHAT_KEYS_MOD_CATEGORY.getKeyBindings()[i];
                keybinding.setBoundKey(keybinding.getDefaultKey());
            }

            this.keyBindsList.resetMappingAndUpdateButtons();
        }, true), Component.translatable("text.not-enough-keybinds.resetAllKeys")));
        ci.cancel();
    }
}
