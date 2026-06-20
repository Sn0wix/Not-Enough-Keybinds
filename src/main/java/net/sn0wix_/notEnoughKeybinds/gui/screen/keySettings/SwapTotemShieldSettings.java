package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.config.SwapTotemShieldConfig;
import net.sn0wix_.notEnoughKeybinds.gui.IntFieldWidget;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.gui.screen.INotEKLayoutTemplate;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class SwapTotemShieldSettings extends SettingsScreen implements INotEKLayoutTemplate {
    public Button swapFirstButton;
    public Button shieldAlgorithmButton;
    public Button swapSecondButton;
    public IntFieldWidget mendingPointsWidget;

    public LinearLayout leftWidget = getColumnWidget();
    public LinearLayout rightWidget = getColumnWidget();

    public SwapTotemShieldSettings(Screen parent) {
        super(parent, Component.translatable(TextUtils.getSettingsTranslationKey("swap_totem_shield")));
    }

    @Override
    protected void initBody() {
        initButtons();
        initBodyWidget(leftWidget, rightWidget, threePartsLayout);
    }

    public void initButtons() {
        swapFirstButton = Button.builder(Component.empty(), button -> {
            NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.cycleSwapFirst();
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("swap_first.offhand")).build();
        leftWidget.addChild(swapFirstButton);

        shieldAlgorithmButton = Button.builder(Component.empty(), button -> {
                    NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield = !NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield;
                    updateButtons();
                })
                .size(150, 20).tooltip(TextUtils.getTooltip("pick_shield")).build();
        rightWidget.addChild(shieldAlgorithmButton);

        swapSecondButton = Button.builder(Component.empty(), button -> {
            NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapSecond = !NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapSecond;
            updateButtons();
        }).size(150, 20).build();
        leftWidget.addChild(swapSecondButton);

        mendingPointsWidget = new IntFieldWidget(font, 150, 20, Component.literal("mending_value"));
        mendingPointsWidget.setValue(String.valueOf(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints));
        mendingPointsWidget.setResponder(s -> {
            try {
                NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints = Integer.parseInt(mendingPointsWidget.getValue());
            } catch (NumberFormatException ignored) {
            }


            updateButtons();
        });
        rightWidget.addChild(mendingPointsWidget);


        updateButtons();
    }

    public void updateButtons() {
        swapFirstButton.setMessage(TextUtils.getCombinedTranslation(TextUtils.getText("swap_first"),
                Component.translatable(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getSwapTranslationKey())));


        swapSecondButton.active = !NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapFirst.equals("off");

        swapSecondButton.setMessage(TextUtils.getCombinedTranslation(TextUtils.getText("swap_second"),
                Component.translatable(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapSecond ? TextUtils.getTranslationKey("on") : TextUtils.getTranslationKey("off"))));
        swapSecondButton.setTooltip(Tooltip.create(swapSecondButton.active ? Component.translatable(TextUtils.getTranslationKey("swap_second", true),
                Language.getInstance().getOrDefault(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getSwapTranslationKey()),
                Language.getInstance().getOrDefault(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getSwapTranslationKey(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.getOppositeSwap()))) : Component.empty()));

        shieldAlgorithmButton.setMessage(Component.translatable(TextUtils.getTranslationKey("pick"),
                Language.getInstance().getOrDefault(TextUtils.getTranslationKey(NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield ? "best" : "first_found")),
                Language.getInstance().getOrDefault(Items.SHIELD.getDescriptionId())));
        shieldAlgorithmButton.setTooltip(Tooltip.create(Component.translatable(TextUtils.getTranslationKey("choose", true), Language.getInstance().getOrDefault(Items.SHIELD.getDescriptionId()))));

        mendingPointsWidget.visible = NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.chooseBestShield;
        mendingPointsWidget.setTooltip(Tooltip.create(Component.translatable(TextUtils.getTranslationKey("mending_points", true), NotEnoughKeybinds.TOTEM_SHIELD_CONFIG.swapMendingPoints)));
    }

    @Override
    public void saveOptions() {
        SwapTotemShieldConfig.saveConfig();
    }
}
