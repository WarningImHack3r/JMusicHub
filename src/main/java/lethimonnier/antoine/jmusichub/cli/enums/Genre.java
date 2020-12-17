package lethimonnier.antoine.jmusichub.cli.enums;

/**
 * Genre
 */
public enum Genre {

    JAZZ("Jazz"), CLASSICAL("Classical"), HIPHOP("Hip-Hop"), ROCK("Rock"), POP("Pop"), RAP("Rap");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String[] getStringValues() {
        String[] vals = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            vals[i] = values()[i].getName();
        }
        return vals;
    }
}
