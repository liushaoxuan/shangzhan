package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.wyu.iwork.R;

public class FeedBackActivity extends BaseActivity {
    private static final String TAG="FeedBackActivity";
    private EditText title;
    private String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("任务描述");
        title=(EditText)findViewById(R.id.ed_Feedback);

        if(!"".equals(getIntent().getStringExtra("intro"))&&getIntent().getStringExtra("intro")!=null){
            title.setText(getIntent().getStringExtra("intro"));
        }
    }

    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        str=title.getText().toString();
        Bundle bundle=new Bundle();
        Intent it=getIntent();
        bundle.putString("Context",str);
        it.putExtras(bundle);
        setResult(102,it);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
