package com.homesnap.android.ui.components;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import com.adgsoftware.mydomo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * Example of how to write a custom subclass of View. LabelView
 * is used to draw simple text views. Note that it does not handle
 * styled text or right-to-left writing systems.
 *
 */
public class CircleView extends View {
    private Paint mTextPaint;
    private String mText = "tex";
    private int mAscent;
    
    /**
     * Constructor.  This version is only needed if you will be instantiating
     * the object manually (not from a layout XML file).
     * @param context
     */
    public CircleView(Context context) {
        super(context);
        initLabelView();
    }

    /**
     * Construct object, initializing with any attributes we understand from a
     * layout file. These attributes are defined in
     * SDK/assets/res/any/classes.xml.
     * 
     * @see android.view.View#View(android.content.Context, android.util.AttributeSet)
     */
    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLabelView();

//        TypedArray a = context.obtainStyledAttributes(attrs,
//                R.styleable.LabelView);
//
//        CharSequence s = a.getString(R.styleable.LabelView_text);
//        if (s != null) {
//            setText(s.toString());
//        }
//
//        // Retrieve the color(s) to be used for this view and apply them.
//        // Note, if you only care about supporting a single color, that you
//        // can instead call a.getColor() and pass that to setTextColor().
//        setTextColor(a.getColor(R.styleable.LabelView_textColor, 0xFF000000));
//
//        int textSize = a.getDimensionPixelOffset(R.styleable.LabelView_textSize, 0);
//        if (textSize > 0) {
//            setTextSize(textSize);
//        }
//
//        a.recycle();
    }

    private final void initLabelView() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        // Must manually scale the desired text size to match screen density
        mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        mTextPaint.setColor(0xFF000000);
        setPadding(3, 3, 3, 3);
    }

    /**
     * Sets the text to display in this label
     * @param text The text to display. This will be drawn as one line.
     */
    public void setText(String text) {
        mText = text;
        requestLayout();
        invalidate();
    }

    /**
     * Sets the text size for this label
     * @param size Font size
     */
    public void setTextSize(int size) {
        // This text size has been pre-scaled by the getDimensionPixelOffset method
        mTextPaint.setTextSize(size);
        requestLayout();
        invalidate();
    }

    /**
     * Sets the text color for this label.
     * @param color ARGB value for the text
     */
    public void setTextColor(int color) {
        mTextPaint.setColor(color);
        invalidate();
    }

    /**
     * @see android.view.View#measure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
                    + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        mAscent = (int) mTextPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * Render the text
     * 
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mText, getPaddingLeft(), getPaddingTop() - mAscent, mTextPaint);
        int midY = getMeasuredHeight()/2;
        int midX = getMeasuredWidth()/2;
        Paint p = new Paint();
        Style s = Style.FILL_AND_STROKE;
        p.setStyle(s);
        p.setStrokeWidth(2);
        p.setColor(Color.BLUE);
        canvas.drawCircle(midX, midY, midX, p);
    }
}
