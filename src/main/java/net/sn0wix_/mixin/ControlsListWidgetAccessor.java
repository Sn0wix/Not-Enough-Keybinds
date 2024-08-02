package net.sn0wix_.mixin;

import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ControlsListWidget.class)
public interface ControlsListWidgetAccessor {
    @Accessor("maxKeyNameLength")
    int getMaxKeyNameLength();

    @Accessor("parent")
    KeybindsScreen getParent();
}
