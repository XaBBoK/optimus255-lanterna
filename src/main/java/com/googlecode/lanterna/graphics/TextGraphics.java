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
 * Copyright (C) 2010-2014 Martin
 */
package com.googlecode.lanterna.graphics;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.screen.TabBehaviour;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;

import java.util.EnumSet;

/**
 * This interface exposes functionality to 'draw' text graphics on a section of the terminal. It has several
 * implementation for the different levels, including one for Terminal, one for Screen and one which is used by the
 * TextGUI system to draw components. They are all very similar and has a lot of graphics functionality in
 * AbstractTextGraphics.
 * <p/>
 * The basic concept behind a TextGraphics implementation is that it keeps a state on four things:
 * <ul>
 *     <li>Foreground color</li>
 *     <li>Background color</li>
 *     <li>Modifiers</li>
 *     <li>Tab-expanding behaviour</li>
 * </ul>
 * These call all be altered through ordinary set* methods, but some will be altered as the result of performing one of
 * the 'drawing' operations. See the documentation to each method for further information (for example, putString).
 * <p/>
 * Don't hold on to your TextGraphics objects for too long; ideally create them and let them be GC:ed when you are done
 * with them. The reason is that not all implementations will handle the underlying terminal changing size.
 * @author Martin
 */
public interface TextGraphics {
    /**
     * Returns the size of the area that this text graphic can write to. Any attempts of placing characters outside of
     * this area will be silently ignored.
     * @return Size of the writable area that this TextGraphics can write too
     */
    TerminalSize getSize();

    /**
     * Creates a new TextGraphics of the same type as this one, using the same underlying subsystem. Using this method,
     * you need to specify a section of the current TextGraphics valid area that this new TextGraphic shall be
     * restricted to. If you call <code>newTextGraphics(TerminalPosition.TOP_LEFT_CORNER, textGraphics.getSize())</code>
     * then the resulting object will be identical to this one, but having a separated state for colors, position and
     * modifiers.
     * @param topLeftCorner Position of this TextGraphics's writable area that is to become the top-left corner (0x0) of
     *                      the new TextGraphics
     * @param size How large area, counted from the topLeftCorner, the new TextGraphics can write to. This cannot be
     *             larger than the current TextGraphics's writable area (adjusted by topLeftCorner)
     * @return A new TextGraphics with the same underlying subsystem, that can write to only the specified area
     * @throws java.lang.IllegalArgumentException If the size the of new TextGraphics exceeds the dimensions of this
     * TextGraphics in any way.
     */
    TextGraphics newTextGraphics(TerminalPosition topLeftCorner, TerminalSize size) throws IllegalArgumentException;

    /**
     * Returns the current background color
     * @return Current background color
     */
    TextColor getBackgroundColor();

    /**
     * Updates the current background color
     * @param backgroundColor New background color
     * @return Itself
     */
    TextGraphics setBackgroundColor(TextColor backgroundColor);

    /**
     * Returns the current foreground color
     * @return Current foreground color
     */
    TextColor getForegroundColor();

    /**
     * Updates the current foreground color
     * @param foregroundColor New foreground color
     * @return Itself
     */
    TextGraphics setForegroundColor(TextColor foregroundColor);

    /**
     * Adds zero or more modifiers to the set of currently active modifiers
     * @param modifiers Modifiers to add to the set of currently active modifiers
     * @return Itself
     */
    TextGraphics enableModifiers(SGR... modifiers);

    /**
     * Removes zero or more modifiers from the set of currently active modifiers
     * @param modifiers Modifiers to remove from the set of currently active modifiers
     * @return Itself
     */
    TextGraphics disableModifiers(SGR... modifiers);

    /**
     * Sets the active modifiers to exactly the set passed in to this method. Any previous state of which modifiers are
     * enabled doesn't matter.
     * @param modifiers Modifiers to set as active
     * @return Itself
     */
    TextGraphics setModifiers(EnumSet<SGR> modifiers);

    /**
     * Removes all active modifiers
     * @return Itself
     */
    TextGraphics clearModifiers();

    /**
     * Returns all the SGR codes that are currently active in the TextGraphic
     * @return Currently active SGR modifiers
     */
    EnumSet<SGR> getActiveModifiers();

    /**
     * Retrieves the current tab behaviour, which is what the TextGraphics will use when expanding \t characters to
     * spaces.
     * @return Current behaviour in use for expanding tab to spaces
     */
    TabBehaviour getTabBehaviour();

    /**
     * Sets the behaviour to use when expanding tab characters (\t) to spaces
     * @param tabBehaviour Behaviour to use when expanding tabs to spaces
     */
    TextGraphics setTabBehaviour(TabBehaviour tabBehaviour);

    /**
     * Fills the entire writable area with a single character, using current foreground color, background color and modifiers.
     * @param c Character to fill the writable area with
     */
    TextGraphics fill(char c);

    /**
     * Sets the character at the current position to the specified value
     * @param column column of the location to set the character
     * @param row row of the location to set the character
     * @param character Character to set at the current position
     * @return Itself
     */
    TextGraphics setCharacter(int column, int row, char character);

    /**
     * Sets the character at the current position to the specified value
     * @param position position of the location to set the character
     * @param character Character to set at the current position
     * @return Itself
     */
    TextGraphics setCharacter(TerminalPosition position, char character);

