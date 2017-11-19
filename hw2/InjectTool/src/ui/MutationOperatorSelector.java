package ui;

import common.Utils;
import model.CodeSegment;
import model.MutationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangtao on 2017/11/17.
 */
public class MutationOperatorSelector extends JFrame {

    private String programName;
    private CodeSegment codeSegment;
    private MutationCreator mutationCreator;

    public MutationOperatorSelector(String programName) {
        setTitle("Mutation Operator Selector");
        this.programName = programName;
        mutationCreator = new MutationCreator(programName, "");
    }

    public void init() {
        setSize(400, 600);
        setLocationRelativeTo(null);

        JPanel classPanel = new JPanel();

        JComboBox classBox = new JComboBox();
        Map<String, String> classNames = Utils.searchAllClassName(programName);
        classBox.addItem("请选择类");
        for (String className : classNames.keySet()) {
            classBox.addItem(className);
        }
        classPanel.add(classBox);

        JTextField funcField = new JTextField(10);
        classPanel.add(funcField);

        JButton classBtn = new JButton("确定");

        classPanel.add(classBtn);

        add(classPanel, BorderLayout.NORTH);


        JPanel codePanel = new JPanel();
        ((FlowLayout) codePanel.getLayout()).setHgap(30);

        String[] operators = {"ABS", "AOR", "LCR", "ROR", "UOI"};
        JList list = new JList(operators);
        JScrollPane operatorScrollPane = new JScrollPane(list);
        codePanel.add(operatorScrollPane);

        JTextArea codeArea = new JTextArea(25, 25);
        codeArea.setEditable(false);
        JScrollPane codeScrollPane = new JScrollPane(codeArea);
        codePanel.add(codeScrollPane);

        add(codePanel, BorderLayout.CENTER);


        JPanel btnPanel = new JPanel();

        JTextField para1Field = new JTextField(6);
        btnPanel.add(para1Field);

        JTextField para2Field = new JTextField(6);
        btnPanel.add(para2Field);

        JButton startBtn = new JButton("start");
        startBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (codeSegment == null) {
                    return;
                }

                String para1 = para1Field.getText();
                String para2 = para2Field.getText();
                String className = (String) classBox.getSelectedItem();
                String functionName = funcField.getText();

                List<MutationModel> mutationModelList = new ArrayList<>();
                String[] codeLines = codeSegment.code.split("\n");
                int startIndex = codeSegment.startLineIndex;

                if (operators[list.getSelectedIndex()].equals("ABS")) {
                    // 取绝对值
                    if (para1.isEmpty()) {
                        return;
                    }

                    for (String codeLine : codeLines) {
                        if (startIndex == codeSegment.startLineIndex) {
                            startIndex++;
                            continue;
                        }
                        int index = -1;
                        int lastIndex = 0;
                        String modifyCodeLine = "";
                        while ((index = codeLine.indexOf(para1, index + 1)) != -1) {
                            if (index != 0) {
                                char frontChar = codeLine.charAt(index - 1);
                                if ((frontChar >= 'a' && frontChar <= 'z') || (frontChar >= 'A' && frontChar <= 'Z')) {
                                    continue;
                                }
                            }
                            if (index + para1.length() < codeLine.length()) {
                                char backendChar = codeLine.charAt(index + para1.length());
                                if ((backendChar >= 'a' && backendChar <= 'z') || (backendChar >= 'A' && backendChar
                                        <= 'Z')) {
                                    continue;
                                }
                            }
                            // 如果是赋值而不是取值
                            if (!Utils.isFetchingVal(codeLine, para1, index)) {
                                continue;
                            }
                            modifyCodeLine += (codeLine.substring(lastIndex, index) + "Math.abs(" + para1 + ")");
                            lastIndex = index + para1.length();
                        }
                        modifyCodeLine += codeLine.substring(lastIndex);

                        MutationModel mutationModel = new MutationModel("/" + className + ".java", startIndex + 1,
                                modifyCodeLine);
                        mutationModelList.add(mutationModel);

                        startIndex++;
                    }
                    String muIndex = className + "-" + functionName + "-ABS-" + "\"" + para1 + "\"";
                    mutationCreator.inject("", mutationModelList, muIndex);
                    JOptionPane.showMessageDialog(MutationOperatorSelector.this, "已保存至" + muIndex, "变异生成成功",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (operators[list.getSelectedIndex()].equals("AOR") || operators[list.getSelectedIndex()]
                        .equals("LCR")) {
                    // 替换二元算数运算符 || 替换逻辑连接符
                    if (para1.isEmpty() || para2.isEmpty()) {
                        return;
                    }

                    for (String codeLine : codeLines) {
                        if (codeLine.contains(para1)) {
                            String modifyCodeLine = codeLine.replace(para1, para2);
                            MutationModel mutationModel = new MutationModel("/" + className + ".java", startIndex +
                                    1, modifyCodeLine);
                            mutationModelList.add(mutationModel);
                        }
                        startIndex++;
                    }
                    String muIndex = className + "-" + functionName + "-" + operators[list.getSelectedIndex()] + "-"
                            + "\"" + para1 + " -> " + para2 + "\"";
                    mutationCreator.inject("", mutationModelList, muIndex);
                    JOptionPane.showMessageDialog(MutationOperatorSelector.this, "已保存至" + muIndex, "变异生成成功",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (operators[list.getSelectedIndex()].equals("ROR")) {
                    // 替换关系运算符
                    if (para1.isEmpty() || para2.isEmpty()) {
                        return;
                    }

                    for (String codeLine : codeLines) {
                        if (codeLine.contains(para1)) {
                            if (para1.equals("<") || para1.equals(">")) {
                                // 要判断是否是<=或>=
                                // TODO List<> 这样的符号没有判断
                                int index = -1;
                                int lastIndex = 0;
                                String modifyCodeLine = "";
                                while ((index = codeLine.indexOf(para1, index + 1)) != -1) {
                                    if (codeLine.charAt(index + 1) != '=') {
                                        modifyCodeLine += (codeLine.substring(lastIndex, index) + para2);
                                        lastIndex = index + 1;
                                    }
                                }
                                if (lastIndex != 0) {
                                    modifyCodeLine += codeLine.substring(lastIndex);
                                    MutationModel mutationModel = new MutationModel("/" + className + ".java",
                                            startIndex + 1, modifyCodeLine);
                                    mutationModelList.add(mutationModel);
                                }
                            } else {
                                // >= <= == != 直接替换
                                String modifyCodeLine = codeLine.replace(para1, para2);
                                MutationModel mutationModel = new MutationModel("/" + className + ".java", startIndex
                                        + 1, modifyCodeLine);
                                mutationModelList.add(mutationModel);
                            }
                        }
                        startIndex++;
                    }
                    String muIndex = className + "-" + functionName + "-ROR-" + "\"" + para1 + " -> " + para2 + "\"";
                    mutationCreator.inject("", mutationModelList, muIndex);
                    JOptionPane.showMessageDialog(MutationOperatorSelector.this, "已保存至" + muIndex, "变异生成成功",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (operators[list.getSelectedIndex()].equals("UOI")) {
                    // 向被测程序中插入一个符号或逻辑非 ？？？

                }
            }
        });
        btnPanel.add(startBtn);

        //        JButton resetBtn = new JButton("reset");
        //        resetBtn.addActionListener(new AbstractAction() {
        //            @Override
        //            public void actionPerformed(ActionEvent e) {
        //                classBox.setSelectedIndex(0);
        //                funcField.setText("");
        //                codeArea.setText("");
        //                para1Field.setText("");
        //                para2Field.setText("");
        //            }
        //        });
        //        btnPanel.add(resetBtn);

        JButton execBtn = new JButton("exec");
        execBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MutationOperatorSelector.this.setVisible(false);
                TestcaseExecFrame testcaseExecFrame = new TestcaseExecFrame(programName);
                testcaseExecFrame.initFrame();
            }
        });
        btnPanel.add(execBtn);

        add(btnPanel, BorderLayout.SOUTH);

        setVisible(true);

        classBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (classBox.getSelectedIndex() == 0) {
                    return;
                }

                String className = (String) classBox.getSelectedItem();
                String functionName = funcField.getText();

                codeSegment = Utils.searchCodeSegmentByFunctionName(programName, className, functionName);
                codeArea.setText(codeSegment.code);
            }
        });
    }


    public static void main(String[] args) {
        MutationOperatorSelector mutationOperatorSelector = new MutationOperatorSelector("waretest-hws-hw1-src");
        mutationOperatorSelector.init();
    }

}
