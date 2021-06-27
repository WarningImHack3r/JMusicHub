package lethimonnier.antoine.jmusichub.cli.interfaces;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * AudioContent
 */
public abstract class AudioContent implements Serializable {

	@Serial
	private static final long serialVersionUID = 1948714750895464113L;
	/**
	 * The Title.
	 */
	private String title;
	/**
	 * The Authors.
	 */
	private String[] authors;
	/**
	 * The Duration.
	 */
	private int duration;
	private final UUID id;

	/**
	 * Instantiates a new Audio content.
	 *
	 * @param title    the title
	 * @param authors  the authors
	 * @param duration the duration
	 */
	protected AudioContent(@NotNull String title, @NotNull String[] authors, int duration) {
		setTitle(title.trim());
		for (var i = 0; i < authors.length; i++) {
			authors[i] = authors[i].trim();
		}
		setAuthors(authors);
		setDuration(duration);
		id = UUID.randomUUID();
	}

	/**
	 * Gets formatted duration.
	 *
	 * @param duration the duration
	 * @return the formatted duration
	 */
	@NotNull
	@Contract(pure = true)
	public static String getFormattedDuration(long duration) {
		if (duration < 60)
			return duration + "s";
		if (duration < 3600)
			return (duration / 60) + "m " + (duration % 60) + "s";
		if (duration < 24 * 3600)
			return (duration / 3600) + "h " + ((duration % 3600) / 60) + "m " + ((duration % 3600) % 60) + "s";
		return (duration / 86400) + "d " + ((duration % 86400) / 3600) + "h " + (((duration % 86400) % 3600) / 60) +
				       "m " + (((duration % 86400) % 3600) % 60) + "s";
	}

	/**
	 * Sets title.
	 *
	 * @param title the title
	 */
	public void setTitle(@NotNull String title) {
        this.title = title.trim();
    }

	/**
	 * Sets duration.
	 *
	 * @param duration the duration
	 */
	public void setDuration(int duration) {
        this.duration = duration;
    }

	/**
	 * Gets title.
	 *
	 * @return the title
	 */
	public String getTitle() {
        return title;
    }

	/**
	 * Get authors string [ ].
	 *
	 * @return the string [ ]
	 */
	public String[] getAuthors() {
        return authors;
    }

	/**
	 * Gets duration.
	 *
	 * @return the duration
	 */
	public int getDuration() {
        return duration;
    }

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Sets authors.
	 *
	 * @param authors the authors
	 */
	public void setAuthors(@NotNull String[] authors) {
		for (var i = 0; i < authors.length; i++) {
			authors[i] = authors[i].trim();
        }
        this.authors = authors;
    }
}
