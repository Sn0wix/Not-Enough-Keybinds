package net.sn0wix_.notEnoughKeybinds.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen.NotEKSettingsScreen;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return NotEKSettingsScreen::new;
    }
}
