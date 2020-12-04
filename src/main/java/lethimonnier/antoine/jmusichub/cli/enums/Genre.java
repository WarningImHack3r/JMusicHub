package lethimonnier.antoine.jmusichub.cli.enums;

/**
 * Genre
 */
public enum Genre {
    
    JAZZ("Jazz"), CLASSICAL("Classical"), HIPHOP("Hip-Hop"),
    ROCK("Rock"), POP("Pop"), RAP("Rap");

    private String name;

    private Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
