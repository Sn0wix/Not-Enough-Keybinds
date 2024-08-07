package net.sn0wix_.notEnoughKeybinds.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.MinecraftClient;
import net.sn0wix_.notEnoughKeybinds.gui.keybindsScreen.NotEKSettingsScreen;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new NotEKSettingsScreen(parent, MinecraftClient.getInstance().options);
    }
}
