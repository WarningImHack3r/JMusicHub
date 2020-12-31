package lethimonnier.antoine.jmusichub.gui.frontend;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.github.lgooddatepicker.components.DatePicker;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;
import lethimonnier.antoine.jmusichub.gui.backend.InterfacesLinker;

public class DynamicGraphics {

    private static final Dimension DEF_TEXT_FIELD_DIM = new Dimension(200, 20);
    private static final Dimension DEF_COMBO_BOX_DIM = new Dimension(150, 20);
    private Field theField;
    private JPanel playlistAddPanel = new JPanel();
    private JPanel songAddPanel = new JPanel();
    private JPanel albumAddPanel = new JPanel();
    private JPanel audioAddPanel = new JPanel();
    private JPanel addButtonCenterPlaylist = new JPanel();
    private JPanel addButtonCenterAlbum = new JPanel();
    private JPanel addButtonCenterAudio = new JPanel();
    private JPanel addButtonCenterSong = new JPanel();

    private String toFirstLetterCap(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Fetchs the <code>type</code>'s fields to create adapted user inputs.
     * <strong>Requires fields to be public</strong>.
     * 
     * @param type the class to look into
     * @return the generated panel
     */
    public JPanel addPanelForType(Class<?> type) {
        JPanel panel = new JPanel();
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
            JComboBox<String> combo = null;
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
            JButton addSong = new JButton("Add song");
            addSong.addActionListener(e -> addElementToPanel(Song.class, albumAddPanel, addButtonCenterAlbum));
            panel.add(addSong);
        } else if (Collection.class.isAssignableFrom(fieldType)) { // Lists
            // more or less the same
            Class<?> collClass = (Class<?>) ((ParameterizedType) theField.getGenericType()).getActualTypeArguments()[0];
            if (collClass.equals(AudioContent.class)) {
                JButton btn = new JButton("Add audio content");
                btn.addActionListener(addContent);
                panel.add(btn);
            }
        } else { // others
            // normal
            JTextField jTextGetNameField = new JTextField(toFirstLetterCap(theField.getName()));
            jTextGetNameField.setForeground(Color.GRAY);
            jTextGetNameField.setPreferredSize(DEF_TEXT_FIELD_DIM);
            jTextGetNameField.addFocusListener(new FieldFocusListener(toFirstLetterCap(theField.getName())));
            panel.add(jTextGetNameField);
        }
    }

    public void addToLibraryFromComponents(List<Component> comps) {
        for (Component component : comps) {
            int index = comps.indexOf(component);
            if (index == 0 || component instanceof JButton)
                continue;
            if (component instanceof JTextField && !(comps.get(index - 1) instanceof JTextField)) {
                // new object
                // TODO finish
            }
        }
    }

    public void addElementToPanel(Class<?> type, Container panel, Component btnCenter) {
        panel.remove(btnCenter);
        panel.add(addPanelForType(type));
        panel.add(btnCenter);
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Creates a panel to remove instances of class <code>type</code>.
     * 
     * @param type the class to fetch
     * @return the generated panel
     */
    public JPanel removePanelForType(Class<?> type) {
        return new JPanel();
    }

    /**
     * Creates a panel to view existing instances of class <code>type</code>
     * 
     * @param type the class to fetch
     * @return the generated panel
     */
    public JPanel viewPanelForType(Class<?> type) {
        JPanel tablePanel = new JPanel();
        JTable table = new JTable(getTableModelForType(type));
        tablePanel.add(new JScrollPane(table));
        formatTable(table);
        return tablePanel;
    }

    private DefaultTableModel getTableModelForType(Class<?> type) {
        ArrayList<Field> typeFields = new ArrayList<>(Arrays.asList(type.getFields()));
        ArrayList<Field> fields = new ArrayList<>(typeFields);
        for (Field field : typeFields) { // removing Song[] and Collection<AudioContent>
            Class<?> fieldType = field.getType();
            if ((fieldType.isArray() && fieldType.getComponentType().equals(Song.class))
                    || (Collection.class.isAssignableFrom(fieldType)
                            && ((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0])
                                    .equals(AudioContent.class)))
                fields.remove(field);
        }
        String[] cols = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) { // getting columns names
            String name = fields.get(i).getName().replaceAll("(.)([A-Z])", "$1 $2");
            cols[i] = name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        return new DefaultTableModel(InterfacesLinker.getInstance().getLibraryContentForType(type), cols) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int column) {
                for (int row = 0; row < getRowCount(); row++) {
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

    private void formatTable(JTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableCellRenderer renderer = table.getColumnModel().getColumn(column).getHeaderRenderer();
            if (renderer == null) {
                renderer = table.getTableHeader().getDefaultRenderer();
            }
            int width = renderer.getTableCellRendererComponent(table,
                    table.getColumnModel().getColumn(column).getHeaderValue(), false, false, 0, 0)
                    .getPreferredSize().width;
            for (int row = 0; row < table.getRowCount(); row++) {
                width = Math.max(
                        table.prepareRenderer(table.getCellRenderer(row, column), row, column).getPreferredSize().width
                                + 1,
                        width);
            }
            table.getColumnModel().getColumn(column).setPreferredWidth(width + 5);
            table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
            ((JLabel) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public void refreshTables(JTabbedPane[] tabs) {
        Class<?>[] classes = { Song.class, AudioBook.class, Album.class, Playlist.class };
        for (int i = 0; i < tabs.length * 2; i++) {
            if (i > 3) { // view panels
                tabs[i - 4].setComponentAt(tabs[i - 4].getTabCount() - 1,
                        new JScrollPane(viewPanelForType(classes[i - 4]),
                                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
            } else { // remove panels
                tabs[i].setComponentAt(tabs[i].getTabCount() - 2,
                        new JScrollPane(viewPanelForType(classes[i]), ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)); // TODO add button to delete
            }
        }
    }

    // GETTERS
    public JPanel getAddButtonCenterSong() {
        return addButtonCenterSong;
    }

    public JPanel getAddButtonCenterAudio() {
        return addButtonCenterAudio;
    }

    public JPanel getAddButtonCenterAlbum() {
        return addButtonCenterAlbum;
    }

    public JPanel getAddButtonCenterPlaylist() {
        return addButtonCenterPlaylist;
    }

    public JPanel getSongAddPanel() {
        return songAddPanel;
    }

    public JPanel getAudioAddPanel() {
        return audioAddPanel;
    }

    public JPanel getAlbumAddPanel() {
        return albumAddPanel;
    }

    public JPanel getPlaylistAddPanel() {
        return playlistAddPanel;
    }

    // Listeners
    ActionListener addContent = e -> {
        int selected = JOptionPane.showOptionDialog(null, "Which content do you want to add?", "Add audio content",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] { "Song", "AudioBook", "Cancel" },
                0);
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
}

class FieldFocusListener implements FocusListener {

    private String fieldName;

    public FieldFocusListener(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (((JTextField) e.getSource()).getText().equals(fieldName)) {
            ((JTextField) e.getSource()).setText("");
            ((JTextField) e.getSource()).setForeground(Color.BLACK);
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (((JTextField) e.getSource()).getText().isEmpty()) {
            ((JTextField) e.getSource()).setForeground(Color.GRAY);
            ((JTextField) e.getSource()).setText(fieldName);
        }
    }
}
