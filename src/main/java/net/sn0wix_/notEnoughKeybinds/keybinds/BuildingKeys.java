package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class BuildingKeys extends NotEKKeyBindings {
    private static int itemUseCooldown = 0;
    public static final String BUILDING_CATEGORY_KEY = "key.category." + NotEnoughKeybinds.MOD_ID + ".building";
    public static final KeyMapping.Category BUILDING_CATEGORY = KeyMapping.Category.register(NotEnoughKeybinds.getIdentifier(BUILDING_CATEGORY_KEY));



    public static final NotEKKeyBinding FAST_BUILDING = registerModKeyBinding(new NotEKKeyBinding("fast_building", BUILDING_CATEGORY, new NotEKKeyBinding.KeybindingTicker() {
        @Override
        public void onWasPressed(Minecraft client, NotEKKeyBinding keyBinding) {
        }

        @Override
        public void onTick(Minecraft client, NotEKKeyBinding keyBinding) {
            for (InteractionHand hand : InteractionHand.values()) {
                if (isInGame(client) && (keyBinding.isDown() || keyBinding.consumeClick())) {
                    ItemStack itemStack = client.player.getItemInHand(hand);

                    if (client.hitResult.getType().equals(HitResult.Type.BLOCK)) {
                        BlockHitResult blockHitResult = (BlockHitResult) client.hitResult;
                        int i = itemStack.getCount();
                        InteractionResult actionResult2 = client.gameMode.useItemOn(client.player, hand, blockHitResult);
                        if (actionResult2.consumesAction()) {
                            if (actionResult2 instanceof InteractionResult.Success success && success.swingSource() == InteractionResult.SwingSource.CLIENT) {
                                client.player.swing(hand);
                                if (!itemStack.isEmpty() && (itemStack.getCount() != i || client.gameMode.getPlayerMode().isCreative())) {
                                    client.gameRenderer.itemInHandRenderer.itemUsed(hand);
                                }
                            }
                            return;
                        }
                        if (actionResult2 == InteractionResult.FAIL) {
                            return;
                        }
                    }
                }
            }
        }
    }) {
        @Override
        public Component getTooltip() {
            return TextUtils.getText("fast_building", true);
        }
    });

    public static final NotEKKeyBinding FAST_BLOCK_BREAKING = registerModKeyBinding(new NotEKKeyBinding("fast_block_breaking", BUILDING_CATEGORY, new NotEKKeyBinding.KeybindingTicker() {
        @Override
        public void onWasPressed(Minecraft client, NotEKKeyBinding keyBinding) {
        }

        @Override
        public void onTick(Minecraft client, NotEKKeyBinding keyBinding) {
            for (InteractionHand hand : InteractionHand.values()) {
                if (isInGame(client) && (keyBinding.isDown() || keyBinding.consumeClick())) {
                    if (client.hitResult.getType().equals(HitResult.Type.BLOCK)) {
                        BlockHitResult blockHitResult = (BlockHitResult) client.hitResult;
                        BlockPos blockPos = blockHitResult.getBlockPos();
                        if (!client.level.getBlockState(blockPos).isAir()) {
                            client.gameMode.startDestroyBlock(blockPos, blockHitResult.getDirection());
                            client.player.swing(hand);
                            break;
                        }
                    }
                }
            }
        }
    }) {
        @Override
        public Component getTooltip() {
            return TextUtils.getText("fast_block_breaking", true);
        }
    });

    public static final NotEKKeyBinding ALWAYS_PLACE_ITEM = registerModKeyBinding(new NotEKKeyBinding("always_place_item", BUILDING_CATEGORY, (client, keyBinding) -> {
        if (itemUseCooldown == 0) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack itemStack = client.player.getItemInHand(hand);

                if (!client.hitResult.getType().equals(HitResult.Type.ENTITY)) {
                    BlockHitResult blockHitResult = (BlockHitResult) client.hitResult;
                    int i = itemStack.getCount();
                    InteractionResult actionResult2 = client.gameMode.useItemOn(client.player, hand, blockHitResult);
                    if (actionResult2.consumesAction()) {
                        itemUseCooldown = 4;
                        if (actionResult2 instanceof InteractionResult.Success success && success.swingSource() == InteractionResult.SwingSource.CLIENT) {
                            client.player.swing(hand);
                            if (!itemStack.isEmpty() && (itemStack.getCount() != i || client.gameMode.getPlayerMode().isCreative())) {
                                client.gameRenderer.itemInHandRenderer.itemUsed(hand);
                            }
                        }
                        return;
                    }
                    if (actionResult2 == InteractionResult.FAIL) {
                        return;
                    }
                }
            }
        }
    }){
        @Override
        public Component getTooltip() {
            return TextUtils.getText("always_place_item", true);
        }

        @Override
        public void tick(Minecraft client) {
            if (itemUseCooldown > 0) {
                itemUseCooldown--;
            }
            super.tick(client);
        }
    });

    @Override
    public KeybindCategory getModCategory() {
        return new KeybindCategory(BUILDING_CATEGORY_KEY, 5, ALWAYS_PLACE_ITEM, FAST_BUILDING, FAST_BLOCK_BREAKING);
    }
}
