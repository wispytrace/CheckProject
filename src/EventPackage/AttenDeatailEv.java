package EventPackage;


import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttenDeatailEv {

    public EventRes eventRes = null;

    private String beginTime = null;
    private String endTime = null;
    private final int  DAY = 24 * 60 * 60 * 1000;
    private final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private int isLegal = 0;

    private String teamChoose = null;

    public AttenDeatailEv(EventRes eventRes){
        this.eventRes = eventRes;
    }

    public void initAttenDetail(){
        try {
            eventRes.mainWindow.navigationPanel.attenDetail.today.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setDetailTimeSelectButton(0);
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.yesterday.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setDetailTimeSelectButton(1);
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.week.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setDetailTimeSelectButton(2);
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.month.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setDetailTimeSelectButton(3);
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.start.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent focusEvent) {
                    eventRes.mainWindow.navigationPanel.attenDetail.start.setText(eventRes.mainWindow.navigationPanel.attenDetail.new DatePicker(eventRes.mainWindow.navigationPanel.attenDetail.clock).setPickedDate());
                    setDetailTimeSelectButton(4);
                    eventRes.mainWindow.navigationPanel.attenDetail.start.setFocusable(false);
                    eventRes.mainWindow.navigationPanel.attenDetail.start.setFocusable(true);
                }
                @Override
                public void focusLost(FocusEvent focusEvent) {
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.end.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent focusEvent) {
                    eventRes.mainWindow.navigationPanel.attenDetail.start.setText(eventRes.mainWindow.navigationPanel.attenDetail.new DatePicker(eventRes.mainWindow.navigationPanel.attenDetail.clock).setPickedDate());
                    setDetailTimeSelectButton(4);
                    eventRes.mainWindow.navigationPanel.attenDetail.start.setFocusable(false);
                    eventRes.mainWindow.navigationPanel.attenDetail.start.setFocusable(true);
                }
                @Override
                public void focusLost(FocusEvent focusEvent) {
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.normal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setDetailStatusSelectButton(0);
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.abnormal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setDetailStatusSelectButton(1);
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.teamSelect.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED){
                        setDetailTeamChoose(eventRes.mainWindow.navigationPanel.attenDetail.teamSelect.getSelectedItem().toString());
                    }
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.search.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        doDetailSearch();
                    }catch (Exception e1){
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.reset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setDetailStatusSelectButton(0);
                    setDetailTimeSelectButton(0);
                    setDetailTeamChoose("全部");
                    eventRes.mainWindow.navigationPanel.attenDetail.teamSelect.setSelectedIndex(0);
                }
            });
            eventRes.mainWindow.navigationPanel.attenDetail.flushTeamList(getDetailGroupContent());
            eventRes.mainWindow.navigationPanel.attenDetail.reset.doClick();
            eventRes.mainWindow.navigationPanel.attenDetail.search.doClick();
        }catch (Exception e){
            eventRes.mainWindow.showErrorDialog(e.getMessage());
        }

    }

    public void setDetailTimeSelectButton(int select){
        eventRes.mainWindow.navigationPanel.attenDetail.today.setBackground(null);
        eventRes.mainWindow.navigationPanel.attenDetail.yesterday.setBackground(null);
        eventRes.mainWindow.navigationPanel.attenDetail.week.setBackground(null);
        eventRes.mainWindow.navigationPanel.attenDetail.month.setBackground(null);
        Date Mytoday = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(Mytoday);
        if (select == 4) {
            beginTime = eventRes.mainWindow.navigationPanel.attenDetail.start.getText();
        }else if (select == 5){
            endTime = eventRes.mainWindow.navigationPanel.attenDetail.end.getText();
        }else {
            eventRes.mainWindow.navigationPanel.attenDetail.start.setText("");
            eventRes.mainWindow.navigationPanel.attenDetail.end.setText("");
        }
        switch (select){
            case 0:
                eventRes.mainWindow.navigationPanel.attenDetail.today.setBackground(Color.lightGray);
                beginTime = DateFormat.format(Mytoday.getTime());
                endTime = DateFormat.format(Mytoday.getTime() + DAY);
                break;
            case 1:
                eventRes.mainWindow.navigationPanel.attenDetail.yesterday.setBackground(Color.lightGray);
                beginTime = DateFormat.format(Mytoday.getTime() - DAY);
                endTime = DateFormat.format(Mytoday.getTime());
                break;
            case 2:
                eventRes.mainWindow.navigationPanel.attenDetail.week.setBackground(Color.lightGray);
                c.setTime(Mytoday);
                int weekday = c.get(Calendar.DAY_OF_WEEK);
                endTime = DateFormat.format(Mytoday.getTime() + DAY);
                beginTime = DateFormat.format(Mytoday.getTime() - ((weekday+5)%7) * DAY);
                break;
            case 3:
                eventRes.mainWindow.navigationPanel.attenDetail.month.setBackground(Color.lightGray);
                int monthday = c.get(Calendar.DAY_OF_MONTH);
                endTime = DateFormat.format(Mytoday.getTime() + DAY);
                beginTime = DateFormat.format(Mytoday.getTime() - (monthday-1) * DAY);
            default:
                break;
        }
    }

    public void setDetailStatusSelectButton(int select){
        if (select == 0){
            eventRes.mainWindow.navigationPanel.attenDetail.normal.setBackground(Color.lightGray);
            eventRes.mainWindow.navigationPanel.attenDetail.abnormal.setBackground(null);
            isLegal = 0;
        }
        else if (select == 1){
            eventRes.mainWindow.navigationPanel.attenDetail.abnormal.setBackground(Color.lightGray);
            eventRes.mainWindow.navigationPanel.attenDetail.normal.setBackground(null);
            isLegal = 1;
        }
    }

    public void setDetailTeamChoose(String team){
        teamChoose = team;
    }

    public String[] getDetailGroupContent() throws Exception{
        List group = new ArrayList<>();
        group.add("全部");
        ResultSet resultSet = eventRes.dbManager.dbSearch("teamrecord", "team", "");
        while (resultSet.next()){
            group.add(resultSet.getString("team"));
        }
        return (String[])group.toArray(new String[group.size()]);
    }

    public void doDetailSearch() throws Exception{
        if ((beginTime.length() == 0) || (endTime.length() == 0)){
            throw new Exception("请输入正确的时间范围");
        }
        ResultSet resultSet = null;
        if (teamChoose.compareTo("全部") != 0) {
            resultSet = eventRes.dbManager.dbSearch("AttendanceRecord, Staff ", "name,team,intime,outime,lastime,isLegal", "where staff.id = attendancerecord.id " +
                    "and isLegal = " + isLegal +
                    " and team = '" + teamChoose + "'" +
                    " and intime between '" + beginTime + "' and '" + endTime + "'");
        }else{
            resultSet = eventRes.dbManager.dbSearch("AttendanceRecord, Staff ", "name,team,intime,outime,lastime,isLegal", "where staff.id = attendancerecord.id " +
                    " and isLegal = " + isLegal +
                    " and intime between '" + beginTime + "' and '" + endTime + "'");
        }

        String[] columnsName = new String[]{"日期", "姓名", "上线时间", "下线时间", "状态", "小组", "持续时间/小时"};
        resultSet.last();
        Object[][] rowdata;
        int rowNum = resultSet.getRow();
        if (rowNum !=0) {
            rowdata = new Object[rowNum][7];
        }
        else {
            rowdata = new Object[][]{{"", "", "", "", "", "", ""}};
        }
        resultSet.beforeFirst();

        while (resultSet.next()){
            rowdata[rowNum - resultSet.getRow()][0] = resultSet.getString("intime").substring(0, 10);
            rowdata[rowNum - resultSet.getRow()][1] = resultSet.getString("name");
            rowdata[rowNum - resultSet.getRow()][2] = resultSet.getString("intime").substring(11, 19);
            if (resultSet.getString("outime") == null){
                rowdata[rowNum - resultSet.getRow()][3] = "";
            }
            else {
                rowdata[rowNum - resultSet.getRow()][3] = resultSet.getString("outime").substring(11, 19);
            }
            if (resultSet.getInt("isLegal") == 0) {
                rowdata[rowNum - resultSet.getRow()][4] = "正常";
            }
            else {
                rowdata[rowNum - resultSet.getRow()][4] = "异常";
            }
            rowdata[rowNum - resultSet.getRow()][5] = resultSet.getString("team");
            rowdata[rowNum - resultSet.getRow()][6] = resultSet.getString("lastime");
        }
        eventRes.mainWindow.navigationPanel.attenDetail.flushTable(rowdata);

    }

}
