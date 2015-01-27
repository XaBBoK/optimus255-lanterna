/*
 * This file is part of lanterna (http://code.google.com/p/lanterna/).
 * 
 * lanterna is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2010-2012 Martin
 */

package com.googlecode.lanterna.gui.component;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.Theme;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TerminalSize;

/**
 *
 * @author Martin
 */
public class ActionListBox extends AbstractListBox {
    
    public ActionListBox() {
        this(null);
    }

    public ActionListBox(TerminalSize preferredSize) {
        super(preferredSize);
    }

    /**
     * Draws an item in the ListBox at specific coordinates. If you override this method,
     * please note that the x and y positions have been pre-calculated for you and you should use
     * the values supplied instead of trying to figure out the position on your own based on the
     * index of the item.
     *
     * @param graphics TextGraphics object to use when drawing the item
     * @param x        X-coordinate on the terminal of the item, pre-adjusted for scrolling
     * @param y        Y-coordinate on the terminal of the item, pre-adjusted for scrolling
     * @param index    Index of the item that is to be drawn
     */
    @Override
    protected void printItem(TextGraphics graphics, int x, int y, int index) {
        //super.printItem(graphics, x, y, index);



        ColorType colorType = ((Item) getItemAt(index)).getItemColorType();
        if (colorType != null) {
            Terminal.Color color = colorType.color;

            //если текст не null и не пустой и поместится, то пишем текст и заливаем указанным цветом
            if (colorType.itemType != null && colorType.itemType.length() > 0 && graphics.getWidth() > colorType.itemType.length()) {
                graphics.setBackgroundColor(color);

                //fill all row
                graphics.fillRectangle(' ', new TerminalPosition(x, y), graphics.getSize());
                graphics.drawString(graphics.getWidth() - colorType.itemType.length(), y, colorType.itemType);
            }

        } else {
            //fill all row
            graphics.fillRectangle(' ', new TerminalPosition(x, y), graphics.getSize());
        }


        String asText = createItemString(index);
        if (asText.length() > graphics.getWidth())
            asText = asText.substring(0, graphics.getWidth());
        graphics.drawString(x, y, asText);

    }

    /**
     * Adds an action to the list, using toString() of the action as a label
     * @param action Action to be performed when the user presses enter key
     */
    public void addAction(final Action action) {
        addAction(action.toString(), action);
    }


    public void addAction(final String label, final Action action) {
        addAction(label, action, null);
    }

    public void addAction(final String label, final Action action, final ColorType itemColorType) {
        super.addItem(new Item() {
            @Override
            public String getTitle() {
                return label;
            }

            @Override
            public void doAction(Key key) {
                action.doAction(key);
            }

            @Override
            public ColorType getItemColorType() {
                return itemColorType;
            }
        });
    }

    @Override
    protected Result unhandledKeyboardEvent(Key key) {
        /*if(key.getKind() == Key.Kind.Enter) {
            ((Item)getSelectedItem()).doAction();
            return Result.EVENT_HANDLED;
        }*/
        ((Item) getSelectedItem()).doAction(key);
        return Result.EVENT_HANDLED;
        //return Result.EVENT_NOT_HANDLED;
    }
    
    @Override
    protected String createItemString(int index) {
        return ((Item)getItemAt(index)).getTitle();
    }

    @Override
    public TerminalPosition getHotspot() {
        return null;    //No hotspot for ActionListBox:es
    }

    @Override
    protected Theme.Definition getListItemThemeDefinition(Theme theme) {
        return theme.getDefinition(Theme.Category.DIALOG_AREA);
    }

    @Override
    protected Theme.Definition getSelectedListItemThemeDefinition(Theme theme) {
        return theme.getDefinition(Theme.Category.TEXTBOX_FOCUSED);
    }

    public static interface Item {
        public String getTitle();
        public void doAction(Key key);

        //for subitems compatible
        public ColorType getItemColorType();
    }
}
