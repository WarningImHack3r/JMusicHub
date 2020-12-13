package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Playlist
 */
public class Playlist implements Serializable {

    private transient ArrayList<AudioContent> content;
    private String name;
    private long totalDuration = 0;
    private final Date creationDate;
    private Date lastModifiedDate;
    private final UUID id;

    public Playlist(String name, List<AudioContent> content) {
        this.name = name.trim();
        setContent(content);
        creationDate = new Date();
        lastModifiedDate = creationDate;
        id = UUID.randomUUID();
    }

    public void setContent(List<AudioContent> content) {
        if (content == null)
            return;
        this.content = new ArrayList<>(content);
        totalDuration = 0;
        for (AudioContent audioContent : this.content) {
            if (audioContent != null)
                totalDuration += audioContent.getDuration();
        }
        lastModifiedDate = new Date();
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public List<AudioContent> getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder audioContent = new StringBuilder();
        for (AudioContent ac : content) {
            if (ac.getClass().isAssignableFrom(Song.class)) {
                Song song = (Song) ac;
                audioContent.append(";").append(song.toString());
            } else {
                AudioBook audioBook = (AudioBook) ac;
                audioContent.append(";").append(audioBook.toString());
            }
        }
        return getName() + ";" + audioContent;
    }
}
