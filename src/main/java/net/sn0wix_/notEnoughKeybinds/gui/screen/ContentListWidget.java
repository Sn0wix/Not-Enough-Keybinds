package net.sn0wix_.notEnoughKeybinds.gui.screen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ContentListWidget extends ElementListWidget<ContentListWidget.WidgetEntry> {
    private static final int rowWidth = 310;
    private static final int field_49482 = 25;
    private final GameOptionsScreen optionsScreen;

    public ContentListWidget(MinecraftClient client, int width, GameOptionsScreen optionsScreen) {
        super(client, width, optionsScreen.layout.getContentHeight(), optionsScreen.layout.getHeaderHeight(), 25);
        this.centerListVertically = false;
        this.optionsScreen = optionsScreen;
    }

    public void addSingleOptionEntry(SimpleOption<?> option) {
        this.addEntry(ContentListWidget.OptionWidgetEntry.create(this.client.options, option, this.optionsScreen));
    }

    public void addAll(SimpleOption<?>... options) {
        for (int i = 0; i < options.length; i += 2) {
            SimpleOption<?> simpleOption = i < options.length - 1 ? options[i + 1] : null;
            this.addEntry(ContentListWidget.OptionWidgetEntry.create(this.client.options, options[i], simpleOption, this.optionsScreen));
        }
    }

    public void addAll(List<ClickableWidget> widgets) {
        for (int i = 0; i < widgets.size(); i += 2) {
            this.addWidgetEntry(widgets.get(i), i < widgets.size() - 1 ? widgets.get(i + 1) : null);
        }
    }

    public void addWidgetEntry(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget) {
        this.addEntry(ContentListWidget.WidgetEntry.create(firstWidget, secondWidget, this.optionsScreen));
    }

    @Override
    public int getRowWidth() {
        return rowWidth;
    }

    public void applyAllPendingValues() {
        for (ContentListWidget.WidgetEntry widgetEntry : this.children()) {
            if (widgetEntry instanceof ContentListWidget.OptionWidgetEntry) {
                ContentListWidget.OptionWidgetEntry optionWidgetEntry = (ContentListWidget.OptionWidgetEntry)widgetEntry;

                for (ClickableWidget clickableWidget : optionWidgetEntry.optionWidgets.values()) {
                    if (clickableWidget instanceof SimpleOption.OptionSliderWidgetImpl<?> optionSliderWidgetImpl) {
                        optionSliderWidgetImpl.applyPendingValue();
                    }
                }
            }
        }
    }

    public Optional<Element> getHoveredWidget(double mouseX, double mouseY) {
        for (ContentListWidget.WidgetEntry widgetEntry : this.children()) {
            for (Element element : widgetEntry.children()) {
                if (element.isMouseOver(mouseX, mouseY)) {
                    return Optional.of(element);
                }
            }
        }

        return Optional.empty();
    }

    @Environment(EnvType.CLIENT)
    protected static class OptionWidgetEntry extends ContentListWidget.WidgetEntry {
        final Map<SimpleOption<?>, ClickableWidget> optionWidgets;

        private OptionWidgetEntry(Map<SimpleOption<?>, ClickableWidget> widgets, GameOptionsScreen optionsScreen) {
            super(ImmutableList.copyOf(widgets.values()), optionsScreen);
            this.optionWidgets = widgets;
        }

        public static ContentListWidget.OptionWidgetEntry create(GameOptions gameOptions, SimpleOption<?> option, GameOptionsScreen optionsScreen) {
            return new ContentListWidget.OptionWidgetEntry(ImmutableMap.of(option, option.createWidget(gameOptions, 0, 0, 310)), optionsScreen);
        }

        public static ContentListWidget.OptionWidgetEntry create(
                GameOptions gameOptions, SimpleOption<?> firstOption, @Nullable SimpleOption<?> secondOption, GameOptionsScreen optionsScreen
        ) {
            ClickableWidget clickableWidget = firstOption.createWidget(gameOptions);
            return secondOption == null
                    ? new ContentListWidget.OptionWidgetEntry(ImmutableMap.of(firstOption, clickableWidget), optionsScreen)
                    : new ContentListWidget.OptionWidgetEntry(ImmutableMap.of(firstOption, clickableWidget, secondOption, secondOption.createWidget(gameOptions)), optionsScreen);
        }
    }

    @Environment(EnvType.CLIENT)
    protected static class WidgetEntry extends ElementListWidget.Entry<ContentListWidget.WidgetEntry> {
        private final List<ClickableWidget> widgets;
        private final Screen screen;
        private static final int WIDGET_X_SPACING = 160;

        WidgetEntry(List<ClickableWidget> widgets, Screen screen) {
            this.widgets = ImmutableList.copyOf(widgets);
            this.screen = screen;
        }

        public static ContentListWidget.WidgetEntry create(List<ClickableWidget> widgets, Screen screen) {
            return new ContentListWidget.WidgetEntry(widgets, screen);
        }

        public static ContentListWidget.WidgetEntry create(ClickableWidget firstWidget, @Nullable ClickableWidget secondWidget, Screen screen) {
            return secondWidget == null
                    ? new ContentListWidget.WidgetEntry(ImmutableList.of(firstWidget), screen)
                    : new ContentListWidget.WidgetEntry(ImmutableList.of(firstWidget, secondWidget), screen);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int i = 0;
            int j = this.screen.width / 2 - 155;

            for (ClickableWidget clickableWidget : this.widgets) {
                clickableWidget.setPosition(j + i, y);
                clickableWidget.render(context, mouseX, mouseY, tickDelta);
                i += 160;
            }
        }

        @Override
        public List<? extends Element> children() {
            return this.widgets;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.widgets;
        }
    }
}
