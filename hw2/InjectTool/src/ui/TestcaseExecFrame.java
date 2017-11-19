package ui;

import common.Common;
import common.Utils;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangtao on 2017/11/18.
 */
public class TestcaseExecFrame extends JFrame {

    private String tcPath;
    private String programName;

    public TestcaseExecFrame(String programName) {
        super();
        this.programName = programName;
    }

    public void initFrame() {
        setTitle("测试用例执行");
        setSize(700, 700);
        setLocationRelativeTo(null);

        JPanel tcPathPanel = new JPanel(new BorderLayout());

        JLabel curPathLabel = new JLabel("当前选择：");
        tcPathPanel.add(curPathLabel, BorderLayout.WEST);

        JButton pathSelectBtn = new JButton("浏览");
        tcPathPanel.add(pathSelectBtn, BorderLayout.EAST);
        pathSelectBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PathSelector tcSelector = new PathSelector("请选择测试用例", JFileChooser.FILES_ONLY);
                tcSelector.start(new SelectorCallback() {
                    @Override
                    public void call(File[] files) {
                        tcPath = files[0].getAbsolutePath();
                        curPathLabel.setText("当前选择：" + tcPath);
                    }
                });
            }
        });

        add(tcPathPanel, BorderLayout.NORTH);

        JPanel tcPanel = new JPanel();
        ((FlowLayout) tcPanel.getLayout()).setHgap(30);

        File muDir = new File(Common.DIR_MUTATION + programName);
        String[] muArray = muDir.list();
        JList list = new JList(muArray);
        JScrollPane tcScrollPane = new JScrollPane(list);
        tcPanel.add(tcScrollPane);

        JPanel codePanel = new JPanel(new BorderLayout());

        JTextArea codeArea = new JTextArea(20, 25);
        codeArea.setEditable(false);
        JScrollPane codeScrollPane = new JScrollPane(codeArea);
        codePanel.add(codeScrollPane, BorderLayout.NORTH);

        JTextArea tcResultArea = new JTextArea(10, 25);
        tcResultArea.setEditable(false);
        JScrollPane tcResultScrollPane = new JScrollPane(tcResultArea);
        codePanel.add(tcResultScrollPane, BorderLayout.SOUTH);

        tcPanel.add(codePanel);

        add(tcPanel, BorderLayout.CENTER);


        JPanel execPanel = new JPanel();
        JButton execBtn = new JButton("exec");
        execBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mutation = list.getSelectedValue().toString();

                // compile
                List<File> srcFiles = new ArrayList<>();
                File muDir = new File(Common.DIR_MUTATION + programName + "/" + mutation);
                for (File file : muDir.listFiles()) {
                    if (file.getName().endsWith(".java")) {
                        srcFiles.add(file);
                    }
                }
                Utils.compile(srcFiles, Common.DIR_MUTATION + programName + "/" + mutation);

                // copy test case file
                try {
                    Utils.copy(new File(tcPath), muDir);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                // exec
                File tc = new File(tcPath);
                try {
                    URL url = muDir.toURI().toURL();
                    ClassLoader loader = new URLClassLoader(new URL[]{url});
                    Class<?> cls = loader.loadClass(tc.getName().replace(".class", ""));
                    Result result = Utils.runJUnitTestcase(cls);
                    tcResultArea.setText("");
                    tcResultArea.append("共执行测试用例数量：" + result.getRunCount() + "\n");
                    tcResultArea.append("失败数：" + result.getFailureCount() + "\n");
                    if (result.getFailureCount() > 0) {
                        tcResultArea.append("失败详情：\n");
                        for (Failure failure : result.getFailures()) {
                            tcResultArea.append(failure.toString() + "\n");
                        }
                    }
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });
        execPanel.add(execBtn);

        add(execPanel, BorderLayout.SOUTH);


        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String fileName = ((JList) e.getSource()).getSelectedValue().toString().split("-")[0] + ".java";
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(new File(Common.DIR_MUTATION +
                            programName + "/" + ((JList) e.getSource()).getSelectedValue().toString() + "/" +
                            fileName)));
                    codeArea.setText("");
                    String codeLine;
                    while ((codeLine = reader.readLine()) != null) {
                        codeArea.append(codeLine + "\n");
                    }
                    codeScrollPane.getViewport().setViewPosition(new Point(0, 0));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        list.setSelectedIndex(0);

        setVisible(true);

        PathSelector tcSelector = new PathSelector("请选择测试用例", JFileChooser.FILES_ONLY);
        tcSelector.start(new SelectorCallback() {
            @Override
            public void call(File[] files) {
                tcPath = files[0].getAbsolutePath();
                curPathLabel.setText("当前选择：" + tcPath);
            }
        });


    }

    public static void main(String[] args) {
        TestcaseExecFrame testcaseExecFrame = new TestcaseExecFrame("软件测试-提交的作业-hw2-源程序代码");
        testcaseExecFrame.initFrame();
    }

}
