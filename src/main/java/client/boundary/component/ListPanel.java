package client.boundary.component;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;

/**
 * ListPanel class is the custom list renderer for rendering JPanel with its own layout. It uses
 * JList as the list.
 *
 * @author  Pratchaya Khansomboon
 * @version 1.0
 */
public class ListPanel<T extends JPanel> {
    /**
     * Custom cell renderer with JPanel.
     */
    private class PanelRenderer implements ListCellRenderer<Object> {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            var panel = (JPanel) value;

            if (isSelected && list.isEnabled()) {
                panel.setBackground(list.getSelectionBackground());
                panel.setForeground(list.getSelectionForeground());
            } else {
                panel.setBackground(list.getBackground());
                panel.setForeground(list.getForeground());
            }

            return panel;
        }
    }

    // ListPanel property
    private JPanel panel;
    private DefaultListModel<JPanel> model;
    private JList<JPanel> list;
    private JScrollPane scrollPane;

    /**
     * Create list panel with the model and its custom renderer.
     */
    public ListPanel() {
        model = new DefaultListModel<>();
        list = new JList<>();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        list.setModel(model);
        list.setCellRenderer(new PanelRenderer());

        scrollPane = new JScrollPane(list);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Get the selected index in the list. -1 will be returned if there's no selection.
     *
     * @return The selected index.
     */
    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    /**
     * Enable or disable the list.
     *
     * @param tof {@code true} or {@code false}.
     */
    public void setEnabled(boolean tof) {
        list.setEnabled(tof);
    }

    /**
     * Clear the content in the list.
     */
    public void clear() {
        model.removeAllElements();
    }

    /**
     * Add component to the list.
     *
     * @param component JPanel subclass.
     */
    public void add(T component) {
        model.addElement(component);
        int lastIndex = model.getSize() - 1;
        if (lastIndex >= 0) list.ensureIndexIsVisible(lastIndex);
    }

    /**
     * Set the list data.
     *
     * @param components The JPanels subclass array.
     */
    public void set(T[] components) {
        model.removeAllElements();
        for (T t : components) model.addElement(t);
        int lastIndex = model.getSize() - 1;
        if (lastIndex >= 0) list.ensureIndexIsVisible(lastIndex);
    }

    /**
     * Get the scroll pane.
     *
     * @return Scroll pane component.
     */
    public JComponent getPane() {
        return scrollPane;
    }
}
