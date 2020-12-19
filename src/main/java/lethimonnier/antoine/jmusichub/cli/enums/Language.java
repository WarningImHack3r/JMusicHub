package lethimonnier.antoine.jmusichub.cli.enums;

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
	public String getName() {
        return name;
    }

	/**
	 * Get string values string [ ].
	 *
	 * @return the string [ ]
	 */
	public static String[] getStringValues() {
        String[] vals = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            vals[i] = values()[i].getName();
        }
        return vals;
    }
}
