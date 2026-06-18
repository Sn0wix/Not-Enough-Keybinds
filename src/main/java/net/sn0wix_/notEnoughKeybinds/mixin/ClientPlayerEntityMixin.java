package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.InventoryKeys;
import net.sn0wix_.notEnoughKeybinds.util.ElytraController;
import net.sn0wix_.notEnoughKeybinds.util.InventoryUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin {
    //Auto elytra detection
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;tryToStartFallFlying()Z", shift = At.Shift.AFTER))
    private void injectTickMovement(CallbackInfo ci) {
        if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect
                && checkFallFlying(((LocalPlayer) (Object) this))
                && !((LocalPlayer) (Object) this).getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
                && InventoryUtils.getSlotWithItem(Items.ELYTRA, ((LocalPlayer) (Object) this).getInventory()) > -1) {

            String swapFirstBefore = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst;
            boolean swapSecondBefore = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond;
            boolean enterFlightBefore = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode;

            //tweaking the parameters needed
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst = "elytra";
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond = false;
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode = true;

            InventoryKeys.EQUIP_ELYTRA.onWasPressed(Minecraft.getInstance());

            //setting back the old parameters
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst = swapFirstBefore;
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond = swapSecondBefore;
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode = enterFlightBefore;

            ElytraController.nextTick(() -> {
                ((LocalPlayer) (Object) this).startFallFlying();
                ((LocalPlayer) (Object) this).connection.send(new ServerboundPlayerCommandPacket(((LocalPlayer) (Object) this), ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            });
        }
    }


    @Unique
    public boolean checkFallFlying(LocalPlayer clientPlayerEntity) {
        //return clientPlayerEntity.checkGliding();
        return !clientPlayerEntity.isInWater() && !clientPlayerEntity.isFallFlying() && !clientPlayerEntity.getAbilities().flying && !clientPlayerEntity.onGround() && !clientPlayerEntity.isPassenger() && !clientPlayerEntity.hasEffect(MobEffects.LEVITATION);
    }
}
