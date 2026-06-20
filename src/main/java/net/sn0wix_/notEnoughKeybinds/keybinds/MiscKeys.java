package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;

public class MiscKeys extends NotEKKeyBindings{
    public static final String MISC_CATEGORY_KEY = NotEnoughKeybinds.getIdentifier("misc").toTranslationKey("key.category");
    public static final KeyBinding.Category MISC_CATEGORY = KeyBinding.Category.create(NotEnoughKeybinds.getIdentifier(MISC_CATEGORY_KEY));

    public static final NotEKKeyBinding DISCONNECT = registerModKeyBinding(new NotEKKeyBinding("disconnect", MISC_CATEGORY, (client, keyBinding) -> {
            client.getAbuseReportContext().tryShowDraftScreen(client, new GameMenuScreen(true), () -> client.disconnect(ClientWorld.QUITTING_MULTIPLAYER_TEXT), true);
    }));

    @Override
    public KeybindCategory getModCategory() {
        return new KeybindCategory(MISC_CATEGORY_KEY, 2, false, DISCONNECT);
    }
}
