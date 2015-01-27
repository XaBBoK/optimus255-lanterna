package com.googlecode.lanterna.gui.component;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Created by user on 27.01.15.
 */
public class ColorType {
    public String itemType;
    public Terminal.Color color;

    public ColorType(String itemType, Terminal.Color color) {
        this.itemType = itemType;
        this.color = color;
    }
}
