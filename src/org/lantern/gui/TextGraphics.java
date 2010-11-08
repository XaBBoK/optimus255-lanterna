/*
 *  Copyright (C) 2010 mabe02
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.lantern.gui;

import org.lantern.gui.theme.Theme;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.lantern.terminal.Terminal.Color;
import org.lantern.screen.Screen;
import org.lantern.terminal.Terminal;
import org.lantern.terminal.TerminalPosition;
import org.lantern.terminal.TerminalSize;

/**
 *
 * @author mabe02
 */
public class TextGraphics
{
    private final TerminalPosition topLeft;
    private final TerminalSize areaSize;
    private final Screen screen;
    private Theme theme;
    private Color foregroundColor;
    private Color backgroundColor;
    private boolean currentlyBold;

    public TextGraphics(final TerminalPosition topLeft, final TerminalSize areaSize, final Screen screen, final Theme theme)
    {
        this.topLeft = topLeft;
        this.areaSize = areaSize;
        this.screen = screen;
        this.theme = theme;
        this.currentlyBold = false;
        this.foregroundColor = Color.DEFAULT;
        this.backgroundColor = Color.DEFAULT;
    }

    private TextGraphics(final TextGraphics graphics, final TerminalPosition topLeft, final TerminalSize areaSize)
    {
        this(new TerminalPosition(topLeft.getColumn() + graphics.topLeft.getColumn(), topLeft.getRow() + graphics.topLeft.getRow()),
                new TerminalSize(areaSize.getColumns() < (graphics.getWidth() - topLeft.getColumn()) ? areaSize.getColumns() : (graphics.getWidth() - topLeft.getColumn()),
                    areaSize.getRows() < (graphics.getHeight() - topLeft.getRow()) ? areaSize.getRows() : (graphics.getHeight() - topLeft.getRow())),
                graphics.screen, graphics.theme);
    }

    public TextGraphics subAreaGraphics(final TerminalPosition terminalPosition)
    {
        terminalPosition.ensurePositivePosition();
        TerminalSize newArea = new TerminalSize(areaSize);
        newArea.setColumns(newArea.getColumns() - terminalPosition.getColumn());
        newArea.setRows(newArea.getRows() - terminalPosition.getRow());
        return subAreaGraphics(terminalPosition, newArea);
    }

    public TextGraphics subAreaGraphics(final TerminalPosition topLeft, final TerminalSize subAreaSize)
    {
        if(topLeft.getColumn() < 0)
            topLeft.setColumn(0);
        if(topLeft.getRow() < 0)
            topLeft.setRow(0);
        if(subAreaSize.getColumns() < 0)
            subAreaSize.setColumns(-subAreaSize.getColumns());
        if(subAreaSize.getRows() < 0)
            subAreaSize.setRows(-subAreaSize.getRows());

        if(topLeft.getColumn() + subAreaSize.getColumns() > areaSize.getColumns())
            subAreaSize.setColumns(areaSize.getColumns() - topLeft.getColumn());
        if(topLeft.getRow() + subAreaSize.getRows() > areaSize.getRows())
            subAreaSize.setRows(areaSize.getRows() - topLeft.getRow());

        return new TextGraphics(this, topLeft, subAreaSize);
    }

    public void drawString(int column, int row, String string, Terminal.Style... styles)
    {
        if(column >= areaSize.getColumns() || row >= areaSize.getRows() || string == null)
            return;

        if(string.length() + column > areaSize.getColumns())
            string = string.substring(0, areaSize.getColumns() - column);

        Set<Terminal.Style> stylesSet = new HashSet<Terminal.Style>(Arrays.asList(styles));
        if(currentlyBold)
            stylesSet.add(Terminal.Style.Bold);

        boolean bold = stylesSet.contains(Terminal.Style.Bold);
        boolean underline = stylesSet.contains(Terminal.Style.Underline);
        boolean reverse = stylesSet.contains(Terminal.Style.Reverse);

        screen.putString(column + topLeft.getColumn(), row + topLeft.getRow(), string,
                foregroundColor, backgroundColor,
                bold, underline, reverse);
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public Color getForegroundColor()
    {
        return foregroundColor;
    }

    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public void setForegroundColor(Color foregroundColor)
    {
        this.foregroundColor = foregroundColor;
    }

    public int getWidth()
    {
        return areaSize.getColumns();
    }

    public int getHeight()
    {
        return areaSize.getRows();
    }

    public void setBoldMask(boolean enabledBoldMask)
    {
        currentlyBold = enabledBoldMask;
    }

    public Theme getTheme()
    {
        return theme;
    }

    public void applyThemeItem(Theme.Category category)
    {
        applyThemeItem(getTheme().getItem(category));
    }

    public TerminalPosition translateToGlobalCoordinates(TerminalPosition pointInArea)
    {
        return new TerminalPosition(pointInArea.getColumn() + topLeft.getColumn(), pointInArea.getRow() + topLeft.getRow());
    }

    public void applyThemeItem(Theme.Item themeItem)
    {
        setForegroundColor(themeItem.foreground);
        setBackgroundColor(themeItem.background);
        setBoldMask(themeItem.highlighted);
    }

    public void fillArea(char character)
    {
        fillRectangle(character, new TerminalPosition(0, 0), new TerminalSize(areaSize));
    }

    public void fillRectangle(char character, TerminalPosition topLeft, TerminalSize rectangleSize)
    {
        StringBuilder emptyLineBuilder = new StringBuilder();
        for(int i = 0; i < rectangleSize.getColumns(); i++)
            emptyLineBuilder.append(character);
        String emptyLine = emptyLineBuilder.toString();
        for(int i = 0; i < rectangleSize.getRows(); i++)
            drawString(topLeft.getColumn(), topLeft.getRow() + i, emptyLine);
    }

    @Override
    public String toString()
    {
        return "TextGraphics {topLeft: " + topLeft.toString() + ", size: " + areaSize.toString() + "}";
    }
}
