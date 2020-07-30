package UiComponent.TabbedPane;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

public class PersonStatus extends JPanel{

    private final int DEFAULT_GRID_NUM = 8;

    private PersonStatus personStatus = this;

    public JPanel statusPanel = null;

    private int gridNum = DEFAULT_GRID_NUM;

    public void PersonStatus(){

    }

    public void setGridNum(int num){
        if (num <= 8){
            gridNum = DEFAULT_GRID_NUM;
        }
        else {
            gridNum = num;
        }
    }

    public void flushStatusPanel(ResultSet resultSet) throws Exception{
        if (personStatus.getComponentCount() != 0) {
            personStatus.remove(0);
        }
        statusPanel = new JPanel(new GridLayout(gridNum, gridNum));
        ArrayList onlineStaff = new ArrayList();
        ArrayList offlineStaff = new ArrayList();
        while (resultSet.next()){
            int status = resultSet.getInt("status");
            String name = resultSet.getString("name");
            if (status == 0){
                offlineStaff.add(name);
            }
            else {
                onlineStaff.add(name);
            }
        }
        Iterator it = onlineStaff.iterator();
        while (it.hasNext()){
            JButton staff = new JButton(it.next() + "  在线");
            staff.setForeground(Color.red);
            statusPanel.add(staff);
        }
        it = offlineStaff.iterator();
        while (it.hasNext()){
            JButton staff = new JButton(it.next()+ "  离线");
            statusPanel.add(staff);
        }
        personStatus.add(statusPanel);
        personStatus.revalidate();
    }

}
