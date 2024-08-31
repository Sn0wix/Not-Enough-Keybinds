package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.config.EquipElytraConfig;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class EquipElytraSettings extends SettingsScreen {
    //swap first
    //swap second
    //choose elytra
    //choose chestplate
    //accept curse of vanishing
    //accept curse of binding
    //auto fly

    //longest/shortest duration
    //can explode
    //firework swap slot
    //use fireworks - auto fly
    //swap back old item
    public ButtonWidget swapFirstButton;
    public ButtonWidget swapSecondButton;
    public ButtonWidget chooseElytraButton;
    public ButtonWidget chooseChestplateButton;
    public ButtonWidget acceptCurseOfVanishingButton;
    public ButtonWidget acceptCurseOfBindingButton;
    public ButtonWidget autoflyButton;

    public ButtonWidget fireworkDurationButton;
    public ButtonWidget canExplodeButton;
    public ButtonWidget fireworkSwapSlotButton;
    public ButtonWidget useFireworksButton;
    public ButtonWidget swapBackOldItemButton;


    public EquipElytraSettings(Screen parent) {
        super(parent, Text.translatable(TextUtils.getSettingsTranslationKey("equip_elytra")));
    }

    @Override
    public void init(int x, int x2, int y, TextRenderer textRenderer) {
        addDoneButton();
        addDrawableChild(new TextWidget(x, y - 20, 200, 20, TextUtils.getText("elytra_and_chestplate").copy().formatted(Formatting.GRAY), textRenderer).alignLeft());
        addDrawableChild(new TextWidget(x2, y - 20, 200, 20, TextUtils.getText("fireworks").copy().formatted(Formatting.GRAY), textRenderer).alignLeft());

        swapFirstButton = ButtonWidget.builder(Text.empty(), button -> {

        }).dimensions(x, y, 150, 20).tooltip(Tooltip.of(TextUtils.getText("swap_first.elytra", true))).build();
        addDrawableChild(swapFirstButton);

        updateButtons();
    }

    public void updateButtons() {
        //swapFirstButton.setMessage(TextUtils.getCombinedTranslation());
    }

    @Override
    public void saveOptions() {
        EquipElytraConfig.saveConfig();
    }
}
