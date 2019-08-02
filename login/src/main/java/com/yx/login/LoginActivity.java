package com.yx.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yx.annotations.BindPath;
import com.yx.arouterx.ARouter;

@BindPath("login/login")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void jumpActivity(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("name","james");
        bundle.putString("age","34");
        ARouter.getInstance().jumpActivity("personal/personal",bundle);
    }
}
