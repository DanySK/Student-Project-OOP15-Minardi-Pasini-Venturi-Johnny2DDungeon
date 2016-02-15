package it.unibo.oop.view;

import it.unibo.oop.controller.KeyboardObserver;

/**
 * 
 * @author Paolo
 * Interface for Level, should be factored.
 */
public interface LevelInterface extends Showable {
    
    /**
     * @param obs
     *          observer to attach.
     */
    public void addObserver(final KeyboardObserver obs);

}