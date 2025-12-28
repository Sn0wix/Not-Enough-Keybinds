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


    public static void registerModKeybinds() {
        registerKeyCategory(new BuildingKeys().getModCategory());
        registerKeyCategory(new F3ShortcutsKeys().getModCategory());
        registerKeyCategory(new InventoryKeys().getModCategory());
        registerKeyCategory(new SkinLayersKeys().getModCategory());
        registerKeyCategory(new ChatKeys().getModCategory());
        registerKeyCategory(new SoundKeys().getModCategory());
        registerKeyCategory(new PresetKeys().getModCategory());
        registerKeyCategory(new MiscKeys().getModCategory());
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
    public abstract KeybindCategory getModCategory();

    public static void registerKeyCategory(KeybindCategory category) {
        KEYBINDING_CATEGORIES.add(category);
        KEYBINDING_CATEGORIES.sort(Comparator.comparingInt(KeybindCategory::getPriority));
    }

    public static void unregisterKeyCategory(KeybindCategory category) {
        KEYBINDING_CATEGORIES.remove(category);
    }

    public static void updateCategory(KeybindCategory category) {
        unregisterKeyCategory(category);
        registerKeyCategory(category);
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
