package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.config.SwapTotemShieldConfig;
import net.sn0wix_.notEnoughKeybinds.gui.IntFieldWidget;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class SwapTotemShieldSettings extends SettingsScreen {
    public ButtonWidget swapFirstButton;
    public ButtonWidget shieldAlgorithmButton;
    public ButtonWidget swapSecondButton;
    public IntFieldWidget mendingPointsWidget;

    public SwapTotemShieldSettings(Screen parent) {
        super(parent, Text.translatable(TextUtils.getSettingsTranslationKey("swap_totem_shield")));
    }

    /*@Override
    public void init(int x, int x2, int y, TextRenderer textRenderer) {
        addDoneButtonFooter();

        swapFirstButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.cycleSwapFirst();
            updateButtons();
        }).dimensions(x, y, 150, 20).tooltip(TextUtils.getTooltip("swap_first.offhand")).build();
        addDrawableChild(swapFirstButton);

        shieldAlgorithmButton = ButtonWidget.builder(Text.empty(), button -> {
                    NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield = !NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield;
                    updateButtons();
                })
                .dimensions(x2, y, 150, 20).tooltip(TextUtils.getTooltip("pick_shield")).build();
        addDrawableChild(shieldAlgorithmButton);

        y += 30;

        swapSecondButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapSecond = !NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapSecond;
            updateButtons();
        }).dimensions(x, y, 150, 20).build();
        addDrawableChild(swapSecondButton);

        mendingPointsWidget = new IntFieldWidget(textRenderer, x2, y, 150, 20, Text.literal("mending_value"));
        mendingPointsWidget.setText(String.valueOf(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints));
        mendingPointsWidget.setChangedListener(s -> {
            try {
                NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints = Integer.parseInt(mendingPointsWidget.getText());
            } catch (NumberFormatException ignored) {
            }


            updateButtons();
        });
        addDrawableChild(mendingPointsWidget);


        updateButtons();
    }

    public void updateButtons() {
        swapFirstButton.setMessage(TextUtils.getCombinedTranslation(TextUtils.getText("swap_first"),
                Text.translatable(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getSwapTranslationKey())));


        swapSecondButton.active = !NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapFirst.equals("off");

        swapSecondButton.setMessage(TextUtils.getCombinedTranslation(TextUtils.getText("swap_second"),
                Text.translatable(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapSecond ? TextUtils.getTranslationKey("on") : TextUtils.getTranslationKey("off"))));
        swapSecondButton.setTooltip(Tooltip.of(swapSecondButton.active ? Text.translatable(TextUtils.getTranslationKey("swap_second", true),
                Language.getInstance().get(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getSwapTranslationKey()),
                Language.getInstance().get(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getSwapTranslationKey(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getOppositeSwap()))) : Text.empty()));

        shieldAlgorithmButton.setMessage(Text.translatable(TextUtils.getTranslationKey("pick"),
                Language.getInstance().get(TextUtils.getTranslationKey(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield ? "best" : "first_found")),
                Language.getInstance().get(Items.SHIELD.getTranslationKey())));
        shieldAlgorithmButton.setTooltip(Tooltip.of(Text.translatable(TextUtils.getTranslationKey("choose", true), Language.getInstance().get(Items.SHIELD.getTranslationKey()))));

        mendingPointsWidget.visible = NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield;
        mendingPointsWidget.setTooltip(Tooltip.of(Text.translatable(TextUtils.getTranslationKey("mending_points", true), NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints)));
    }

    @Override
    public void saveOptions() {
        SwapTotemShieldConfig.saveConfig();
    }*/
}
