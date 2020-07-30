package UiComponent;

import UiComponent.TabbedPane.*;

import javax.swing.*;

public class NavigationPanel extends JTabbedPane {

    private JTabbedPane navigationPanel = this;

    public AttenDetail attenDetail = null;
    public AttenStastis attenStastis = null;
    public PersonManager personManager = null;
    public PersonStatus personStatus = null;
    public SystemSet systemSet = null;

    private ImageIcon icon = new ImageIcon("UiResource/Icon.jpg");

    public  NavigationPanel(){
        attenDetail = new AttenDetail();
        attenStastis = new AttenStastis();
        personManager = new PersonManager();
        personStatus = new PersonStatus();
        systemSet = new SystemSet();

        navigationPanel.setTabPlacement(JTabbedPane.LEFT);
        navigationPanel.addTab("考勤状态", icon, personStatus,"查看考勤状态信息");
        navigationPanel.addTab("考勤明细", icon, attenDetail,"查看近期考勤信息");
        navigationPanel.addTab("考勤统计", icon, attenStastis, "查看考勤统计信息");
        navigationPanel.addTab("人员管理", icon, personManager, "管理人员录入与移出");
        navigationPanel.addTab("系统设置", icon, systemSet, "更改系统设置");
        navigationPanel.setSelectedIndex(0);
    }

}
