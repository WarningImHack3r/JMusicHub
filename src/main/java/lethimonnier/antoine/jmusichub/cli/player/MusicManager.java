package lethimonnier.antoine.jmusichub.cli.player;

import lethimonnier.antoine.jmusichub.cli.MusicHub;
import lethimonnier.antoine.jmusichub.cli.logging.MusicLogger;

import java.util.Scanner;
import java.util.logging.Logger;

public class MusicManager {

	private static final Scanner scanner = MusicHub.scanner;
	private final Logger logger = MusicLogger.getLogger("output.log");

	public MusicManager() {
		logger.info("MusicHub - Music Player");
		startManager();
	}

	private void startManager() {
		while (true) {
			logger.info("What do you want to do? (Press 'h' to show help)");
			switch (scanner.nextLine().toLowerCase().charAt(0)) {
				case 'm' -> MusicPlayer.getPlayer().setPlayingSong(null).play();

				case 'h' -> logger.info("""
						MusicHub - MusicPlayer help - available options
						m: play music
						p: pause/stop music
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
