package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.sn0wix_.notEnoughKeybinds.util.ElytraController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin {
    @Shadow
    @Final
    private GameOptions settings;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/input/KeyboardInput;getMovementMultiplier(ZZ)F", shift = At.Shift.BEFORE, ordinal = 0))
    public void injectJumping(CallbackInfo ci) {
        if (ElytraController.shouldStimulateJump() || this.settings.jumpKey.isPressed()) {
            ((Input) (Object) this).jump();
        }
    }
}