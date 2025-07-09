package net.sn0wix_.notEnoughKeybinds.gui.screen;

import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;

public interface INotEKLayoutTemplate {
    default void initBodyWidget(DirectionalLayoutWidget left, DirectionalLayoutWidget right, ThreePartsLayoutWidget layoutWidget) {
        DirectionalLayoutWidget body = DirectionalLayoutWidget.horizontal().spacing(5);
        body.add(left);
        body.add(right);

        layoutWidget.addBody(body);
    }

    default DirectionalLayoutWidget getColumnWidget() {
        return DirectionalLayoutWidget.vertical().spacing(2);
    }

    void initButtons();
}
