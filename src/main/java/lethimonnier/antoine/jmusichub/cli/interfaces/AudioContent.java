package lethimonnier.antoine.jmusichub.cli.interfaces;

import java.util.UUID;

/**
 * AudioContent
 */
public abstract class AudioContent {

    private String title;
    private String[] authors;
    private int duration;
    private UUID id;
    private Object content = null;

    protected AudioContent(String title, String[] authors, int duration) {
        setTitle(title);
        setAuthors(authors);
        setDuration(duration);
        id = UUID.randomUUID();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public int getDuration() {
        return duration;
    }

    public UUID getId() {
        return id;
    }

    public Object getContent() {
        return content;
    }
}
