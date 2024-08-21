package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.config.CommonConfig;
import net.sn0wix_.notEnoughKeybinds.gui.IntFieldWidget;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class SwapTotemShieldSettings extends SettingsScreen {
    //what to swap first if holding none
    //mending slider
    //swap second
    //choose best shield/first found one

    public ButtonWidget swapFirstButton;
    public ButtonWidget shieldAlgorithmButton;
    public ButtonWidget swapSecondButton;
    public IntFieldWidget mendingPointsWidget;

    public SwapTotemShieldSettings(Screen parent, GameOptions gameOptions) {
        super(parent, gameOptions, Text.translatable(TextUtils.getSettingsTranslation("swap_totem_shield")));
    }

    @Override
    public void init(int x, int x2, int y, TextRenderer textRenderer) {
        addDoneButton();

        swapFirstButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.COMMON_CONFIG.cycleSwapFirst();
            updateButtons();
        }).dimensions(x, y, 150, 20).tooltip(Tooltip.of(Text.translatable(TextUtils.getTextTranslation("swap_first", true)))).build();
        addDrawableChild(swapFirstButton);

        shieldAlgorithmButton = ButtonWidget.builder(Text.empty(), button -> {
                    NotEnoughKeybinds.COMMON_CONFIG.chooseBestShield = !NotEnoughKeybinds.COMMON_CONFIG.chooseBestShield;
                    updateButtons();
                })
                .dimensions(x2, y, 150, 20).tooltip(Tooltip.of(Text.translatable(TextUtils.getTextTranslation("pick_shield", true)))).build();
        addDrawableChild(shieldAlgorithmButton);

        y += 30;

        swapSecondButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.COMMON_CONFIG.swapSecond = !NotEnoughKeybinds.COMMON_CONFIG.swapSecond;
            updateButtons();
        }).dimensions(x, y, 150, 20).build();
        addDrawableChild(swapSecondButton);

        mendingPointsWidget = new IntFieldWidget(textRenderer, x2, y, 150, 20, Text.literal("mending_value"));
        mendingPointsWidget.setText(String.valueOf(NotEnoughKeybinds.COMMON_CONFIG.swapMendingPoints));
        mendingPointsWidget.setChangedListener(s -> {
            try {
                NotEnoughKeybinds.COMMON_CONFIG.swapMendingPoints = Integer.parseInt(mendingPointsWidget.getText());
            } catch (NumberFormatException ignored) {
            }


            updateButtons();
        });
        addDrawableChild(mendingPointsWidget);


        updateButtons();
    }

    public void updateButtons() {
        swapFirstButton.setMessage(TextUtils.getCombinedTranslation(Text.translatable(TextUtils.getTextTranslation("swap_first")),
                Text.translatable(NotEnoughKeybinds.COMMON_CONFIG.getSwapTranslationKey())));


        swapSecondButton.active = !NotEnoughKeybinds.COMMON_CONFIG.swapFirst.equals("off");

        swapSecondButton.setMessage(TextUtils.getCombinedTranslation(Text.translatable(TextUtils.getTextTranslation("swap_second")),
                Text.translatable(NotEnoughKeybinds.COMMON_CONFIG.swapSecond ? TextUtils.getTextTranslation("on") : TextUtils.getTextTranslation("off"))));
        swapSecondButton.setTooltip(Tooltip.of(swapSecondButton.active ? Text.translatable(TextUtils.getTextTranslation("swap_second", true),
                Language.getInstance().get(NotEnoughKeybinds.COMMON_CONFIG.getSwapTranslationKey()),
                Language.getInstance().get(NotEnoughKeybinds.COMMON_CONFIG.getSwapTranslationKey(NotEnoughKeybinds.COMMON_CONFIG.getOppositeSwap()))) : Text.empty()));

        shieldAlgorithmButton.setMessage(Text.translatable(TextUtils.getTextTranslation("pick_shield"),
                Language.getInstance().get(TextUtils.getTextTranslation(NotEnoughKeybinds.COMMON_CONFIG.chooseBestShield ? "best" : "first_found"))));
        shieldAlgorithmButton.setTooltip(Tooltip.of(Text.translatable(TextUtils.getTextTranslation("pick_shield", true))));

        mendingPointsWidget.visible = NotEnoughKeybinds.COMMON_CONFIG.chooseBestShield;
        mendingPointsWidget.setTooltip(Tooltip.of(Text.translatable(TextUtils.getTextTranslation("mending_points", true), NotEnoughKeybinds.COMMON_CONFIG.swapMendingPoints)));
    }

    @Override
    public void close() {
        CommonConfig.saveConfig();
        super.close();
    }
}
