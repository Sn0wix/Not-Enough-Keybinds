package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.lwjgl.glfw.GLFW;

public class PresetEditScreen extends SettingsScreen {
    public static final Component NAME_TEXT = Component.translatable(TextUtils.getTranslationKey("name")).withStyle(ChatFormatting.GRAY);
    public static final Component DESCRIPTION_TEXT = Component.translatable(TextUtils.getTranslationKey("description")).withStyle(ChatFormatting.GRAY);


    public final PresetLoader.KeybindPreset preset;
    public EditBox nameWidget;
    public EditBox descriptionWidget;
    public Button doneButton;

    public String name;
    public String description;
    public boolean newPreset;

    public LinearLayout body = LinearLayout.vertical().spacing(20);


    public PresetEditScreen(Screen parent, PresetLoader.KeybindPreset preset, boolean newPreset) {
        super(parent, Component.translatable(TextUtils.getSettingsTranslationKey(newPreset ? "preset.new" : "preset")));
        this.preset = preset;
        this.name = preset.getName();
        this.description = preset.getDescription();
        this.newPreset = newPreset;
    }

    @Override
    protected void initBody() {
        initButtons();
        threePartsLayout.addToContents(body);
    }

    @Override
    public void initFooter() {
        LinearLayout directionalLayoutWidget = this.threePartsLayout.addToFooter(LinearLayout.horizontal().spacing(8));
        directionalLayoutWidget.addChild(Button.builder(CommonComponents.GUI_BACK, button -> minecraft.setScreen(parent)
        ).build());

        directionalLayoutWidget.addChild(doneButton);
    }

    public void initButtons() {
        doneButton = Button.builder(CommonComponents.GUI_DONE, button -> {
            preset.setName(nameWidget.getValue());
            preset.setDescription(descriptionWidget.getValue());
            PresetLoader.writePreset(preset, preset.getContent().isEmpty() ? Utils.bindingsToList(true) : preset.getContent());
            PresetLoader.reload(false);

            if (parent instanceof PresetsSettingScreen presetsSettingScreen) {
                presetsSettingScreen.rebuildWidgets();
            }

            Utils.showToastNotification(TextUtils.getText("preset." + (newPreset ? "create" : "edit") + ".toast", preset.getName()));

            assert minecraft != null;
            minecraft.setScreen(parent);
        }).build();

        LinearLayout top = LinearLayout.vertical().spacing(2);
        LinearLayout bottom = LinearLayout.vertical().spacing(2);

        top.addChild(new StringWidget(200, 20, NAME_TEXT, font));
        nameWidget = new EditBox(font, 310, 20, NAME_TEXT);
        nameWidget.setResponder(s -> updateChildren());
        nameWidget.setMaxLength(Integer.MAX_VALUE);
        nameWidget.setValue(name);
        top.addChild(nameWidget);


        bottom.addChild(new StringWidget(200, 20, DESCRIPTION_TEXT, font));
        descriptionWidget = new EditBox(font, 310, 20, DESCRIPTION_TEXT);
        descriptionWidget.setMaxLength(Integer.MAX_VALUE);
        descriptionWidget.setValue(description);
        bottom.addChild(descriptionWidget);

        body.addChild(top);
        body.addChild(bottom);

        setInitialFocus(nameWidget);
        updateChildren();
    }

    public void updateChildren() {
        try {
            description = descriptionWidget.getValue();
            name = nameWidget.getValue();
            doneButton.active = !nameWidget.getValue().isEmpty();
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        if (getFocused() == null || getFocused() == descriptionWidget || getFocused() == nameWidget) {
            if (input.input() == GLFW.GLFW_KEY_DELETE) {
                minecraft.setScreen(parent);
                return true;
            }

            if (input.input() == GLFW.GLFW_KEY_ENTER) {
                doneButton.onPress(input);
                return true;
            }
        }

        return super.keyPressed(input);
    }
}
