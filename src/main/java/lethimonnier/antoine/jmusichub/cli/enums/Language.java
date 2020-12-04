package lethimonnier.antoine.jmusichub.cli.enums;

/**
 * Language
 */
public enum Language {
    
    FRENCH("French"), ENGLISH("English"), ITALIAN("Italian"), SPANISH("Spanish"),
    DEUTSCH("Deutsch");

    private String name;

    private Language(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
