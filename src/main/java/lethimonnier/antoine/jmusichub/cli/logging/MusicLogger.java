package lethimonnier.antoine.jmusichub.cli.logging;

import lethimonnier.antoine.jmusichub.cli.Utils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Music logger.
 */
public class MusicLogger extends Logger {

	private final Logger casualLogger = Logger.getLogger(getClass().getSimpleName(), null);
	private final String path;

	private MusicLogger(String filePath) {
		super(MusicLogger.class.getName(), null);
		path = filePath;
	}

	/**
	 * Gets an instance of the logger.
	 *
	 * @param filePath the file path for the output file
	 * @return the logger
	 */
	@NotNull
	@Contract("_ -> new")
	public static MusicLogger getLogger(String filePath) {
		return new MusicLogger(filePath);
	}

	/**
	 * Outputs each log to the file path specified in constructor
	 *
	 * @param filePath the path of the file to output in
	 * @param level the log level
	 * @param message the message
	 */
	private void logToFile(String filePath, @NotNull Level level, String message) {
		String logLine =
				"(" + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)) + ") [" + level + "] " + message;
		try {
			Files.writeString(Paths.get(Utils.getRootPath().toString(), filePath), logLine + System.lineSeparator(),
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void log(Level level, String msg) {
		casualLogger.log(level, msg);
		logToFile(path, level, msg);
	}

	@Override
	public void log(Level level, String msg, Object param1) {
		casualLogger.log(level, msg, param1);
		logToFile(path, level, MessageFormat.format(msg, param1));
	}

	@Override
	public void log(Level level, String msg, Object[] params) {
		casualLogger.log(level, msg, params);
		logToFile(path, level, MessageFormat.format(msg, params));
	}

	@Override
	public void info(String msg) {
		casualLogger.info(msg);
		logToFile(path, Level.INFO, msg);
	}

	@Override
	public void info(Supplier<String> msgSupplier) {
		casualLogger.info(msgSupplier);
		logToFile(path, Level.INFO, msgSupplier.get());
	}

	@Override
	public void fine(String msg) {
		casualLogger.fine(msg);
		logToFile(path, Level.FINE, msg);
	}

	@Override
	public void fine(Supplier<String> msgSupplier) {
		casualLogger.fine(msgSupplier);
		logToFile(path, Level.FINE, msgSupplier.get());
	}

	@Override
	public void warning(String msg) {
		casualLogger.warning(msg);
		logToFile(path, Level.WARNING, msg);
	}

	@Override
	public void warning(Supplier<String> msgSupplier) {
		casualLogger.warning(msgSupplier);
		logToFile(path, Level.WARNING, msgSupplier.get());
	}

	@Override
	public void severe(String msg) {
		casualLogger.severe(msg);
		logToFile(path, Level.SEVERE, msg);
	}

	@Override
	public void severe(Supplier<String> msgSupplier) {
		casualLogger.severe(msgSupplier);
		logToFile(path, Level.SEVERE, msgSupplier.get());
	}
}
