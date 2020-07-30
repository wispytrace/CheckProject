import BottomDriver.DbManager;
import BottomDriver.FingerManager;
import EventPackage.MainEvent;
import ThreadPackage.WorkThread;
import UiComponent.MainWindow;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AttendanceSystem {

    public DbManager dbManager = null;
    public FingerManager fingerManager = null;
    public WorkThread workThread = null;
    public MainEvent mainEvent = null;
    public MainWindow mainWindow = null;

    public AttendanceSystem(){
        dbManager = new DbManager();
        fingerManager = new FingerManager();
        mainWindow = new MainWindow();
        initAllDriver();
        mainEvent = new MainEvent(dbManager, fingerManager, mainWindow);
        workThread = new WorkThread(mainEvent);

    }

    public void initAllDriver(){
        try {
            fingerManager.deviceInit();
            dbManager.dbConnect("root", "root");
            mainWindow.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {
                    closeSystem();
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
        }catch (Exception e){
            mainWindow.showErrorDialog(e.getMessage());
            System.exit(-1);
        }
    }

    public void closeSystem(){
        try {
            fingerManager.deviceClose();
            dbManager.dbClose();
            mainEvent.recordEv.doClose();
            System.out.println("Close Successful!");
            System.exit(0);
        }catch (Exception e){
            mainWindow.showErrorDialog(e.getMessage());
            System.exit(-1);
        }
    }

    public void startAll(){
        workThread.start();
        mainWindow.setVisible(true);
    }

    public static void main(String args[]) {
        AttendanceSystem attendanceSystem = new AttendanceSystem();
        attendanceSystem.startAll();
    }
}
