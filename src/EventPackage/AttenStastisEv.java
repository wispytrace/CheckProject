package EventPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttenStastisEv {
    public EventRes eventRes = null;

    private String nameChoose = null;

    private String beginTime = null;
    private String endTime = null;
    private final int  DAY = 24 * 60 * 60 * 1000;
    private final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public AttenStastisEv(EventRes eventRes){
        this.eventRes = eventRes;
    }

    public void initAttenStastis() {
        try {
            eventRes.mainWindow.navigationPanel.attenStastis.nameSelect.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        nameChoose = eventRes.mainWindow.navigationPanel.attenStastis.nameSelect.getSelectedItem().toString();
                    }
                }
            });
            eventRes.mainWindow.navigationPanel.attenStastis.week.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setStasticTimeChoose(0);
                }
            });
            eventRes.mainWindow.navigationPanel.attenStastis.start.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    eventRes.mainWindow.navigationPanel.attenStastis.start.setText(eventRes.mainWindow.navigationPanel.attenStastis.new DatePicker(eventRes.mainWindow.navigationPanel.attenStastis.clock).setPickedDate());
                    setStasticTimeChoose(1);
                    eventRes.mainWindow.navigationPanel.attenStastis.start.setFocusable(false);
                    eventRes.mainWindow.navigationPanel.attenStastis.start.setFocusable(true);
                }

                @Override
                public void focusLost(FocusEvent e) {

                }
            });
            eventRes.mainWindow.navigationPanel.attenStastis.end.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    eventRes.mainWindow.navigationPanel.attenStastis.end.setText(eventRes.mainWindow.navigationPanel.attenStastis.new DatePicker(eventRes.mainWindow.navigationPanel.attenStastis.clock).setPickedDate());
                    setStasticTimeChoose(2);
                    eventRes.mainWindow.navigationPanel.attenStastis.end.setFocusable(false);
                    eventRes.mainWindow.navigationPanel.attenStastis.end.setFocusable(true);
                }

                @Override
                public void focusLost(FocusEvent e) {

                }
            });
            eventRes.mainWindow.navigationPanel.attenStastis.reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eventRes.mainWindow.navigationPanel.attenStastis.nameSelect.setSelectedIndex(0);
                    nameChoose = eventRes.mainWindow.navigationPanel.attenStastis.nameSelect.getSelectedItem().toString();
                    setStasticTimeChoose(0);
                }
            });
            eventRes.mainWindow.navigationPanel.attenStastis.search.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        doStasticSearch();
                    } catch (Exception e1) {
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
            eventRes.mainWindow.navigationPanel.attenStastis.output.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        doCheckFileOut(eventRes.mainWindow.getOutputDialog());
                    }catch (Exception e1){
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
            eventRes.mainWindow.navigationPanel.attenStastis.flushNameList(getAllStaffName());
            eventRes.mainWindow.navigationPanel.attenStastis.reset.doClick();
            eventRes.mainWindow.navigationPanel.attenStastis.search.doClick();
        }catch (Exception e){
            eventRes.mainWindow.showErrorDialog(e.getMessage());
        }
    }

    public void setStasticTimeChoose(int select){
        Date Mytoday = new Date();
        eventRes.mainWindow.navigationPanel.attenStastis.week.setBackground(null);
        if (select == 0){
            eventRes.mainWindow.navigationPanel.attenStastis.week.setBackground(Color.lightGray);
            beginTime = DateFormat.format(Mytoday.getTime());
            endTime = DateFormat.format(Mytoday.getTime() + DAY);
            eventRes.mainWindow.navigationPanel.attenStastis.start.setText("");
            eventRes.mainWindow.navigationPanel.attenStastis.end.setText("");
        }
        else if (select == 1){
            beginTime = eventRes.mainWindow.navigationPanel.attenStastis.start.getText();
        }
        else if (select == 2){
            endTime = eventRes.mainWindow.navigationPanel.attenStastis.end.getText();
        }
    }

    public String[] getAllStaffName() throws Exception{
        List group = new ArrayList<>();
        group.add("全部");
        ResultSet resultSet = eventRes.dbManager.dbSearch("Staff", "name", "");
        while (resultSet.next()){
            group.add(resultSet.getString("name"));
        }
        return (String[])group.toArray(new String[group.size()]);
    }




    public String[] getPersonStastic(String name) throws Exception{
        ResultSet resultSet = eventRes.dbManager.dbSearch("SystemSet","*", "");
        resultSet.next();
        float excellent = resultSet.getFloat("excellent");
        float good = resultSet.getFloat("good");
        float fair = resultSet.getFloat("fair");

        resultSet = eventRes.dbManager.dbSearch("AttendanceRecord, Staff ", "name,outime,lastime,isLegal", "where staff.id = attendancerecord.id " +
                " and name = '" + name + "'" +
                " and outime between '" + beginTime + "' and '" + endTime + "'");
        float totalTime = 0;
        int normalFrequency = 0;
        int abnormalFrequency = 0;
        long totalDay = (DateFormat.parse(beginTime).getTime() - DateFormat.parse(endTime).getTime()) / DAY;
        while (resultSet.next()){
            totalTime += resultSet.getFloat("lastime");
            if (resultSet.getInt("isLegal") == 0){
                normalFrequency++;
            }
            else {
                abnormalFrequency++;
            }
        }
        float average = totalTime / totalDay;
        String comment = null;
        if ((average - excellent)> 1e-6){
            comment = "优秀";
        }
        else if ((average - good) > 1e-6) {
            comment = "良好";
        }
        else if ((average - fair) > 1e-6){
            comment = "及格";
        }
        else {
            comment = "不合格";
        }
        String[] result = new String[4];
        result[0] = Float.toString(totalTime);
        result[1] = Integer.toString(normalFrequency);
        result[2] = comment;
        result[3] = Integer.toString(abnormalFrequency);
        return result;
    }

    private ArrayList getPersonAllDate(String name) throws Exception{
        ArrayList dateList = new ArrayList();
        ResultSet resultSet = eventRes.dbManager.dbSearch("AttendanceRecord, Staff ", "outime", "where staff.id = attendancerecord.id " +
                " and name = '" + name + "'" );
        while (resultSet.next()){
            if (resultSet.getString("outime") != null) {
                dateList.add(resultSet.getString("outime").substring(0, 10));
            }
        }
        return dateList;
    }

    public void doStasticSearch() throws Exception{
        if (beginTime == "" || endTime == ""){
            throw new Exception("请输入合法的查询时间段");
        }
        if (nameChoose == "全部"){
            String[] nameList = getAllStaffName();
            Object[][] rowdate = {{"","","","","",""}};
            if (nameList.length != 1){
                rowdate = new Object[nameList.length - 1][6];
            }

            for (int i=1; i<nameList.length; i++ ){
                String[] personStastic = getPersonStastic(nameList[i]);
                rowdate[i - 1][0] = beginTime + "—" + endTime;
                rowdate[i - 1][1] = nameList[i];
                rowdate[i - 1][2] = personStastic[0];
                rowdate[i - 1][3] = personStastic[1];
                rowdate[i - 1][4] = personStastic[2];
                rowdate[i - 1][5] = personStastic[3];
            }
            eventRes.mainWindow.navigationPanel.attenStastis.flushAllTable(rowdate);
        }
        else {
            ArrayList dateList = new ArrayList();
            String[] personStastic = getPersonStastic(nameChoose);
            dateList = getPersonAllDate(nameChoose);
            eventRes.mainWindow.navigationPanel.attenStastis.flushPersonalStatis(personStastic, dateList);
        }
    }

    public String[] getPersonStastic(String name, ArrayList record) throws Exception{
        ResultSet resultSet = eventRes.dbManager.dbSearch("SystemSet","*", "");
        resultSet.next();
        float excellent = resultSet.getFloat("excellent");
        float good = resultSet.getFloat("good");
        float fair = resultSet.getFloat("fair");

        resultSet = eventRes.dbManager.dbSearch("AttendanceRecord, Staff ", "name,outime,lastime,isLegal,intime", "where staff.id = attendancerecord.id " +
                " and name = '" + name + "'" +
                " and outime between '" + beginTime + "' and '" + endTime + "'");
        float totalTime = 0;
        int normalFrequency = 0;
        int abnormalFrequency = 0;
        long totalDay = (DateFormat.parse(endTime).getTime() - DateFormat.parse(beginTime).getTime()) / DAY;
        while (resultSet.next()){
            record.add(resultSet.getString("intime"));
            record.add(resultSet.getString("outime"));
            record.add(resultSet.getString("lastime"));
            if (resultSet.getInt("isLegal") == 0){
                normalFrequency++;
                totalTime += resultSet.getFloat("lastime");
                record.add("正常");
            }
            else {
                abnormalFrequency++;
                record.add("异常");
            }
        }
        float average = totalTime / totalDay;
        String comment = null;
        if ((average - excellent)> 1e-6){
            comment = "优秀";
        }
        else if ((average - good) > 1e-6) {
            comment = "良好";
        }
        else if ((average - fair) > 1e-6){
            comment = "及格";
        }
        else {
            comment = "不合格";
        }
        String[] result = new String[4];
        result[0] = Float.toString(totalTime);
        result[1] = Integer.toString(normalFrequency);
        result[2] = comment;
        result[3] = Integer.toString(abnormalFrequency);
        return result;
    }

    public void doCheckFileOut (File file) throws Exception{
        String[] staffName = getAllStaffName();
        FileWriter fw = new FileWriter(file);
        for (int i = 1; i < staffName.length; i++){
            fw.write(staffName[i]+"\r\n");
            ArrayList recordList = new ArrayList();
            String[] result = getPersonStastic(staffName[i], recordList);
            for (int j = 0; j < recordList.toArray().length ; j = j + 4){
                fw.write("进入时间:\t" + recordList.get(j).toString());
                fw.write("\t离开时间:\t" + recordList.get(j + 1).toString());
                fw.write("\t持续时间:\t" + recordList.get(j + 2).toString());
                fw.write("\t记录状态:\t" + recordList.get(j + 3).toString() + "\r\n");
            }
            fw.write("统计信息如下:\r\n");
            fw.write("总持续时间:\t" + result[0] + "\t总出入次数:\t" + result[1] + "\t考勤评价:\t" + result[2] + "\t异常记录次数:\t" + result[3] + "\r\n\r\n\r\n");
        }
        fw.close();
    }
}
