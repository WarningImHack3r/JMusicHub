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

    private static final long serialVersionUID = 6521686263527169809L;
    public String name;
	public transient List<AudioContent> content;
    private long totalDuration = 0;
    private final Date creationDate;
    private Date lastModifiedDate;
    private final UUID id;

	/**
	 * Instantiates a new Playlist.
	 *
	 * @param name    the name
	 * @param content the content
	 */
	public Playlist(String name, List<AudioContent> content) {
        this.name = name.trim();
        setContent(content);
        creationDate = new Date();
        lastModifiedDate = creationDate;
        id = UUID.randomUUID();
    }

	/**
	 * Sets content.
	 *
	 * @param content the content
	 */
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

	/**
	 * Sets name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
        this.name = name.trim();
    }

	/**
	 * Gets content.
	 *
	 * @return the content
	 */
	public List<AudioContent> getContent() {
        return content;
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
	 * Gets total duration.
	 *
	 * @return the total duration
	 */
	public long getTotalDuration() {
        return totalDuration;
    }

	/**
	 * Gets creation date.
	 *
	 * @return the creation date
	 */
	public Date getCreationDate() {
        return creationDate;
    }

	/**
	 * Gets last modified date.
	 *
	 * @return the last modified date
	 */
	public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder audioContent = new StringBuilder();
        if (content != null) {
            for (AudioContent ac : content) {
                if (ac instanceof Song) {
                    Song song = (Song) ac;
                    audioContent.append(";").append(song.toString().replace(";", "-"));
                } else {
                    AudioBook audioBook = (AudioBook) ac;
                    audioContent.append(";").append(audioBook.toString().replace(";", "-"));
                }
            }
        }
        return getName() + audioContent;
    }
}
