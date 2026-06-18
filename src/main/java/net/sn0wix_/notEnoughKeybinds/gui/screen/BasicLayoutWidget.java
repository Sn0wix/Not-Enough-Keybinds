package net.sn0wix_.notEnoughKeybinds.gui.screen;

import java.util.function.Consumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.*;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class BasicLayoutWidget extends HeaderAndFooterLayout {
    public static final int DEFAULT_HEADER_FOOTER_HEIGHT = 33;
    private int BODY_MARGIN_TOP = 5;
    private final FrameLayout header = new FrameLayout();
    private final FrameLayout footer = new FrameLayout();
    private final FrameLayout body = new FrameLayout();
    private final Screen screen;
    private int headerHeight;
    private int footerHeight;

    public BasicLayoutWidget(int bodyMarginTop, Screen screen) {
        this(screen);
        BODY_MARGIN_TOP = bodyMarginTop;
    }

    public BasicLayoutWidget(Screen screen) {
        this(screen, DEFAULT_HEADER_AND_FOOTER_HEIGHT);
    }

    public BasicLayoutWidget(Screen screen, int headerFooterHeight) {
        this(screen, headerFooterHeight, headerFooterHeight);
    }

    public BasicLayoutWidget(Screen screen, int headerHeight, int footerHeight) {
        super(screen, headerHeight, footerHeight);
        this.screen = screen;
        this.headerHeight = headerHeight;
        this.footerHeight = footerHeight;
        this.header.defaultChildLayoutSetting().align(0.5F, 0.5F);
        this.footer.defaultChildLayoutSetting().align(0.5F, 0.5F);
    }

    @Override
    public int getWidth() {
        return this.screen.width;
    }

    @Override
    public int getHeight() {
        return this.screen.height;
    }

    public int getFooterHeight() {
        return this.footerHeight;
    }

    public void setFooterHeight(int footerHeight) {
        this.footerHeight = footerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public int getHeaderHeight() {
        return this.headerHeight;
    }

    public int getContentHeight() {
        return this.screen.height - this.getHeaderHeight() - this.getFooterHeight();
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumer) {
        this.header.visitChildren(consumer);
        this.body.visitChildren(consumer);
        this.footer.visitChildren(consumer);
    }

    @Override
    public void arrangeElements() {
        int i = this.getHeaderHeight();
        int j = this.getFooterHeight();
        this.header.setMinWidth(this.screen.width);
        this.header.setMinHeight(i);
        this.header.setPosition(0, 0);
        this.header.arrangeElements();
        this.footer.setMinWidth(this.screen.width);
        this.footer.setMinHeight(j);
        this.footer.arrangeElements();
        this.footer.setY(this.screen.height - j);
        this.body.setMinWidth(this.screen.width);
        this.body.arrangeElements();
        int k = i + BODY_MARGIN_TOP;
        int l = this.screen.height - j - this.body.getHeight();
        this.body.setPosition(0, Math.min(k, l));
    }

    public <T extends LayoutElement> T addToHeader(T widget) {
        return this.header.addChild(widget);
    }

    public <T extends LayoutElement> T addToHeader(T widget, Consumer<LayoutSettings> callback) {
        return this.header.addChild(widget, callback);
    }

    public void addTitleHeader(Component text, Font textRenderer) {
        this.header.addChild(new StringWidget(text, textRenderer));
    }

    public <T extends LayoutElement> T addToFooter(T widget) {
        return this.footer.addChild(widget);
    }

    public <T extends LayoutElement> T addToFooter(T widget, Consumer<LayoutSettings> callback) {
        return this.footer.addChild(widget, callback);
    }

    public <T extends LayoutElement> T addToContents(T widget) {
        return this.body.addChild(widget);
    }

    public <T extends LayoutElement> T addToContents(T widget, Consumer<LayoutSettings> callback) {
        return this.body.addChild(widget, callback);
    }
}
