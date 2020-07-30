package android.check.checkmobile.FunctionPage;


import android.check.checkmobile.MainActivity;
import android.check.checkmobile.R;
import android.os.Message;
import android.view.View;
import android.widget.TextView;


public class HomePage {
    public TextView checkDetail = null;
    public TextView checkStastic = null;
    public TextView personInfo = null;
    public TextView systemSet = null;
    private MainActivity mainActivity;


    public  HomePage(MainActivity mainActivity){
        this.mainActivity = mainActivity;

    }
    public void Shift() {
        mainActivity.setContentView(R.layout.activity_main);
        checkDetail = mainActivity.findViewById(R.id.checkDetail);
        checkStastic = mainActivity.findViewById(R.id.checkStastic);
        personInfo = mainActivity.findViewById(R.id.personInfo);
        systemSet = mainActivity.findViewById(R.id.systemSet);
        checkDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mainActivity.DETAIL_STATE);
            }
        });
        checkStastic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mainActivity.STASTIC_STATE);
            }
        });
        personInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mainActivity.INFO_STATE);
            }
        });
        systemSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mainActivity.SET_STATE);
            }
        });
    }

    private void sendMessage(int nextState){
        Message message = new Message();
        message.what = mainActivity.SUCC_CODE;
        message.arg1 = mainActivity.HOMES_STATE;
        message.arg2 = nextState;
        mainActivity.myHander.sendMessage(message);
    }
}
