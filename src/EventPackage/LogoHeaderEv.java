package EventPackage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class LogoHeaderEv {
    public EventRes eventRes = null;

    public LogoHeaderEv(EventRes eventRes){
        this.eventRes = eventRes;
    }

    public void initLogoHeader(){
        eventRes.mainWindow.loginDialog.confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    doLogin();
                }catch (Exception e1){
                    eventRes.mainWindow.showErrorDialog(e1.getMessage());
                }
            }
        });
        eventRes.mainWindow.logoHeader.cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                eventRes.mainWindow.logoHeader.userPanel.remove(0);
                eventRes.mainWindow.logoHeader.userPanel.remove(0);
                eventRes.mainWindow.logoHeader.userPanel.add(eventRes.mainWindow.logoHeader.login);
                eventRes.mainWindow.showMessageDialog("注销成功!");
                eventRes.mainWindow.logoHeader.userPanel.revalidate();
            }
        });
        eventRes.mainWindow.loginDialog.cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventRes.mainWindow.loginDialog.setVisible(false);
            }
        });
        eventRes.mainWindow.logoHeader.login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventRes.mainWindow.loginDialog.showLoginDialog();
            }
        });
    }

    public void doLogin() throws Exception{
        String userName = eventRes.mainWindow.loginDialog.nameContent.getText();
        ResultSet resultSet = eventRes.dbManager.dbSearch("Staff", "*", "where name = '"+ userName +"' " );
        if (resultSet.next()) {
            if (eventRes.mainWindow.loginDialog.passwordConten.getText().compareTo(resultSet.getString("password")) == 0) {
                eventRes.mainWindow.logoHeader.userName = userName;
                eventRes.mainWindow.showMessageDialog(userName + "登陆成功, 欢迎您!");
                eventRes.mainWindow.loginDialog.setVisible(false);
                eventRes.mainWindow.logoHeader.userPanel.remove(0);
                eventRes.mainWindow.logoHeader.userPanel.add(new JLabel(userName));
                eventRes.mainWindow.logoHeader.userPanel.add(eventRes.mainWindow.logoHeader.cancel);
                eventRes.mainWindow.logoHeader.userPanel.revalidate();
                return;
            }
        }
        throw new Exception("用户不存在,或输入的密码错误!");
    }

}