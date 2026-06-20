package net.sn0wix_.notEnoughKeybinds.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.InventoryKeys;
import net.sn0wix_.notEnoughKeybinds.util.ElytraController;
import net.sn0wix_.notEnoughKeybinds.util.InventoryUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    //Auto elytra detection
    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;checkGliding()Z", shift = At.Shift.AFTER))
    private void injectTickMovement(CallbackInfo ci) {
        if (NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.autoDetect
                && checkFallFlying(((ClientPlayerEntity) (Object) this))
                && !((ClientPlayerEntity) (Object) this).getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
                && InventoryUtils.getSlotWithItem(Items.ELYTRA, ((ClientPlayerEntity) (Object) this).getInventory()) > -1) {

            String swapFirstBefore = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst;
            boolean swapSecondBefore = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond;
            boolean enterFlightBefore = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode;

            //tweaking the parameters needed
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst = "elytra";
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond = false;
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode = true;

            InventoryKeys.EQUIP_ELYTRA.onWasPressed(MinecraftClient.getInstance());

            //setting back the old parameters
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst = swapFirstBefore;
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond = swapSecondBefore;
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode = enterFlightBefore;

            ElytraController.nextTick(() -> {
                ((ClientPlayerEntity) (Object) this).startGliding();
                ((ClientPlayerEntity) (Object) this).networkHandler.sendPacket(new ClientCommandC2SPacket(((ClientPlayerEntity) (Object) this), ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            });
        }
    }


    @Unique
    public boolean checkFallFlying(ClientPlayerEntity clientPlayerEntity) {
        //return clientPlayerEntity.checkGliding();
        return !clientPlayerEntity.isTouchingWater() && !clientPlayerEntity.isGliding() && !clientPlayerEntity.getAbilities().flying && !clientPlayerEntity.isOnGround() && !clientPlayerEntity.hasVehicle() && !clientPlayerEntity.hasStatusEffect(StatusEffects.LEVITATION);
    }
}
