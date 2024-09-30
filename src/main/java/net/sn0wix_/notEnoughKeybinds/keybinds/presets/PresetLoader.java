package net.sn0wix_.notEnoughKeybinds.keybinds.presets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.keybinds.ChatKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.F3DebugKeys;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class PresetLoader {
    public static final Float CURRENT_VERSION = 1f;
    public static final File PRESETS_DIR = new File("config" + File.separator + NotEnoughKeybinds.MOD_ID + File.separator + "presets");
    private static final ArrayList<KeybindPreset> presets = new ArrayList<>();


    public static void init() {
        PRESETS_DIR.mkdirs();
        List<File> presetFiles = List.of(Objects.requireNonNull(PRESETS_DIR.listFiles()));

        presetFiles.forEach(file -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                float version;
                try {
                    version = Float.parseFloat(Utils.nextValue("version", reader));
                } catch (NumberFormatException e) {
                    NotEnoughKeybinds.LOGGER.error("The version string is not a number!");
                    throw new RuntimeException(e);
                }

                if (version == 0) {
                    NotEnoughKeybinds.LOGGER.error("Incorrect version number in " + file.getAbsolutePath());
                    throw new Exception();
                }

                String name = Utils.nextValue("preset_name", reader);
                String description = Utils.nextValue("description", reader);
                List<String> content = new ArrayList<>();

                reader.lines().forEach(content::add);
                reader.close();

                presets.add(new KeybindPreset(file.getName(), version, name, description, content));
            } catch (Exception e) {
                NotEnoughKeybinds.LOGGER.info("Could not read from file " + file.getAbsolutePath(), e);
            }
        });

        NotEnoughKeybinds.LOGGER.info("Presets initialized");
    }

    public static void reload() {
        NotEnoughKeybinds.LOGGER.info("Reloading presets...");
        presets.clear();
        init();
    }

    /**
     * Load presents only after options were initialized
     */
    public static void loadPreset(KeybindPreset preset) {
        preset.content.forEach(line -> {
            String translation = line.split(":")[0];
            String value = Utils.getValue(line);

            if (translation.isEmpty() || value.isEmpty()) {
                NotEnoughKeybinds.LOGGER.error("Incorrect keybinding entry in preset" + preset.name + ": " + translation + ":" + value);
            } else {
                GameOptions options = MinecraftClient.getInstance().options;
                boolean found = false;

                for (int i = 0; i < options.allKeys.length; i++) {
                    KeyBinding binding = options.allKeys[i];
                    if (binding.getTranslationKey().equals(translation)) {
                        binding.setBoundKey(InputUtil.fromTranslationKey(value));
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    //check if the keys is saved in another file than options.txt
                    if (translation.contains("key." + NotEnoughKeybinds.MOD_ID)) {
                        Stream.of(ChatKeys.CHAT_KEYS_CATEGORY.getKeyBindings(), F3DebugKeys.F3_DEBUG_KEYS_CATEGORY.getKeyBindings()).toList().forEach(iNotEKKeybindings -> {
                            for (INotEKKeybinding keybinding : iNotEKKeybindings) {
                                if (keybinding.getTranslationKey().equals(translation)) {
                                    keybinding.setBoundKey(InputUtil.fromTranslationKey(value));
                                    break;
                                }
                            }
                        });
                    }
                }
            }
        });

        NotEnoughKeybinds.LOGGER.info("Preset " + preset.name + " was loaded");
    }

    public static void createNewPreset(KeybindPreset preset) {
        try {
            PRESETS_DIR.mkdirs();
            File presetFile = new File(PRESETS_DIR.getPath() + File.separator + "preset" + (Objects.requireNonNull(PRESETS_DIR.listFiles()).length + 1) + ".txt");
            presetFile.createNewFile();

            writePreset(preset, presetFile);

            NotEnoughKeybinds.LOGGER.info("Preset " + preset.name + " was created");
        } catch (IOException e) {
            NotEnoughKeybinds.LOGGER.error("Could not create preset file!");
        }
    }

    public static void writePreset(KeybindPreset preset, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write("version:" + CURRENT_VERSION);
            writer.newLine();
            writer.write("name:" + preset.name);
            writer.newLine();
            writer.write("description:" + preset.description);
            writer.newLine();

            preset.content.forEach(line -> {
                try {
                    writer.write(line);
                    writer.newLine();
                } catch (IOException e) {
                    NotEnoughKeybinds.LOGGER.error("Could not write " + line + " into " + file.getAbsolutePath());
                }
            });

            writer.close();
        } catch (IOException e) {
            NotEnoughKeybinds.LOGGER.error("Could not create file writer of file " + file.getAbsolutePath());
        }
    }

    public static List<KeybindPreset> getPresets() {
        return presets.stream().toList();
    }

    public static String getCurrentPresetName() {
        return "none0000000000000";
    }


    public record KeybindPreset(String fileName, float version, String name, String description, List<String> content) {
    }
}
