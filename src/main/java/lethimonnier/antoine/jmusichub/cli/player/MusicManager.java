package lethimonnier.antoine.jmusichub.cli.player;

import lethimonnier.antoine.jmusichub.cli.MusicHub;
import lethimonnier.antoine.jmusichub.cli.logging.MusicLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Music manager.
 */
public class MusicManager {

	private final Logger logger = MusicLogger.getLogger("output.log");
	private static final Scanner scanner = MusicHub.scanner;
	private static final Map<Integer, String> songs = new HashMap<>();

	static {
		songs.put(0, "Pool.mp3");
		songs.put(1, "Kalimba.mp3");
		songs.put(2, "Peaches.mp3");
		songs.put(3, "Blinding Lights.mp3");
		songs.put(4, "Leave The Door Open.mp3");
	}

	/**
	 * Instantiates a new Music manager.
	 */
	public MusicManager() {
		logger.info("MusicHub - Music Player");
		startManager();
	}

	/**
	 * Gets song at index.
	 *
	 * @param index the index
	 * @return the song at index
	 */
	public static String getSongAtIndex(int index) {
		return songs.get(index);
	}

	@NotNull
	@Contract(pure = true)
	private String getSongsList() {
		var builder = new StringBuilder();
		songs.forEach((key, value) -> builder.append(key + 1).append(": ").append(value.replaceAll(".mp3", "")).append(System.lineSeparator()));
		return builder.toString();
	}

	private void startManager() {
		while (true) {
			logger.info("What do you want to do? (Press 'h' to show help)");
			switch (scanner.nextLine().toLowerCase().charAt(0)) {
				case 'm' -> {
					int index;
					do {
						logger.log(Level.INFO, "Choose an index to play:{0}{1}", new Object[] { System.lineSeparator(), getSongsList() });
						index = scanner.nextInt();
						scanner.nextLine();
					} while (index < 0 || index > songs.size());
					var player = MusicPlayer.getPlayer();
					if (player.isPlaying()) player.stop();
					player.setPlayingSongIndex(index).play();
				}

				case 'p' -> {
					var player = MusicPlayer.getPlayer();
					if (player.isPlaying())
						player.stop();
					else
						logger.warning("No music is currently playing");
				}

				case 's' -> logger.log(Level.INFO, "{0}{1}", new Object[] { System.lineSeparator(), getSongsList() });

				case 'h' -> logger.info("""
						MusicHub - MusicPlayer help - available options
						m: play music
						p: pause/stop music (when available)
						s: see available songs
						h: shows this help menu
						q: quit the player and returns to the main menu
						""");

				case 'q' -> {
					logger.info("Back to main menu, bye!");
					return;
				}

				default -> logger.info("Unknown option");
			}
		}
	}
}
