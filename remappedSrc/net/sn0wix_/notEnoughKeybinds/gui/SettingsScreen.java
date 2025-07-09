package net.sn0wix_.notEnoughKeybinds.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.sn0wix_.notEnoughKeybinds.gui.screen.BasicLayoutWidget;

@Environment(EnvType.CLIENT)
public abstract class SettingsScreen extends Screen {
    protected final Screen parent;
    public BasicLayoutWidget threePartsLayout = new BasicLayoutWidget(this);

    public SettingsScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    public SettingsScreen(Screen parent, Text title, int headerHeight, int footerHeight) {
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
        this.threePartsLayout.forEachChild(this::addDrawableChild);

        this.refreshWidgetPositions();
    }

    protected void initBody() {}

    public void initFooter() {
        this.threePartsLayout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).width(200).build());
    }

    public void initHeader() {
        this.threePartsLayout.addHeader(this.title, this.textRenderer);
    }


    @Override
    public void refreshWidgetPositions() {
        this.threePartsLayout.refreshPositions();
    }

    @Override
    public void removed() {
        assert this.client != null;
        this.client.options.write();
    }

    @Override
    public void close() {
        saveOptions();

        assert this.client != null;
        this.client.setScreen(this.parent);
    }

    public void saveOptions() {
    }
}
