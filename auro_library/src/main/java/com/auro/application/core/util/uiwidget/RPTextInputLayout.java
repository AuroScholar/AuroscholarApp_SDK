package com.auro.application.core.util.uiwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;
import com.auro.application.R;


/**
 * Created by ashutosh on 2/5/18.
 */

public class RPTextInputLayout extends TextInputLayout {
    public RPTextInputLayout(Context context) {
        super(context);
    }

    public RPTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RPView, 0, 0);

        String font = a.getString(R.styleable.RPView_Font);
        if(font != null){
            Typeface typeface = getTypeface(font, context);
            if(typeface != null){
                setTypeface(typeface);
            }
        }
        a.recycle();
    }

    public RPTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Typeface getTypeface(String fontname, Context context) {

        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/"+fontname);
        } catch (Exception e) {
            return null;
        }
        return typeface;
    }
}
