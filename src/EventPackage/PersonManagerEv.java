package EventPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonManagerEv {
    public EventRes eventRes = null;

    public boolean isEnroll = false;
    public byte[] template = null;

    public PersonManagerEv(EventRes eventRes) {
        this.eventRes = eventRes;
    }

    public void initPersonManager() {
        try {
            eventRes.mainWindow.navigationPanel.personManager.enroll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isEnroll = true;
                    eventRes.mainWindow.enrollDialog.showEnrollDialog();
                }
            });
            eventRes.mainWindow.navigationPanel.personManager.modify.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eventRes.mainWindow.modifyDialog.showModifyDialog(eventRes.mainWindow.navigationPanel.personManager.modifyID);
                }
            });
            eventRes.mainWindow.enrollDialog.confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String nameContent = eventRes.mainWindow.enrollDialog.nameContent.getText();
                        String snoContent = eventRes.mainWindow.enrollDialog.snoContent.getText();
                        String permissionContent = eventRes.mainWindow.enrollDialog.permissionContent.getText();
                        String tutorContent = eventRes.mainWindow.enrollDialog.tutorContent.getText();
                        String teamContent = eventRes.mainWindow.enrollDialog.teamContent.getText();
                        String phoneContent = eventRes.mainWindow.enrollDialog.phoneContent.getText();
                        String passwordContent = eventRes.mainWindow.enrollDialog.passwordContent.getText();
                        String passwordAgainContent = eventRes.mainWindow.enrollDialog.passwordAgainContent.getText();
                        doPersonEnroll(new String[]{nameContent, snoContent, permissionContent, tutorContent, teamContent, phoneContent
                                , passwordContent, passwordAgainContent}, template);
                        isEnroll = false;
                        eventRes.mainWindow.enrollDialog.setVisible(false);
                    } catch (Exception e1) {
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
            eventRes.mainWindow.enrollDialog.cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isEnroll = false;
                    eventRes.mainWindow.enrollDialog.setVisible(false);
                }
            });
            eventRes.mainWindow.enrollDialog.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                    isEnroll = true;
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    isEnroll = false;
                    eventRes.mainWindow.enrollDialog.setVisible(false);
                }

                @Override
                public void windowClosed(WindowEvent e) {

                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });
            eventRes.mainWindow.modifyDialog.confirm.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String nameContent = eventRes.mainWindow.modifyDialog.nameContent.getText();
                        String snoContent = eventRes.mainWindow.modifyDialog.snoContent.getText();
                        String permissionContent = eventRes.mainWindow.modifyDialog.permissionContent.getText();
                        String tutorContent = eventRes.mainWindow.modifyDialog.tutorContent.getText();
                        String teamContent = eventRes.mainWindow.modifyDialog.teamContent.getText();
                        String phoneContent = eventRes.mainWindow.modifyDialog.phoneContent.getText();
                        String passwordContent = eventRes.mainWindow.modifyDialog.passwordContent.getText();
                        String passwordAgainContent = eventRes.mainWindow.modifyDialog.passwordAgainContent.getText();
                        doPersonModyify(new String[]{nameContent, snoContent, permissionContent, tutorContent, teamContent, phoneContent
                                , passwordContent, passwordAgainContent}, eventRes.mainWindow.navigationPanel.personManager.modifyID);
                    } catch (Exception e1) {
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
            eventRes.mainWindow.modifyDialog.cancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    eventRes.mainWindow.modifyDialog.setVisible(false);
                }
            });
            eventRes.mainWindow.modifyDialog.delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        deletePersonInfo(eventRes.mainWindow.navigationPanel.personManager.modifyID);
                    } catch (Exception e1) {
                        eventRes.mainWindow.showErrorDialog(e1.getMessage());
                    }
                }
            });
            flushPersonTable();
        }catch (Exception e){
            eventRes.mainWindow.showErrorDialog(e.getMessage());
        }


    }

    public void deletePersonInfo(String id) throws Exception{
        eventRes.dbManager.dbDelete("Staff", "id", id);
        eventRes.dbManager.dbDelete("FingerBase", "id", id);
        eventRes.dbManager.dbDelete("AttendanceRecord", "id", id);
        eventRes.fingerManager.fingerDelete(Integer.parseInt(id));
        flushPersonTable();
        flushStatusPanel();
        flushNameList();
        eventRes.mainWindow.showMessageDialog("删除成功");
        eventRes.mainWindow.modifyDialog.setVisible(false);
    }

    public void doPersonModyify(String[] information, String id) throws Exception{
        if (information[6].compareTo(information[7]) != 0){
            throw new Exception("两次密码输入不一样,请重新输入");
        }
        if (information[0].length() > 20){
            throw new Exception("用户姓名输入过长,请重新输入");
        }
        if (information[1].length() > 15){
            throw new Exception("用户学号输入过长,请重新输入");
        }
        if (information[3].length() > 20){
            throw new Exception("导师名称输入过长, 请重新输入");
        }
        if (information[4].length() > 20){
            throw new Exception("小组名称输入过长, 请重新输入");
        }
        if (information[5].length() > 20){
            throw new Exception("手机号码输入过长,请重新输入");
        }
        if (information[6].length() > 20){
            throw new Exception("密码输入过长, 请重新输入");
        }
        if (information[0].length()>0){
            eventRes.dbManager.dbUpdateChar("Staff", "name", information[0], "id="+id);
        }
        if (information[1].length()>0){
            eventRes.dbManager.dbUpdateChar("Staff", "sno", information[1], "id="+id);
        }
        try{
            if (information[2].length() > 1) {
                throw new Exception();
            }
            if (information[2].length() == 1) {
//                if (Integer.parseInt(information[2]) < guiManager.userPemission){
//                    throw new Exception();
//                }
                if ((Integer.parseInt(information[2]) < 0) || (Integer.parseInt(information[2]) > 2)) {
                    throw new Exception();
                }
                else {
                    eventRes.dbManager.dbUpdate("Staff", "permission", information[2], "id=" + id);
                }
            }
        }catch (Exception e){
            throw new Exception("请输入正确的权限等级");
        }

        if (information[3].length()>0){
            eventRes.dbManager.dbUpdateChar("Staff", "tutor", information[3], "id="+id);
        }
        if (information[4].length()>0){
            eventRes.dbManager.dbUpdateChar("Staff", "team", information[4], "id="+id);
        }
        if (information[5].length()>0){
            eventRes.dbManager.dbUpdateChar("Staff", "phone", information[5], "id="+id);
        }
        if (information[6].length()>0){
            eventRes.dbManager.dbUpdateChar("Staff", "phone", information[6], "id="+id);
        }
        eventRes.mainWindow.showMessageDialog("修改成功");
        eventRes.mainWindow.modifyDialog.setVisible(false);
        flushPersonTable();
        flushNameList();
        flushStatusPanel();
    }

    public void doPersonEnroll(String[] information, byte[] fingerTemplate) throws Exception {
        if (fingerTemplate == null) {
            throw new Exception("请先录入你的指纹");
        }
        if (information[0].length() == 0) {
            throw new Exception("姓名不能为空,请输入姓名");
        }
        if (information[1].length() == 0) {
            throw new Exception("学号不能为空,请输入学号");
        }
        try {
            if (information[2].length() != 1) {
                throw new Exception();
            }
            if ((Integer.parseInt(information[2]) < 0) || (Integer.parseInt(information[2]) > 2)) {
                throw new Exception();
            }
//            if (Integer.parseInt(information[2]) < guiManager.userPemission){
//                throw new Exception();
//            }
        } catch (Exception e) {
            throw new Exception("请输入正确的权限等级");
        }

        if (information[6].length() == 0) {
            throw new Exception("密码不能为空, 请输入密码");
        }
        if (information[7].length() == 0) {
            throw new Exception("请再次输入密码");
        }
        if (information[6].compareTo(information[7]) != 0) {
            throw new Exception("两次密码输入不一样,请重新输入");
        }
        if (information[0].length() > 20) {
            throw new Exception("用户姓名输入过长,请重新输入");
        }
        if (information[1].length() > 15) {
            throw new Exception("用户学号输入过长,请重新输入");
        }
        if (information[3].length() > 20) {
            throw new Exception("导师名称输入过长, 请重新输入");
        }
        if (information[4].length() > 20) {
            throw new Exception("小组名称输入过长, 请重新输入");
        }
        if (information[5].length() > 20) {
            throw new Exception("手机号码输入过长,请重新输入");
        }
        if (information[6].length() > 20) {
            throw new Exception("密码输入过长, 请重新输入");
        }
        int id = eventRes.dbManager.dbGetMaxId("Staff", "id");
//       dbManager.dbInsert(id, information[0].getBytes(), information[1].getBytes(), Integer.parseInt(information[2]), information[3].getBytes(), information[4].getBytes(), 0, information[5].getBytes(), information[6].getBytes());
        eventRes.dbManager.dbInsert(id, information[0], information[1], Integer.parseInt(information[2]), information[3], information[4], 0, information[5], information[6]);
        eventRes.dbManager.dbInsert(id, fingerTemplate);
        eventRes.fingerManager.fingerAdd(id, fingerTemplate);
        eventRes.mainWindow.showMessageDialog("人员录入成功!");
        template = null;
        eventRes.mainWindow.enrollDialog.setVisible(false);
        flushStatusPanel();
        flushPersonTable();
        flushNameList();
    }

    public Object[][] getPersonTable() throws Exception {
        ResultSet resultSet = eventRes.dbManager.dbSearch("Staff", "*", "");
        resultSet.last();
        Object[][] rowdata = null;
        if (resultSet.getRow() == 0) {
            rowdata = new Object[][]{{"", "", "", "", "", "", ""}};
        } else {
            rowdata = new Object[resultSet.getRow()][7];
        }
        resultSet.beforeFirst();
        while (resultSet.next()) {
            rowdata[resultSet.getRow() - 1][0] = resultSet.getString("id");
            rowdata[resultSet.getRow() - 1][1] = resultSet.getString("name");
            rowdata[resultSet.getRow() - 1][2] = resultSet.getString("team");
            rowdata[resultSet.getRow() - 1][3] = resultSet.getString("tutor");
            rowdata[resultSet.getRow() - 1][4] = resultSet.getString("sno");
            rowdata[resultSet.getRow() - 1][5] = resultSet.getString("phone");
            rowdata[resultSet.getRow() - 1][6] = resultSet.getString("id");
        }
        return rowdata;
    }

    public void flushPersonTable() throws Exception {
        eventRes.mainWindow.navigationPanel.personManager.flushTable(getPersonTable());
    }

    public void flushNameList() throws Exception{
        eventRes.mainWindow.navigationPanel.attenStastis.flushNameList(getAllStaffName());
    }

    public void flushStatusPanel() throws Exception {
        ResultSet resultSet = eventRes.dbManager.dbSearch("Staff", "*", " ");
        resultSet.last();
        int num = (int) Math.sqrt(resultSet.getRow());
        resultSet.beforeFirst();
        eventRes.mainWindow.navigationPanel.personStatus.setGridNum(num);
        eventRes.mainWindow.navigationPanel.personStatus.flushStatusPanel(resultSet);
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
}
