package com.tonyjs.textviewexpand;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tonyjs on 14. 12. 30..
 */
public class CharacterBreakTextView extends TextView {
    public static final String TAG = CharacterBreakTextView.class.getSimpleName();

    public CharacterBreakTextView(Context context) {
        super(context);
    }

    public CharacterBreakTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CharacterBreakTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private String mText;
    public void setBrokenText(String text) {
        mText = text;
        breakText();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        breakText();
    }

    private String mTitle;

    public void setTitle(String title) {
        mTitle = title;
    }

    private void breakText() {
        int width = getWidth();
        boolean wrapContent = false;
        if (getLayoutParams() != null) {
            wrapContent = getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if (wrapContent) {
            setText(mText);
            return;
        }

        if (width == 0) {
            setText(mText);
            return;
        }

        if (TextUtils.isEmpty(mText)) {
            mText = "";
            setText(mText);
            return;
        }

        Paint paint = getPaint();
        int textLength = mText.length();
        width = width - getPaddingLeft() - getPaddingRight();
        int breakPosition = paint.breakText(mText, true, width, null);
        if (breakPosition >= textLength) {
            setText(mText);
            return;
        }

        StringBuilder sb = new StringBuilder();
        String[] newLineSection = mText.split("\n");
        if (newLineSection.length > 1) {
            int i = 0, max = newLineSection.length - 1;

            for (String s : newLineSection) {
                if (!TextUtils.isEmpty(s)) {
                    append(width, paint, s, sb);
                }
                if (i < max) {
                    if (!TextUtils.isEmpty(s)) {
                        sb.append('\n');
                    }
                }
                i++;
            }
            mText = sb.toString();
        } else {
            append(width, paint, mText, sb);
            mText = sb.toString();
        }
        setText(mText);
    }

    private void append(int width, Paint paint, String source, StringBuilder sb){
        int textLength = source.length();
        int breakPosition = paint.breakText(source, true, width, null);
        if (breakPosition >= textLength) {
            sb.append(source);
            return;
        }

        String brokenText = source.substring(0, breakPosition);
        String extraText = source.substring(breakPosition, source.length());
        sb.append(brokenText + '\n');
        while (true) {
            int extraTextLength = extraText.length();
            breakPosition = paint.breakText(extraText, true, width, null);
            if (breakPosition >= extraTextLength) {
                sb.append(extraText);
                break;
            }

            brokenText = extraText.substring(0, breakPosition) + '\n';
            extraText = extraText.substring(breakPosition, extraText.length());
            sb.append(brokenText);
        }
    }
}
