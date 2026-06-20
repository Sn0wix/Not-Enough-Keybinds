package net.sn0wix_.notEnoughKeybinds.keybinds;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.NotEKKeyBinding;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class SoundKeys extends NotEKKeyBindings {
    public static final String SOUND_KEYS_CATEGORY_KEY = "key.category." + NotEnoughKeybinds.MOD_ID + ".sound";
    public static final KeyMapping.Category SOUND_KEYS_CATEGORY = KeyMapping.Category.register(NotEnoughKeybinds.getIdentifier(SOUND_KEYS_CATEGORY_KEY));


    public static final NotEKKeyBinding TOGGLE_SUBTITLES = registerModKeyBinding(new NotEKKeyBinding("toggle_subtitles", SOUND_KEYS_CATEGORY, (client, keyBinding) -> {
        client.options.showSubtitles().set(!client.options.showSubtitles().get());
    }));

    public static final NotEKKeyBinding INCREASE_MASTER_VOLUME = registerModKeyBinding(new NotEKKeyBinding("increase_master_volume", SOUND_KEYS_CATEGORY, new VolumeKeybindingTicker(SoundSource.MASTER, 0.1f)));
    public static final NotEKKeyBinding DECREASE_MASTER_VOLUME = registerModKeyBinding(new NotEKKeyBinding("decrease_master_volume", SOUND_KEYS_CATEGORY, new VolumeKeybindingTicker(SoundSource.MASTER, -0.1f)));


    @Override
    public KeybindCategory getModCategory() {
        INotEKKeybinding[] bindings = new INotEKKeybinding[SoundSource.values().length + 3];
        bindings[0] = TOGGLE_SUBTITLES;
        bindings[1] = INCREASE_MASTER_VOLUME;
        bindings[2] = DECREASE_MASTER_VOLUME;

        int i = bindings.length - SoundSource.values().length;

        for (SoundSource category : SoundSource.values()) {
            bindings[i] = registerModKeyBinding(new SoundKeybinding("soundCategory." + category.getName(), SOUND_KEYS_CATEGORY, category));
            i++;
        }

        return new KeybindCategory(SOUND_KEYS_CATEGORY_KEY, 10, bindings);
    }


    public static class SoundKeybinding extends NotEKKeyBinding {
        public SoundSource category;

        public SoundKeybinding(String translationKey, Category category, SoundSource soundCategory) {
            super(translationKey, category, new ToggleCategoryBindingTicker(soundCategory));
            this.category = soundCategory;
        }

        @Override
        public Component getSettingsDisplayName() {
            return TextUtils.getCombinedTranslation(Component.translatable(TextUtils.getTranslationKey("toggle")),
                    Component.translatable("soundCategory." + category.getName()));
        }
    }


    public static class ToggleCategoryBindingTicker implements INotEKKeybinding.KeybindingTicker {
        private double previousVolume = 1;

        private final SoundSource category;

        public ToggleCategoryBindingTicker(SoundSource category) {
            this.category = category;
        }

        @Override
        public void onWasPressed(Minecraft client, NotEKKeyBinding keyBinding) {
            if (client.options.getSoundSourceOptionInstance(category).get() > 0) {
                previousVolume = client.options.getSoundSourceOptionInstance(category).get() > 0 ? client.options.getSoundSourceOptionInstance(category).get() : 1;
                client.options.getSoundSourceOptionInstance(category).set(0.0);
            } else {
                client.options.getSoundSourceOptionInstance(category).set(previousVolume);
            }
        }
    }

    public record VolumeKeybindingTicker(SoundSource category, float scaling) implements INotEKKeybinding.KeybindingTicker {
        @Override
        public void onTick(Minecraft client, NotEKKeyBinding keyBinding) {
            while (keyBinding.consumeClick()) {
                onWasPressed(client, keyBinding);
            }
        }

        @Override
        public void onWasPressed(Minecraft client, NotEKKeyBinding keyBinding) {
            double value = client.options.getSoundSourceOptionInstance(category).get();
            value += scaling;

            if (value < 0) {
                value = 0;
            } else if (value > 1) {
                value = 1;
            }

            client.options.getSoundSourceOptionInstance(category).set(value);
        }
    }
}
