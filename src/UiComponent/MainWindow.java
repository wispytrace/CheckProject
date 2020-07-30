package UiComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;


public class MainWindow extends JFrame{

    private MainWindow mainWindow = this;
    private String CHECK_TITLE = "USTC振动控制与运动体控制科学实验室-考勤系统";

    private static final int TOP_CONTAINER_HEIGHT = 960;
    private static final int TOP_CONTAINER_WIDTH = 1280;
    private ImageIcon icon = new ImageIcon("UiResource/Icon.jpg");

    public LogoHeader logoHeader = null;
    public NavigationPanel navigationPanel = null;
    public LoginDialog loginDialog = null;
    public EnrollDialog enrollDialog = null;
    public ModifyDialog modifyDialog = null;


    public MainWindow(){
        logoHeader = new LogoHeader();
        navigationPanel = new NavigationPanel();
        loginDialog = new LoginDialog();
        enrollDialog = new EnrollDialog();
        modifyDialog = new ModifyDialog();

        GridBagLayout frameLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        mainWindow.setLayout(frameLayout);
        mainWindow.setSize(TOP_CONTAINER_WIDTH,TOP_CONTAINER_HEIGHT);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setIconImage(icon.getImage());
        format.weighty = 1;
        format.weightx = 1;
        format.fill = GridBagConstraints.BOTH;
        format.gridwidth = GridBagConstraints.REMAINDER;
        mainWindow.add(logoHeader, format);
        mainWindow.add(navigationPanel, format);
        mainWindow.setTitle(CHECK_TITLE);
        setWindowTray();
    }

    public void showWindow(){
        mainWindow.setVisible(true);
    }

