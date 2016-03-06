package com.bobomee.android.colorstatusbar.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobomee.android.colorstatusbar.R;

/**
 * Created by bobomee on 16/3/6.
 */
public class BaseActivity extends BaseBaseActivity {

    private TextView textView1;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        initView();

    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);

        initView();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);

        initView();
    }

    private void initView() {
        textView1 = (TextView) findViewById(android.R.id.text1);
        String title = getString(R.string.header_tips_content);
        title = String.format(title, TAG);
        textView1.setText(title);

    }

}
