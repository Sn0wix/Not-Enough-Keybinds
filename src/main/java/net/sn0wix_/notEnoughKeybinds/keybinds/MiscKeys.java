package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.MessageScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;

public class MiscKeys extends NotEKKeyBindings{
    public static final String MISC_CATEGORY_KEY = NotEnoughKeybinds.getIdentifier("misc").toTranslationKey("key.category");

    public static final NotEKKeyBinding DISCONNECT = registerModKeyBinding(new NotEKKeyBinding("disconnect", MISC_CATEGORY_KEY, (client, keyBinding) -> {
        client.getAbuseReportContext().tryShowDraftScreen(client, new GameMenuScreen(true), () -> disconnectWorld(client), true);
    }));

    @Override
    public KeybindCategory getCategory() {
        return new KeybindCategory(MISC_CATEGORY_KEY, 2, false, DISCONNECT);
    }

    public static void disconnectWorld(MinecraftClient client) {
        boolean bl = client.isInSingleplayer();
        ServerInfo serverInfo = client.getCurrentServerEntry();
        client.world.disconnect();
        if (bl) {
            client.disconnect(new MessageScreen(Text.translatable("menu.savingLevel")));
        } else {
            client.disconnect();
        }
        TitleScreen titleScreen = new TitleScreen();
        if (bl) {
            client.setScreen(titleScreen);
        } else if (serverInfo != null && serverInfo.isRealm()) {
            client.setScreen(new RealmsMainScreen(titleScreen));
        } else {
            client.setScreen(new MultiplayerScreen(titleScreen));
        }
    }
}
