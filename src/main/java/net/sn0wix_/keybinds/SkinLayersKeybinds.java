package net.sn0wix_.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.sn0wix_.NotEnoughKeybinds;
import net.sn0wix_.keybinds.custom.ModKeyBinding;

public class SkinLayersKeybinds extends ModKeybinds {
    public static final KeyBinding TOGGLE_SKIN_LAYER_CAPE = registerModKeyBinding(new ModKeyBinding("toggle_skin_layer_cape", SKIN_LAYERS_CATEGORY, new SkinLayerKeybind(PlayerModelPart.CAPE)));
    public static final KeyBinding TOGGLE_SKIN_LAYER_HAT = registerModKeyBinding(new ModKeyBinding("toggle_skin_layer_hat", SKIN_LAYERS_CATEGORY, new SkinLayerKeybind(PlayerModelPart.HAT)));
    public static final KeyBinding TOGGLE_SKIN_LAYER_JACKET = registerModKeyBinding(new ModKeyBinding("toggle_skin_layer_jacket", SKIN_LAYERS_CATEGORY, new SkinLayerKeybind(PlayerModelPart.JACKET)));
    public static final KeyBinding TOGGLE_SKIN_LAYER_LEFT_SLEEVE = registerModKeyBinding(new ModKeyBinding("toggle_skin_layer_left_sleeve", SKIN_LAYERS_CATEGORY, new SkinLayerKeybind(PlayerModelPart.LEFT_SLEEVE)));
    public static final KeyBinding TOGGLE_SKIN_LAYER_RIGHT_SLEEVE = registerModKeyBinding(new ModKeyBinding("toggle_skin_layer_right_sleeve", SKIN_LAYERS_CATEGORY, new SkinLayerKeybind(PlayerModelPart.RIGHT_SLEEVE)));
    public static final KeyBinding TOGGLE_SKIN_LAYER_LEFT_PANTS_LEG = registerModKeyBinding(new ModKeyBinding("toggle_skin_layer_left_pants_leg", SKIN_LAYERS_CATEGORY, new SkinLayerKeybind(PlayerModelPart.LEFT_PANTS_LEG)));
    public static final KeyBinding TOGGLE_SKIN_LAYER_RIGHT_PANTS_LEG = registerModKeyBinding(new ModKeyBinding("toggle_skin_layer_right_pants_leg", SKIN_LAYERS_CATEGORY, new SkinLayerKeybind(PlayerModelPart.RIGHT_PANTS_LEG)));


    public static class SkinLayerKeybind implements ModKeyBinding.OnClientTick {
        private final PlayerModelPart modelPart;

        public SkinLayerKeybind(PlayerModelPart modelPart) {
            this.modelPart = modelPart;
        }

        @Override
        public void onWasPressedInGame(MinecraftClient client, KeyBinding keyBinding) {
            client.options.togglePlayerModelPart(modelPart, !client.options.isPlayerModelPartEnabled(modelPart));
        }
    }

    public static void registerSkinLayerKeybinds() {
        NotEnoughKeybinds.LOGGER.info("Registering skin layers keybinds");
    }
}
