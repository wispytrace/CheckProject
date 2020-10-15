package UiComponent.TabbedPane;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.logging.Handler;

public class AttenDetail extends JPanel {

    private AttenDetail attenDetail = this;

    public JPanel searchPanel = null;
    public JPanel resultPanel = null;
    public JComboBox<String> teamSelect = null;

    // Deatail Search Component
    public JButton today = null;
    public JButton yesterday = null;
    public JButton week = null;
    public JButton month = null;
    public JFrame clock = null;
    public JTextField start = null;
    public JTextField end = null;
    public JButton normal = null;
    public JButton abnormal = null;
    public JButton search = null;
    public JButton reset = null;

    // Deatail Search Result Component
    private JScrollPane scrollPane = null;
    public DetailTable detailTable = null;


    private JPanel createSeizePanel(){
        JPanel blankSpace = new JPanel();
        blankSpace.setOpaque(false);
        return blankSpace;
    }

    public  AttenDetail(){
        GridBagLayout layout = new  GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        attenDetail.setLayout(layout);

        searchPanel = createDetailSearch();
        format.weighty = 1;
        format.weightx = 1;
        format.gridwidth = GridBagConstraints.REMAINDER;
        format.fill = GridBagConstraints.BOTH;
        layout.addLayoutComponent(searchPanel, format);

        resultPanel = createDetailResult();
        format.weightx = 1;
        format.weighty = 6;
        layout.addLayoutComponent(resultPanel, format);

        attenDetail.add(searchPanel);
        attenDetail.add(resultPanel);
    }

    private  JPanel createDetailSearch(){
        searchPanel = new JPanel();
        GridBagLayout searchLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        searchPanel.setLayout(searchLayout);

        JLabel time = new JLabel("时间范围");
        today = new JButton("今天");
        yesterday = new JButton("昨天");
        week = new JButton("本周");
        month = new JButton("本月");
        clock = new JFrame();
        start = new JTextField(10);
        JLabel to = new JLabel("到");
        end = new JTextField(10);

        today.setBorderPainted(false);

        yesterday.setBorderPainted(false);

        week.setBorderPainted(false);

        month.setBorderPainted(false);

        //LayOut
        format.weighty = 1;
        format.weightx = 1;
        searchPanel.add(time, format);
        searchPanel.add(today, format);
        searchPanel.add(yesterday, format);
        searchPanel.add(week, format);
        searchPanel.add(month, format);
        searchPanel.add(start,format);
        format.weightx = 0.3;
        searchPanel.add(to, format);
        format.weightx = 1;
        searchPanel.add(end, format);
        format.weightx = 6;
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchPanel.add(createSeizePanel(), format);


        JLabel status = new JLabel("状态");
        normal = new JButton("正常");
        abnormal = new JButton("异常");
        normal.setBorderPainted(false);

        abnormal.setBorderPainted(false);


        //LayOut
        format.weightx = 1;
        format.weighty = 1;
        format.gridwidth = 1;
        searchPanel.add(status, format);
        searchPanel.add(normal, format);
        searchPanel.add(abnormal, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchPanel.add(createSeizePanel(), format);

        teamSelect = new JComboBox<String>(new String[]{"全部"});
        JLabel team = new JLabel("组别");
        format.gridwidth = 1;
        format.weightx = 1;
        searchPanel.add(team, format);
        format.gridwidth = 2;
        format.fill = GridBagConstraints.HORIZONTAL;
        searchPanel.add(teamSelect, format);
        format.gridwidth = GridBagConstraints.REMAINDER;
        searchPanel.add(createSeizePanel(), format);

        search = new JButton("搜索");
        reset = new JButton("重置");



        format.gridwidth = 8;
        format.weightx = 1;
        format.weighty = 1.5;
        format.fill = GridBagConstraints.NONE;
        searchPanel.add(createSeizePanel(), format);
        format.weightx = 1;
        searchPanel.add(search, format);
        searchPanel.add(reset, format);


        return  searchPanel;
    }

    private JPanel createDetailResult(){
        resultPanel = new JPanel(new BorderLayout());
        detailTable = new DetailTable(new Object[][]{{"", "", "", "", "", "", ""}});
        scrollPane = new JScrollPane(detailTable);
        resultPanel.add(scrollPane);
        return resultPanel;
    }

    public void flushTeamList(String[] teamList){
        teamSelect.removeAllItems();
//        teamSelect.addItem("全部");
        for (int i = 0; i < teamList.length; i++){
            teamSelect.addItem(teamList[i]);
        }
        teamSelect.revalidate();
    }

    public void flushTable(Object[][] rowdata){
        if (resultPanel.getComponentCount() != 0){
            resultPanel.remove(0);
            scrollPane = null;
            detailTable = null;
        }
        detailTable = new DetailTable(rowdata);
        scrollPane = new JScrollPane(detailTable);
        resultPanel.add(scrollPane);
        resultPanel.revalidate();
    }

    private class DetailTable extends JTable{

        public DetailTable detailTable = this;
        public Object[][] rowData = new Object[][]{{"", "", "", "", "", "", ""}};
        public String[] columnNames = new String[]{"日期", "姓名", "上线时间", "下线时间", "状态", "小组", "持续时间/小时"};

        public  DetailTable(Object[][] rowData){
            detailTable.rowData = rowData;
            detailTable.setModel(new AbstractTableModel() {
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
            detailTable.setForeground(Color.BLACK);                   // 字体颜色
            detailTable.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
            detailTable.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
            detailTable.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
            detailTable.setGridColor(Color.GRAY);                     // 网格颜色

            // 设置表头
            detailTable.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
            detailTable.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
            detailTable.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
            detailTable.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列

            // 设置行高
            detailTable.setRowHeight(40);
            // 第一列列宽设置为40
            detailTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            MyTableCellRenderer renderer = new MyTableCellRenderer();
            for (int i =0; i < columnNames.length; i++ ){
                TableColumn tableColumn = detailTable.getColumn(columnNames[i]);
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
        int m_month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int m_year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);;
        JLabel l = new JLabel("", JLabel.CENTER);
        String m_day = "";
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
                            m_day = button[selection].getActionCommand();
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
                    m_month--;

                    if (m_month <= 0){
                        m_month = 12;
                        m_year--;
                    }
                    displayDate();
                }
            });
            p2.add(previous);
            p2.add(l);
            JButton next = new JButton("后一月 >>");
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    m_month++;
                    if (m_month > 12){
                        m_month = 1;
                        m_year++;
                    }
                    displayDate();
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
            cal.set(m_year, m_month, 1);
            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
            int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
            for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++)
                button[x].setText("" + day);
            l.setText(sdf.format(cal.getTime()));
            d.setTitle("日期选取");
        }

        public String setPickedDate() {
            if (m_day.equals(""))
                return m_day;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                    "yyyy-MM-dd");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(m_year, m_month, Integer.parseInt(m_day));
            return sdf.format(cal.getTime());
        }
    }
}
