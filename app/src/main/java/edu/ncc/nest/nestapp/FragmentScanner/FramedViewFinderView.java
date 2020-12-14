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

        if (frameStyle != 0 && framingRect != null) {

            if (frameStyle == 2) {

                int offset = (int) PAINT.getStrokeWidth() / 2;

                int x1 = framingRect.left - frameOffset;
                int y1 = framingRect.top - frameOffset;
                int x2 = framingRect.right + frameOffset;
                int y2 = framingRect.bottom + frameOffset;

                int x1_offset = x1 - offset;
                int y1_offset = y1 - offset;
                int x2_offset = x2 + offset;
                int y2_offset = y2 + offset;

                // Top-Left Corner
                canvas.drawLine(x1_offset, y1, x1_offset + frameCornerSize, y1, PAINT);
                canvas.drawLine(x1, y1_offset, x1, y1_offset + frameCornerSize, PAINT);

                // Top-Right Corner
                canvas.drawLine(x2_offset - frameCornerSize, y1, x2_offset, y1, PAINT);
                canvas.drawLine(x2, y1_offset, x2, y1_offset + frameCornerSize, PAINT);

                // Bottom-Right Corner
                canvas.drawLine(x2_offset, y2, x2_offset - frameCornerSize, y2, PAINT);
                canvas.drawLine(x2, y2_offset - frameCornerSize, x2, y2_offset, PAINT);

                // Bottom-Left Corner
                canvas.drawLine(x1_offset, y2, x1_offset + frameCornerSize, y2, PAINT);
                canvas.drawLine(x1, y2_offset - frameCornerSize, x1, y2_offset, PAINT);

            } else

                // Draw the frame as a rectangle
                canvas.drawRect(framingRect, PAINT);

        }


    }

}
