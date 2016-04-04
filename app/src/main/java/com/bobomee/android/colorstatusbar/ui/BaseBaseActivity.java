package com.bobomee.android.colorstatusbar.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bobomee.android.colorstatus.StatusBarUtils;
import com.bobomee.android.colorstatusbar.MApp;
import com.bobomee.android.colorstatusbar.R;
import com.bobomee.android.colorstatusbar.util.ToastUtil;

/**
 * Created by bobomee on 16/3/6.
 */
public class BaseBaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();
    private boolean isFill = true;
    protected StatusBarUtils instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retireData();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar(getResources().getColor(R.color.colorPrimaryDark), 112);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setStatusBar(getResources().getColor(R.color.colorPrimaryDark), 112);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setStatusBar(getResources().getColor(R.color.colorPrimaryDark), 112);
    }

    protected void setStatusBar(int color, int alpha) {
        instance = StatusBarUtils.instance(this);
        instance.setColor(color).
                setStyle(isFill ? StatusBarUtils.TYPE.FILL : StatusBarUtils.TYPE.NOMAL).setAlpha(alpha).init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected void ationFill() {
        ToastUtil.show(this, getResources().getString(R.string.action_fill));

        this.isFill = true;
        restart();
    }

    protected void ationNomal() {
        ToastUtil.show(this, getResources().getString(R.string.action_nomal));

        this.isFill = false;
        restart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_fill) {

            ationFill();

            return true;
        } else if (id == R.id.action_nomal) {
            ationNomal();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restart() {
        this.finish();
        storeData();
        startActivity(new Intent(this, getClass()));
    }

    private void storeData() {
        MApp app = (MApp) getApplication();
        app.mState = isFill;
    }

    private void retireData() {
        MApp app = (MApp) getApplication();
        isFill = app.mState;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StatusBarUtils.instance(this).clear();
    }
}
