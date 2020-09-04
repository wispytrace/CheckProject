package EventPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SystemSetEv {
    public EventRes eventRes = null;

    public SystemSetEv(EventRes eventRes){
        this.eventRes = eventRes;
    }

    public void initSystemSet(){
        try{
            setSelectTime();
            eventRes.mainWindow.navigationPanel.systemSet.save.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if ((eventRes.mainWindow.navigationPanel.systemSet.end.getSelectedIndex() - eventRes.mainWindow.navigationPanel.systemSet.start.getSelectedIndex()) <= 0){
                            throw new Exception("请选择正确的时间范围");
                        }
                        setCheckTime();
                    }catch (Exception e1){
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
            eventRes.mainWindow.navigationPanel.systemSet.add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        addTeam();
                    }catch (Exception e1){
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
            eventRes.mainWindow.navigationPanel.systemSet.delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        deleteTeam();
                    }catch (Exception e1){
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
        }catch (Exception e){
            eventRes.mainWindow.showErrorDialog(e.getMessage());
        }
    }

    public void setSelectTime() throws Exception{
        ResultSet resultSet = eventRes.dbManager.dbSearch("SystemSet", "*", "where device=0");
        resultSet.next();
        for (int i = 0; i < eventRes.mainWindow.navigationPanel.systemSet.TIME_LIST.length; i++){
            if (resultSet.getString("beginTime").compareTo(eventRes.mainWindow.navigationPanel.systemSet.TIME_LIST[i]) == 0){
                eventRes.mainWindow.navigationPanel.systemSet.start.setSelectedIndex(i);
                eventRes.mainWindow.navigationPanel.systemSet.start.revalidate();
            }
            if (resultSet.getString("endTime").compareTo(eventRes.mainWindow.navigationPanel.systemSet.TIME_LIST[i]) == 0){
                eventRes.mainWindow.navigationPanel.systemSet.end.setSelectedIndex(i);
                eventRes.mainWindow.navigationPanel.systemSet.end.revalidate();
            }
        }
    }

    public void setCheckTime() throws Exception{
        eventRes.dbManager.dbUpdate("SystemSet","beginTime","'" + eventRes.mainWindow.navigationPanel.systemSet.start.getSelectedItem().toString() + "'", "device = 0");
        eventRes.dbManager.dbUpdate("SystemSet","endTime ","'" + eventRes.mainWindow.navigationPanel.systemSet.end.getSelectedItem().toString() + "'", "device = 0");
        eventRes.mainWindow.showMessageDialog("修改成功");
    }

    public void addTeam() throws Exception{
        if (eventRes.mainWindow.navigationPanel.systemSet.groupContent.getText().compareTo("") == 0){
            throw new Exception("输入组名不能为空");
        }
        eventRes.dbManager.dbInsert("TeamRecord", "team ", "'" + eventRes.mainWindow.navigationPanel.systemSet.groupContent.getText() + "'");
        eventRes.mainWindow.navigationPanel.attenDetail.teamSelect.addItem(eventRes.mainWindow.navigationPanel.systemSet.groupContent.getText());
        eventRes.mainWindow.navigationPanel.attenDetail.teamSelect.revalidate();
        eventRes.mainWindow.navigationPanel.systemSet.groupContent.setText("");
        eventRes.mainWindow.showMessageDialog("添加成功");
        eventRes.mainWindow.navigationPanel.attenDetail.flushTeamList(getGroupContent());
    }

    public void deleteTeam() throws Exception{
        JTextField team = eventRes.mainWindow.navigationPanel.systemSet.groupContent;
        if (team.getText().compareTo("")  == 0){
            throw new Exception("请输入需要删除的小组名称");
        }
        eventRes.dbManager.dbDelete("TeamRecord", "team", "'" + team.getText() + "'");
        eventRes.mainWindow.navigationPanel.attenDetail.teamSelect.removeItem(team.getText());
        eventRes.mainWindow.navigationPanel.attenDetail.teamSelect.revalidate();
        team.setText("");
        eventRes.mainWindow.showMessageDialog("删除成功");
    }
    public String[] getGroupContent() throws Exception{
        List group = new ArrayList<>();
        group.add("全部");
        ResultSet resultSet = eventRes.dbManager.dbSearch("teamrecord", "team", "");
        while (resultSet.next()){
            group.add(resultSet.getString("team"));
        }
        return (String[])group.toArray(new String[group.size()]);
    }
}
