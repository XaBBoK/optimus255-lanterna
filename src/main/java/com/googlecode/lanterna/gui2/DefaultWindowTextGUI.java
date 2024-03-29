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
package com.googlecode.lanterna.gui2;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.VirtualScreen;

import java.io.IOException;

/**
 *
 * @author Martin
 */
public class DefaultWindowTextGUI extends AbstractTextGUI implements WindowBasedTextGUI {
    private final VirtualScreen virtualScreen;
    private final WindowManager windowManager;
    private final TextGUIElement background;
    private final WindowPostRenderer postRenderer;
    private boolean eofWhenNoWindows;

    public DefaultWindowTextGUI(Screen screen) {
        this(screen, TextColor.ANSI.BLUE);
    }

    public DefaultWindowTextGUI(Screen screen, TextColor backgroundColor) {
        this(screen, new StackedModalWindowManager(), new EmptySpace(backgroundColor));
    }

    public DefaultWindowTextGUI(Screen screen, WindowManager windowManager, TextGUIElement background) {
        this(screen, windowManager, new WindowShadowRenderer(), background);
    }

    public DefaultWindowTextGUI(Screen screen, WindowManager windowManager, WindowPostRenderer postRenderer, TextGUIElement background) {
        this(new VirtualScreen(screen), windowManager, postRenderer, background);
    }

    private DefaultWindowTextGUI(VirtualScreen screen, WindowManager windowManager, WindowPostRenderer postRenderer, TextGUIElement background) {
        super(screen);
        if(windowManager == null) {
            throw new IllegalArgumentException("Creating a window-based TextGUI requires a WindowManager");
        }
        if(background == null) {
            //Use a sensible default instead of throwing
            background = new EmptySpace(TextColor.ANSI.BLUE);
        }
        this.virtualScreen = screen;
        this.windowManager = windowManager;
        this.background = background;
        this.postRenderer = postRenderer;
        this.eofWhenNoWindows = true;
        
        this.windowManager.addListener(new WindowManager.Listener() {
            @Override
            public void onWindowAdded(WindowManager manager, Window window) {
                window.setTextGUI(DefaultWindowTextGUI.this);
            }

            @Override
            public void onWindowRemoved(WindowManager manager, Window window) {
                window.setTextGUI(null);
            }
        });
    }

    @Override
    public boolean isPendingUpdate() {
        return super.isPendingUpdate() || background.isInvalid() || windowManager.isInvalid();
    }

    @Override
    public void updateScreen() throws IOException {
        TerminalSize preferredSize = TerminalSize.ONE;
        for(Window window: windowManager.getWindows()) {
            preferredSize = preferredSize.max(window.getPreferredSize());
        }
        virtualScreen.setMinimumSize(preferredSize.withRelativeColumns(10).withRelativeRows(5));
        super.updateScreen();
    }

    @Override
    protected KeyStroke readKeyStroke() throws IOException {
        KeyStroke keyStroke = super.pollInput();
        if(eofWhenNoWindows && keyStroke == null && windowManager.getWindows().isEmpty()) {
            return new KeyStroke(KeyType.EOF);
        }
        else if(keyStroke != null) {
            return keyStroke;
        }
        else {
            return super.readKeyStroke();
        }
    }

    @Override
    protected void drawGUI(TextGUIGraphics graphics) {
        background.draw(graphics);
        for(Window window: getWindowManager().getWindows()) {
            WindowDecorationRenderer decorationRenderer = getWindowManager().getWindowDecorationRenderer(window);
            TerminalPosition topLeft = getWindowManager().getTopLeftPosition(window, graphics.getSize());
            TerminalSize windowSize = getWindowManager().getSize(window, topLeft, graphics.getSize());
            window.setPosition(topLeft.withRelative(decorationRenderer.getOffset(window)));
            window.setDecoratedSize(windowSize);
            TextGUIGraphics windowGraphics = graphics.newTextGraphics(topLeft, windowSize);
            if(!window.getHints().contains(Window.Hint.NO_DECORATIONS)) {
                windowGraphics = decorationRenderer.draw(this, windowGraphics, window);
            }
            window.draw(windowGraphics);
            if(postRenderer != null && !window.getHints().contains(Window.Hint.NO_POST_RENDERING)) {
                postRenderer.postRender(graphics, this, window, topLeft, windowSize);
            }
        }
    }

    @Override
    protected TerminalPosition getCursorPosition() {
        Window activeWindow = windowManager.getActiveWindow();
        if(activeWindow == null) {
            return null;
        }
        return activeWindow.toGlobal(activeWindow.getCursorPosition());
    }

    /**
     * Sets whether the TextGUI should return EOF when you try to read input while there are no windows in the window
     * manager. Setting this to true (on by default) will make the GUI automatically exit when the last window has been
     * closed.
     * @param eofWhenNoWindows Should the GUI return EOF when there are no windows left
     */
    public void setEOFWhenNoWindows(boolean eofWhenNoWindows) {
        this.eofWhenNoWindows = eofWhenNoWindows;
    }

    /**
     * Returns whether the TextGUI should return EOF when you try to read input while there are no windows in the window
     * manager. When this is true (true by default) will make the GUI automatically exit when the last window has been
     * closed.
     * @return Should the GUI return EOF when there are no windows left
     */
    public boolean isEOFWhenNoWindows() {
        return eofWhenNoWindows;
    }

    @Override
    public Interactable getFocusedInteractable() {
        Window activeWindow = windowManager.getActiveWindow();
        if(activeWindow == null) {
            return null;
        }
        return activeWindow.getFocusedInteractable();
    }

    @Override
    protected boolean handleInput(KeyStroke keyStroke) {
        Window activeWindow = windowManager.getActiveWindow();
        return activeWindow != null && activeWindow.handleInput(keyStroke);
    }

    @Override
    public WindowManager getWindowManager() {
        return windowManager;
    }
}
