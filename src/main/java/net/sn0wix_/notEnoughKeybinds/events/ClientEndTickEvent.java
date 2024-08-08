package net.sn0wix_.notEnoughKeybinds.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeybindings;

public class ClientEndTickEvent implements ClientTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftClient client) {
        try {
            NotEKKeybindings.getModKeybindsAsList().forEach(modKeyBinding -> modKeyBinding.tick(client));
        } catch (NullPointerException e) {
            NotEnoughKeybinds.LOGGER.error("Something went wrong with executing keybinding behavior.");
            e.printStackTrace();
        }
    }
}
