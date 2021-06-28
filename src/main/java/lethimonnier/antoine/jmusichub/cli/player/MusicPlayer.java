package lethimonnier.antoine.jmusichub.cli.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import lethimonnier.antoine.jmusichub.cli.Utils;
import lethimonnier.antoine.jmusichub.cli.logging.MusicLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

/**
 * The type Music player.
 */
public class MusicPlayer {

    private final Logger logger = MusicLogger.getLogger("output.log");
    private static MusicPlayer instance;
    private File currentSong;
    private Thread playingThread;
    private AdvancedPlayer player;

    /**
     * Gets player.
     *
     * @return the player
     */
    protected static synchronized MusicPlayer getPlayer() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    /**
     * Sets playing song index.
     *
     * @param index the index
     * @return the playing song index
     */
    protected MusicPlayer setPlayingSongIndex(int index) {
        currentSong = Utils.getRootPath().resolve("io/musics/" + MusicManager.getSongAtIndex(index - 1)).toFile();
        return this;
    }

    /**
     * Is playing boolean.
     *
     * @return the boolean
     */
    protected boolean isPlaying() {
        return playingThread != null && playingThread.isAlive();
    }

    /**
     * Play.
     */
    protected void play() {
        if (currentSong == null) {
            logger.severe("No song to play");
            return;
        }
        try {
            player = new AdvancedPlayer(new FileInputStream(currentSong));
            player.setPlayBackListener(new PlaybackListener() {

                @Override
                public void playbackStarted(PlaybackEvent evt) {
                    logger.info("▶ Reading music \"" + currentSong.getName().replace(".mp3", "") + "\"");
                }

                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    logger.info("⏹ Music playback finished");
                }
            });
            playingThread = new Thread(() -> {
                try {
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            });
            playingThread.start();
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop.
     */
    protected void stop() {
        if (currentSong == null) {
            logger.severe("No song to stop");
            return;
        }
        if (playingThread.isAlive())
            player.stop();
    }
}
