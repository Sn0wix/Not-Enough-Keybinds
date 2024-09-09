package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.sn0wix_.notEnoughKeybinds.util.ElytraController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    public void injectJumping(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
        ((Input) (Object) this).jumping = ElytraController.shouldSimulateJump() || ((Input) (Object) this).jumping;
    }
}
