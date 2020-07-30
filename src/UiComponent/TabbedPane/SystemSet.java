package UiComponent.TabbedPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemSet extends JPanel{

    public static final String[] TIME_LIST = new String[]{"06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
            "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00",
            "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "24:00"};

    private SystemSet systemSet = this;

    //Conponent
    public JComboBox<String> start = null;
    public JComboBox<String> end = null;
    public JButton save = null;
    public JTextField groupContent = null;
    public JButton delete = null;
    public JButton add = null;


    private JPanel createSeizePanel(){
        JPanel blankSpace = new JPanel();
        blankSpace.setOpaque(false);
        return blankSpace;
    }

    public SystemSet(){
        GridBagLayout sysLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        systemSet.setLayout(sysLayout);
        JLabel startTime = new JLabel("打卡起始时间");


        start = new JComboBox<String>(TIME_LIST);
        JLabel endTime = new JLabel("打卡终止时间");
        end = new JComboBox<String>(TIME_LIST);

        format.weightx = 1;
        format.weighty = 1;
        systemSet.add(startTime, format);
        systemSet.add(start, format);
        format.weightx = 8;
        format.gridwidth = GridBagConstraints.REMAINDER;
        systemSet.add(createSeizePanel(), format);

        format.weightx = 1;
        format.weighty = 1;
        format.gridwidth = 1;
        systemSet.add(endTime, format);
        systemSet.add(end, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        systemSet.add(createSeizePanel(), format);
        format.weightx = 1;
        format.gridwidth = 2;
        save = new JButton("保存");

        systemSet.add(save, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        systemSet.add(createSeizePanel(), format);


        JLabel group = new JLabel("小组设置");
        groupContent = new JTextField(20);
        format.weightx = 1;
        format.gridwidth = 1;
        systemSet.add(group, format);
        systemSet.add(groupContent, format);
        format.weightx = 8;
        format.gridwidth = GridBagConstraints.REMAINDER;
        systemSet.add(createSeizePanel(), format);

        delete = new JButton("删除");
        add = new JButton("增加");

        format.weightx = 1;
        format.gridwidth = 1;
        systemSet.add(add, format);
        systemSet.add(delete, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        systemSet.add(createSeizePanel(), format);

        format.weighty = 10;
        systemSet.add(createSeizePanel(), format);

    }

}
