package it.unibo.oop.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import it.unibo.oop.controller.GameLoop;
import it.unibo.oop.controller.KeyboardObserver;

/**
 * Simple custom class implementing {@link KeyListener} that should be attached to
 * the main view (where will show the game).
 */
public class MainKeyListener implements KeyListener {

    private final List<KeyboardObserver> obsList;

    /**
     * @param obs
     *            a {@link GameLoop} instance
     */
    public MainKeyListener(final List<KeyboardObserver> obs) {
        this.obsList = obs;
    }

    /**
     * Empty class's constructor.
     */
    public MainKeyListener() {
        this.obsList = new ArrayList<>();
    }

    /**
     * @param obs
     *          observer to attach.
     */
    public void addObserver(final KeyboardObserver obs) {
        this.obsList.add(obs);
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        this.action(e.getKeyCode(), e.getID());
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        this.action(e.getKeyCode(), e.getID());
    }

    @Override
    public void keyTyped(final KeyEvent e) {
    }

    private void action(final int keyCode, final int eventID) {
        this.obsList.forEach(elem -> {
            new Thread(() -> {
                elem.keyAction(keyCode, eventID);
            }).start();
        });
    }
}