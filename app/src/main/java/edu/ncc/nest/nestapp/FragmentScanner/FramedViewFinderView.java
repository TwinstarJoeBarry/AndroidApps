package edu.ncc.nest.nestapp.FragmentScanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.journeyapps.barcodescanner.ViewfinderView;

public class FramedViewFinderView extends ViewfinderView {

    private final Paint PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);

    public FramedViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);



    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);




    }

}
