package android.check.checkmobile.FunctionPage;

import android.check.checkmobile.MainActivity;
import android.check.checkmobile.R;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class LoginPage {
    MainActivity mainActivity = null;
    EditText account = null;
    EditText password = null;
    Thread loginThread = null;

    public LoginPage(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    private class LoginThread extends Thread{
        public void run() {
            try {
                String JsonStr = GetData.postHtml("http://39.99.225.161/check/Login.php", new String[]{"name", "password"}, new String[]{account.getText().toString(), password.getText().toString()});
                JSONObject jsonObject = new JSONObject(JsonStr);
                Message message = new Message();
                message.what = jsonObject.getInt("status");
                message.arg1 = mainActivity.LOGIN_STATE;
                message.obj = jsonObject.getString("erro");
                mainActivity.myHander.sendMessage(message);
            } catch (Exception e) {
                Message message = new Message();
                message.what = mainActivity.ERRO_CODE;
                message.obj = e.getMessage();
                mainActivity.myHander.sendMessage(message);
                Log.e("lou", e.getMessage() + "");
            }
        }
    }
    public void Shift(){
        mainActivity.setContentView(R.layout.activity_login);
        mainActivity.setContentView(R.layout.activity_login);
        Button LoginButton = mainActivity.findViewById(R.id.login);
        account = mainActivity.findViewById(R.id.loginAccount_id);
        password = mainActivity.findViewById(R.id.password_id);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_main)
                if (loginThread==null || !loginThread.isAlive()){
                    loginThread = new LoginThread();
                    loginThread.start();
                }
            }
        });
    }
}
