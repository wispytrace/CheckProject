package android.check.checkmobile.FunctionPage;

import android.check.checkmobile.MainActivity;
import android.check.checkmobile.R;
import android.os.Message;
import android.util.JsonReader;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonInfo {
    private MainActivity mainActivity = null;
    private JSONObject jsonMessage = null;
    private Button refreshButton = null;
    private InfoThread infoThread = null;
    public PersonInfo(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    public void Shift(){
        mainActivity.setContentView(R.layout.activity_info);
        refreshButton = mainActivity.findViewById(R.id.info_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infoThread==null || !infoThread.isAlive()){
                    infoThread = new InfoThread();
                    infoThread.start();
                }
            }
        });
        if (infoThread==null || !infoThread.isAlive()){
            infoThread = new InfoThread();
            infoThread.start();
        }
    }
    public void Refresh() throws Exception{
        ListView infoTable = mainActivity.findViewById(R.id.info_table);
        JSONArray infoList = jsonMessage.getJSONArray("data");
        String[] nameList = new String[infoList.length()];
        String[] tutorList = new String[infoList.length()];
        String[] snoList = new String[infoList.length()];
        String[] teamList = new String[infoList.length()];
        String[] phoneList = new String[infoList.length()];
        for (int i = 0; i < infoList.length(); i++){
            JSONObject unitObject = infoList.getJSONObject(i);
            nameList[i] = unitObject.getString("name");
            snoList[i] = unitObject.getString("sno");
            tutorList[i] = unitObject.getString("tutor");
            teamList[i] = unitObject.getString("team");
            phoneList[i] = unitObject.getString("phone");
        }
        infoTable.setAdapter(getAdapter(nameList, snoList, tutorList, teamList, phoneList));
    }

    private SimpleAdapter getAdapter(String[] name, String[] sno, String[] tutor, String[] team, String[] phone){
        List<Map<String, Object>> lists = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name[i]);
            map.put("sno","学号: " + sno[i]);
            map.put("tutor", "导师: " + tutor[i]);
            map.put("team", "小组: " + team[i]);
            map.put("phone","手机号码: "+ phone[i]);
            lists.add(map);
        }
        //适配器指定应用自己定义的xml格式
        SimpleAdapter adapter = new SimpleAdapter(mainActivity, lists, R.layout.info_list, new String[]{"name", "sno", "tutor", "team", "phone"},
                new int[]{R.id.info_name, R.id.info_sno, R.id.info_tutor, R.id.info_team, R.id.info_phone});
        return adapter;
    }
//    private TableRow createRow(String[] content){
//        TableRow tableRow = new TableRow(mainActivity);
//        for (int column = 0; column < content.length; column++){
//            TextView unit = new TextView(mainActivity);
//            unit.setText(content[column]);
//            unit.setTextColor(mainActivity.getResources().getColor(R.color.WHITE));
//            unit.setGravity(Gravity.CENTER);
//            tableRow.addView(unit);
//        }
//        return tableRow;
//    }
    private class InfoThread extends Thread{
        public void run() {
            try {
                String jsonStr = GetData.postHtml("http://39.99.225.161/check/Info.php", new String[]{""},new String[]{""});
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
