package com.bobomee.android.colorstatusbar.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bobomee.android.colorstatusbar.R;

import java.util.Random;

/**
 * Created by bobomee on 16/3/6.
 */
public class BaseViewGroupDrawerLayout extends BaseBaseDrawerLayout implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    private ListView listView;

    private CheckBox checkBox;

    private SeekBar mSbChangeAlpha;
    private TextView mTvStatusAlpha;

    private int mAlpha = 112;

    protected String[] getListData() {
        return getResources().getStringArray(R.array.clz);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        listView = (ListView) findViewById(android.R.id.list);
        mSbChangeAlpha = (SeekBar) findViewById(R.id.sb_change_alpha);
        mTvStatusAlpha = (TextView) findViewById(R.id.tv_status_alpha);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.simple_list_item,
                getListData());

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(this);

        checkBox = (CheckBox) findViewById(android.R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);

        mSbChangeAlpha.setMax(255);
        mSbChangeAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                setStatusBar(instance.getColor(), mAlpha);
                mTvStatusAlpha.setText(String.valueOf(mAlpha));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbChangeAlpha.setProgress(mAlpha);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = ((TextView) view).getText().toString().trim();
        try {
            Intent intent = new Intent(this, Class.forName(name));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void changeStatus(View view) {
        Random random = new Random();
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);

        if (toolbar.getVisibility() == View.VISIBLE) {
            toolbar.setBackgroundColor(ranColor);
        }

        setStatusBar(ranColor, mAlpha);
    }

    public void changeBackground(View view) {

        Random random = new Random();
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);

        getWindow().getDecorView().setBackgroundColor(ranColor);
        setStatusBar(ranColor, 0);

        if (toolbar.getVisibility() == View.VISIBLE) {
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        toolbar.setVisibility(isChecked ? View.GONE : View.VISIBLE);
    }
}
