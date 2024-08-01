package net.sn0wix_.keybinds;

import net.minecraft.client.option.KeyBinding;
import net.sn0wix_.NotEnoughKeybinds;
import net.sn0wix_.keybinds.custom.F3ShortcutKeybinding;

public class F3ShortcutsKeybinds extends ModKeybinds {
    public static final KeyBinding TOGGLE_HITBOXES = registerModKeyBinding(new F3ShortcutKeybinding("toggle_hitboxes", F3_SHORTCUTS_CATEGORY, 66));
    public static final KeyBinding TOGGLE_CHUNK_GRID = registerModKeyBinding(new F3ShortcutKeybinding("toggle_chunk_grid", F3_SHORTCUTS_CATEGORY, 71));
    public static final KeyBinding TOGGLE_ADVANCED_TOOLTIPS = registerModKeyBinding(new F3ShortcutKeybinding("toggle_advanced_tooltips", F3_SHORTCUTS_CATEGORY, 72));
    public static final KeyBinding CLEAR_CHAT_HISTORY = registerModKeyBinding(new F3ShortcutKeybinding("clear_chat_history", F3_SHORTCUTS_CATEGORY, 68));
    public static final KeyBinding TOGGLE_SPECTATOR_MODE = registerModKeyBinding(new F3ShortcutKeybinding("toggle_spectator_mode", F3_SHORTCUTS_CATEGORY, 78));
    public static final KeyBinding TOGGLE_AUTO_PAUSE = registerModKeyBinding(new F3ShortcutKeybinding("toggle_auto_pause", F3_SHORTCUTS_CATEGORY, 80));
    public static final KeyBinding RELOAD_ALL_CHUNKS = registerModKeyBinding(new F3ShortcutKeybinding("reload_all_chunks", F3_SHORTCUTS_CATEGORY, 65));
    public static final KeyBinding RELOAD_RESOURCE_PACKS = registerModKeyBinding(new F3ShortcutKeybinding("reload_resource_packs", F3_SHORTCUTS_CATEGORY, 84));

    public static void registerKeybinds() {
        NotEnoughKeybinds.LOGGER.info("Registering f3 shortcuts");
    }
}
