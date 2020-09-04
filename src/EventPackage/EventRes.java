package EventPackage;

import BottomDriver.DbManager;
import BottomDriver.FingerManager;
import UiComponent.MainWindow;

public class EventRes {
    public DbManager dbManager = null;
    public FingerManager fingerManager = null;
    public MainWindow mainWindow = null;



    public int currentPemission = 2;
    public static final int GENERAL = 2;
    public static final int MANAGER = 1;
    public static final int ROOT = 0;

    

    public EventRes(DbManager dbManager, FingerManager fingerManager, MainWindow mainWindow){
        this.dbManager = dbManager;
        this.fingerManager = fingerManager;
        this.mainWindow =  mainWindow;
    }

}
