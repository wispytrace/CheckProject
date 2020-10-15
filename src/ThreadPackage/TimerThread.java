package ThreadPackage;

import EventPackage.MainEvent;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerThread {


    public Timer dbTimer = null;
    public MainEvent mainEvent = null;

    public TimerThread(MainEvent mainEvent){
        this.mainEvent = mainEvent;
    }

    public void setDbTimer(){
        dbTimer = new Timer();
        Date nowTime = new Date();
        dbTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try{
                    Thread.sleep(5000);
                    mainEvent.eventRes.dbManager.dbClose();
                    mainEvent.eventRes.dbManager.dbConnect("root", "root");
                }catch (Exception e){
                    mainEvent.eventRes.mainWindow.showErrorDialog(e.getMessage());
                }
            }
        }, nowTime, 1000 * 60 * 60 * 4);// 这里设定将延时每天固定执行
    }

}
