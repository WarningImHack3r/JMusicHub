package lethimonnier.antoine.jmusichub.cli;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;

/**
 * The type Utils.
 */
public class Utils {

	private Utils() {
	}

	/**
	 * Returns a formatted <code>Date</code> as a <code>String</code>
	 *
	 * @param date the <code>Date</code> to format
	 * @return the formatted <code>Date</code> as a <code>String</code>
	 */
	@NotNull
	public static String getFormattedDate(@NotNull Date date) {
		return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
	}

	/**
	 * Returns a <code>Date</code> object from a <code>String</code> input.
	 *
	 * @param dateToParse the <code>String</code> date to parse
	 * @return the parsed <code>Date</code>
	 */
	@NotNull
	@Contract("_ -> new")
	public static Date getDateFromString(String dateToParse) {
		return java.sql.Date.valueOf(LocalDate.parse(dateToParse, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
	}

	/**
	 * Gets project's root path
	 *
	 * @return the root path
	 */
	public static Path getRootPath() {
		try {
			var resource = Thread.currentThread().getContextClassLoader().getResource(".");
			if (resource == null)
				throw new URISyntaxException("", "");
			var classLoaderPath = Paths.get(resource.toURI());
			for (var i = 0; i < 2; i++) {
				classLoaderPath = classLoaderPath.getParent() != null ? classLoaderPath.getParent() : classLoaderPath;
			}
			return classLoaderPath;
		} catch (URISyntaxException e) {
			return Paths.get(Utils.class.getProtectionDomain().getCodeSource().getLocation().toString());
		}
	}
}
