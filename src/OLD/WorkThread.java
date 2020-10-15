package OLD;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkThread extends Thread{
    private GuiManager guiManager = null;
    private FingerManager fingerManager = null;
    private DbManager dbManager = null;
    private ComponentManger componentManger = null;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String checkBeginTime = null;
    private String checkEndTime = null;
    private int recordid = 0;
    public void WorkThread(GuiManager guiManager, FingerManager fingerManager, DbManager dbManager, ComponentManger componentManger){
        try {

            this.guiManager = guiManager;
            this.fingerManager = fingerManager;
            this.dbManager = dbManager;
            this.componentManger = componentManger;
            ResultSet resultSet = dbManager.dbSearch("SystemSet", "beginTime", "");
            resultSet.next();
            checkBeginTime = resultSet.getString("beginTime") + ":00";
            resultSet = dbManager.dbSearch("SystemSet", "endTime", "");
            resultSet.next();
            checkEndTime = resultSet.getString("endTime") + ":00";
        }catch (Exception e){
            guiManager.showMessageDialog(e.getMessage());
        }
    }
    private boolean isLegal(float lastime, String  intime) throws Exception{
        if (lastime > 8.0){
            return false;
        }
        Date nowTime = new Date();
        Date endTime = dateFormat.parse(intime.substring(0, 11)+checkEndTime);
        if ((nowTime.getTime() - endTime.getTime()) > 0){
            return false;
        }
        return true;
    }

    public void setInOutStatus(int fid) throws Exception{
        String nowTime = dateFormat.format(new Date());
        if (fingerManager.Online.containsKey(fid)){
            dbManager.dbUpdate("AttendanceRecord","outime", "'" + nowTime + "'", "recordid="+fingerManager.Online.get(fid));
            dbManager.dbUpdate("Staff", "status", "0", "id="+fid);
            ResultSet resultSet =  dbManager.dbSearch("AttendanceRecord", "intime", " where recordid=" + fingerManager.Online.get(fid));
            resultSet.next();
            String intime = resultSet.getString("intime");
            float hour = 1000*60*60;
            float lastTime = (dateFormat.parse(nowTime).getTime() - dateFormat.parse(intime).getTime()) / hour;
            if (!isLegal(lastTime, intime)){
                dbManager.dbUpdate("AttendanceRecord", "isLegal", Integer.toString(1), "recordid="+fingerManager.Online.get(fid));
                dbManager.dbUpdate("AttendanceRecord", "lastime", Float.toString(0), "recordid=" + fingerManager.Online.get(fid));
            }
            else {
                dbManager.dbUpdate("AttendanceRecord", "lastime", Float.toString(lastTime), "recordid=" + fingerManager.Online.get(fid));
            }
            fingerManager.Online.remove(fid);
            fingerManager.lightControl("red");
            componentManger.statusDisplay(guiManager.statusPanel);
            componentManger.doDetailSearch(guiManager.detailResultPanel);
        }
        else {
            fingerManager.Online.put(fid, recordid);
            dbManager.dbInsert(recordid, fid);
            recordid++;
            dbManager.dbUpdate("Staff", "status", "1", "id="+fid);
            fingerManager.lightControl("green");
            componentManger.statusDisplay(guiManager.statusPanel);
            componentManger.doDetailSearch(guiManager.detailResultPanel);
        }
    }
    public void checkIsStart() throws Exception{
        Date now = new Date();
        String nowTime = dateFormat.format(now);
        Date beginTime = dateFormat.parse(nowTime.substring(0, 11)+checkBeginTime);
        if ((now.getTime() - beginTime.getTime()) < 0){
            guiManager.showErrorDialog("未到指定打卡时间");
            throw new IOException();
        }
    }
    public void run() {
        super.run();
        byte[] img = new byte[fingerManager.figureHeight * fingerManager.figureWidth];
        byte[] template = new byte[fingerManager.templateLen];
        int[] fid = new int[1];
        try {
           recordid = dbManager.dbGetMaxId("AttendanceRecord");
        }catch (Exception e){
            guiManager.showErrorDialog(e.getMessage());
        }
        while (true){
            try{
                fingerManager.figureAcquire(img, template);
                checkIsStart();
                fingerManager.fingerIdentity(template, fid);
                setInOutStatus(fid[0]);
            }catch (IOException e){
                try{
                    Thread.sleep(500);
                }catch (InterruptedException ie){
                    guiManager.showErrorDialog(ie.getMessage());
                }
            }catch (Exception e){
                guiManager.showErrorDialog(e.getMessage());
            }
        }
    }
}
