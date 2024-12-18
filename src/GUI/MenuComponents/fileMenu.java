package GUI.MenuComponents;

import javax.swing.*;

public class fileMenu extends JMenu {
    JMenuItem saveItem = new JMenuItem("保存");
    JMenuItem loadItem = new JMenuItem("读档");
    JMenuItem deleteItem = new JMenuItem("管理存档");

    public fileMenu(String s) {
        super(s);
        saveItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "saveItem"));
        loadItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "loadItem"));
        deleteItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "deleteItem"));

        this.add(saveItem);
        this.add(loadItem);
        this.addSeparator();
        this.add(deleteItem);
    }
}
