package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.sn0wix_.notEnoughKeybinds.util.ElytraController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow public Input input;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void injectTickMovement(CallbackInfo ci) {
        this.input.jumping = ElytraController.shouldSimulateJump();
    }
}
