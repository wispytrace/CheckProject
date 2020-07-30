package EventPackage;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordEv {

    public EventRes eventRes = null;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String checkBeginTime = null;
    public String checkEndTime = null;



    public RecordEv(EventRes eventRes) {
        this.eventRes = eventRes;
    }

    public void doInit() {
        try {
            initTimeConstrain();
            fixInitRecord();
            fixPersonStatus();
        } catch (Exception e) {
            eventRes.mainWindow.showErrorDialog(e.getMessage());
        }
    }

    public void doClose() {
        try {
            fixPersonStatus();
            fixCloseRecord();
        } catch (Exception e) {
            eventRes.mainWindow.showErrorDialog(e.getMessage());
        }
    }

    private void initTimeConstrain() throws Exception {
        ResultSet resultSet = eventRes.dbManager.dbSearch("SystemSet", "beginTime", "");
        resultSet.next();
        this.checkBeginTime = resultSet.getString("beginTime") + ":00";
        resultSet = eventRes.dbManager.dbSearch("SystemSet", "endTime", "");
        resultSet.next();
        this.checkEndTime = resultSet.getString("endTime") + ":00";
    }

    private void fixPersonStatus() throws Exception {
        ResultSet resultSet = eventRes.dbManager.dbSearch("Staff", "id", " where status=1");
        while (resultSet.next()) {
            eventRes.dbManager.dbUpdate("Staff", "status", "0", "id=" + resultSet.getString("id"));
        }
    }

    private void fixInitRecord() throws Exception {
        ResultSet resultSet = eventRes.dbManager.dbSearch("AttendanceRecord", "recordid, intime", " where outime is null");

        while (resultSet.next()) {
            String intime = resultSet.getString("intime");
            int fixRecordId = resultSet.getInt("recordid");
            eventRes.dbManager.dbUpdate("AttendanceRecord", "outime", "'" + intime + "'", " recordid=" + fixRecordId);
            eventRes.dbManager.dbUpdate("AttendanceRecord", "isLegal", Integer.toString(1), "recordid=" + fixRecordId);
            eventRes.dbManager.dbUpdate("AttendanceRecord", "lastime", Float.toString(0), "recordid=" + fixRecordId);
        }
    }

    private void fixCloseRecord() throws Exception {
        String nowTime = dateFormat.format(new Date());
        ResultSet resultSet = eventRes.dbManager.dbSearch("AttendanceRecord", "recordid, intime", " where outime is null");
        while (resultSet.next()) {
            String intime = resultSet.getString("intime");
            int fixRecordId = resultSet.getInt("recordid");
            eventRes.dbManager.dbUpdate("AttendanceRecord", "outime", "'" + nowTime + "'", " recordid=" + fixRecordId);
            float hour = 1000 * 60 * 60;
            float lastTime = (dateFormat.parse(nowTime).getTime() - dateFormat.parse(intime).getTime()) / hour;
            if (!isLegal(lastTime, intime)) {
                    eventRes.dbManager.dbUpdate("AttendanceRecord", "isLegal", Integer.toString(1), "recordid=" + fixRecordId);
                    eventRes.dbManager.dbUpdate("AttendanceRecord", "lastime", Float.toString(0), "recordid=" + fixRecordId);
                } else {
                    eventRes.dbManager.dbUpdate("AttendanceRecord", "lastime", Float.toString(lastTime), "recordid=" + fixRecordId);
                }
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
}