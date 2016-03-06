package com.bobomee.android.colorstatusbar.ui;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bobomee.android.colorstatusbar.R;

/**
 * Created by bobomee on 16/3/6.
 */
public class BaseBaseDrawerLayout extends BaseActivity {

    protected DrawerLayout drawerLayout;
    protected Toolbar toolbar;
    private boolean hasTranslation = false;
    private TextView textView2;


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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        textView2 = (TextView) headerView.findViewById(android.R.id.text2);

        String title = getString(R.string.nav_header_name);
        title = String.format(title, TAG);

        textView2.setText(title);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (null == toolbar) return;
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

    }

    private void initEvents() {
        if (hasTranslation) {
            drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    View mContent = drawerLayout.getChildAt(0);
                    float scale = 1 - slideOffset;
                    float rightScale = 0.8f + scale * 0.2f;

                    float leftScale = 1 - 0.3f * scale;

                    drawerView.setScaleX(leftScale);
                    drawerView.setScaleY(leftScale);
                    drawerView.setAlpha(0.6f + 0.4f * (1 - scale));
                    mContent.setTranslationX(drawerView.getMeasuredWidth() * (1 - scale));
                    mContent.setPivotX(0);
                    mContent.setPivotY(mContent.getMeasuredHeight() / 2);

                    mContent.setScaleX(rightScale);
                    mContent.setScaleY(rightScale);
                    mContent.invalidate();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });
        } else {
            drawerLayout.setDrawerListener(null);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_anim) {
            hasTranslation = !hasTranslation;
            setHasTranslation(hasTranslation);
            this.getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.getItem(2);
        String title = getString(R.string.action_anim);
        title = String.format(title, hasTranslation);
        item.setTitle(title);

        return super.onPrepareOptionsMenu(menu);

    }

    protected void setHasTranslation(boolean hasTranslation) {
        this.hasTranslation = hasTranslation;
        initEvents();
    }


}
