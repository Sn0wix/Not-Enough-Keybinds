package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class NotEKKeyBindings {
    private static final ArrayList<KeybindCategory> KEYBINDING_CATEGORIES = new ArrayList<>();


    public static final String KEY_BINDING_PREFIX = "key." + NotEnoughKeybinds.MOD_ID + ".";


    //why this isn't a thing in vanilla
    public static final KeyBinding TOGGLE_HIDE_HUD = registerKeyBinding(new KeyBinding(KEY_BINDING_PREFIX + "toggle_hide_hud", InputUtil.GLFW_KEY_F1, KeyBinding.MISC_CATEGORY));


    public static void registerModKeybinds() {
        registerKeyCategory(new BuildingKeys().getCategory());
        registerKeyCategory(new F3ShortcutsKeys().getCategory());
        registerKeyCategory(new InventoryKeys().getCategory());
        registerKeyCategory(new SkinLayersKeys().getCategory());
        registerKeyCategory(new F3DebugKeys().getCategory());
        registerKeyCategory(new ChatKeys().getCategory());
    }

    public static KeybindCategory getCategoryByTranslation(String translationKey) {
        AtomicReference<KeybindCategory> returnValue = new AtomicReference<>(null);
        KEYBINDING_CATEGORIES.forEach(keybindCategory -> {
            if (keybindCategory.getTranslationKey().equals(translationKey))
                returnValue.set(keybindCategory);
        });

        return returnValue.get();
    }

    //registering stuff
    public abstract KeybindCategory getCategory();

    public static void registerKeyCategory(KeybindCategory category) {
        KEYBINDING_CATEGORIES.add(category);
        KEYBINDING_CATEGORIES.sort(Comparator.comparingInt(KeybindCategory::getPriority));
    }

    public static void unregisterKeyCategory(KeybindCategory category) {
        KEYBINDING_CATEGORIES.remove(category);
    }


    public static NotEKKeyBinding registerModKeyBinding(NotEKKeyBinding keyBinding) {
        return (NotEKKeyBinding) KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    public static KeyBinding registerKeyBinding(KeyBinding keyBinding) {
        return KeyBindingHelper.registerKeyBinding(keyBinding);
    }

    //Helper methods
    public static List<KeybindCategory> getCategories() {
        return KEYBINDING_CATEGORIES.stream().toList();
    }

    public static List<INotEKKeybinding> getModKeybindsAsList() {
        ArrayList<INotEKKeybinding> keyBindings = new ArrayList<>();
        KEYBINDING_CATEGORIES.forEach(category -> keyBindings.addAll(Arrays.asList(category.getKeyBindings())));
        return keyBindings.stream().toList();
    }

    public static INotEKKeybinding[] getModKeybinds() {
        List<INotEKKeybinding> bindings = getModKeybindsAsList();

        INotEKKeybinding[] list = new INotEKKeybinding[bindings.size()];

        for (int i = 0; i < bindings.size(); i++) {
            list[i] = bindings.get(i);
        }

        return list;
    }
}
