package net.sn0wix_.notEnoughKeybinds.gui.screen.keySettings;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.config.EquipElytraConfig;
import net.sn0wix_.notEnoughKeybinds.gui.SettingsScreen;
import net.sn0wix_.notEnoughKeybinds.gui.screen.INotEKLayoutTemplate;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;

public class EquipElytraSettings extends SettingsScreen implements INotEKLayoutTemplate {
    public ButtonWidget swapFirstButton;
    public ButtonWidget swapSecondButton;
    public ButtonWidget chooseElytraButton;
    public ButtonWidget chooseChestplateButton;
    public ButtonWidget acceptCurseOfVanishingButton;
    public ButtonWidget acceptCurseOfBindingButton;
    public ButtonWidget enterFlightButton;

    public ButtonWidget fireworkDurationButton;
    public ButtonWidget canExplodeButton;
    public ButtonWidget equipSlotButton;
    public ButtonWidget fireworkEquipMode;
    public ButtonWidget swapBackOldItemButton;
    public ButtonWidget useRocket;
    public ButtonWidget selectRocket;

    public DirectionalLayoutWidget leftWidget = getColumnWidget();
    public DirectionalLayoutWidget rightWidget = getColumnWidget();

    public EquipElytraSettings(Screen parent) {
        super(parent, Text.translatable(TextUtils.getSettingsTranslationKey("equip_elytra")));
    }

    @Override
    protected void initBody() {
        initButtons();
        initBodyWidget(leftWidget, rightWidget, threePartsLayout);
    }

    public void initButtons() {
        leftWidget.add(new TextWidget(TextUtils.getText("elytra_and_chestplate").copy().formatted(Formatting.GRAY), textRenderer).alignLeft());
        rightWidget.add(new TextWidget(TextUtils.getText("fireworks").copy().formatted(Formatting.GRAY), textRenderer).alignLeft());

        //Elytra
        swapFirstButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.cycleSwapFirst();
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("swap_first.elytra")).build();
        leftWidget.add(swapFirstButton);

        swapSecondButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond;
            updateButtons();
        }).size(150, 20).build();
        leftWidget.add(swapSecondButton);

        chooseElytraButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestElytra = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestElytra;
            updateButtons();
        }).size(150, 20).tooltip(Tooltip.of(Text.translatable(TextUtils.getTranslationKey("choose", true), Language.getInstance().get(Items.ELYTRA.getTranslationKey())))).build();
        leftWidget.add(chooseElytraButton);

        chooseChestplateButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestChestplate = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestChestplate;
            updateButtons();
        }).size(150, 20).tooltip(Tooltip.of(Text.translatable(TextUtils.getTranslationKey("choose", true), Language.getInstance().get(TextUtils.getTranslationKey("chestplate"))))).build();
        leftWidget.add(chooseChestplateButton);

        acceptCurseOfVanishingButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfVanishing = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfVanishing;
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("accept_enchantment", Language.getInstance().get(Enchantments.VANISHING_CURSE.getRegistry().toTranslationKey()))).build();
        leftWidget.add(acceptCurseOfVanishingButton);

        acceptCurseOfBindingButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfBinding = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfBinding;
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("accept_enchantment", Language.getInstance().get(Enchantments.BINDING_CURSE.getRegistry().toTranslationKey()))).build();
        leftWidget.add(acceptCurseOfBindingButton);

        enterFlightButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode;
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("toggle_flight")).build();
        leftWidget.add(enterFlightButton);

        //Fireworks
        fireworkEquipMode = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.cycleUseMode();
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("firework.equip")).build();
        rightWidget.add(fireworkEquipMode);

        equipSlotButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.cycleSlot();
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("firework.slot")).build();
        rightWidget.add(equipSlotButton);


        DirectionalLayoutWidget widget = DirectionalLayoutWidget.horizontal().spacing(2);

        useRocket = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.useRocket = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.useRocket;
            updateButtons();
        }).size(74, 20).tooltip(TextUtils.getTooltip("firework.use")).build();
        widget.add(useRocket);

        selectRocket = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.selectRocket = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.selectRocket;
            updateButtons();
        }).size(74, 20).tooltip(TextUtils.getTooltip("firework.select")).build();
        widget.add(selectRocket);

        rightWidget.add(widget);

        swapBackOldItemButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapBackOldItem = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapBackOldItem;
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("swap_old")).build();
        rightWidget.add(swapBackOldItemButton);

        fireworkDurationButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.longestDuration = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.longestDuration;
            updateButtons();
        }).size(150, 20).tooltip(TextUtils.getTooltip("firework.duration")).build();
        rightWidget.add(fireworkDurationButton);

        canExplodeButton = ButtonWidget.builder(Text.empty(), button -> {
            NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.canExplode = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.canExplode;
            updateButtons();
        }).size(150, 20).build();
        rightWidget.add(canExplodeButton);

        updateButtons();
    }

    public void updateButtons() {
        swapFirstButton.setMessage(TextUtils.getCombinedTranslation(TextUtils.getText("swap_first"),
                Text.translatable(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.getSwapTranslationKey())));

        swapSecondButton.active = !NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapFirst.equals("off");
        swapSecondButton.setMessage(TextUtils.getCombinedTranslation(TextUtils.getText("swap_second"),
                Text.translatable(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapSecond ? TextUtils.getTranslationKey("on") : TextUtils.getTranslationKey("off"))));
        swapSecondButton.setTooltip(Tooltip.of(swapSecondButton.active ? Text.translatable(TextUtils.getTranslationKey("swap_second", true),
                Language.getInstance().get(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.getSwapTranslationKey()),
                Language.getInstance().get(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.getSwapTranslationKey(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.getOppositeSwap()))) : Text.empty()));

        chooseElytraButton.setMessage(Text.translatable(TextUtils.getTranslationKey("pick"),
                Language.getInstance().get(TextUtils.getTranslationKey(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestElytra ? "best" : "first_found")),
                Language.getInstance().get(Items.ELYTRA.getTranslationKey())));

        chooseChestplateButton.setMessage(Text.translatable(TextUtils.getTranslationKey("pick"),
                Language.getInstance().get(TextUtils.getTranslationKey(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.chooseBestChestplate ? "best" : "first_found")),
                Language.getInstance().get(TextUtils.getTranslationKey("chestplate"))));

        acceptCurseOfBindingButton.setMessage(TextUtils.getText("accept",
                Language.getInstance().get(Enchantments.BINDING_CURSE.getRegistry().toTranslationKey()),
                Language.getInstance().get(TextUtils.getTranslationKey(String.valueOf(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfBinding)))));

        acceptCurseOfVanishingButton.setMessage(TextUtils.getText("accept",
                Language.getInstance().get(Enchantments.VANISHING_CURSE.getRegistry().toTranslationKey()),
                Language.getInstance().get(TextUtils.getTranslationKey(String.valueOf(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.acceptCurseOfVanishing)))));

        enterFlightButton.setMessage(TextUtils.getCombinedTranslation(TextUtils.getText("enter_flight_mode"),
                Text.translatable(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.enterFlightMode ? TextUtils.getTranslationKey("on") : TextUtils.getTranslationKey("off"))));


        fireworkDurationButton.setMessage(Text.translatable(TextUtils.getTranslationKey("firework.duration"),
                Language.getInstance().get(TextUtils.getTranslationKey(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.longestDuration ? "longest" : "shortest"))));

        canExplodeButton.setMessage(TextUtils.getText("firework.can_explode",
                Language.getInstance().get(TextUtils.getTranslationKey(String.valueOf(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.canExplode)))));

        equipSlotButton.setMessage(TextUtils.getText("firework.slot", String.valueOf(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.fireworkSwapSlot + 1)));

        fireworkEquipMode.setMessage(TextUtils.getText("firework.equip",
                Language.getInstance().get(TextUtils.getTranslationKey(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.getUseModeString()))));

        swapBackOldItemButton.setMessage(TextUtils.getText("swap_old",
                Text.translatable(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.swapBackOldItem ? TextUtils.getTranslationKey("on") : TextUtils.getTranslationKey("off"))));

        useRocket.setMessage(TextUtils.getText("firework.use",
                Text.translatable(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.useRocket ? TextUtils.getTranslationKey("on") : TextUtils.getTranslationKey("off"))));

        selectRocket.setMessage(TextUtils.getText("firework.select",
                Text.translatable(NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.selectRocket ? TextUtils.getTranslationKey("on") : TextUtils.getTranslationKey("off"))));

        equipSlotButton.active = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode == 1;
        useRocket.active = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode >= 1 && NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode <= 3;
        selectRocket.active = NotEnoughKeybinds.EQUIP_ELYTRA_CONFIG.equipMode == 1;
        swapBackOldItemButton.active = useRocket.active;
    }

    @Override
    public void saveOptions() {
        EquipElytraConfig.saveConfig();
    }
}
