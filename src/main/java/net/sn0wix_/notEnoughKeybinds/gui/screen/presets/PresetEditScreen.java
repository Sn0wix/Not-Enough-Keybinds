package net.sn0wix_.notEnoughKeybinds.gui.screen.presets;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.keybinds.presets.PresetLoader;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.lwjgl.glfw.GLFW;

public class PresetEditScreen extends SettingsScreen {
    public static final Text NAME_TEXT = Text.translatable(TextUtils.getTranslationKey("name")).formatted(Formatting.GRAY);
    public static final Text DESCRIPTION_TEXT = Text.translatable(TextUtils.getTranslationKey("description")).formatted(Formatting.GRAY);


    public final PresetLoader.KeybindPreset preset;
    public TextFieldWidget nameWidget;
    public TextFieldWidget descriptionWidget;
    public ButtonWidget doneButton;

    public String name;
    public String description;
    public boolean newPreset;

    public DirectionalLayoutWidget body = DirectionalLayoutWidget.vertical().spacing(20);


    public PresetEditScreen(Screen parent, PresetLoader.KeybindPreset preset, boolean newPreset) {
        super(parent, Text.translatable(TextUtils.getSettingsTranslationKey(newPreset ? "preset.new" : "preset")));
        this.preset = preset;
        this.name = preset.getName();
        this.description = preset.getDescription();
        this.newPreset = newPreset;
    }

    @Override
    protected void initBody() {
        initButtons();
        threePartsLayout.addBody(body);
    }

    @Override
    public void initFooter() {
        DirectionalLayoutWidget directionalLayoutWidget = this.threePartsLayout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        directionalLayoutWidget.add(ButtonWidget.builder(ScreenTexts.BACK, button -> client.setScreen(parent)
        ).build());

        directionalLayoutWidget.add(doneButton);
    }

    public void initButtons() {
        doneButton = ButtonWidget.builder(ScreenTexts.DONE, button -> {
            preset.setName(nameWidget.getText());
            preset.setDescription(descriptionWidget.getText());
            PresetLoader.writePreset(preset, preset.getContent().isEmpty() ? Utils.bindingsToList(true) : preset.getContent());
            PresetLoader.reload(false);

            if (parent instanceof PresetsSettingScreen presetsSettingScreen) {
                presetsSettingScreen.clearAndInit();
            }

            Utils.showToastNotification(TextUtils.getText("preset." + (newPreset ? "create" : "edit") + ".toast", preset.getName()));

            assert client != null;
            client.setScreen(parent);
        }).build();

        DirectionalLayoutWidget top = DirectionalLayoutWidget.vertical().spacing(2);
        DirectionalLayoutWidget bottom = DirectionalLayoutWidget.vertical().spacing(2);

        top.add(new TextWidget(200, 20, NAME_TEXT, textRenderer));
        nameWidget = new TextFieldWidget(textRenderer, 310, 20, NAME_TEXT);
        nameWidget.setChangedListener(s -> updateChildren());
        nameWidget.setMaxLength(Integer.MAX_VALUE);
        nameWidget.setText(name);
        top.add(nameWidget);


        bottom.add(new TextWidget(200, 20, DESCRIPTION_TEXT, textRenderer));
        descriptionWidget = new TextFieldWidget(textRenderer, 310, 20, DESCRIPTION_TEXT);
        descriptionWidget.setMaxLength(Integer.MAX_VALUE);
        descriptionWidget.setText(description);
        bottom.add(descriptionWidget);

        body.add(top);
        body.add(bottom);

        setInitialFocus(nameWidget);
        updateChildren();
    }

    public void updateChildren() {
        try {
            description = descriptionWidget.getText();
            name = nameWidget.getText();
            doneButton.active = !nameWidget.getText().isEmpty();
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (getFocused() == null || getFocused() == descriptionWidget || getFocused() == nameWidget) {
            if (input.getKeycode() == GLFW.GLFW_KEY_DELETE) {
                client.setScreen(parent);
                return true;
            }

            if (input.getKeycode() == GLFW.GLFW_KEY_ENTER) {
                doneButton.onPress(input);
                return true;
            }
        }

        return super.keyPressed(input);
    }
}
