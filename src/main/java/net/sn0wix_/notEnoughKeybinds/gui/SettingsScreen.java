package net.sn0wix_.notEnoughKeybinds.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.sn0wix_.notEnoughKeybinds.gui.screen.BasicLayoutWidget;

@Environment(EnvType.CLIENT)
public abstract class SettingsScreen extends Screen {
    protected final Screen parent;
    public BasicLayoutWidget threePartsLayout = new BasicLayoutWidget(this);

    public SettingsScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
    }

    public SettingsScreen(Screen parent, Component title, int headerHeight, int footerHeight) {
        this(parent, title);
        this.threePartsLayout.setHeaderHeight(headerHeight);
        this.threePartsLayout.setFooterHeight(footerHeight);
    }

    @Override
    protected void init() {
        int header = threePartsLayout.getHeaderHeight();
        int footer = threePartsLayout.getFooterHeight();

        threePartsLayout = new BasicLayoutWidget(this);
        threePartsLayout.setHeaderHeight(header);
        threePartsLayout.setFooterHeight(footer);

        this.initHeader();
        this.initBody();
        this.initFooter();
        this.threePartsLayout.visitWidgets(this::addRenderableWidget);

        this.repositionElements();
    }

    protected void initBody() {}

    public void initFooter() {
        this.threePartsLayout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
    }

    public void initHeader() {
        this.threePartsLayout.addTitleHeader(this.title, this.font);
    }


    @Override
    public void repositionElements() {
        this.threePartsLayout.arrangeElements();
    }

    @Override
    public void removed() {
        assert this.minecraft != null;
        this.minecraft.options.save();
    }

    @Override
    public void onClose() {
        saveOptions();

        assert this.minecraft != null;
        this.minecraft.setScreen(this.parent);
    }

    public void saveOptions() {
    }
}
