package android.check.checkmobile.FunctionPage;

import android.check.checkmobile.MainActivity;
import android.check.checkmobile.R;
import android.view.View;
import android.widget.Button;

public class SystemSet {
    private MainActivity mainActivity = null;
    private Button exitLogin = null;
    private Button modifyPassword = null;
    public SystemSet(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    public void Shift(){
        mainActivity.setContentView(R.layout.activity_set);
        exitLogin = mainActivity.findViewById(R.id.set_exit_login);
        modifyPassword = mainActivity.findViewById(R.id.set_modify_password);
        exitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.loginShift();
            }
        });
        modifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.showMessage("暂未开放");
            }
        });
    }
}
