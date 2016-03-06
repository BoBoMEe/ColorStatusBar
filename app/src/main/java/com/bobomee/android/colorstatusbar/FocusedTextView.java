package com.bobomee.android.colorstatusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by bobomee on 16/3/6.
 */
public class FocusedTextView extends TextView {

    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

}