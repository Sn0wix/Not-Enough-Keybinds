package net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.gui.TexturedButtonWidget;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ControlsListWidget extends ElementListWidget<ControlsListWidget.Entry> {
    public final NotEKSettingsScreen parent;
    private int maxKeyNameLength;

    public ControlsListWidget(NotEKSettingsScreen parent, MinecraftClient client) {
        super(client, parent.width, parent.threePartsLayout.getContentHeight(), parent.threePartsLayout.getHeaderHeight(), 20);
        this.parent = parent;
        initEntries();
    }

    public void initEntries() {
        this.clearEntries();

        NotEKKeyBindings.getCategories().forEach(category -> {
            this.addEntry(new ControlsListWidget.CategoryEntry(Text.translatable(category.getTranslationKey()), category));

            for (INotEKKeybinding keybinding : category.getKeyBindings()) {
                Text text = keybinding.getSettingsDisplayName();

                int textWidth = client.textRenderer.getWidth(text);
                if (textWidth > this.maxKeyNameLength) {
                    this.maxKeyNameLength = textWidth;
                }

                this.addEntry(new ControlsListWidget.KeyBindingEntry(keybinding, text));
            }

            if (category.getAddNewButtonScreen(parent) != null) {
                this.addEntry(new AddNewKeyButtonEntry(category.getAddNewButtonScreen(parent), category.getAddNewButtonTranslation()));
            }
        });
    }

    public void update() {
        KeyBinding.updateKeysByCode();
        this.updateChildren();
    }

    public void updateChildren() {
        this.children().forEach(ControlsListWidget.Entry::update);
    }

    @Override
    public int getRowWidth() {
        return getWidth();
    }

    @Override
    protected int getScrollbarX() {
        return this.getX() + this.width / 2 - 340 / 2 + 340 + 10;
    }

    //Entries
    @Environment(EnvType.CLIENT)
    public class CategoryEntry extends ControlsListWidget.Entry {
        final Text text;
        private final int textWidth;
        private final ButtonWidget resetCategoryButton;

        public CategoryEntry(Text text, KeybindCategory category) {
            this.text = text;
            this.textWidth = ControlsListWidget.this.client.textRenderer.getWidth(this.text);

            resetCategoryButton = ButtonWidget.builder(TextUtils.getText("reset_category"), button ->
                            client.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(parent, client1 -> {
                                for (int i = 0; i < category.getKeyBindings().length; i++) {
                                    category.getKeyBindings()[i].setAndSaveKeyBinding(category.getKeyBindings()[i].getDefaultKey());
                                }
                            }, true), Text.translatable(TextUtils.getTranslationKey("reset_category.confirm"), Language.getInstance().get(category.getTranslationKey())))))
                    .size(85, 16).build();
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            assert ControlsListWidget.this.client.currentScreen != null;
            context.drawText(
                    ControlsListWidget.this.client.textRenderer,
                    this.text,
                    ControlsListWidget.this.width / 2 - this.textWidth / 2,
                    getContentY() + getContentHeight() - 9 - 1,
                    Colors.WHITE,
                    false
            );

            resetCategoryButton.setWidth(client.textRenderer.getWidth(TextUtils.getText("reset_category")) + 6);
            resetCategoryButton.setX((ControlsListWidget.this.width / 2) - maxKeyNameLength - 2);
            resetCategoryButton.setY(getContentY() + 2);
            resetCategoryButton.render(context, mouseX, mouseY, deltaTicks);
        }

        @Nullable
        @Override
        public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
            return null;
        }

        @Override
        public List<? extends Element> children() {
            return List.of(resetCategoryButton);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(new Selectable() {
                @Override
                public Selectable.SelectionType getType() {
                    return Selectable.SelectionType.HOVERED;
                }

                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, CategoryEntry.this.text);
                }
            });
        }

        @Override
        protected void update() {
        }
    }

    @Environment(EnvType.CLIENT)
    public class AddNewKeyButtonEntry extends Entry {
        public final ButtonWidget button;
        public final String translationKey;

        public AddNewKeyButtonEntry(Screen screen, String translationKey) {
            button = ButtonWidget.builder(Text.translatable(translationKey), button1 ->
                    MinecraftClient.getInstance().setScreen(screen)
            ).size(200, 20).build();

            this.translationKey = translationKey;
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int resetButtonPos = ControlsListWidget.this.getScrollbarX() - 50 - 10;
            int editButtonPos = resetButtonPos - 5 - 75;

            int resetCategoryButtonWidth = client.textRenderer.getWidth(TextUtils.getText("reset_category")) + 6;
            int resetCategoryButtonPos = (ControlsListWidget.this.width / 2) - maxKeyNameLength - 2;

            int width = resetButtonPos - resetCategoryButtonWidth - resetCategoryButtonPos - 10;

            button.setDimensionsAndPosition(
                    width,
                    20, resetButtonPos - 5 - width, getContentY() + 2);
            button.render(context, mouseX, mouseY, deltaTicks);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(button);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(new Selectable() {
                @Override
                public SelectionType getType() {
                    return SelectionType.HOVERED;
                }

                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, Text.translatable(translationKey));
                }
            });
        }

        @Override
        void update() {
        }
    }

    @Environment(EnvType.CLIENT)
    public class KeyBindingEntry extends ControlsListWidget.Entry {
        private final INotEKKeybinding binding;
        private final Text bindingName;
        private final ButtonWidget editButton;
        private final ButtonWidget resetButton;
        private final ButtonWidget settingsButton;

        private boolean duplicate = false;

        public KeyBindingEntry(INotEKKeybinding binding, Text bindingName) {
            this.binding = binding;
            this.bindingName = bindingName;
            this.editButton = ButtonWidget.builder(bindingName, button -> {
                        ControlsListWidget.this.parent.selectedKeyBinding = binding;
                        ControlsListWidget.this.update();
                    })
                    .dimensions(0, 0, 75, 20)
                    .narrationSupplier(
                            textSupplier -> binding.isUnbound()
                                    ? Text.translatable("narrator.controls.unbound", bindingName)
                                    : Text.translatable("narrator.controls.bound", bindingName, textSupplier.get())
                    )
                    .build();
            this.resetButton = ButtonWidget.builder(Text.translatable("controls.reset"), button -> {
                binding.setAndSaveKeyBinding(binding.getDefaultKey());
                ControlsListWidget.this.update();
            }).dimensions(0, 0, 50, 20).narrationSupplier(textSupplier -> Text.translatable("narrator.controls.reset", bindingName)).build();
            this.settingsButton = new TexturedButtonWidget(0, 0, 20, 20, Text.empty(), button -> client.setScreen(binding.getSettingsScreen(parent))
                    , Supplier::get, Identifier.of(NotEnoughKeybinds.MOD_ID, "textures/settings.png"), 14, 14, 14, 14);

            this.settingsButton.setTooltip(TextUtils.getTooltip("settings"));
            this.update();
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int resetButtonPos = ControlsListWidget.this.getScrollbarX() - this.resetButton.getWidth() - 10;
            int j = getContentY() - 2;
            int startPos = getWidth() / 2 - maxKeyNameLength;
            this.resetButton.setPosition(resetButtonPos, j);
            this.resetButton.render(context, mouseX, mouseY, deltaTicks);
            int editButtonPos = resetButtonPos - 5 - this.editButton.getWidth();
            this.editButton.setPosition(editButtonPos, j);
            this.editButton.render(context, mouseX, mouseY, deltaTicks);
            context.drawTextWithShadow(ControlsListWidget.this.client.textRenderer, this.bindingName, startPos, getContentY() + getContentHeight() / 2 - 9 / 2, Colors.WHITE);
            if (this.duplicate) {
                int m = this.editButton.getX() - 6;
                context.fill(m, getContentY() - 1, m + 3, getContentY() + getContentHeight(), -65536);
            }

            this.settingsButton.setX(startPos - 25);
            this.settingsButton.setY(getContentY());
            this.settingsButton.render(context, mouseX, mouseY, deltaTicks);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(this.editButton, this.resetButton, this.settingsButton);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(this.editButton, this.resetButton, this.settingsButton);
        }

        @Override
        protected void update() {
            this.editButton.setMessage(this.binding.getBoundKeyLocalizedText());
            this.resetButton.active = !this.binding.isDefault();
            this.duplicate = false;
            MutableText mutableText = Text.empty();
            if (!this.binding.isUnbound()) {
                for (KeyBinding keyBinding : MinecraftClient.getInstance().options.allKeys) {
                    if (keyBinding != this.binding.getBinding() && this.binding.getBinding().equals(keyBinding)) {
                        if (this.duplicate) {
                            mutableText.append(", ");
                        }

                        this.duplicate = true;
                        mutableText.append(Text.translatable(keyBinding.getId()));
                    }
                }
            }

            if (this.duplicate) {
                this.editButton
                        .setMessage(Text.literal("[ ").append(this.editButton.getMessage().copy().formatted(Formatting.WHITE)).append(" ]").formatted(Formatting.RED));
                this.editButton.setTooltip(Tooltip.of(Text.translatable("controls.keybinds.duplicateKeybinds", mutableText)));
            } else {
                this.editButton.setTooltip(binding.getTooltip().getString().isEmpty() ? null : Tooltip.of(binding.getTooltip()));
            }

            if (ControlsListWidget.this.parent.selectedKeyBinding == this.binding) {
                this.editButton
                        .setMessage(
                                Text.literal("> ")
                                        .append(this.editButton.getMessage().copy().formatted(Formatting.WHITE, Formatting.UNDERLINE))
                                        .append(" <")
                                        .formatted(Formatting.YELLOW)
                        );
            }
            this.settingsButton.visible = binding.getSettingsScreen(parent) != null;
        }
    }


    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<ControlsListWidget.Entry> {
        abstract void update();
    }
}
