package com.kw.top.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.kw.top.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class UILetterListView extends View {

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    List<String> sections = new ArrayList<String>();
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;

    public UILetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public UILetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UILetterListView(Context context) {
        super(context);
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
        sections.add(0, "");
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = 0;
        if (sections.size() > 0) {
            if (sections.size() < 20) {
                singleHeight = height / (sections.size() + 1);
            } else {
                singleHeight = height / (sections.size());
            }
        }

        int textSize = (DisplayUtils.getScreenHeight()
                - DisplayUtils.dip2px(getContext(), 110) - DisplayUtils.dip2px(
                getContext(), 8) * 26) / 26;

        for (int i = 0; i < sections.size(); i++) {
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            int screenHeightDp = DisplayUtils.px2dip(getContext(),
                                                     DisplayUtils.getScreenHeight());
            if (screenHeightDp < 530) {
                paint.setTextSize(10);
            } else if (screenHeightDp < 600) {
                paint.setTextSize(30);
            } else {
                paint.setTextSize(textSize);
            }

            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(Color.parseColor("#ffffff"));
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(sections.get(i)) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(sections.get(i), xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * sections.size());

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c > 0 && c < sections.size()) {
                        listener.onTouchingLetterChanged(sections.get(c));
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c > 0 && c < sections.size()) {
                        listener.onTouchingLetterChanged(sections.get(c));
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}
