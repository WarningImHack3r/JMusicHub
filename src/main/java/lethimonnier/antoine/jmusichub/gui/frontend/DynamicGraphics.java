package lethimonnier.antoine.jmusichub.gui.frontend;

import com.github.lgooddatepicker.components.DatePicker;
import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.classes.parsing.ConsoleParser;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;
import lethimonnier.antoine.jmusichub.gui.backend.InterfacesLinker;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.Serial;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

/**
 * The type Dynamic graphics.
 */
public class DynamicGraphics {

	private static final Dimension DEF_TEXT_FIELD_DIM = new Dimension(200, 20);
	private static final Dimension DEF_COMBO_BOX_DIM = new Dimension(150, 20);
	private Field theField;
	private final JPanel playlistAddPanel = new JPanel();
	private final JPanel songAddPanel = new JPanel();
	private final JPanel albumAddPanel = new JPanel();
	private final JPanel audioAddPanel = new JPanel();
	private final JPanel addButtonCenterPlaylist = new JPanel();
	private final JPanel addButtonCenterAlbum = new JPanel();
	private final JPanel addButtonCenterAudio = new JPanel();
	private final JPanel addButtonCenterSong = new JPanel();
	/**
	 * The Add content.
	 */
	// Listeners
	ActionListener addContent = e -> {
		int selected = JOptionPane.showOptionDialog(null, "Which content do you want to add?", "Add audio content",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] { "Song", "AudioBook",
						"Cancel" }, 0);
		switch (selected) {
			case 0:
				// song
				addElementToPanel(Song.class, playlistAddPanel, addButtonCenterPlaylist);
				break;

			case 1:
				// audiobook
				addElementToPanel(AudioBook.class, playlistAddPanel, addButtonCenterPlaylist);
				break;

			default:
				break;
		}
	};

	@NotNull
	private String toFirstLetterCap(@NotNull String input) {
		return input.substring(0, 1).toUpperCase() + input.substring(1);
	}

	/**
	 * Fetchs the <code>type</code>'s fields to create adapted user inputs.
	 * <strong>Requires fields to be public</strong>.
	 *
	 * @param type the class to look into
	 * @return the generated panel
	 */
	public JPanel addPanelForType(@NotNull Class<?> type) {
		var panel = new JPanel();
		ArrayList<JComboBox<String>> combos = new ArrayList<>();

		for (Field field : type.getFields()) {
			theField = field;
			manageFieldForPanel(panel, combos);
		}
		for (JComboBox<?> combo : combos) {
			panel.add(combo);
		}
		return panel;
    }

    private void manageFieldForPanel(JPanel panel, ArrayList<JComboBox<String>> combos) {
        Class<?> fieldType = theField.getType();
        if (Enum.class.isAssignableFrom(fieldType)) { // Enums
            // dropdown menu
	        JComboBox<String> combo;
            if (fieldType.equals(Genre.class)) {
                combo = new JComboBox<>(Genre.getStringValues());
            } else if (fieldType.equals(Category.class)) {
                combo = new JComboBox<>(Category.getStringValues());
            } else if (fieldType.equals(Language.class)) {
                combo = new JComboBox<>(Language.getStringValues());
            } else {
                return;
            }
            combo.setPreferredSize(DEF_COMBO_BOX_DIM);
            combos.add(combo);
        } else if (fieldType.equals(Long.TYPE) || fieldType.equals(Integer.TYPE)) { // int/long
            // stepper
            panel.add(new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1)));
        } else if (fieldType.equals(Date.class)) { // Dates
            // calendar
            panel.add(new DatePicker());
        } else if (fieldType.isArray() && fieldType.getComponentType().equals(Song.class)) {
            // Song[]: create a button for the specified type
	        var addSong = new JButton("Add song");
            addSong.addActionListener(e -> addElementToPanel(Song.class, albumAddPanel, addButtonCenterAlbum));
            panel.add(addSong);
        } else if (Collection.class.isAssignableFrom(fieldType)) { // Lists
            // more or less the same
            Class<?> collClass = (Class<?>) ((ParameterizedType) theField.getGenericType()).getActualTypeArguments()[0];
            if (collClass.equals(AudioContent.class)) {
	            var btn = new JButton("Add audio content");
                btn.addActionListener(addContent);
                panel.add(btn);
            }
        } else { // others
	        // normal
	        var jTextGetNameField = new JTextField(toFirstLetterCap(theField.getName()));
            jTextGetNameField.setForeground(Color.GRAY);
            jTextGetNameField.setPreferredSize(DEF_TEXT_FIELD_DIM);
            jTextGetNameField.addFocusListener(new FieldFocusListener(toFirstLetterCap(theField.getName())));
            panel.add(jTextGetNameField);
        }
    }

	/**
	 * Add to library from components.
	 *
	 * @param comps   the comps
	 * @param library the library
	 */
	public void addToLibraryFromComponents(List<Component> comps, Library library) {
        List<Component> filteredComps = new ArrayList<>(comps);
        for (Component component : comps) { // remove JButtons
            if (!(component instanceof JButton))
                filteredComps.add(component);
        }
        comps = filteredComps;

        ArrayList<Integer> objStarts = new ArrayList<>();
        for (Component component : comps) { // list indexes of new objects starts
            int index = comps.indexOf(component);
            if (index == 0 || (component instanceof JTextField && !(comps.get(index - 1) instanceof JTextField))
                    || index == objStarts.size() - 1) // new object
                objStarts.add(index);
        }
        // NOT FINISHED, MISS SOME TIME
		for (var i = 0; i < objStarts.size(); i++) {
			if (objStarts.get(i) == objStarts.size() - 1) // don't use last element
				continue;

			if (objStarts.get(i + 1) - objStarts.get(i) == 1) // playlist
				library.addToPlaylistsLibrary(new Playlist("", new ArrayList<>())); // titre audiocontents
			else if (objStarts.get(i + 1) - objStarts.get(i) == 3) // album
				library.addToAlbumsLibrary(new Album(new Song[] {}, "", "", new Date())); // songs titre auteur date
			else if (objStarts.get(i + 1) - objStarts.get(i) == 4) // song
				library.addToSongsLibrary(new Song("", new String[] {}, 0, null)); // titre auteurs durée genre
			else if (objStarts.get(i + 1) - objStarts.get(i) == 5) // audiobook
				library.addToAudioBooksLibrary(new AudioBook("", "", 0, null, null)); // titre auteur durée langue
                                                                                          // catégorie
		}
	}

	/**
	 * Add element to panel.
	 *
	 * @param type      the type
	 * @param panel     the panel
	 * @param btnCenter the btn center
	 */
	public void addElementToPanel(Class<?> type, @NotNull Container panel, Component btnCenter) {
		panel.remove(btnCenter);
		panel.add(addPanelForType(type));
		panel.add(btnCenter);
		panel.revalidate();
		panel.repaint();
    }

	/**
	 * Creates a panel to remove instances of class <code>type</code>.
	 *
	 * @param type    the class to fetch
	 * @param library the library
	 * @return the generated panel
	 */
	public JPanel removePanelForType(Class<?> type, Library library) {
		var tableRemovePanel = new JPanel();
		var removeBtn = new JButton("Delete row(s)");
		removeBtn.setEnabled(false);
		var removeTable = new JTable(getTableModelForType(type));
		removeTable.getSelectionModel().addListSelectionListener(e -> removeBtn.setEnabled(!removeTable.getSelectionModel().isSelectionEmpty()));
		removeBtn.addActionListener(e -> {
			var consoleParser = new ConsoleParser();
			for (var i = 0; i < removeTable.getSelectedRows().length; i++) {
				var line = new String[removeTable.getModel().getColumnCount()];
				for (var j = 0; j < removeTable.getColumnCount(); j++) {
					Object value = removeTable.getModel().getValueAt(removeTable.getSelectedRows()[i], j);
					line[j] = value != null ? value.toString() : "";
					if (type.equals(Album.class) || type.equals(Playlist.class)) {
						while (line[0].equals("")) {
							line[j] =
									removeTable.getModel().getValueAt(removeTable.getSelectedRows()[i] - 1, j).toString();
						}
					}
				}
				if (type.equals(Song.class)) {
                    library.getStoredSongs().remove(consoleParser.getSongFromString(line[1], library));
                } else if (type.equals(AudioBook.class)) {
                    library.getStoredAudioBooks().remove(consoleParser.getAudioBookFromString(line[2], library));
                } else if (type.equals(Album.class)) {
                    library.getStoredAlbums().remove(consoleParser.getAlbumFromString(line[0], library));
                } else if (type.equals(Playlist.class)) {
                    library.getStoredPlaylists().remove(consoleParser.getPlaylistFromString(line[0], library));
                }
            }
            JOptionPane.showMessageDialog(tableRemovePanel, "Please use the File > Refresh tables to see the changes",
                    "Refresh required", JOptionPane.INFORMATION_MESSAGE);
        });
        tableRemovePanel.add(new JScrollPane(removeTable));
        tableRemovePanel.add(removeBtn);
        formatTable(removeTable);
        return tableRemovePanel;
    }

	/**
	 * Creates a panel to view existing instances of class <code>type</code>
	 *
	 * @param type the class to fetch
	 * @return the generated panel
	 */
	public JPanel viewPanelForType(Class<?> type) {
		var tablePanel = new JPanel();
		var table = new JTable(getTableModelForType(type));
		tablePanel.add(new JScrollPane(table));
		formatTable(table);
		return tablePanel;
	}

	@NotNull
	private DefaultTableModel getTableModelForType(@NotNull Class<?> type) {
		ArrayList<Field> typeFields = new ArrayList<>(Arrays.asList(type.getFields()));
		ArrayList<Field> fields = new ArrayList<>(typeFields);
		for (Field field : typeFields) { // removing Song[] and Collection<AudioContent>
			Class<?> fieldType = field.getType();
			if (fieldType.isArray() && fieldType.getComponentType().equals(Song.class)) {
				fields.remove(field);
				fields.addAll(Arrays.asList(Song.class.getFields()));
			}
			if (Collection.class.isAssignableFrom(fieldType) && ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(AudioContent.class)) {
				fields.remove(field);
				fields.addAll(Arrays.asList(AudioContent.class.getFields()));
				for (Field declaredField : Stream.concat(Arrays.stream(Song.class.getDeclaredFields()),
						Arrays.stream(AudioBook.class.getDeclaredFields())).toArray(Field[]::new)) {
					if (!declaredField.getName().equals("serialVersionUID"))
						fields.add(declaredField);
				}
			}
		}
		var cols = new String[fields.size()];
		for (var i = 0; i < fields.size(); i++) { // getting columns names
			String name = fields.get(i).getName().replaceAll("(.)([A-Z])", "$1 $2");
			cols[i] = name.substring(0, 1).toUpperCase() + name.substring(1);
		}

		return new DefaultTableModel(InterfacesLinker.getInstance().getLibraryContentForType(type), cols) {

			@Serial
			private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int column) {
	            for (var row = 0; row < getRowCount(); row++) {
		            Object o = getValueAt(row, column);
		            if (o != null) {
			            return o.getClass();
                    }
                }
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
		};
	}

	private void formatTable(@NotNull JTable table) {
		table.getTableHeader().setReorderingAllowed(false);
		var centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		for (var column = 0; column < table.getColumnCount(); column++) {
			TableCellRenderer renderer = table.getColumnModel().getColumn(column).getHeaderRenderer();
			if (renderer == null) {
				renderer = table.getTableHeader().getDefaultRenderer();
			}
			int width = renderer.getTableCellRendererComponent(table, table.getColumnModel().getColumn(column).getHeaderValue(), false, false, 0, 0).getPreferredSize().width;
			for (var row = 0; row < table.getRowCount(); row++) {
				width =
						Math.max(table.prepareRenderer(table.getCellRenderer(row, column), row, column).getPreferredSize().width + 1, width);
            }
            table.getColumnModel().getColumn(column).setPreferredWidth(width + 5);
            table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
            ((JLabel) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

	/**
	 * Gets add button center song.
	 *
	 * @return the add button center song
	 */
	// GETTERS
    public JPanel getAddButtonCenterSong() {
        return addButtonCenterSong;
    }

	/**
	 * Gets add button center audio.
	 *
	 * @return the add button center audio
	 */
	public JPanel getAddButtonCenterAudio() {
        return addButtonCenterAudio;
    }

	/**
	 * Gets add button center album.
	 *
	 * @return the add button center album
	 */
	public JPanel getAddButtonCenterAlbum() {
        return addButtonCenterAlbum;
    }

	/**
	 * Gets add button center playlist.
	 *
	 * @return the add button center playlist
	 */
	public JPanel getAddButtonCenterPlaylist() {
        return addButtonCenterPlaylist;
    }

	/**
	 * Gets song add panel.
	 *
	 * @return the song add panel
	 */
	public JPanel getSongAddPanel() {
        return songAddPanel;
    }

	/**
	 * Gets audio add panel.
	 *
	 * @return the audio add panel
	 */
	public JPanel getAudioAddPanel() {
        return audioAddPanel;
    }

	/**
	 * Gets album add panel.
	 *
	 * @return the album add panel
	 */
	public JPanel getAlbumAddPanel() {
        return albumAddPanel;
    }

	/**
	 * Gets playlist add panel.
	 *
	 * @return the playlist add panel
	 */
	public JPanel getPlaylistAddPanel() {
		return playlistAddPanel;
	}

	/**
	 * Refresh tables.
	 *
	 * @param tabs    the tabs
	 * @param library the library
	 */
	public void refreshTables(@NotNull JTabbedPane[] tabs, Library library) {
		var classes = new Class[] { Song.class, AudioBook.class, Album.class, Playlist.class };
		for (var i = 0; i < tabs.length * 2; i++) {
			if (i > 3) { // view panels
				tabs[i - 4].setComponentAt(tabs[i - 4].getTabCount() - 1, viewPanelForType(classes[i - 4]));
			} else { // remove panels
				tabs[i].setComponentAt(tabs[i].getTabCount() - 2, removePanelForType(classes[i], library));
			}
		}
	}
}

/**
 * The type Field focus listener.
 */
record FieldFocusListener(String fieldName) implements FocusListener {

	@Override
	public void focusGained(@NotNull FocusEvent e) {
		if (((JTextField) e.getSource()).getText().equals(fieldName)) {
			((JTextField) e.getSource()).setText("");
			((JTextField) e.getSource()).setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(@NotNull FocusEvent e) {
		if (((JTextField) e.getSource()).getText().isEmpty()) {
			((JTextField) e.getSource()).setForeground(Color.GRAY);
			((JTextField) e.getSource()).setText(fieldName);
		}
	}
}
