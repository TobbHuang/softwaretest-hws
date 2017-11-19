package ui;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by huangtao on 2017/11/17.
 */
public class PathSelector extends JFrame {

    private int fileSelectionMode;
    private File[] files;

    public PathSelector(String title) {
        this(title, JFileChooser.DIRECTORIES_ONLY);
    }

    public PathSelector(String title, int fileSelectionMode) {
        setTitle(title);
        this.fileSelectionMode = fileSelectionMode;
    }

    public void start(SelectorCallback callback) {
        setSize(600, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2));
        JFrame parent = this;

        JTextField pathField = new JTextField();
        pathField.setEditable(false);
        add(pathField);

        JButton selectBtn = new JButton("浏览");
        selectBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();
                fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                fileChooser.setDialogTitle("请选择要上传的文件...");
                fileChooser.setApproveButtonText("确定");
                fileChooser.setFileSelectionMode(fileSelectionMode);
                fileChooser.setMultiSelectionEnabled(true);
                int result = fileChooser.showOpenDialog(parent);
                if (JFileChooser.APPROVE_OPTION == result) {
                    pathField.setText(fileChooser.getSelectedFile().getPath());
                    files = fileChooser.getSelectedFiles();
                }
            }
        });
        add(selectBtn);

        JButton yesBtn = new JButton("确认");
        yesBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callback.call(files);
                setVisible(false);
            }
        });
        add(yesBtn);

        JButton noBtn = new JButton("取消");
        noBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        add(noBtn);

        setVisible(true);
    }

}
