package net.sn0wix_.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.sn0wix_.NotEnoughKeybinds;
import net.sn0wix_.keybinds.ModKeybinds;

public class ClientEndTickEvent implements ClientTickEvents.EndTick {
    //quick ender pearl
    public boolean shouldThrowPearl = false;
    public int slotWithPearl = -1;


    @Override
    public void onEndTick(MinecraftClient client) {
        try {
            if (client.player != null && client.interactionManager != null) {
                for (Hand hand : Hand.values()) {
                    ItemStack itemStack = client.player.getStackInHand(hand);

                    //TODO fix holding not triggering bow and eating
                    if (ModKeybinds.ALWAYS_USE_ITEM.wasPressed()) {
                        if (interactItem(itemStack, hand, client)) {
                            return;
                        }
                    }


                    if (ModKeybinds.ALWAYS_PLACE_ITEM.wasPressed()) {
                        if (!client.crosshairTarget.getType().equals(HitResult.Type.ENTITY)) {
                            BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                            int i = itemStack.getCount();
                            ActionResult actionResult2 = client.interactionManager.interactBlock(client.player, hand, blockHitResult);
                            if (actionResult2.isAccepted()) {
                                if (actionResult2.shouldSwingHand()) {
                                    client.player.swingHand(hand);
                                    if (!itemStack.isEmpty() && (itemStack.getCount() != i || client.interactionManager.hasCreativeInventory())) {
                                        client.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                                    }
                                }
                                return;
                            }
                            if (actionResult2 == ActionResult.FAIL) {
                                return;
                            }
                        }
                    }


                    if (ModKeybinds.ALWAYS_INTERACT_WITH_ENTITY.wasPressed()) {
                        if (client.crosshairTarget.getType().equals(HitResult.Type.ENTITY)) {
                            EntityHitResult entityHitResult = (EntityHitResult) client.crosshairTarget;
                            Entity entity = entityHitResult.getEntity();
                            if (!client.world.getWorldBorder().contains(entity.getBlockPos())) {
                                return;
                            }

                            ActionResult actionResult = client.interactionManager.interactEntityAtLocation(client.player, entity, entityHitResult, hand);
                            if (!actionResult.isAccepted()) {
                                actionResult = client.interactionManager.interactEntity(client.player, entity, hand);
                            }

                            if (actionResult.isAccepted()) {
                                if (actionResult.shouldSwingHand()) {
                                    client.player.swingHand(hand);
                                }
                                return;
                            }
                        }
                    }


                    if (ModKeybinds.THROW_ENDER_PEARL.wasPressed()) {
                        int pearlSlot = client.player.getInventory().getSlotWithStack(Items.ENDER_PEARL.getDefaultStack());

                        if (pearlSlot > -1 && !PlayerInventory.isValidHotbarIndex(pearlSlot)) {
                            client.interactionManager.clickSlot(new InventoryScreen(client.player).getScreenHandler().syncId, pearlSlot, client.player.getInventory().selectedSlot, SlotActionType.SWAP, client.player);
                            interactItem(hand, client);
                            client.interactionManager.clickSlot(new InventoryScreen(client.player).getScreenHandler().syncId, pearlSlot, client.player.getInventory().selectedSlot, SlotActionType.SWAP, client.player);

                        } else if (pearlSlot > -1 && PlayerInventory.isValidHotbarIndex(pearlSlot)) {
                            int slotBefore = client.player.getInventory().selectedSlot;
                            client.player.getInventory().selectedSlot = pearlSlot;
                            interactItem(hand, client);
                            client.player.getInventory().selectedSlot = slotBefore;
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            NotEnoughKeybinds.LOGGER.error("Something went wrong with executing keybinding behavior.");
            e.printStackTrace();
        }
    }

    public static boolean interactItem(ItemStack itemStack, Hand hand, MinecraftClient client) throws NullPointerException {
        if (!itemStack.isEmpty()) {
            return interactItem(hand, client);
        }
        return false;
    }

    public static boolean interactItem(Hand hand, MinecraftClient client) throws NullPointerException {
        ActionResult actionResult3 = client.interactionManager.interactItem(client.player, hand);
        if (actionResult3.isAccepted()) {
            if (actionResult3.shouldSwingHand()) {
                client.player.swingHand(hand);
            }
            client.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
            return true;
        }
        return false;
    }
}
