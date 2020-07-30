package UiComponent.TabbedPane;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class AttenStastis extends JPanel{

    private AttenStastis attenStastis = this;

    public JPanel resultStatis = null;
    public JScrollPane scrollPane = null;
    public StastisTable stastisTable = null;


    // Component
    public JComboBox<String> nameSelect = null;
    public JButton week = null;
    public JFrame clock = null;
    public JTextField start = null;
    public JTextField end  = null;
    public JButton search = null;
    public JButton output = null;
    public JButton reset = null;


    private JPanel createSeizePanel(){
        JPanel blankSpace = new JPanel();
        blankSpace.setOpaque(false);
        return blankSpace;
    }

    public AttenStastis(){
        GridBagLayout statisLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        attenStastis.setLayout(statisLayout);

        JLabel name = new JLabel("姓名:");
        nameSelect = new JComboBox<String>(new String[]{"全部"});

        format.weightx = 0.2;
        format.weighty = 1;
        attenStastis.add(name, format);
        format.weightx = 1;
        attenStastis.add(nameSelect, format);
        format.weightx = 30;
        format.gridwidth = GridBagConstraints.REMAINDER;
        attenStastis.add(createSeizePanel(), format);


        JLabel time = new JLabel("时间范围:");
        week = new JButton("今日");
        clock = new JFrame();
        start = new JTextField(10);
        JLabel to = new JLabel("到");
        end = new JTextField(10);
        search = new JButton("搜索");
        output  = new JButton("导出");
        reset = new JButton("重置");

        week.setBorderPainted(false);


        nameSelect.setSelectedIndex(0);


        format.gridwidth = 1;
        format.weightx = 0.2;
        attenStastis.add(time, format);
        format.weightx = 1;
        attenStastis.add(week, format);
        attenStastis.add(start, format);
        format.weightx = 0.1;
        attenStastis.add(to, format);
        format.weightx = 1;
        attenStastis.add(end, format);
        format.weightx = 30;
        format.gridwidth = GridBagConstraints.REMAINDER;
        attenStastis.add(createSeizePanel(), format);
        format.gridwidth = 10;
        format.weightx = 20;
        attenStastis.add(createSeizePanel(), format);
        format.weightx = 1;
        attenStastis.add(search, format);
        attenStastis.add(reset, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        attenStastis.add(output, format);

//        resultStatis = createPersonalStatis(new String[]{"0", "0", "未达标", "0"});

        stastisTable = new StastisTable(new Object[][]{{"","","","","",""}});
        scrollPane = new JScrollPane(stastisTable);
        resultStatis = new JPanel(new BorderLayout());
        resultStatis.add(scrollPane);
        format.weightx = 1;
        format.weighty = 12;
        format.fill = GridBagConstraints.BOTH;
        attenStastis.add(resultStatis, format);
    }

    public void flushAllTable(Object[][] rowdata){
        if (resultStatis.getComponentCount() != 0){
            resultStatis.remove(0);
            scrollPane = null;
            stastisTable = null;
        }
        stastisTable = new StastisTable(rowdata);
        scrollPane = new JScrollPane(stastisTable);
        resultStatis.add(scrollPane);
    }

    public JPanel flushPersonalStatis(String[] message, ArrayList dateList){
        if (resultStatis.getComponentCount() != 0){
            resultStatis.removeAll();
        }
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        resultStatis.setLayout(layout);
        JLabel totalTimeLabel = new JLabel("考勤总时间/小时:");
        JLabel totalTimesLabel = new JLabel("进出总次数:");
        JLabel commentLabel = new JLabel("评价:");
        JLabel abornomalLabel = new JLabel("异常记录次数");
        JLabel totalTime = new JLabel(message[0]);
        JLabel totalTimes = new JLabel(message[1]);
        JLabel comment = new JLabel(message[2]);
        JLabel abornomal = new JLabel(message[3]);
        format.weighty = 1;
        format.weightx = 0.5;
        resultStatis.add(totalTimeLabel, format);
        format.weightx = 1;
        resultStatis.add(totalTime, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weightx = 6;
        resultStatis.add(createSeizePanel(), format);
        format.weightx = 0.5;
        format.gridwidth = 1;
        resultStatis.add(totalTimesLabel, format);
        format.weightx = 1;
        resultStatis.add(totalTimes, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weightx = 6;
        resultStatis.add(createSeizePanel(), format);
        format.weightx = 0.5;
        format.gridwidth = 1;
        resultStatis.add(commentLabel, format);
        format.weightx = 1;
        resultStatis.add(comment, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weightx = 6;
        resultStatis.add(createSeizePanel(), format);
        format.weightx = 0.5;
        format.gridwidth = 1;
        resultStatis.add(abornomalLabel, format);
        format.weightx = 1;
        resultStatis.add(abornomal, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.weightx = 6;
        resultStatis.add(createSeizePanel(), format);

        JPanel calendar = new DateCalendar(dateList);
        format.weightx = 1;
        format.weighty = 10;
        format.fill = GridBagConstraints.BOTH;
        resultStatis.add(calendar, format);
        format.weighty = 1;
        resultStatis.add(createSeizePanel(), format);
        return resultStatis;
    }

    public void flushNameList(String[] nameList){
        nameSelect.removeAllItems();
        nameSelect.addItem("全部");
        for (int i = 0; i < nameList.length; i++){
            nameSelect.addItem(nameList[i]);
        }
        nameSelect.revalidate();
    }

    private class StastisTable extends JTable{

        public StastisTable stastisTable = this;
        public Object[][] rowData = null;
        public String[] columnNames = {"时间段" ,"姓名", "考勤总时间/小时", "考勤总次数", "评价", "异常记录次数"};

        public  StastisTable(Object[][] rowData){
            stastisTable.rowData = rowData;
            stastisTable.setModel(new AbstractTableModel() {
                @Override
                public int getRowCount() {
                    return rowData.length;
                }
                @Override
                public int getColumnCount() {
                    return rowData[0].length;
                }
                @Override
                public String getColumnName(int column) {
                    return columnNames[column].toString();
                }
                @Override
                public Object getValueAt(int i, int i1) {
                    return rowData[i][i1];
                }
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }
            });
            // 设置表格内容颜色
            stastisTable.setForeground(Color.BLACK);                   // 字体颜色
            stastisTable.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
            stastisTable.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
            stastisTable.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
            stastisTable.setGridColor(Color.GRAY);                     // 网格颜色

            // 设置表头
            stastisTable.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
            stastisTable.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
            stastisTable.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
            stastisTable.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列

            // 设置行高
            stastisTable.setRowHeight(40);
            // 第一列列宽设置为40
            stastisTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            AttenStastis.StastisTable.MyTableCellRenderer renderer = new AttenStastis.StastisTable.MyTableCellRenderer();
            for (int i =0; i < columnNames.length; i++ ){
                TableColumn tableColumn = stastisTable.getColumn(columnNames[i]);
                // 设置 表格列 的 单元格渲染器
                tableColumn.setCellRenderer(renderer);
            }
        }



        private class MyTableCellRenderer extends DefaultTableCellRenderer {
            /**
             * 返回默认的表单元格渲染器，此方法在父类中已实现，直接调用父类方法返回，在返回前做相关参数的设置即可
             */
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                // 偶数行背景设置为白色，奇数行背景设置为灰色
                if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                } else {
                    setBackground(Color.LIGHT_GRAY);
                }

                // 第一列的内容水平居中对齐，最后一列的内容水平右对齐，其他列的内容水平左对齐
                if (column == 0) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == (table.getColumnCount() - 1)) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                // 设置提示文本，当鼠标移动到当前(row, column)所在单元格时显示的提示文本
//            setToolTipText("提示的内容: " + row + ", " + column);

                // PS: 多个单元格使用同一渲染器时，需要自定义的属性，必须每次都设置，否则将自动沿用上一次的设置。

                /*
                 * 单元格渲染器为表格单元格提供具体的显示，实现了单元格渲染器的 DefaultTableCellRenderer 继承自
                 * 一个标准的组件类 JLabel，因此 JLabel 中相应的 API 在该渲染器实现类中都可以使用。
                 *
                 * super.getTableCellRendererComponent(...) 返回的实际上是当前对象（this），即 JLabel 实例，
                 * 也就是以 JLabel 的形式显示单元格。
                 *
                 * 如果需要自定义单元格的显示形式（比如显示成按钮、复选框、内嵌表格等），可以在此自己创建一个标准组件
                 * 实例返回。
                 */

                // 调用父类的该方法完成渲染器的其他设置
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }

    }

    public class DatePicker {
        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);;
        JLabel l = new JLabel("", JLabel.CENTER);
        String day = "";
        JDialog d;
        JButton[] button = new JButton[49];

        public DatePicker(JFrame parent) {
            d = new JDialog();
            d.setModal(true);
            String[] header = { "星期六", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
            JPanel p1 = new JPanel(new GridLayout(7, 7));
            p1.setPreferredSize(new Dimension(800, 200));

            for (int x = 0; x < button.length; x++) {
                final int selection = x;
                button[x] = new JButton();
                button[x].setFocusPainted(false);
                button[x].setBackground(Color.white);
                if (x > 6)
                    button[x].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            day = button[selection].getActionCommand();
                            d.dispose();
                        }
                    });
                if (x < 7) {
                    button[x].setText(header[x]);
                    button[x].setForeground(Color.red);
                }
                p1.add(button[x]);
            }
            JPanel p2 = new JPanel(new GridLayout(1, 3));
            JButton previous = new JButton("<< 前一月");
            previous.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month--;
                    if (month <= 0){
                        month = 12;
                        year--;
                    }
                    displayDate();
                }
            });
            p2.add(previous);
            p2.add(l);
            JButton next = new JButton("后一月 >>");
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month++;
                    if (month > 12){
                        month = 1;
                        year++;
                    }
                }
            });
            p2.add(next);
            d.add(p1, BorderLayout.CENTER);
            d.add(p2, BorderLayout.SOUTH);
            d.pack();
            d.setLocationRelativeTo(parent);
            displayDate();
            d.setVisible(true);
        }

        public void displayDate() {
            for (int x = 7; x < button.length; x++)
                button[x].setText("");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                    "yyyy MMMM");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, 1);
            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
            int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
                button[x].setText("" + day);
            l.setText(sdf.format(cal.getTime()));
            d.setTitle("日期选取");
        }

        public String setPickedDate() {
            if (day.equals(""))
                return day;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                    "yyyy-MM-dd");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, Integer.parseInt(day));
            return sdf.format(cal.getTime());
        }
    }

    private class DateCalendar extends JPanel {
        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        JLabel dateDsiplay = new JLabel("", JLabel.CENTER);
        JButton[] button = new JButton[49];
        ArrayList checkDays = null;

        public DateCalendar(ArrayList dateList) {
            checkDays = dateList;
            String[] header = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
            JPanel p1 = new JPanel(new GridLayout(7, 7));

            for (int x = 0; x < button.length; x++) {
                final int selection = x;
                button[x] = new JButton();
                button[x].setFocusPainted(false);
                button[x].setBackground(Color.white);
                if (x < 7) {
                    button[x].setText(header[x]);
                    button[x].setForeground(Color.red);
                }
                p1.add(button[x]);
            }
            JPanel p2 = new JPanel(new GridLayout(1, 3));
            JButton previous = new JButton("<< 前一月");
            previous.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month--;
                    if (month <= 0){
                        month = 12;
                        year--;
                    }
                    displayDate();
                }
            });
            p2.add(previous);
            JButton next = new JButton("后一月 >>");
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month++;
                    if (month > 12){
                        month = 1;
                        year++;
                    }
                    displayDate();
                }
            });
            p2.add(dateDsiplay);
            p2.add(next);
            this.setLayout(new BorderLayout());
            this.add(p1, BorderLayout.CENTER);
            this.add(p2, BorderLayout.NORTH);
            displayDate();
            this.setVisible(true);
        }

        private int[] dateParse(String date){
            int[] dateNum = new int[3];
            dateNum[0] = Integer.parseInt(date.substring(0, 4));
            dateNum[1] = Integer.parseInt(date.substring(5, 7)) - 1;
            dateNum[2] = Integer.parseInt(date.substring(8, 10));

            return dateNum;
        }

        private void displayDate() {
            for (int x = 7; x < button.length; x++) {
                button[x].setText("");
            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                    "yyyy MMMM");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, 1);
            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
            int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            for (int x = 0; x < button.length; x++){
                button[x].setBackground(Color.white);
            }
            for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
                button[x].setText("" + day);
            }
            for (int i=0; i<checkDays.size(); i++) {
                int[] dateNum =  dateParse(checkDays.get(i).toString());
                if ((dateNum[0] == year) && (dateNum[1] == month)) {
                    button[6 + dayOfWeek + dateNum[2] - 1].setBackground(Color.red);
                }
            }
            dateDsiplay.setText(sdf.format(cal.getTime()));
        }
    }

}
