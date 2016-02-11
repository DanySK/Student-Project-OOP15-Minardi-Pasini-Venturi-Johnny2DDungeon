package it.unibo.oop.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import it.unibo.oop.controller.GameLoop;
import it.unibo.oop.controller.KeyboardObserver;

/**
 * 
 * @author Paolo
 * 
 * simple custom class implementing {@link KeyListener} should be attached to the main view (where to play the game).
 *
 */
public class MainKeyListener implements KeyListener {

    private final List<KeyboardObserver> obsList;
    
    /**
     * 
     * @param gL
     *          a {@link GameLoop} instance
     * 
     */
    public MainKeyListener(final List<KeyboardObserver> obs) {
        this.obsList = obs;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        this.obsList.forEach(elem -> {
            new Thread(() -> {
                elem.keyPressed(e.getKeyCode());
            }).start();
        });
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.obsList.forEach(elem -> {
            new Thread(() -> {
                elem.keyReleased(e.getKeyCode());
            }).start();
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        this.obsList.forEach(elem -> {
            new Thread(() -> {
                elem.keyTyped(e.getKeyCode());
            }).start();
        });
    }
}
