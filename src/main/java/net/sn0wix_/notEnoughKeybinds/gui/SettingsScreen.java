package net.sn0wix_.notEnoughKeybinds.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class SettingsScreen extends Screen {
    protected final Screen parent;
    @Nullable
    protected OptionListWidget body;
    public ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);

    public SettingsScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    public SettingsScreen(Screen parent, Text title, int headerFooterHeight) {
        this(parent, title);
        layout = new ThreePartsLayoutWidget(this, headerFooterHeight);
    }

    public SettingsScreen(Screen parent, Text title, int headerHeight, int footerHeight) {
        this(parent, title);
        layout = new ThreePartsLayoutWidget(this, headerHeight, footerHeight);
    }

    @Override
    protected void init() {
        this.initHeader();
        this.initBody();
        this.initFooter();
        this.layout.forEachChild(this::addDrawableChild);
        this.initTabNavigation();
    }

    protected void initHeader() {
        this.layout.addHeader(this.title, this.textRenderer);
    }

    protected void initBody() {

    }

    protected void initFooter() {
        this.layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).width(200).build());
    }

    @Override
    protected void initTabNavigation() {
        this.layout.refreshPositions();
        if (this.body != null) {
            this.body.position(this.width, this.layout);
        }
    }

    @Override
    public void removed() {
        this.client.options.write();
    }

    @Override
    public void close() {
        if (this.body != null) {
            this.body.applyAllPendingValues();
        }
        saveOptions();

        this.client.setScreen(this.parent);
    }

    public void saveOptions() {
    }
}