    private void setWindowTray(){
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage("UiResource/Icon.jpg");//*.gif与该类文件同一目录
        SystemTray systemTray = SystemTray.getSystemTray(); //获得系统托盘的实例
        TrayIcon trayIcon = null;

        try {
            trayIcon = new TrayIcon(img, "USTC振动与控制科学实验室-考勤系统");
            systemTray.add(trayIcon); //设置托盘的图标，*.gif与该类文件同一目录
            trayIcon.setImageAutoSize(true);
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }

        //窗口最小化时软件dispose
        mainWindow.addWindowListener(new WindowAdapter() {
            //图标化窗口时调用事件
            public void windowIconified(WindowEvent e) {
                mainWindow.dispose(); //窗口最小化时dispose该窗口
            }
        });

        //双击托盘图标，软件正常显示
        trayIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) //双击托盘窗口再现
                    //置此 frame 的状态。该状态表示为逐位掩码。
                    mainWindow.setExtendedState(Frame.NORMAL); //正常化状态
                mainWindow.setVisible(true);
            }
        });
    }

    private JPanel createSeizePanel(){
        JPanel blankSpace = new JPanel();
        blankSpace.setOpaque(false);
        return blankSpace;
    }

    public File getOutputDialog(){
        JFileChooser fileChooser = new JFileChooser();

        // 设置打开文件选择框后默认输入的文件名
        fileChooser.setSelectedFile(new File("考勤明细.txt"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(false);
        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"保存", 则获取选择的保存路径
            File file = fileChooser.getSelectedFile();
            System.out.println("保存到文件: " + file.getAbsolutePath() + "\n\n");
            return file;
        }
        return null;
    }

    public class LoginDialog extends JDialog{
        public LoginDialog loginDialog = this;
        public JButton confirm = null;
        public JButton cancel = null;
        public JTextField nameContent = null;
        public JPasswordField passwordConten = null;

        public LoginDialog(){
            super(mainWindow, "考勤系统登陆", true);
            loginDialog.setSize(TOP_CONTAINER_WIDTH/2, TOP_CONTAINER_HEIGHT/3);
            loginDialog.setResizable(false);
            loginDialog.setIconImage(icon.getImage());
            JLabel name = new JLabel("姓名:");
            JLabel password = new JLabel("密码:");
            nameContent = new JTextField(15);
            passwordConten = new JPasswordField(15);
            confirm = new JButton("确定");
            cancel = new JButton("取消");

            GridBagLayout loginLayout = new GridBagLayout();
            GridBagConstraints format = new GridBagConstraints();
            loginDialog.setLayout(loginLayout);

            format.gridwidth = GridBagConstraints.REMAINDER;
            format.weighty = 0.5;
            loginDialog.add(createSeizePanel(), format);
            format.gridwidth = 1;
            format.weightx = 2;
            format.weighty = 1;
            loginDialog.add(createSeizePanel(), format);
            format.weightx = 1.5;
            loginDialog.add(name, format);
            loginDialog.add(nameContent,format);
            format.weightx = 2;
            format.gridwidth = GridBagConstraints.REMAINDER;
            loginDialog.add(createSeizePanel(), format);

            format.gridwidth = 1;
            format.weightx = 2;
            loginDialog.add(createSeizePanel(), format);
            format.weightx = 1;
            loginDialog.add(password, format);
            loginDialog.add(passwordConten,format);
            format.weightx = 2;
            format.gridwidth = GridBagConstraints.REMAINDER;
            loginDialog.add(createSeizePanel(), format);

            format.weightx = 2;
            format.gridwidth = 1;
            loginDialog.add(createSeizePanel(), format);
            format.weightx = 1;
            loginDialog.add(confirm, format);
            format.gridwidth = GridBagConstraints.REMAINDER;
            loginDialog.add(cancel,format);

            loginDialog.setLocationRelativeTo(null);
        }

        public void showLoginDialog(){
            nameContent.setText("");
            passwordConten.setText("");
            loginDialog.setVisible(true);
        }
    }

    public class EnrollDialog extends JDialog  {
        public EnrollDialog enrollDialog = this;
        public JPanel imgPanel = null;
        public JButton imgbtn = null;
        public JPanel infPanel = null;
        public JTextField nameContent = null;
        public JTextField snoContent = null;
        public JTextField permissionContent = null;
        public JTextField tutorContent = null;
        public JTextField teamContent = null;
        public JTextField phoneContent = null;
        public JPasswordField passwordContent = null;
        public JPasswordField passwordAgainContent = null;
        public JButton confirm = null;
        public JButton cancel = null;

        public EnrollDialog() {
            super(mainWindow, "人员录入", true);
            enrollDialog.setSize(TOP_CONTAINER_WIDTH * 3 / 5, TOP_CONTAINER_HEIGHT * 3 / 5);
            enrollDialog.setResizable(false);
            GridBagLayout enrollLayout = new GridBagLayout();
            enrollDialog.setLayout(enrollLayout);
            GridBagConstraints format = new GridBagConstraints();
            enrollDialog.setIconImage(icon.getImage());

            JLabel message = new JLabel("请输入下列相关信息,并输入三次指纹。含有*号的选项不可以为空,权限设置中(0-最高级, 1-管理级, 2-一般用户)");
            message.setFont(new Font(null, Font.BOLD, 14));
            format.weightx = 2;
            format.weighty = 1;
            format.gridwidth = GridBagConstraints.REMAINDER;
            enrollDialog.add(message, format);

            imgPanel = new JPanel(new BorderLayout());
            imgbtn = new JButton();
            imgbtn.setDefaultCapable(false);
            imgPanel.add(imgbtn);

            infPanel = new JPanel(new GridBagLayout());
            JLabel name = new JLabel("姓名*");
            nameContent = new JTextField(20);
            JLabel sno = new JLabel("学号*");
            snoContent = new JTextField(20);
            JLabel permission = new JLabel("权限设置*");
            permissionContent = new JTextField(20);
            JLabel tutor = new JLabel("导师");
            tutorContent = new JTextField(20);
            JLabel team = new JLabel("小组");
            teamContent = new JTextField(20);
            JLabel phone = new JLabel("手机号码");
            phoneContent = new JTextField(20);
            JLabel password = new JLabel("密码设置*");
            passwordContent = new JPasswordField(20);
            JLabel passwrodAgain = new JLabel("请再确认密码*");
            passwordAgainContent = new JPasswordField(20);
            quickLayout(infPanel, name, nameContent);
            quickLayout(infPanel, sno, snoContent);
            quickLayout(infPanel, permission, permissionContent);
            quickLayout(infPanel, tutor, tutorContent);
            quickLayout(infPanel, team, teamContent);
            quickLayout(infPanel, phone, phoneContent);
            quickLayout(infPanel, password, passwordContent);
            quickLayout(infPanel, passwrodAgain, passwordAgainContent);

            format.weighty = 2;
            format.gridwidth = 1;
            format.weightx = 1;
            format.fill = GridBagConstraints.BOTH;
            enrollDialog.add(infPanel, format);
            format.weightx = 1;
            format.gridwidth = GridBagConstraints.REMAINDER;
            enrollDialog.add(imgPanel, format);

            confirm = new JButton("确认");
            cancel = new JButton("取消");

            format.fill = GridBagConstraints.NONE;
            format.weighty = 1;
            format.gridwidth = 1;
            enrollDialog.add(confirm, format);
            enrollDialog.add(cancel, format);
            format.gridwidth = GridBagConstraints.REMAINDER;
            enrollDialog.add(createSeizePanel(), format);

            enrollDialog.setLocationRelativeTo(null);
        }

        public void showEnrollDialog(){
            nameContent.setText("");
            snoContent.setText("");
            permissionContent.setText("");
            tutorContent.setText("");
            teamContent.setText("");
            phoneContent.setText("");
            passwordContent.setText("");
            passwordAgainContent.setText("");
            imgbtn.removeAll();
            enrollDialog.setVisible(true);
        }

        private void quickLayout(JPanel infPanel ,JComponent label, JComponent content){
            GridBagConstraints format = new GridBagConstraints();
            format.weightx = 1;
            infPanel.add(label, format);
            format.gridwidth = GridBagConstraints.REMAINDER;
            infPanel.add(content, format);
            infPanel.add(createSeizePanel(), format);
        }

    }

    public class ModifyDialog extends JDialog  {
        public ModifyDialog modifyDialog = this;
        public JPanel infPanel = null;
        public JTextField nameContent = null;
        public JTextField snoContent = null;
        public JTextField permissionContent = null;
        public JTextField tutorContent = null;
        public JTextField teamContent = null;
        public JTextField phoneContent = null;
        public JPasswordField passwordContent = null;
        public JPasswordField passwordAgainContent = null;
        public JButton confirm = null;
        public JButton cancel = null;
        public JButton delete = null;
        private String userID = null;

        public ModifyDialog() {
            super(mainWindow, "人员录入", true);
            modifyDialog.setSize(TOP_CONTAINER_WIDTH * 3 / 5, TOP_CONTAINER_HEIGHT * 3 / 5);
            modifyDialog.setResizable(false);
            GridBagLayout enrollLayout = new GridBagLayout();
            modifyDialog.setLayout(enrollLayout);
            GridBagConstraints format = new GridBagConstraints();
            modifyDialog.setIconImage(icon.getImage());

            JLabel message = new JLabel("请修改下列的相关信息");
            message.setFont(new Font(null, Font.BOLD, 14));
            format.weightx = 2;
            format.weighty = 1;
            format.gridwidth = GridBagConstraints.REMAINDER;
            modifyDialog.add(message, format);


            infPanel = new JPanel(new GridBagLayout());
            JLabel name = new JLabel("姓名*");
            nameContent = new JTextField(20);
            JLabel sno = new JLabel("学号*");
            snoContent = new JTextField(20);
            JLabel permission = new JLabel("权限设置*");
            permissionContent = new JTextField(20);
            JLabel tutor = new JLabel("导师");
            tutorContent = new JTextField(20);
            JLabel team = new JLabel("小组");
            teamContent = new JTextField(20);
            JLabel phone = new JLabel("手机号码");
            phoneContent = new JTextField(20);
            JLabel password = new JLabel("密码设置*");
            passwordContent = new JPasswordField(20);
            JLabel passwrodAgain = new JLabel("请再确认密码*");
            passwordAgainContent = new JPasswordField(20);
            quickLayout(infPanel, name, nameContent);
            quickLayout(infPanel, sno, snoContent);
            quickLayout(infPanel, permission, permissionContent);
            quickLayout(infPanel, tutor, tutorContent);
            quickLayout(infPanel, team, teamContent);
            quickLayout(infPanel, phone, phoneContent);
            quickLayout(infPanel, password, passwordContent);
            quickLayout(infPanel, passwrodAgain, passwordAgainContent);

            format.weighty = 2;
            format.gridwidth = 1;
            format.weightx = 1;
            format.fill = GridBagConstraints.BOTH;
            format.gridwidth = GridBagConstraints.REMAINDER;
            modifyDialog.add(infPanel, format);
            format.weightx = 1;

            confirm = new JButton("确认");
            cancel = new JButton("取消");
            delete = new JButton("删除");

            format.fill = GridBagConstraints.NONE;
            format.weighty = 1;
            format.gridwidth = 1;
            modifyDialog.add(confirm, format);
            modifyDialog.add(cancel, format);
            modifyDialog.add(delete, format);
            format.gridwidth = GridBagConstraints.REMAINDER;
            modifyDialog.add(createSeizePanel(), format);


            modifyDialog.setLocationRelativeTo(null);

        }

        public void showModifyDialog(String userID){
            this.userID = userID;
            nameContent.setText("");
            snoContent.setText("");
            permissionContent.setText("");
            tutorContent.setText("");
            teamContent.setText("");
            phoneContent.setText("");
            passwordContent.setText("");
            passwordAgainContent.setText("");
            modifyDialog.setVisible(true);
        }

        private void quickLayout(JPanel infPanel ,JComponent label, JComponent content){
            GridBagConstraints format = new GridBagConstraints();
            format.weightx = 1;
            infPanel.add(label, format);
            format.gridwidth = GridBagConstraints.REMAINDER;
            infPanel.add(content, format);
            infPanel.add(createSeizePanel(), format);
        }

    }

    public void showErrorDialog(String message){
        JOptionPane.showMessageDialog(
                mainWindow,
                message,
                "错误提示",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public void showMessageDialog(String message){
        JOptionPane.showMessageDialog(
                mainWindow,
                message,
                "消息提示",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

//    public static void main(String args[]){
//        new MainWindow();
//    }
}
