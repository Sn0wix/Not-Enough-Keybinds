package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class BuildingKeys extends NotEKKeyBindings {
    private static int itemUseCooldown = 0;
    public static final String BUILDING_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".building";

    public static final NotEKKeyBinding FAST_BUILDING = registerModKeyBinding(new NotEKKeyBinding("fast_building", BUILDING_CATEGORY, new NotEKKeyBinding.KeybindingTicker() {
        @Override
        public void onWasPressed(MinecraftClient client, NotEKKeyBinding keyBinding) {
        }

        @Override
        public void onTick(MinecraftClient client, NotEKKeyBinding keyBinding) {
            for (Hand hand : Hand.values()) {
                if (isInGame(client) && (keyBinding.isPressed() || keyBinding.wasPressed())) {
                    ItemStack itemStack = client.player.getStackInHand(hand);

                    if (client.crosshairTarget.getType().equals(HitResult.Type.BLOCK)) {
                        BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                        int i = itemStack.getCount();
                        ActionResult actionResult2 = client.interactionManager.interactBlock(client.player, hand, blockHitResult);
                        if (actionResult2.isAccepted()) {
                            if (actionResult2 instanceof ActionResult.Success success && success.swingSource() == ActionResult.SwingSource.CLIENT) {
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
            return TextUtils.getText("fast_building", true);
        }
    });

    public static final NotEKKeyBinding FAST_BLOCK_BREAKING = registerModKeyBinding(new NotEKKeyBinding("fast_block_breaking", BUILDING_CATEGORY, new NotEKKeyBinding.KeybindingTicker() {
        @Override
        public void onWasPressed(MinecraftClient client, NotEKKeyBinding keyBinding) {
        }

        @Override
        public void onTick(MinecraftClient client, NotEKKeyBinding keyBinding) {
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
            return TextUtils.getText("fast_block_breaking", true);
        }
    });

    public static final NotEKKeyBinding ALWAYS_PLACE_ITEM = registerModKeyBinding(new NotEKKeyBinding("always_place_item", BUILDING_CATEGORY, (client, keyBinding) -> {
        if (itemUseCooldown == 0) {
            for (Hand hand : Hand.values()) {
                ItemStack itemStack = client.player.getStackInHand(hand);

                if (!client.crosshairTarget.getType().equals(HitResult.Type.ENTITY)) {
                    BlockHitResult blockHitResult = (BlockHitResult) client.crosshairTarget;
                    int i = itemStack.getCount();
                    ActionResult actionResult2 = client.interactionManager.interactBlock(client.player, hand, blockHitResult);
                    if (actionResult2.isAccepted()) {
                        itemUseCooldown = 4;
                        if (actionResult2 instanceof ActionResult.Success success && success.swingSource() == ActionResult.SwingSource.CLIENT) {
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
            return TextUtils.getText("always_place_item", true);
        }

        @Override
        public void tick(MinecraftClient client) {
            if (itemUseCooldown > 0) {
                itemUseCooldown--;
            }
            super.tick(client);
        }
    });

    @Override
    public KeybindCategory getCategory() {
        return new KeybindCategory(BUILDING_CATEGORY, 5, ALWAYS_PLACE_ITEM, FAST_BUILDING, FAST_BLOCK_BREAKING);
    }
}
