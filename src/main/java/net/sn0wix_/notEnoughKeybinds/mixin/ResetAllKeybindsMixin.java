package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.screen.ParentScreenBlConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(KeybindsScreen.class)
public abstract class ResetAllKeybindsMixin {
    @Shadow private ControlsListWidget controlsList;

    //confirmation dialog
    @Inject(method = "method_38532", at = @At("HEAD"), cancellable = true)
    private void injectUpdate(ButtonWidget button, CallbackInfo ci) {
        MinecraftClient.getInstance().setScreen(new ConfirmScreen(new ParentScreenBlConsumer(((KeybindsScreen) (Object) this), client -> {
            for (KeyBinding keyBinding : client.options.allKeys) {
                keyBinding.setBoundKey(keyBinding.getDefaultKey());
            }

            this.controlsList.update();
        }) , Text.empty(), Text.translatable("text.not-enough-keybinds.resetAllKeys"),
                Text.translatable("text.not-enough-keybinds.confirm." + new Random().nextInt(4)),
                Text.translatable("text.not-enough-keybinds.cancel." + new Random().nextInt(4))));
        ci.cancel();
    }
}