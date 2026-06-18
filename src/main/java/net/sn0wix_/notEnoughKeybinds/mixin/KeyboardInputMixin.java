package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
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
    private Options options;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/KeyboardInput;calculateImpulse(ZZ)F", shift = At.Shift.BEFORE, ordinal = 0))
    public void injectJumping(CallbackInfo ci) {
        if (ElytraController.shouldStimulateJump() || this.options.keyJump.isDown()) {
            ((ClientInput) (Object) this).makeJump();
        }
    }
}