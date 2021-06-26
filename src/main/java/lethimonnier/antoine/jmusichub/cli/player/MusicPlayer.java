package lethimonnier.antoine.jmusichub.cli.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;
import lethimonnier.antoine.jmusichub.cli.logging.MusicLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class MusicPlayer {

    private final Logger logger = MusicLogger.getLogger("output.log");
    private static MusicPlayer instance;
    private AudioContent currentSong;
    private Thread playingThread;
    private AdvancedPlayer player;

    public static synchronized MusicPlayer getPlayer() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    public MusicPlayer setPlayingSong(AudioContent audioContent) {
        currentSong = audioContent;
        return this;
    }

    public void play() {
        /*if (currentSong == null) {
            logger.severe("No song to play");
            return;
        }*/
        try {
            player = new AdvancedPlayer(new FileInputStream("sample.mp3"));
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackStarted(PlaybackEvent evt) {
                    logger.info("started");
                }

                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    logger.info("finished");
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
            logger.info("Player: Started");
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        /*if (currentSong == null) {
            logger.severe("No song to stop");
            return;
        }*/
        if (playingThread.isAlive())
            player.stop();
        logger.info("Player: Stopped");
    }
}
