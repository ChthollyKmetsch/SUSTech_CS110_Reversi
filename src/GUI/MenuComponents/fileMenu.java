package GUI.MenuComponents;

import Algorithms.Algo;
import Algorithms.Saving;
import GUI.GeneralStatePanel;
import GUI.ReversiBoard;
import GUI.ReversiBoardFrame;
import GUI.SoundPlayer;
import GUI.SoundPlayer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;

import static Utility.Constants.DISPLAY_PANEL_RECTANGLE;

public class fileMenu extends JMenu {
    JMenuItem saveItem = new JMenuItem("保存");
    JMenuItem loadItem = new JMenuItem("读档");
    JMenuItem deleteItem = new JMenuItem("管理存档");

    public fileMenu(ReversiBoardFrame owner, Algo algo, ReversiBoard board, GeneralStatePanel displayPanel) {
        super("文件");
//        Algo algo = app;
        saveItem.addActionListener(e -> {
            try {
                algo.saveToFile(algo, board.currentOperator, owner.finalPlayWithAI);
                SoundPlayer.playSound("click_default.wav");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(this, "保存成功！");
        });

        loadItem.addActionListener(e -> {
            JDialog frame = new JDialog(owner,"选取存档", true);
            frame.setSize(300, 400);
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            String folderPath;

            if (owner.finalPlayWithAI) {
                folderPath = "saves";
            } else {
                folderPath = "mp_saves";
            }

            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> fileList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(fileList);
            frame.add(scrollPane, BorderLayout.CENTER);

            File folder = new File(folderPath);
            if (!folder.exists() || !folder.isDirectory()) {
                JOptionPane.showMessageDialog(frame, "文件夹不存在或路径错误！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        listModel.addElement(file.getName());
                    }
                }
            }

            fileList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // 双击事件
                        int selectedIndex = fileList.getSelectedIndex();
                        if (selectedIndex != -1) {
                            SoundPlayer.playSound("click_default.wav");
                            String selectedFileName = listModel.get(selectedIndex);
                            File selectedFile = new File(folderPath, selectedFileName);
                            String tmpName = folderPath + "/" + selectedFileName;
                            // Serialization loading
                            try {
                                Algo tmp = null;
                                FileInputStream fileInput = new FileInputStream(tmpName);
                                ObjectInputStream objectInput = new ObjectInputStream(fileInput);
                                tmp = (Algo) objectInput.readObject();
                                algo.loadFromAlgo(tmp);
                            } catch (IOException | ClassNotFoundException ex) {
                                throw new RuntimeException(ex);
                            }
                            board.updateBoardWith(algo);
                            board.repaintWithYellowRing();

                            displayPanel.setBlackCount(algo.numOfBlack);
                            displayPanel.setWhiteCount(algo.numOfWhite);
                            displayPanel.setTotCount(algo.getTotPieces());
                            displayPanel.setCurrentOperator(algo.currentOperatorForLoading);
                            displayPanel.paintImmediately(DISPLAY_PANEL_RECTANGLE);
                        }
                        frame.dispose();
                    }
                }
            });
            frame.setVisible(true);
        });

        deleteItem.addActionListener(e -> {
            SoundPlayer.playSound("click_default.wav");
            JDialog frame = new JDialog(owner,"存档管理", true);
            frame.setSize(300, 400);
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(this);
            String folderPath;

            if (owner.finalPlayWithAI) {
                folderPath = "saves";
            } else {
                folderPath = "mp_saves";
            }

            DefaultListModel<String> listModel = new DefaultListModel<>();
            JList<String> fileList = new JList<>(listModel);
            JScrollPane scrollPane = new JScrollPane(fileList);

            File folder = new File(folderPath);
            if (!folder.exists() || !folder.isDirectory()) {
                JOptionPane.showMessageDialog(frame, "文件夹不存在或路径错误！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        listModel.addElement(file.getName());
                    }
                }
            }

            JButton deleteButton = new JButton("删除存档");
            deleteButton.addActionListener(e1 -> {
//                Delete file
                String selectedFileName = fileList.getSelectedValue();
                if (selectedFileName != null) {
                    File selectedFile = new File(folderPath, selectedFileName);
                    if (selectedFile.delete()) {
                        SoundPlayer.playSound("click_default.wav");
                        listModel.removeElement(selectedFileName);
                        JOptionPane.showMessageDialog(fileList, "删除了存档！");
                    } else {
                        JOptionPane.showMessageDialog(fileList, "存档因未知原因删除失败");
                    }
                } else {
                    JOptionPane.showMessageDialog(fileList, "请先选择一个存档");
                }
            });

            JButton renameButton = new JButton("重命名");
            renameButton.addActionListener(e1 -> {
//                Rename file
                String selectedFileName = fileList.getSelectedValue();
                if (selectedFileName != null) {
                    SoundPlayer.playSound("click_default.wav");
                    File selectedFile = new File(folderPath, selectedFileName);
                    String newName = JOptionPane.showInputDialog(fileList, "输入新的存档名：", selectedFileName);
                    if (newName != null && !newName.equals(".sav")) {
                        if (!newName.endsWith(".sav")) {
                            newName += ".sav";
                        }
                        File renamedFile = new File(folderPath, newName);
                        if (selectedFile.renameTo(renamedFile)) {
                            listModel.setElementAt(newName, fileList.getSelectedIndex());
                            JOptionPane.showMessageDialog(fileList, "存档重命名成功");
                        } else {
                            JOptionPane.showMessageDialog(fileList, "存档重命名失败 —— 不合法的名称或名称重复");
                        }
                    } else {
                        JOptionPane.showMessageDialog(fileList, "存档重命名失败 —— 存档名不能为空白");
                    }
                } else {
                    JOptionPane.showMessageDialog(fileList, "请先选择一个存档");
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(deleteButton);
            buttonPanel.add(renameButton);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);
        });

        this.add(saveItem);
        this.addSeparator();
        this.add(loadItem);
        this.addSeparator();
        this.add(deleteItem);
    }
}
