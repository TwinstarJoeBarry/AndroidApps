package edu.ncc.nest.nestapp.FragmentScanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.journeyapps.barcodescanner.ViewfinderView;

import edu.ncc.nest.nestapp.R;

public class FramedViewFinderView extends ViewfinderView {

    private final Paint PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int frameCornerSize;
    private int frameOffset;
    private int frameStyle;

    public FramedViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.FramedViewFinderView, 0, 0);

        try {

            PAINT.setStrokeWidth(a.getInteger(R.styleable.FramedViewFinderView_scanFrameThickness, 10));

            PAINT.setColor(a.getColor(R.styleable.FramedViewFinderView_scanFrameColor, 0xFFFFFF));

            PAINT.setAlpha((int) (0xFF * a.getFloat(R.styleable.FramedViewFinderView_scanFrameAlpha, 1f)));

            frameCornerSize = a.getDimensionPixelSize(R.styleable.FramedViewFinderView_scanFrameCornerSize, 48);

            frameOffset = a.getDimensionPixelSize(R.styleable.FramedViewFinderView_scanFrameOffset, 10);

            frameStyle = a.getInteger(R.styleable.FramedViewFinderView_scanFrameStyle, 2);

        } catch (RuntimeException exception) {

            Log.e(TAG, exception.getMessage());

            exception.printStackTrace();

        } finally { a.recycle(); }

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);




    }

}
