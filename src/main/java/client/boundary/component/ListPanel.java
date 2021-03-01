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
 * ListPanel
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class ListPanel<T extends JPanel> {
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

    private JPanel panel;
    private DefaultListModel<JPanel> model;
    private JList<JPanel> list;
    private JScrollPane scrollPane;

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

    public int getSelectedIndex() {
        return list.getSelectedIndex();
    }

    public void setEnabled(boolean tof) {
        list.setEnabled(tof);
    }

    public void clear() {
        model.removeAllElements();
    }

    public void add(T component) {
        model.addElement(component);
        int lastIndex = model.getSize() - 1;
        if (lastIndex >= 0) list.ensureIndexIsVisible(lastIndex);
    }

    public void set(T[] components) {
        model.removeAllElements();
        for (T t : components) model.addElement(t);
        int lastIndex = model.getSize() - 1;
        if (lastIndex >= 0) list.ensureIndexIsVisible(lastIndex);
    }

    public JComponent getPane() {
        return scrollPane;
    }
}
