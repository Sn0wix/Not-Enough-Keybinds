package net.sn0wix_.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.sn0wix_.NotEnoughKeybinds;
import net.sn0wix_.keybinds.ModKeybinds;

public class ClientEndTickEvent implements ClientTickEvents.EndTick {

    @Override
    public void onEndTick(MinecraftClient client) {
        try {
            ModKeybinds.MOD_KEY_BINDINGS.forEach(modKeyBinding -> modKeyBinding.tick(client));
        } catch (NullPointerException e) {
            NotEnoughKeybinds.LOGGER.error("Something went wrong with executing keybinding behavior.");
            e.printStackTrace();
        }
    }
}
