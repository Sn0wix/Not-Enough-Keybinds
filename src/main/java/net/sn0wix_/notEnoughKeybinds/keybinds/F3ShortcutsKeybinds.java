package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.F3ShortcutKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindingCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.ModKeyBinding;

import java.util.ArrayList;
import java.util.List;

public class F3ShortcutsKeybinds extends ModKeybindings {
    public static final String F3_SHORTCUTS_CATEGORY_STRING = "key.category." + NotEnoughKeybinds.MOD_ID + ".f3_shortcuts";

    public static final ModKeyBinding TOGGLE_HITBOXES = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_hitboxes", F3_SHORTCUTS_CATEGORY_STRING, 66));
    public static final ModKeyBinding TOGGLE_CHUNK_GRID = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_chunk_grid", F3_SHORTCUTS_CATEGORY_STRING, 71));
    public static final ModKeyBinding TOGGLE_ADVANCED_TOOLTIPS = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_advanced_tooltips", F3_SHORTCUTS_CATEGORY_STRING, 72));
    public static final ModKeyBinding CLEAR_CHAT_HISTORY = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("clear_chat_history", F3_SHORTCUTS_CATEGORY_STRING, 68));
    public static final ModKeyBinding TOGGLE_SPECTATOR_MODE = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_spectator_mode", F3_SHORTCUTS_CATEGORY_STRING, 78));
    public static final ModKeyBinding TOGGLE_AUTO_PAUSE = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("toggle_auto_pause", F3_SHORTCUTS_CATEGORY_STRING, 80));
    public static final ModKeyBinding RELOAD_ALL_CHUNKS = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("reload_all_chunks", F3_SHORTCUTS_CATEGORY_STRING, 65));
    public static final ModKeyBinding RELOAD_RESOURCE_PACKS = (ModKeyBinding) registerModKeyBinding(new F3ShortcutKeybinding("reload_resource_packs", F3_SHORTCUTS_CATEGORY_STRING, 84));

    public static final KeybindingCategory F3_SHORTCUTS_CATEGORY = new KeybindingCategory(F3_SHORTCUTS_CATEGORY_STRING, 69, TOGGLE_HITBOXES, TOGGLE_CHUNK_GRID, TOGGLE_ADVANCED_TOOLTIPS, CLEAR_CHAT_HISTORY, TOGGLE_SPECTATOR_MODE, TOGGLE_AUTO_PAUSE, RELOAD_ALL_CHUNKS, RELOAD_RESOURCE_PACKS);


    @Override
    public KeybindingCategory getCategory() {
        return F3_SHORTCUTS_CATEGORY;
    }

    public static List<F3ShortcutKeybinding> getF3ShortcutKeys() {
        ArrayList<F3ShortcutKeybinding> list = new ArrayList<>(F3_SHORTCUTS_CATEGORY.getKeyBindings().length);

        for (int i = 0; i < F3_SHORTCUTS_CATEGORY.getKeyBindings().length; i++) {
            if (F3_SHORTCUTS_CATEGORY.getKeyBindings()[i] instanceof F3ShortcutKeybinding keybinding) {
                list.add(keybinding);
            }
        }
        return list;
    }
}
