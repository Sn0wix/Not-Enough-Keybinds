package net.sn0wix_;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.sn0wix_.events.ClientEndTickEvent;
import net.sn0wix_.keybinds.ModKeybindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class NotEnoughKeybinds implements ClientModInitializer {
    public static final String MOD_ID = "not-enough-keybinds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        ModKeybindings.registerModKeybinds();

        ClientTickEvents.END_CLIENT_TICK.register(new ClientEndTickEvent());
    }
}