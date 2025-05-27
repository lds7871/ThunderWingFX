package com.lds7871.flyaway.C_LGUI;

import com.lds7871.flyaway.A_Contorller.Main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;



public class ConfigReader extends JFrame {
    private static Map<JLabel, JTextField> fieldMap = new LinkedHashMap<>(); // 用LinkedHashMap确保顺序一致
    private static ArrayList<String> valuesList = new ArrayList<>();
    private static int How=0;
    public ConfigReader() {
        setTitle("GSL_Loader");
        setSize(260, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // 使用自由布局
        ImageIcon icon = new ImageIcon("Img/HEAD.jpg");
        setIconImage(icon.getImage());
        中心打开();

        //读取Config创建
        try (BufferedReader br = new BufferedReader(new FileReader("Config.txt")))
        {
            String line;
            int yPosition = 10; // 初始y坐标
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("_");
                if (parts.length == 2) {
                    String label = parts[0];
                    String defaultValue =parts[1];

                    // 创建 JLabel
                    JLabel jLabel = new JLabel(label);
                    jLabel.setBounds(10, yPosition, 150, 30); // 设置位置和大小
                    add(jLabel);

                    // 创建 JTextField
                    JTextField jTextField = new JTextField(defaultValue);
                    jTextField.setBounds(170, yPosition, 50, 30); // 设置位置和大小
                    add(jTextField);

                    // 存储 JLabel 和 JTextField 的对应关系
                    fieldMap.put(jLabel, jTextField);

                    yPosition += 40; // 下移位置
                }
            }
        }
        catch (IOException e) {e.printStackTrace();}

        // 保存按钮
        {
            JButton saveButton = new JButton("保存");
            saveButton.setBounds(10, 660, 80, 40); // 设置按钮位置和大小
            add(saveButton);
            // 添加按钮事件监听器
            saveButton.addActionListener(e -> 文件保存());
        }
        //挑战模式按钮
        {
            JButton 挑战Button = new JButton("挑战模式");
            挑战Button.setBounds(100, 660, 120, 40); // 设置按钮位置和大小
            add(挑战Button);
            // 添加按钮事件监听器
            挑战Button.addActionListener(e -> {
                dispose(); // 关闭当前窗口
                Main.startMain(new String[]{});
            });
        }
        //指南按钮
        {
            JButton openButton = new JButton("指南");
            openButton.setBounds(10, 710, 80, 40); // 设置按钮位置和大小
            add(openButton);
            openButton.addActionListener(e -> {
                try {
                    File pdfFile = new File("手册.pdf");
                    if (pdfFile.exists())
                    {
                        if (Desktop.isDesktopSupported())
                        {
                            Desktop.getDesktop().open(pdfFile);
                        }
                        else {
                            JOptionPane.showMessageDialog(this, "Desktop is not supported on this system.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "File not found.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error opening file.");
                }
            });
        }
        //无尽模式按钮
        {
            JButton 无尽Button = new JButton("无尽模式");
            无尽Button.setBounds(100, 710, 120, 40); // 设置按钮位置和大小
            add(无尽Button);
            无尽Button.addActionListener(e -> {
                dispose(); // 关闭当前窗口
                Main.startMain2(new String[]{});
            });
        }





    }

    // 保存到文件的方法
    private void 文件保存()
    {
        读取文本框();
        int i=0;
        if (How==valuesList.size())
        {
            try (PrintWriter pw = new PrintWriter(new FileWriter("Config.txt"))) {
                for (Map.Entry<JLabel, JTextField> entry : fieldMap.entrySet()) {
                    JLabel label = entry.getKey();
                    String labelText = label.getText(); // 获取标签文本
                    Object textFieldValue = valuesList.get(i);
                    pw.println(labelText + "_" + textFieldValue); // 写入文件
                    i++;
                }
                JOptionPane.showMessageDialog(this, "保存成功！");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "保存失败！");
            }
        }
    }

    // 读取文本框的方法
    public static void 读取文本框() {
        How=0;
        valuesList.clear();
        for (Map.Entry<JLabel, JTextField> entry : fieldMap.entrySet()) {
            JTextField textField = entry.getValue();
            String text = textField.getText();

            try {
                Integer.parseInt(text);
                valuesList.add(text);
                How++;
            } catch (NumberFormatException e) {
                valuesList.add("0");
                How=0;
                JOptionPane.showMessageDialog(null,
                        "警告: '" + entry.getKey().getText() + "' 的值 '" + text + "' 不是有效的整数。请重新输入。",
                        "格式错误",
                        JOptionPane.WARNING_MESSAGE);
                textField.setText("");
            }
        }

    }

    // 居中窗口的方法
    private void 中心打开()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 获取屏幕尺寸
        Dimension windowSize = getSize(); // 获取窗口尺寸
        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;
        setLocation(x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConfigReader().setVisible(true));
    }
}
