package com.googlecode.lanterna.test.issue;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.input.Key;

public class Issue2 {

    public static void main(String[] args) {

        GUIScreen textGUI = TerminalFacade.createGUIScreen();
        if (textGUI == null) {
            return;
        }
        textGUI.getScreen().startScreen();


        final Window window = new Window("test");
        window.addComponent(new Label("test"));
        window.addComponent(new Button("Close", new Action() {
            @Override
            public void doAction(Key key) {
                window.close();
            }
        }));
        textGUI.showWindow(window, GUIScreen.Position.CENTER);
        textGUI.getScreen().stopScreen();
    }
}
