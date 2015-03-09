package com.tonyjs.textviewexpand;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tonyjs on 15. 3. 4..
 */
public class AutoScaleTextView extends TextView {
    public static final String TAG = AutoScaleTextView.class.getSimpleName();

    public AutoScaleTextView(Context context) {
        super(context);
    }

    public AutoScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScaleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private String mOriginText;

    public void setOriginText(String text) {
        mOriginText = text;
        scaleText();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        scaleText();
    }

    private void scaleText() {
        int width = getWidth();
        boolean wrapContent = false;
        if (getLayoutParams() != null) {
            wrapContent = getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if (wrapContent) {
            setText(mOriginText);
            return;
        }

        if (width == 0) {
            setText(mOriginText);
            return;
        }

        if (TextUtils.isEmpty(mOriginText)) {
            mOriginText = "";
            setText(mOriginText);
            return;
        }

        recursiveScaleText();
    }

    private void recursiveScaleText() {
        int width = getWidth();
        Paint paint = getPaint();
        int textLength = mOriginText.length();
        width = width - getPaddingLeft() - getPaddingRight();
        int breakPosition = paint.breakText(mOriginText, true, width, null);
        if (breakPosition >= textLength) {
            setText(mOriginText);
            return;
        }

        float density = getContext().getResources().getDisplayMetrics().density;
        float textSize = (int)(getTextSize() / density) - 1.0f;
        if (textSize <= 8.0f) {
            setText(mOriginText);
            return;
        }
        setTextSize(textSize);
        recursiveScaleText();
    }

}
