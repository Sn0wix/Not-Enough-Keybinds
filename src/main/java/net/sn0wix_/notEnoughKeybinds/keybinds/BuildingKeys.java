package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindingCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ModKeyBinding;

public class BuildingKeys extends NotEKKeybindings {
    private static int itemUseCooldown = 0;
    public static final String BUILDING_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".building";

    public static final ModKeyBinding FAST_BUILDING = (ModKeyBinding) registerModKeyBinding(new ModKeyBinding("fast_building", BUILDING_CATEGORY, new ModKeyBinding.KeybindingTicker() {
        @Override
        public void onWasPressed(MinecraftClient client, KeyBinding keyBinding) {
        }

        @Override
        public void onTick(MinecraftClient client, KeyBinding keyBinding) {
            for (Hand hand : Hand.values()) {
                if (isInGame(client) && (keyBinding.isPressed() || keyBinding.wasPressed())) {
                    ItemStack itemStack = client.player.getStackInHand(hand);

                    if (client.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
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
            }
        }
    }) {
        @Override
        public Text getTooltip() {
            return Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".tooltip.fast_building");
        }
    });

    public static final ModKeyBinding FAST_BLOCK_BREAKING = (ModKeyBinding) registerModKeyBinding(new ModKeyBinding("fast_block_breaking", BUILDING_CATEGORY, new ModKeyBinding.KeybindingTicker() {
        @Override
        public void onWasPressed(MinecraftClient client, KeyBinding keyBinding) {
        }

        @Override
        public void onTick(MinecraftClient client, KeyBinding keyBinding) {
            for (Hand hand : Hand.values()) {
                if (isInGame(client) && (keyBinding.isPressed() || keyBinding.wasPressed())) {
                    if (client.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
                        BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                        BlockPos blockPos = blockHitResult.getBlockPos();
                        if (!client.world.getBlockState(blockPos).isAir()) {
                            client.interactionManager.attackBlock(blockPos, blockHitResult.getSide());
                            client.player.swingHand(hand);
                            break;
                        }
                    }
                }
            }
        }
    }) {
        @Override
        public Text getTooltip() {
            return Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".tooltip.fast_block_breaking");
        }
    });

    public static final ModKeyBinding ALWAYS_PLACE_ITEM = (ModKeyBinding) registerModKeyBinding(new ModKeyBinding("always_place_item", BUILDING_CATEGORY, (client, keyBinding) -> {
        if (itemUseCooldown == 0) {
            for (Hand hand : Hand.values()) {
                ItemStack itemStack = client.player.getStackInHand(hand);

                if (!client.crosshairTarget.getType().equals(HitResult.Type.ENTITY)) {
                    BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                    int i = itemStack.getCount();
                    ActionResult actionResult2 = client.interactionManager.interactBlock(client.player, hand, blockHitResult);
                    if (actionResult2.isAccepted()) {
                        itemUseCooldown = 4;
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
        }
    }){
        @Override
        public Text getTooltip() {
            return Text.translatable("text." + NotEnoughKeybinds.MOD_ID + ".tooltip.always_place_item");
        }
    });

    @Override
    public KeybindingCategory getCategory() {
        return new KeybindingCategory(BUILDING_CATEGORY, 1, ALWAYS_PLACE_ITEM, FAST_BUILDING, FAST_BLOCK_BREAKING);
    }
}
