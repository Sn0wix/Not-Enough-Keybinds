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
    public BasicLayoutWidget basicLayout;
    public ThreePartsLayoutWidget threePartsLayout;

    public SettingsScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    public SettingsScreen(Screen parent, Text title, int headerHeight, int footerHeight) {
        this(parent, title);
        initThreePartsLayout();
        this.threePartsLayout.setHeaderHeight(headerHeight);
        this.threePartsLayout.setFooterHeight(footerHeight);
    }

    public void initBasicLayout() {
        basicLayout = new BasicLayoutWidget(this);
        basicLayout.getMainPositioner().relative(0.5F, 0.5F);
    }

    public void initThreePartsLayout() {
        threePartsLayout = new ThreePartsLayoutWidget(this);
    }

    @Override
    protected void init() {
        if (threePartsLayout != null) {
            this.initHeader();
            this.initBody();
            this.initFooter();
            this.threePartsLayout.forEachChild(this::addDrawableChild);
        } else {
            this.basicLayout.forEachChild(this::addDrawableChild);
        }

        this.initTabNavigation();
    }

    public void initBody() {
    }

    public void initFooter() {
        this.threePartsLayout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).width(200).build());
    }

    public void initHeader() {
        this.threePartsLayout.addHeader(this.title, this.textRenderer);
    }


    @Override
    protected void initTabNavigation() {
        if (threePartsLayout != null) {
            this.threePartsLayout.refreshPositions();
        } else {
            this.basicLayout.refreshPositions();
        }
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
