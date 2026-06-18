package net.sn0wix_.notEnoughKeybinds.gui.screen;

import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;

public interface INotEKLayoutTemplate {
    default void initBodyWidget(LinearLayout left, LinearLayout right, HeaderAndFooterLayout layoutWidget) {
        LinearLayout body = LinearLayout.horizontal().spacing(5);
        body.addChild(left);
        body.addChild(right);

        layoutWidget.addToContents(body);
    }

    default LinearLayout getColumnWidget() {
        return LinearLayout.vertical().spacing(2);
    }

    void initButtons();
}
