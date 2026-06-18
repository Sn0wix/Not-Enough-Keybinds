package net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.util.CommonColors;
import net.sn0wix_.notEnoughKeybinds.NotEnoughKeybinds;
import net.sn0wix_.notEnoughKeybinds.gui.ParentScreenBlConsumer;
import net.sn0wix_.notEnoughKeybinds.gui.TexturedButtonWidget;
import net.sn0wix_.notEnoughKeybinds.keybinds.NotEKKeyBindings;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.F3DebugKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.INotEKKeybinding;
import net.sn0wix_.notEnoughKeybinds.keybinds.custom.KeybindCategory;
import net.sn0wix_.notEnoughKeybinds.util.TextUtils;
import net.sn0wix_.notEnoughKeybinds.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ControlsListWidget extends ContainerObjectSelectionList<ControlsListWidget.Entry> {
    public final NotEKSettingsScreen parent;
    private int maxKeyNameLength;

    public ControlsListWidget(NotEKSettingsScreen parent, Minecraft client) {
        super(client, parent.width, parent.threePartsLayout.getContentHeight(), parent.threePartsLayout.getHeaderHeight(), 20);
        this.parent = parent;
        initEntries();
    }

    public void initEntries() {
        this.clearEntries();

        NotEKKeyBindings.getCategories().forEach(category -> {
            this.addEntry(new ControlsListWidget.CategoryEntry(Component.translatable(category.getTranslationKey()), category));

            for (INotEKKeybinding keybinding : category.getKeyBindings()) {
                Component text = keybinding.getSettingsDisplayName();

                int textWidth = minecraft.font.width(text);
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
        KeyMapping.resetMapping();
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
    protected int scrollBarX() {
        return this.getX() + this.width / 2 - 340 / 2 + 340 + 10;
    }

    //Entries
    @Environment(EnvType.CLIENT)
    public class CategoryEntry extends ControlsListWidget.Entry {
        final Component text;
        private final int textWidth;
        private final Button resetCategoryButton;

        public CategoryEntry(Component text, KeybindCategory category) {
            this.text = text;
            this.textWidth = ControlsListWidget.this.minecraft.font.width(this.text);

            resetCategoryButton = Button.builder(TextUtils.getText("reset_category"), button ->
                            minecraft.setScreen(Utils.getModConfirmScreen(new ParentScreenBlConsumer(parent, client1 -> {
                                for (int i = 0; i < category.getKeyBindings().length; i++) {
                                    category.getKeyBindings()[i].setAndSaveKeyBinding(category.getKeyBindings()[i].getDefaultKey());
                                }
                            }, true), Component.translatable(TextUtils.getTranslationKey("reset_category.confirm"), Language.getInstance().getOrDefault(category.getTranslationKey())))))
                    .size(85, 16).build();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            assert ControlsListWidget.this.minecraft.screen != null;
            context.text(
                    ControlsListWidget.this.minecraft.font,
                    this.text,
                    ControlsListWidget.this.width / 2 - this.textWidth / 2,
                    getContentY() + getContentHeight() - 9 - 1,
                    CommonColors.WHITE,
                    false
            );

            resetCategoryButton.setWidth(minecraft.font.width(TextUtils.getText("reset_category")) + 6);
            resetCategoryButton.setX((ControlsListWidget.this.width / 2) - maxKeyNameLength - 2);
            resetCategoryButton.setY(getContentY() + 2);
            resetCategoryButton.extractRenderState(context, mouseX, mouseY, deltaTicks);
        }

        @Nullable
        @Override
        public ComponentPath nextFocusPath(FocusNavigationEvent navigation) {
            return null;
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(resetCategoryButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(new NarratableEntry() {
                @Override
                public NarratableEntry.NarrationPriority narrationPriority() {
                    return NarratableEntry.NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput builder) {
                    builder.add(NarratedElementType.TITLE, CategoryEntry.this.text);
                }
            });
        }

        @Override
        protected void update() {
        }
    }

    @Environment(EnvType.CLIENT)
    public class AddNewKeyButtonEntry extends net.sn0wix_.notEnoughKeybinds.gui.screen.keybindsScreen.ControlsListWidget.Entry {
        public final Button button;
        public final String translationKey;

        public AddNewKeyButtonEntry(Screen screen, String translationKey) {
            button = Button.builder(Component.translatable(translationKey), button1 ->
                    Minecraft.getInstance().setScreen(screen)
            ).size(200, 20).build();

            this.translationKey = translationKey;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int resetButtonPos = ControlsListWidget.this.scrollBarX() - 50 - 10;
            int editButtonPos = resetButtonPos - 5 - 75;


            int resetCategoryButtonWidth = minecraft.font.width(TextUtils.getText("reset_category")) + 6;
            int resetCategoryButtonPos = (ControlsListWidget.this.width / 2) - maxKeyNameLength - 2;

            //Math.abs(((ControlsListWidget.this.width / 2) - maxKeyNameLength - 2) - 2 * (resetCategoryButtonWidth + resetCategoryButtonPos + 10))
            int width = resetButtonPos - resetCategoryButtonWidth - resetCategoryButtonPos - 10;

            button.setRectangle(
                    width,
                    20, resetButtonPos - 5 - width, getContentY() + 2);
            button.extractRenderState(context, mouseX, mouseY, deltaTicks);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(button);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(new NarratableEntry() {
                @Override
                public NarrationPriority narrationPriority() {
                    return NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput builder) {
                    builder.add(NarratedElementType.TITLE, Component.translatable(translationKey));
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
        private final Component bindingName;
        private final Button editButton;
        private final Button resetButton;
        private final Button settingsButton;

        private boolean duplicate = false;

        public KeyBindingEntry(INotEKKeybinding binding, Component bindingName) {
            this.binding = binding;
            this.bindingName = bindingName;
            this.editButton = Button.builder(bindingName, button -> {
                        ControlsListWidget.this.parent.selectedKeyBinding = binding;
                        ControlsListWidget.this.update();
                    })
                    .bounds(0, 0, 75, 20)
                    .createNarration(
                            textSupplier -> binding.isUnbound()
                                    ? Component.translatable("narrator.controls.unbound", bindingName)
                                    : Component.translatable("narrator.controls.bound", bindingName, textSupplier.get())
                    )
                    .build();
            this.resetButton = Button.builder(Component.translatable("controls.reset"), button -> {
                binding.setAndSaveKeyBinding(binding.getDefaultKey());
                ControlsListWidget.this.update();
            }).bounds(0, 0, 50, 20).createNarration(textSupplier -> Component.translatable("narrator.controls.reset", bindingName)).build();
            this.settingsButton = new TexturedButtonWidget(0, 0, 20, 20, Component.empty(), button -> minecraft.setScreen(binding.getSettingsScreen(parent))
                    , Supplier::get, Identifier.fromNamespaceAndPath(NotEnoughKeybinds.MOD_ID, "textures/settings.png"), 14, 14, 14, 14);

            this.settingsButton.setTooltip(TextUtils.getTooltip("settings"));
            this.update();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int resetButtonPos = ControlsListWidget.this.scrollBarX() - this.resetButton.getWidth() - 10;
            int j = getContentY() - 2;
            int startPos = getWidth() / 2 - maxKeyNameLength;
            this.resetButton.setPosition(resetButtonPos, j);
            this.resetButton.extractRenderState(context, mouseX, mouseY, deltaTicks);
            int editButtonPos = resetButtonPos - 5 - this.editButton.getWidth();
            this.editButton.setPosition(editButtonPos, j);
            this.editButton.extractRenderState(context, mouseX, mouseY, deltaTicks);
            context.text(ControlsListWidget.this.minecraft.font, this.bindingName, startPos, getContentY() + getContentHeight() / 2 - 9 / 2, CommonColors.WHITE);
            if (this.duplicate) {
                int m = this.editButton.getX() - 6;
                context.fill(m, getContentY() - 1, m + 3, getContentY() + getContentHeight(), -65536);
            }

            this.settingsButton.setX(startPos - 25);
            this.settingsButton.setY(getContentY());
            this.settingsButton.extractRenderState(context, mouseX, mouseY, deltaTicks);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(this.editButton, this.resetButton, this.settingsButton);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(this.editButton, this.resetButton, this.settingsButton);
        }

        @Override
        protected void update() {
            this.editButton.setMessage(this.binding.getBoundKeyLocalizedText());
            this.resetButton.active = !this.binding.isDefault();
            this.duplicate = false;
            MutableComponent mutableText = Component.empty();
            if (!this.binding.isUnbound()) {
                for (KeyMapping keyBinding : Minecraft.getInstance().options.keyMappings) {
                    if (!(binding instanceof F3DebugKeybinding) && keyBinding != this.binding.getBinding() && this.binding.getBinding().same(keyBinding)) {
                        if (this.duplicate) {
                            mutableText.append(", ");
                        }

                        this.duplicate = true;
                        mutableText.append(Component.translatable(keyBinding.getName()));
                    }
                }
            }

            if (this.duplicate) {
                this.editButton
                        .setMessage(Component.literal("[ ").append(this.editButton.getMessage().copy().withStyle(ChatFormatting.WHITE)).append(" ]").withStyle(ChatFormatting.RED));
                this.editButton.setTooltip(Tooltip.create(Component.translatable("controls.keybinds.duplicateKeybinds", mutableText)));
            } else {
                this.editButton.setTooltip(binding.getTooltip().getString().isEmpty() ? null : Tooltip.create(binding.getTooltip()));
            }

            if (ControlsListWidget.this.parent.selectedKeyBinding == this.binding) {
                this.editButton
                        .setMessage(
                                Component.literal("> ")
                                        .append(this.editButton.getMessage().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
                                        .append(" <")
                                        .withStyle(ChatFormatting.YELLOW)
                        );
            }
            this.settingsButton.visible = binding.getSettingsScreen(parent) != null;
        }
    }


    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<ControlsListWidget.Entry> {
        abstract void update();
    }
}
