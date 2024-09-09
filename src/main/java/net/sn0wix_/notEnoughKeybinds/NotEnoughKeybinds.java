package net.sn0wix_.notEnoughKeybinds;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.util.Identifier;
import net.sn0wix_.notEnoughKeybinds.config.ChatKeysConfig;
import net.sn0wix_.notEnoughKeybinds.config.EquipElytraConfig;
import net.sn0wix_.notEnoughKeybinds.config.SwapTotemShieldConfig;
import net.sn0wix_.notEnoughKeybinds.config.DebugKeysConfig;
import net.sn0wix_.notEnoughKeybinds.events.ClientEndTickEvent;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotEnoughKeybinds implements ClientModInitializer {
    public static final String MOD_ID = "not-enough-keybinds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Identifier ICON = new Identifier(MOD_ID, "icon.png");

    public static final DebugKeysConfig DEBUG_KEYS_CONFIG = DebugKeysConfig.getConfig();
    public static final ChatKeysConfig CHAT_KEYS_CONFIG = ChatKeysConfig.getConfig();
    public static final SwapTotemShieldConfig TOTEM_SHIELD_CONFIG = SwapTotemShieldConfig.getConfig();
    public static final EquipElytraConfig EQUIP_ELYTRA_CONFIG = EquipElytraConfig.getConfig();

    /*bugs:
    elytra equip - offhand
    chat keys translation saving two times
     */

    /*Ideas:
    rocket to offhand
    hotbar + quick use rocket
     */


    @Override
    public void onInitializeClient() {
        NotEKKeyBindings.registerModKeybinds();

        ClientTickEvents.END_CLIENT_TICK.register(new ClientEndTickEvent());
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> ChatKeys.CHAT_KEYS_CATEGORY.initializeKeys());
    }
}