    /**
     * Draws a line from a specified position to a specified position, using a supplied character. The current
     * foreground color, background color and modifiers will be applied.
     * @param fromPoint From where to draw the line
     * @param toPoint Where to draw the line
     * @param character Character to use for the line
     * @return Itself
     */
    TextGraphics drawLine(TerminalPosition fromPoint, TerminalPosition toPoint, char character);
    
    /**
     * Draws a line from a specified position to a specified position, using a supplied character. The current
     * foreground color, background color and modifiers will be applied.
     * @param fromX Column of the starting position to draw the line from (inclusive)
     * @param fromY Row of the starting position to draw the line from (inclusive)
     * @param toX Column of the end position to draw the line to (inclusive)
     * @param toY Row of the end position to draw the line to (inclusive)
     * @param character Character to use for the line
     * @return Itself
     */
    TextGraphics drawLine(int fromX, int fromY, int toX, int toY, char character);
    
    /**
     * Draws the outline of a triangle on the screen, using a supplied character. The triangle will begin at p1, go
     * through p2 and then p3 and then back to p1. The current foreground color, background color and modifiers will be
     * applied.
     * @param p1 First point on the screen of the triangle
     * @param p2 Second point on the screen of the triangle
     * @param p3 Third point on the screen of the triangle
     * @param character What character to use when drawing the lines of the triangle
     */
    TextGraphics drawTriangle(TerminalPosition p1, TerminalPosition p2, TerminalPosition p3, char character);

    /**
     * Draws a filled triangle, using a supplied character. The triangle will begin at p1, go
     * through p2 and then p3 and then back to p1. The current foreground color, background color and modifiers will be
     * applied.
     * @param p1 First point on the screen of the triangle
     * @param p2 Second point on the screen of the triangle
     * @param p3 Third point on the screen of the triangle
     * @param character What character to use when drawing the lines of the triangle
     */
    TextGraphics fillTriangle(TerminalPosition p1, TerminalPosition p2, TerminalPosition p3, char character);

    /**
     * Draws the outline of a rectangle with a particular character (and the currently active colors and
     * modifiers). The topLeft coordinate is inclusive.
     * <p/>
     * For example, calling drawRectangle with size being the size of the terminal and top-left value being the terminal's
     * top-left (0x0) corner will draw a border around the terminal.
     * <p/>
     * The current foreground color, background color and modifiers will be applied.
     * @param topLeft Coordinates of the top-left position of the rectangle
     * @param size Size (in columns and rows) of the area to draw
     * @param character What character to use when drawing the outline of the rectangle
     */
    TextGraphics drawRectangle(TerminalPosition topLeft, TerminalSize size, char character);

    /**
     * Takes a rectangle and fills it with a particular character (and the currently active colors and
     * modifiers). The topLeft coordinate is inclusive.
     * <p/>
     * For example, calling fillRectangle with size being the size of the terminal and top-left value being the terminal's
     * top-left (0x0) corner will fill the entire terminal with this character.
     * <p/>
     * The current foreground color, background color and modifiers will be applied.
     * @param topLeft Coordinates of the top-left position of the rectangle
     * @param size Size (in columns and rows) of the area to draw
     * @param character What character to use when filling the rectangle
     */
    TextGraphics fillRectangle(TerminalPosition topLeft, TerminalSize size, char character);
    
    /**
     * Takes a TextImage and draws it on the surface this TextGraphics is targeting, given the coordinates on the target
     * that is specifying where the top-left corner of the image should be drawn.
     * @param topLeft Position of the top-left corner of the image on the target
     * @param image Image to draw
     * @return Itself
     */
    TextGraphics drawImage(TerminalPosition topLeft, TextImage image);

    /**
     * Puts a string on the screen at the specified position with the current colors and modifiers. If the string
     * contains newlines (\r and/or \n), the method will stop at the character before that; you have to manage
     * multi-line strings yourself! The current foreground color, background color and modifiers will be applied.
     * @param column What column to put the string at
     * @param row What row to put the string at
     * @param string String to put on the screen
     * @return Itself
     */
    TextGraphics putString(int column, int row, String string);

    /**
     * Shortcut to calling:
     * <pre>
     *  putString(position.getColumn(), position.getRow(), string);
     * </pre>
     * @param position Position to put the string at
     * @param string String to put on the screen
     * @return Itself
     */
    TextGraphics putString(TerminalPosition position, String string);

    /**
     * Puts a string on the screen at the specified position with the current colors and modifiers. If the string
     * contains newlines (\r and/or \n), the method will stop at the character before that; you have to manage
     * multi-line strings yourself! If you supplied any extra modifiers, they will be applied when writing the string
     * as well.
     * @param column What column to put the string at
     * @param row What row to put the string at
     * @param string String to put on the screen
     * @param extraModifier Modifier to apply to the string
     * @param optionalExtraModifiers Optional extra modifiers to apply to the string
     * @return Itself
     */
    TextGraphics putString(int column, int row, String string, SGR extraModifier, SGR... optionalExtraModifiers);

    /**
     * Shortcut to calling:
     * <pre>
     *  putString(position.getColumn(), position.getRow(), string, modifiers, optionalExtraModifiers);
     * </pre>
     * @param position Position to put the string at
     * @param string String to put on the screen
     * @param extraModifier Modifier to apply to the string
     * @param optionalExtraModifiers Optional extra modifiers to apply to the string
     * @return Itself
     */
    TextGraphics putString(TerminalPosition position, String string, SGR extraModifier, SGR... optionalExtraModifiers);
}
