package net.sn0wix_.notEnoughKeybinds;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.config.*;
import net.sn0wix_.notEnoughKeybinds.events.ClientEndTickEvent;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotEnoughKeybinds implements ClientModInitializer {

    public static final String MOD_ID = "not-enough-keybinds";
    public static final String MOD_NAME = "NotEnoughKeybinds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier ICON = Identifier.of(MOD_ID, "icon.png");

    public static final DebugKeysConfig DEBUG_KEYS_CONFIG = DebugKeysConfig.getConfig();
    public static final ChatKeysConfig CHAT_KEYS_CONFIG = ChatKeysConfig.getConfig();
    public static final SwapTotemShieldConfig TOTEM_SHIELD_CONFIG = SwapTotemShieldConfig.getConfig();
    public static final EquipElytraConfig EQUIP_ELYTRA_CONFIG = EquipElytraConfig.getConfig();
    public static final NotEKSettings GENERAL_CONFIG = NotEKSettings.getConfig();

    /**TODO
     swap totem shield delay (do not swap if the same item is in swapped)
     1.21.4 port newest version
     pie chart bug?
     fast place - delay (in ticks) - configurable
     */

    @Override
    public void onInitializeClient() {
        NotEKKeyBindings.registerModKeybinds();

        ClientTickEvents.END_CLIENT_TICK.register(new ClientEndTickEvent());
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> ChatKeys.CHAT_KEYS_CATEGORY.initializeKeys());

        PresetLoader.init();
    }
}