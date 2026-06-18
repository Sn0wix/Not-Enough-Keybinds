package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;

public class MiscKeys extends NotEKKeyBindings{
    public static final String MISC_CATEGORY_KEY = NotEnoughKeybinds.getIdentifier("misc").toLanguageKey("key.category");
    public static final KeyMapping.Category MISC_CATEGORY = KeyMapping.Category.register(NotEnoughKeybinds.getIdentifier(MISC_CATEGORY_KEY));

    public static final NotEKKeyBinding DISCONNECT = registerModKeyBinding(new NotEKKeyBinding("disconnect", MISC_CATEGORY, (client, keyBinding) -> {
            client.getReportingContext().draftReportHandled(client, new PauseScreen(true), () -> client.disconnectFromWorld(ClientLevel.DEFAULT_QUIT_MESSAGE), true);
    }));

    @Override
    public KeybindCategory getModCategory() {
        return new KeybindCategory(MISC_CATEGORY_KEY, 2, false, DISCONNECT);
    }
}
