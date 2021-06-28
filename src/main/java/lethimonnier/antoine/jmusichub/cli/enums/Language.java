package lethimonnier.antoine.jmusichub.cli.enums;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Language
 */
public enum Language {

	/**
	 * French language.
	 */
	FRENCH("French"),
	/**
	 * English language.
	 */
	ENGLISH("English"),
	/**
	 * Italian language.
	 */
	ITALIAN("Italian"),
	/**
	 * Spanish language.
	 */
	SPANISH("Spanish"),
	/**
	 * Deutsch language.
	 */
	DEUTSCH("Deutsch");

    private final String name;

    Language(String name) {
        this.name = name;
    }

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	@Contract(pure = true)
	public String getName() {
		return name;
	}

	/**
	 * Get string values string [ ].
	 *
	 * @return the string [ ]
	 */
	@NotNull
	public static String[] getStringValues() {
		var vals = new String[values().length];
		for (var i = 0; i < values().length; i++) {
			vals[i] = values()[i].getName();
		}
		return vals;
    }
}
