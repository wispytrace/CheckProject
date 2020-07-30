package android.check.checkmobile.FunctionPage;

import android.check.checkmobile.MainActivity;
import android.check.checkmobile.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckStastic {
    private MainActivity mainActivity = null;
    private JSONObject jsonMessage = null;
    private Button lastWeek = null;
    private Button nextWeek = null;
    private final int DAY = 60 * 60 * 24 * 1000;
    private final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar calendar = Calendar.getInstance();
    private Date today = null;
    private String beginTime = null;
    private String endTime = null;
    private StasticThread stasticThread = null;
    private int dayIndx = 0;
    private int weekBegin = 3;
    private int weekDay = 0;

    public CheckStastic(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    public void Shift(){
        mainActivity.setContentView(R.layout.activity_stastic);
        lastWeek = mainActivity.findViewById(R.id.stasic_lastweek);
        nextWeek = mainActivity.findViewById(R.id.stastic_nextweek);
        today = new Date();
        calendar.setTime(today);
        weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        endTime = DateFormat.format(today.getTime() + DAY);
        beginTime = DateFormat.format(today.getTime() - ((weekDay+5)%7) * DAY);
        Log.e("test", weekDay+"");
        lastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                    dayIndx++;
                    endTime = beginTime;
//                    beginTime = DateFormat.format(today.getTime() - ((weekDay+5)%7) * DAY - 7 * DAY * dayIndx);
                    try {
                        beginTime = DateFormat.format(DateFormat.parse(beginTime).getTime() - 7 * DAY);
                    }catch (Exception e){
                        mainActivity.showMessage(e.getMessage());
                    }
                    if (stasticThread==null || !stasticThread.isAlive()){
                        stasticThread = new StasticThread();
                        stasticThread.start();
                    }

            }
        });
        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayIndx--;
                if (dayIndx==0){
                    endTime = DateFormat.format(today.getTime() + DAY);
                    beginTime = DateFormat.format(today.getTime() - ((weekDay+5)%7) * DAY);
                }
                else {
                    beginTime = endTime;
//                    endTime = DateFormat.format(today.getTime()  - ((weekDay+5)%7) * DAY - (dayIndx - 1) * 7 * DAY);
                    try {
                        endTime = DateFormat.format(DateFormat.parse(endTime).getTime() + 7*DAY);
                    }catch (Exception e){
                        mainActivity.showMessage(e.getMessage());
                    }
                }
//                endTime = DateFormat.format(today.getTime() - 7 * DAY * dayIndx);
//                beginTime = DateFormat.format(today.getTime() - 7 * DAY * dayIndx);
                if (stasticThread == null || !stasticThread.isAlive()) {
                    stasticThread = new StasticThread();
                    stasticThread.start();
                }

            }
        });
        if (stasticThread==null || !stasticThread.isAlive()){
            stasticThread = new StasticThread();
            stasticThread.start();
        }
    }
    private SimpleAdapter getAdapter(String[] name, String[] totalInOut, String[] totalTime, String[] totalInlegal, String[] evaluate){
        List<Map<String, Object>> lists = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name[i]);
            map.put("inOutTimes","总出入次数: " + totalInOut[i]);
            map.put("time", "总打卡时间: " + totalTime[i]+" 小时" );
            map.put("ilegalTimes", "总违规次数: " + totalInlegal[i]);
            map.put("evaluate","评价: "+ evaluate[i]);
            lists.add(map);
        }
        //适配器指定应用自己定义的xml格式
        SimpleAdapter adapter = new SimpleAdapter(mainActivity, lists, R.layout.stastic_list, new String[]{"name", "inOutTimes", "time", "ilegalTimes", "evaluate"},
                new int[]{R.id.stastic_name, R.id.stastic_inOutTimes, R.id.stastic_time, R.id.stastic_ilegalTimes, R.id.stastic_evaluate});
        return adapter;
    }

    public void Refresh() throws Exception{
        if (dayIndx==0){
            nextWeek.setVisibility(View.INVISIBLE);
        }else {
            nextWeek.setVisibility(View.VISIBLE);
        }
        TextView timeText = mainActivity.findViewById(R.id.stastic_time);
        timeText.setText(beginTime + "  到  " + endTime);
        ListView stasticTable = mainActivity.findViewById(R.id.stastic_table);
        JSONArray infoList= jsonMessage.getJSONArray("data");
        String[] nameList = new String[infoList.length()];
        String[] inOutTimesList = new String[infoList.length()];
        String[] timeList = new String[infoList.length()];
        String[] ilegalTimesList = new String[infoList.length()];
        String[] evaluateList = new String[infoList.length()];
        for (int i = 0; i < infoList.length(); i++){
            JSONObject unitObject = infoList.getJSONObject(i);
            nameList[i] = unitObject.getString("name");
            inOutTimesList[i] = unitObject.getString("totalNum");
            timeList[i] = unitObject.getString("totlLastime");
            ilegalTimesList[i] = unitObject.getString("totalInLegal");
            evaluateList[i] = unitObject.getString("evaluate");
        }
        stasticTable.setAdapter(getAdapter(nameList, inOutTimesList, timeList, ilegalTimesList, evaluateList));
    }
    private class StasticThread extends Thread{
        public void run() {
            try {
                String jsonStr = GetData.postHtml("http://39.99.225.161/check/Stastic.php", new String[]{"beginTime","endTime"},new String[]{beginTime, endTime});
                jsonMessage = new JSONObject(jsonStr);
                Message message = new Message();
                message.what = jsonMessage.getInt("status");
                message.arg1 = mainActivity.uiState;
                message.obj = jsonMessage.get("erro");
                mainActivity.myHander.sendMessage(message);
            } catch (Exception e) {
                Log.e("lou", e.getMessage() + "");
                Message message = new Message();
                message.what = mainActivity.ERRO_CODE;
                message.obj = e.getMessage();
                mainActivity.myHander.sendMessage(message);
            }
        }
    }
}
