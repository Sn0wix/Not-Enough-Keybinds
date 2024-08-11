package net.sn0wix_.notEnoughKeybinds;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.config.ChatKeysConfig;
import net.sn0wix_.notEnoughKeybinds.config.DebugKeysConfig;
import net.sn0wix_.notEnoughKeybinds.events.ClientEndTickEvent;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotEnoughKeybinds implements ClientModInitializer {
    public static final String MOD_ID = "not-enough-keybinds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier ICON = new Identifier(MOD_ID, "icon.png");

    public static final DebugKeysConfig DEBUG_KEYS_CONFIG = DebugKeysConfig.getConfig();
    public static final ChatKeysConfig CHAT_KEYS_CONFIG = ChatKeysConfig.getConfig();

    @Override
    public void onInitializeClient() {
        NotEKKeyBindings.registerModKeybinds();

        ClientTickEvents.END_CLIENT_TICK.register(new ClientEndTickEvent());
    }
}