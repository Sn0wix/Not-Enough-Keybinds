package net.sn0wix_.keybinds;

import net.sn0wix_.NotEnoughKeybinds;
import net.sn0wix_.keybinds.custom.F3ShortcutKeybinding;
import net.sn0wix_.keybinds.custom.KeybindingCategory;
import net.sn0wix_.keybinds.custom.ModKeyBinding;

public class F3ShortcutsKeybinds extends ModKeybindings {
    public static final String F3_SHORTCUTS_CATEGORY = "key.category." + NotEnoughKeybinds.MOD_ID + ".f3_shortcuts";

    public static final ModKeyBinding TOGGLE_HITBOXES = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_hitboxes", F3_SHORTCUTS_CATEGORY, 66));
    public static final ModKeyBinding TOGGLE_CHUNK_GRID = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_chunk_grid", F3_SHORTCUTS_CATEGORY, 71));
    public static final ModKeyBinding TOGGLE_ADVANCED_TOOLTIPS = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_advanced_tooltips", F3_SHORTCUTS_CATEGORY, 72));
    public static final ModKeyBinding CLEAR_CHAT_HISTORY = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("clear_chat_history", F3_SHORTCUTS_CATEGORY, 68));
    public static final ModKeyBinding TOGGLE_SPECTATOR_MODE = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_spectator_mode", F3_SHORTCUTS_CATEGORY, 78));
    public static final ModKeyBinding TOGGLE_AUTO_PAUSE = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_auto_pause", F3_SHORTCUTS_CATEGORY, 80));
    public static final ModKeyBinding RELOAD_ALL_CHUNKS = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("reload_all_chunks", F3_SHORTCUTS_CATEGORY, 65));
    public static final ModKeyBinding RELOAD_RESOURCE_PACKS = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("reload_resource_packs", F3_SHORTCUTS_CATEGORY, 84));

    @Override
    public KeybindingCategory getCategory() {
        return new KeybindingCategory(F3_SHORTCUTS_CATEGORY, 69, TOGGLE_HITBOXES, TOGGLE_CHUNK_GRID, TOGGLE_ADVANCED_TOOLTIPS, CLEAR_CHAT_HISTORY, TOGGLE_SPECTATOR_MODE, TOGGLE_AUTO_PAUSE, RELOAD_ALL_CHUNKS, RELOAD_RESOURCE_PACKS);
    }
}
