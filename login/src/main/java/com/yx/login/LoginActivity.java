package com.yx.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yx.arouter.ARouter;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void jumpActivity(){
        ARouter.getInstance().jumpActivity("personal",null);
    }
}
