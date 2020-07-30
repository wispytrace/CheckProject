package UiComponent;


import com.sun.javaws.util.JfxHelper;

import javax.swing.*;
import java.awt.*;


public class LogoHeader extends JPanel{
    private ImageIcon logo = new ImageIcon("UiResource/logoheader.jpg");
    private ImageIcon icon = new ImageIcon("UiResource/Icon.jpg");

    private LogoHeader logoHeader = this;

    private JPanel headerPanel = null;
    public JPanel userPanel = null;
    public String userName = null;

    public JButton login = null;
    public JButton cancel = null;


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Dimension d=this.getSize();
        g.drawImage(logo.getImage(), 0, 0, d.width,d.height,null);
    }

    private JPanel createSeizePanel(){
        JPanel blankSpace = new JPanel();
        blankSpace.setOpaque(false);
        return blankSpace;
    }

    public  LogoHeader(){
        logoHeader.setLayout(new BorderLayout());
        headerPanel = createHeader();
        logoHeader.add(headerPanel);
    }

    private JPanel createHeader(){
        headerPanel = new JPanel();
        GridBagLayout headerLayout = new GridBagLayout();
        GridBagConstraints format = new GridBagConstraints();
        headerPanel.setLayout(headerLayout);

        userPanel = new JPanel();
        login = new JButton("登陆");
        cancel = new JButton("注销");

        cancel.setBorderPainted(false);
        login.setBorderPainted(false);

        userPanel.add(login);
        userPanel.setOpaque(false);

        format.fill = GridBagConstraints.BOTH;
        format.weightx = 10;
        format.weighty = 1;
        format.gridwidth = 10;
        headerPanel.add(createSeizePanel(), format);
        format.weightx = 1;
        format.gridwidth = 1;
        format.gridwidth = GridBagConstraints.REMAINDER;
        headerPanel.add(userPanel, format);
        format.weighty = 5;
        headerPanel.add(createSeizePanel(), format);
        headerPanel.setOpaque(false);
        return headerPanel;
    }


}
