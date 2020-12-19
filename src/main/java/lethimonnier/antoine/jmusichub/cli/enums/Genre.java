package lethimonnier.antoine.jmusichub.cli.enums;

/**
 * Genre
 */
public enum Genre {

	/**
	 * Jazz genre.
	 */
	JAZZ("Jazz"),
	/**
	 * Classical genre.
	 */
	CLASSICAL("Classical"),
	/**
	 * Hiphop genre.
	 */
	HIPHOP("Hip-Hop"),
	/**
	 * Rock genre.
	 */
	ROCK("Rock"),
	/**
	 * Pop genre.
	 */
	POP("Pop"),
	/**
	 * Rap genre.
	 */
	RAP("Rap");

    private final String name;

    Genre(String name) {
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
