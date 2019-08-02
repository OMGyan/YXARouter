package com.yx.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yx.annotations.BindPath;

@BindPath("personal/personal")
public class PersonalActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        textView = ((TextView) findViewById(R.id.tv_person));
        Bundle bundle = getIntent().getBundleExtra("bundle");
        textView.setText(bundle.getString("name")+"  "+bundle.getString("age"));
    }
}
