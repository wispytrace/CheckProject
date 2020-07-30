package android.check.checkmobile.FunctionPage;

import android.check.checkmobile.MainActivity;
import android.check.checkmobile.R;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckDetail {
    private MainActivity mainActivity = null;
    private JSONObject jsonMessage = null;
    private DetailThread detailThread = null;
    private Button refresh = null;
    public CheckDetail(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

//    private TableRow createRow(String[] content){
//        TableRow tableRow = new TableRow(mainActivity);
//        for (int column = 0; column < content.length; column++){
//            TextView unit = new TextView(mainActivity);
//            unit.setText(content[column] + '\n');
//            unit.setTextColor(mainActivity.getResources().getColor(R.color.WHITE));
//            unit.setGravity(Gravity.CENTER);
//            tableRow.addView(unit);
//        }
//        return tableRow;
//    }



    private SimpleAdapter getAdapter(String[] name, String[] status, String[] lastIn, String[] lastOut, String[] last){
        List<Map<String, Object>> lists = new ArrayList<>();

        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", name[i]);
            map.put("status","状态: " + status[i]);
            map.put("lastIn", "最后一次进入时间: " + lastIn[i]);
            map.put("lastOut", "最后一次离开时间: " + lastOut[i]);
            map.put("last","持续时间: "+ last[i] +" 小时");
            lists.add(map);
        }
        //适配器指定应用自己定义的xml格式
        SimpleAdapter adapter = new SimpleAdapter(mainActivity, lists, R.layout.detail_list, new String[]{"name", "status", "lastIn", "lastOut", "last"},
                new int[]{R.id.detail_name, R.id.detail_status, R.id.detail_last_in, R.id.detail_last_out, R.id.detail_last});
        return adapter;
    }
    public void Shift(){
        mainActivity.setContentView(R.layout.activity_detail);
        refresh = mainActivity.findViewById(R.id.detail_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailThread==null || !detailThread.isAlive())
                {
                    detailThread = new DetailThread();
                    detailThread.start();
                }
            }
        });
        if (detailThread==null || !detailThread.isAlive())
        {
            detailThread = new DetailThread();
            detailThread.start();
        }
    }
    public void Refresh() throws Exception {
        ListView detailTable = (ListView) mainActivity.findViewById(R.id.detai_table);

        JSONArray infoList = jsonMessage.getJSONArray("data");
        String[] nameList = new String[infoList.length()];
        String[] statusList = new String[infoList.length()];
        String[] lastInList = new String[infoList.length()];
        String[] lastoutList = new String[infoList.length()];
        String [] lastList = new String[infoList.length()];
        for (int i = 0; i < infoList.length(); i++) {
            JSONObject unitObject = infoList.getJSONObject(i);
            nameList[i] = unitObject.getString("name");
            statusList[i] = unitObject.getString("status");
            lastInList[i] = unitObject.getString("intime");
            lastoutList[i] = unitObject.getString("outime");
            lastList[i] = unitObject.getString("lastime");
            if (lastInList[i]=="null"){
                lastInList[i] = "";
            }
//            else {
//                content[2] = content[2].substring(5);
//            }
            if (lastoutList[i]=="null"){
                lastoutList[i] = "";
            }
//            else {
//                content[3] = content[3].substring(5);
//            }
            if(lastList[i]=="null"){
                lastList[i] = "0";
            }
            if (statusList[i]=="0"){
                statusList[i] = "离线";
            }else {
                statusList[i] = "在线";
            }
        }
        detailTable.setAdapter(getAdapter(nameList, statusList, lastInList, lastoutList, lastList));
    }

    private class DetailThread extends Thread{
        public void run() {
            try {
                String jsonStr = GetData.postHtml("http://39.99.225.161/check/Detail.php", new String[]{""},new String[]{""});
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
