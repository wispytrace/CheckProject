package ThreadPackage;

import EventPackage.EventRes;
import EventPackage.MainEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkThread extends Thread{
    MainEvent mainEvent = null;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int recordid = 0;
    //Work
    byte[] img = null;
    byte[] template = null;
    int[] fid = new int[1];
    //Enroll
    byte[][] preRegTemplate = null;
    int recordTimes = 0;


    public WorkThread(MainEvent mainEvent){
        super();
        this.mainEvent = mainEvent;
        this.img = new byte[mainEvent.eventRes.fingerManager.figureHeight * mainEvent.eventRes.fingerManager.figureWidth];
        this.template = new byte[mainEvent.eventRes.fingerManager.templateLen];
        this.preRegTemplate = new byte[mainEvent.eventRes.fingerManager.CONFIRM_TIMES][mainEvent.eventRes.fingerManager.templateLen];
    }



    private boolean isLegal(float lastime, String  intime) throws Exception{
        if (lastime > 8.0){
            return false;
        }
        Date nowTime = new Date();
        Date endTime = dateFormat.parse(intime.substring(0, 11)+mainEvent.recordEv.checkEndTime);
        if ((nowTime.getTime() - endTime.getTime()) > 0){
            return false;
        }
        return true;
    }

    public void setInOutStatus(int fid) throws Exception{
        String nowTime = dateFormat.format(new Date());
        if (mainEvent.eventRes.fingerManager.Online.containsKey(fid)){
            mainEvent.eventRes.dbManager.dbUpdate("AttendanceRecord","outime", "'" + nowTime + "'", "recordid="+mainEvent.eventRes.fingerManager.Online.get(fid));
            mainEvent.eventRes.dbManager.dbUpdate("Staff", "status", "0", "id="+fid);
            ResultSet resultSet =  mainEvent.eventRes.dbManager.dbSearch("AttendanceRecord", "intime", " where recordid=" + mainEvent.eventRes.fingerManager.Online.get(fid));
            resultSet.next();
            String intime = resultSet.getString("intime");
            float hour = 1000*60*60;
            float lastTime = (dateFormat.parse(nowTime).getTime() - dateFormat.parse(intime).getTime()) / hour;
            if (!isLegal(lastTime, intime)){
                mainEvent.eventRes.dbManager.dbUpdate("AttendanceRecord", "isLegal", Integer.toString(1), "recordid="+mainEvent.eventRes.fingerManager.Online.get(fid));
                mainEvent.eventRes.dbManager.dbUpdate("AttendanceRecord", "lastime", Float.toString(0), "recordid=" + mainEvent.eventRes.fingerManager.Online.get(fid));
            }
            else {
                mainEvent.eventRes.dbManager.dbUpdate("AttendanceRecord", "lastime", Float.toString(lastTime), "recordid=" + mainEvent.eventRes.fingerManager.Online.get(fid));
            }
            mainEvent.eventRes.fingerManager.Online.remove(fid);
            mainEvent.eventRes.fingerManager.lightControl("red");
            flushAll();
        }
        else {
            mainEvent.eventRes.fingerManager.Online.put(fid, recordid);
            mainEvent.eventRes.dbManager.dbInsert(recordid, fid);
            recordid++;
            mainEvent.eventRes.dbManager.dbUpdate("Staff", "status", "1", "id="+fid);
            mainEvent.eventRes.fingerManager.lightControl("green");
            flushAll();
        }
    }

    public void flushAll() throws Exception{
        mainEvent.personStatusEv.initPersonStatus();
        mainEvent.eventRes.mainWindow.navigationPanel.attenDetail.reset.doClick();
        mainEvent.attenDetailEv.doDetailSearch();
        mainEvent.eventRes.mainWindow.navigationPanel.attenStastis.reset.doClick();
        mainEvent.attenStastisEv.doStasticSearch();
    }

    public void checkIsStart() throws Exception{
        Date now = new Date();
        String nowTime = dateFormat.format(now);
        Date beginTime = dateFormat.parse(nowTime.substring(0, 11)+mainEvent.recordEv.checkBeginTime);
        if ((now.getTime() - beginTime.getTime()) < 0){
            mainEvent.eventRes.mainWindow.showErrorDialog("未到指定打卡时间");
            throw new IOException();
        }
    }

    public void checkOutIndetify() throws Exception{
        mainEvent.eventRes.fingerManager.figureAcquire(img, template);
        checkIsStart();
        mainEvent.eventRes.fingerManager.fingerIdentity(template, fid);
        setInOutStatus(fid[0]);
    }

    public void checkOutEnroll() throws Exception {
        //********************Set invoke user to press finger message
        while (recordTimes < 3) {
            mainEvent.eventRes.fingerManager.figureAcquire(img, preRegTemplate[recordTimes]);
            mainEvent.eventRes.fingerManager.fingerFileWrite(img, "temp.bmp");
            mainEvent.eventRes.mainWindow.enrollDialog.imgbtn.setIcon(new ImageIcon(ImageIO.read(new File("temp.bmp"))));
            mainEvent.eventRes.mainWindow.enrollDialog.imgbtn.revalidate();
            if (recordTimes >= 1) {
                mainEvent.eventRes.fingerManager.fingerMatch(preRegTemplate[recordTimes - 1], preRegTemplate[recordTimes]);
            }
            mainEvent.eventRes.fingerManager.lightControl("green");
            recordTimes = recordTimes + 1;
            if (recordTimes != 3) {
                mainEvent.eventRes.mainWindow.showMessageDialog("请再次输入指纹" + (3 - recordTimes) + "次");
            }
        }
        mainEvent.personManagerEv.template = new byte[mainEvent.eventRes.fingerManager.templateLen];
        mainEvent.eventRes.fingerManager.fingerMerge(preRegTemplate, mainEvent.personManagerEv.template);
        mainEvent.personManagerEv.isEnroll = false;
        mainEvent.eventRes.mainWindow.showMessageDialog("指纹录入成功！");
    }

    public void run() {
        super.run();

        try {
           recordid = mainEvent.eventRes.dbManager.dbGetMaxId("AttendanceRecord", "recordid");
        }catch (Exception e){
            System.out.println(dateFormat.format(new Date())+" 数据库连接失败, 错误提示"+ e.getMessage());
            mainEvent.eventRes.mainWindow.showErrorDialog(e.getMessage());
        }
        while (true){
            try{
                if (!mainEvent.personManagerEv.isEnroll){
                    recordTimes = 0;
                    checkOutIndetify();
                }else {
                    checkOutEnroll();
                }
            }catch (IOException e){
                try{
                    Thread.sleep(500);
                }catch (InterruptedException ie){
                    System.out.println(dateFormat.format(new Date())+" sleep语句发生错误, 错误提示"+ e.getMessage());
                    mainEvent.eventRes.mainWindow.showErrorDialog(ie.getMessage());
                }
            }catch (Exception e){
                recordTimes = 0;
                System.out.println(dateFormat.format(new Date())+" 语句发生错误, 错误提示"+ e.getMessage());
                mainEvent.eventRes.mainWindow.showErrorDialog(e.getMessage());
            }
        }
    }

}
