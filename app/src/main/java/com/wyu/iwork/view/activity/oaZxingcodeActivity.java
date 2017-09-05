package com.wyu.iwork.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wyu.iwork.R;
import com.wyu.iwork.util.ZxingCodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class oaZxingcodeActivity extends BaseActivity {

    @BindView(R.id.activity_oa_zxingcode_input)
    EditText editText;

    @BindView(R.id.activity_oa_zxingcode_btn)
    Button btn;
    @BindView(R.id.activity_oa_zxingcode_image)
    ImageView imageView;

    private Bitmap code_bit = null;
    private Bitmap logo_bit = null;
    private Bitmap logo = null;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_zxingcode);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        logo = BitmapFactory.decodeResource(getResources(), R.mipmap.blog);
    }

    @OnClick(R.id.activity_oa_zxingcode_btn)
    void Click() {
        String content = editText.getText().toString();
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        code_bit = ZxingCodeUtils.generateBitmap(content, 600, 600);
        logo_bit = ZxingCodeUtils.addLogo(code_bit, logo);
        imageView.setImageBitmap(logo_bit);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        code_bit.recycle();
        code_bit = null;
        logo_bit.recycle();
        logo_bit = null;
        logo.recycle();
        logo = null;
    }
}
