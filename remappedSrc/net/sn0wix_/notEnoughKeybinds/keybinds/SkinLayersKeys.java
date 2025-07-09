package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerModelPart;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;

public class SkinLayersKeys extends NotEKKeyBindings {
    public static final String SKIN_LAYERS_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".skin_layers";

    public static final NotEKKeyBinding TOGGLE_SKIN_LAYER_CAPE = registerModKeyBinding(new NotEKKeyBinding("toggle_skin_layer_cape", SKIN_LAYERS_CATEGORY, new SkinLayerKeybinding(PlayerModelPart.CAPE)));
    public static final NotEKKeyBinding TOGGLE_SKIN_LAYER_HAT = registerModKeyBinding(new NotEKKeyBinding("toggle_skin_layer_hat", SKIN_LAYERS_CATEGORY, new SkinLayerKeybinding(PlayerModelPart.HAT)));
    public static final NotEKKeyBinding TOGGLE_SKIN_LAYER_JACKET = registerModKeyBinding(new NotEKKeyBinding("toggle_skin_layer_jacket", SKIN_LAYERS_CATEGORY, new SkinLayerKeybinding(PlayerModelPart.JACKET)));
    public static final NotEKKeyBinding TOGGLE_SKIN_LAYER_LEFT_SLEEVE = registerModKeyBinding(new NotEKKeyBinding("toggle_skin_layer_left_sleeve", SKIN_LAYERS_CATEGORY, new SkinLayerKeybinding(PlayerModelPart.LEFT_SLEEVE)));
    public static final NotEKKeyBinding TOGGLE_SKIN_LAYER_RIGHT_SLEEVE = registerModKeyBinding(new NotEKKeyBinding("toggle_skin_layer_right_sleeve", SKIN_LAYERS_CATEGORY, new SkinLayerKeybinding(PlayerModelPart.RIGHT_SLEEVE)));
    public static final NotEKKeyBinding TOGGLE_SKIN_LAYER_LEFT_PANTS_LEG = registerModKeyBinding(new NotEKKeyBinding("toggle_skin_layer_left_pants_leg", SKIN_LAYERS_CATEGORY, new SkinLayerKeybinding(PlayerModelPart.LEFT_PANTS_LEG)));
    public static final NotEKKeyBinding TOGGLE_SKIN_LAYER_RIGHT_PANTS_LEG = registerModKeyBinding(new NotEKKeyBinding("toggle_skin_layer_right_pants_leg", SKIN_LAYERS_CATEGORY, new SkinLayerKeybinding(PlayerModelPart.RIGHT_PANTS_LEG)));

    public static final NotEKKeyBinding TOGGLE_SECOND_SKIN_LAYER = registerModKeyBinding(new NotEKKeyBinding("toggle_second_skin_layer", SKIN_LAYERS_CATEGORY, (client, keyBinding) -> {
        for (PlayerModelPart modelPart : PlayerModelPart.values()) {
            enableModelPart(client, modelPart);
        }
    }));

    @Override
    public KeybindCategory getCategory() {
        return new KeybindCategory(SKIN_LAYERS_CATEGORY, 30, TOGGLE_SKIN_LAYER_CAPE, TOGGLE_SKIN_LAYER_HAT, TOGGLE_SKIN_LAYER_JACKET, TOGGLE_SKIN_LAYER_LEFT_SLEEVE, TOGGLE_SKIN_LAYER_RIGHT_SLEEVE, TOGGLE_SKIN_LAYER_LEFT_PANTS_LEG, TOGGLE_SKIN_LAYER_RIGHT_PANTS_LEG, TOGGLE_SECOND_SKIN_LAYER);
    }


    public static class SkinLayerKeybinding implements INotEKKeybinding.KeybindingTicker {
        private final PlayerModelPart modelPart;

        public SkinLayerKeybinding(PlayerModelPart modelPart) {
            this.modelPart = modelPart;
        }

        @Override
        public void onWasPressed(MinecraftClient client, NotEKKeyBinding keyBinding) {
            enableModelPart(client, modelPart);
        }
    }

    public static void enableModelPart(MinecraftClient client, PlayerModelPart playerModelPart) {
        client.options.setPlayerModelPart(playerModelPart, !client.options.isPlayerModelPartEnabled(playerModelPart));
        client.options.write();
    }
}
