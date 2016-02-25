package it.unibo.oop.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.Random;

import it.unibo.oop.model.GameState;
import it.unibo.oop.model.GameStateImpl;
import it.unibo.oop.model.Score;
import it.unibo.oop.utilities.Settings;
import it.unibo.oop.view.View;
import it.unibo.oop.view.ViewImpl;

/**
 * Class implementing the Controller of the MVC model.
 */
public final class ControllerImpl implements Controller {

    private static final int LEVELS = 10;
    private static Optional<ControllerImpl> singleton = Optional.empty();
    private Optional<AgentInterface> gLAgent = Optional.empty();
    private volatile boolean record;
    private volatile boolean isReset;
    private final View view = ViewImpl.getInstance();
    private final GameState gameState = GameStateImpl.getInstance();
    
    private ControllerImpl() {
        this.createStatFile();
        this.view.showView(AppState.LAUNCHING);
    }

    /**
     * @return the SINGLETON instance of the class.
     */
    public static synchronized Controller getInstance() {
        if (!singleton.isPresent()) {
            singleton = Optional.of(new ControllerImpl());
        }
        return singleton.get();
    }

    @Override
    public void start() { // launcher -> play / pause -> replay
        final int levelNumber = new Random().nextInt(LEVELS);
        this.gameState.initialize(levelNumber);
        this.view.getLevelView().initialize(levelNumber);
        this.play();
    }

    @Override
    public void play() { // pause -> play
        view.reset();
        this.isReset = false;
        this.record = false;
        this.view.hideView();
        if (!this.gLAgent.isPresent()) {
            this.gLAgent = Optional.ofNullable(new GameLoopAgent());
            new Thread(this.gLAgent.get()).start();
        } else {
            this.gLAgent.get().play();
        }
    }

    private void createStatFile() {
        final File statDir = new File(Settings.HIGHSCORE_FOLDER);
        final File statFile = new File(Settings.HIGHSCORE_FOLDER + Settings.HIGHSCORE_FILE);
        try {
            statDir.mkdir();
            statFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    @Override
    public synchronized void resetStatFile() {
        this.isReset = true;
        try (ObjectOutputStream outStream = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(Settings.HIGHSCORE_FOLDER + Settings.HIGHSCORE_FILE)))) {
            outStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Score getStatFromFile() {
        Score topScore = new Score();
        try (ObjectInputStream inStream = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(Settings.HIGHSCORE_FOLDER + Settings.HIGHSCORE_FILE)))) {
            topScore = (Score) inStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File was empty or doesn't exist.");
            this.createStatFile();
        }
        return topScore;
    }
    
    @Override
    public synchronized void putStatToFile(final Score topScore) {
        this.record = true;
        try (ObjectOutputStream outStream = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(Settings.HIGHSCORE_FOLDER + Settings.HIGHSCORE_FILE)))) {
            outStream.writeObject(topScore);
        } catch (IOException e) {
            System.out.println("File doesn't exist.");
        }
    }

    @Override
    public boolean isRecord() {
        return this.record;
    }
    
    @Override
    public boolean isScoreReset() {
        return this.isReset;
    }
}