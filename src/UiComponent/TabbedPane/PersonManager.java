package UiComponent.TabbedPane;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonManager extends JPanel {

    private PersonManager personManager = this;

    public JButton enroll = null;
    public JPanel personResult = null;
    public JScrollPane scrollPane = null;
    public PersonTable personTable = null;

    public JButton modify = new JButton();
    public String modifyID = null;

    public PersonManager(){
        GridBagLayout personLayout = new GridBagLayout();
        personManager.setLayout(personLayout);
        GridBagConstraints format = new GridBagConstraints();
        enroll = new JButton("新增成员");

        format.fill = GridBagConstraints.BOTH;
        format.weightx = 1;
        format.weighty = 4;
        format.gridwidth = GridBagConstraints.REMAINDER;
        personManager.add(enroll, format);
        format.weighty = 10;
        personResult = new JPanel(new BorderLayout());
        personTable = new PersonTable(new Object[][]{{"","","","","","",""}});
        scrollPane = new JScrollPane(personTable);
        personResult.add(scrollPane);
        personManager.add(personResult, format);
    }

    public void flushTable(Object[][] rowdata){
        if (personResult.getComponentCount() != 0){
            personResult.remove(0);
            scrollPane = null;
            personTable = null;
        }
        personTable = new PersonTable(rowdata);
        scrollPane = new JScrollPane(personTable);
        personResult.add(scrollPane);
        personResult.revalidate();
    }


    private class PersonTable extends JTable{

        public PersonTable personTable = this;
        public Object[][] rowData = null;
        public String[] columnNames = new String[]{"编号", "姓名", "小组", "导师", "学号", "手机号码", "操作"};

        public  PersonTable(Object[][] rowData){
            personTable.rowData = rowData;
            personTable.setModel(new AbstractTableModel() {
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
                    if ((columnIndex == (this.getColumnCount()-1))) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });
            // 设置表格内容颜色
            personTable.setForeground(Color.BLACK);                   // 字体颜色
            personTable.setFont(new Font(null, Font.PLAIN, 14));      // 字体样式
            personTable.setSelectionForeground(Color.DARK_GRAY);      // 选中后字体颜色
            personTable.setSelectionBackground(Color.LIGHT_GRAY);     // 选中后字体背景
            personTable.setGridColor(Color.GRAY);                     // 网格颜色

            // 设置表头
            personTable.getTableHeader().setFont(new Font(null, Font.BOLD, 14));  // 设置表头名称字体样式
            personTable.getTableHeader().setForeground(Color.RED);                // 设置表头名称字体颜色
            personTable.getTableHeader().setResizingAllowed(false);               // 设置不允许手动改变列宽
            personTable.getTableHeader().setReorderingAllowed(false);             // 设置不允许拖动重新排序各列

            // 设置行高
            personTable.setRowHeight(40);
            // 第一列列宽设置为40
            personTable.getColumnModel().getColumn(0).setPreferredWidth(60);
            MyTableCellRenderer renderer = new MyTableCellRenderer();
            for (int i =0; i < columnNames.length; i++ ){
                TableColumn tableColumn = personTable.getColumn(columnNames[i]);
                // 设置 表格列 的 单元格渲染器
                tableColumn.setCellRenderer(renderer);
            }

            personTable.getColumnModel().getColumn(personTable.getColumnCount()-1).setCellEditor(new MyButtonEditor());
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
                if (column == (personTable.getColumnCount()-1)){
                    JButton update = new JButton("修改");
                    return update;
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }

        private class MyButtonEditor extends AbstractCellEditor implements TableCellEditor {
            /**
             * serialVersionUID
             */
//        private static final long serialVersionUID = -6546334664166791132L;
            private JPanel panel;
            private String  id;
            public JButton button = null;

            public MyButtonEditor() {
                initButton();
                initPanel();
                panel.add(button, BorderLayout.CENTER);
            }

            private void initButton() {
                button = new JButton("修改");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        modifyID = id;
                        modify.doClick();
                    }
                });
            }

            private void initPanel() {
                panel = new JPanel();

                panel.setLayout(new BorderLayout());
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                id = value.toString();
                return panel;
            }
            @Override
            public Object getCellEditorValue() {
                return id;
            }
        }

    }


}
