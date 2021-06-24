package lethimonnier.antoine.jmusichub.cli.player;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;
//import lethimonnier.antoine.jmusichub.cli.logging.MusicLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class MusicPlayer {

    private static MusicPlayer instance;
    private AudioContent currentSong;
    private AdvancedPlayer player;
    private final Logger logger = Logger.getGlobal();
    private int frame = 0;

    public static MusicPlayer getPlayer() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    public void play() {
//        if (currentSong == null) {
//            logger.severe("No song to play");
//            return;
//        }
        try {
            player = new AdvancedPlayer(new FileInputStream("Blinding_Lights.mp3"));
            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackStarted(PlaybackEvent evt) {
                    // do something?
                }

                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    frame = evt.getFrame();
                }
            });
            player.play(frame, Integer.MAX_VALUE);
        } catch (FileNotFoundException | JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        logger.info("Player: Paused " + currentSong.title);
    }

    public void stop() {
        logger.info("Player: Stopped");
    }
}
