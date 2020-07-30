package android.check.checkmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.check.checkmobile.FunctionPage.CheckDetail;
import android.check.checkmobile.FunctionPage.CheckStastic;
import android.check.checkmobile.FunctionPage.HomePage;
import android.check.checkmobile.FunctionPage.LoginPage;
import android.check.checkmobile.FunctionPage.PersonInfo;
import android.check.checkmobile.FunctionPage.SystemSet;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public static final int LOGIN_STATE = 0;
    public static final int HOMES_STATE = 1;
    public static final int DETAIL_STATE = 2;
    public static final int STASTIC_STATE = 3;
    public static final int INFO_STATE = 4;
    public static final int SET_STATE = 5;
    public static final int ERRO_CODE = -1;
    public static final int SUCC_CODE = 0;


    public int uiState = LOGIN_STATE;
    private HomePage homePage = new HomePage(this);
    private LoginPage loginPage = new LoginPage(this);
    private CheckDetail checkDetail = new CheckDetail(this);
    private CheckStastic checkStastic = new CheckStastic(this);
    private PersonInfo personInfo = new PersonInfo(this);
    private SystemSet systemSet = new SystemSet(this);

    public Handler myHander = new Handler(){
        public void handleMessage(Message message){
            try {
                if (message.what == ERRO_CODE) {
                    showMessage(message.obj.toString());
                } else {
                    switch (message.arg1) {
                        case LOGIN_STATE:
                            homePage.Shift();
                            uiState = HOMES_STATE;
                            showMessage("登陆成功");
                            break;
                        case HOMES_STATE:
                            switch (message.arg2){
                                case DETAIL_STATE:
                                    checkDetail.Shift();
                                    uiState = DETAIL_STATE;
                                    break;
                                case STASTIC_STATE:
                                    checkStastic.Shift();
                                    uiState = STASTIC_STATE;
                                    break;
                                case INFO_STATE:
                                    personInfo.Shift();
                                    uiState = INFO_STATE;
                                    break;
                                case SET_STATE:
                                    systemSet.Shift();
                                    uiState = SET_STATE;
                                    break;
                                    default:
                                        showMessage("发生了未知错误1");
                                        break;
                            }
                            break;
                        case DETAIL_STATE:
                            checkDetail.Refresh();
                            break;
                        case STASTIC_STATE:
                            checkStastic.Refresh();
                            break;
                        case INFO_STATE:
                            personInfo.Refresh();
                            break;
                        case SET_STATE:
                            break;
                        default:
                            showMessage("发生了未知错误2");
                    }
                }
                super.handleMessage(message);
            }catch (Exception e){
                showMessage(e.getMessage());
            }
        }
    };

    public void showMessage(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
    public void loginShift(){
        uiState = LOGIN_STATE;
        loginPage.Shift();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPage.Shift();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            switch (uiState){
                case LOGIN_STATE:
                    return super.onKeyDown(keyCode, event);
                case HOMES_STATE:
                    uiState = LOGIN_STATE;
                    loginPage.Shift();
                    break;
                case DETAIL_STATE:
                case STASTIC_STATE:
                case SET_STATE:
                case INFO_STATE:
                    uiState = HOMES_STATE;
                    homePage.Shift();
                    break;
                    default:
                        return super.onKeyDown(keyCode, event);
            }
//            Toast.makeText(this, "按下了back键   onKeyDown()", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

}

