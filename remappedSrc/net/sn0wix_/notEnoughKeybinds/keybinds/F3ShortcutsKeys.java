package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.F3ShortcutKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;

import java.util.ArrayList;
import java.util.List;

public class F3ShortcutsKeys extends NotEKKeyBindings {
    public static final String F3_SHORTCUTS_CATEGORY_STRING = "key.category." + NotEnoughKeybinds.MOD_ID + ".f3_shortcuts";

    public static final NotEKKeyBinding TOGGLE_HITBOXES = registerModKeyBinding(new F3ShortcutKeybinding("toggle_hitboxes", 66));
    public static final NotEKKeyBinding TOGGLE_CHUNK_GRID = registerModKeyBinding(new F3ShortcutKeybinding("toggle_chunk_grid", 71));
    public static final NotEKKeyBinding TOGGLE_ADVANCED_TOOLTIPS = registerModKeyBinding(new F3ShortcutKeybinding("toggle_advanced_tooltips", 72));
    public static final NotEKKeyBinding CLEAR_CHAT_HISTORY = registerModKeyBinding(new F3ShortcutKeybinding("clear_chat_history", 68));
    public static final NotEKKeyBinding TOGGLE_SPECTATOR_MODE = registerModKeyBinding(new F3ShortcutKeybinding("toggle_spectator_mode", 78));
    public static final NotEKKeyBinding TOGGLE_AUTO_PAUSE = registerModKeyBinding(new F3ShortcutKeybinding("toggle_auto_pause", 80));
    public static final NotEKKeyBinding RELOAD_ALL_CHUNKS = registerModKeyBinding(new F3ShortcutKeybinding("reload_all_chunks", 65));
    public static final NotEKKeyBinding RELOAD_RESOURCE_PACKS = registerModKeyBinding(new F3ShortcutKeybinding("reload_resource_packs", 84));

    public static final KeybindCategory F3_SHORTCUTS_CATEGORY = new KeybindCategory(F3_SHORTCUTS_CATEGORY_STRING, 96, TOGGLE_HITBOXES, TOGGLE_CHUNK_GRID, TOGGLE_ADVANCED_TOOLTIPS, CLEAR_CHAT_HISTORY, TOGGLE_SPECTATOR_MODE, TOGGLE_AUTO_PAUSE, RELOAD_ALL_CHUNKS, RELOAD_RESOURCE_PACKS);


    @Override
    public KeybindCategory getCategory() {
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
