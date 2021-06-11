package edu.ncc.nest.nestapp.AbstractScanner;

/**
 * Copyright (C) 2020 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2012-2018 ZXing authors, Journey Mobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.journeyapps.barcodescanner.ViewfinderView;

import edu.ncc.nest.nestapp.R;

/**
 * FramedViewFinderView --
 * Adds a frame around a ViewFinderView
 * @author Tyler Sizse
 */
public class FramedViewFinderView extends ViewfinderView {

    private final Paint PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int FRAME_CORNER_SIZE;
    private final int FRAME_OFFSET;
    private final int FRAME_STYLE;


    ////////////// Constructor //////////////

    public FramedViewFinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.FramedViewFinderView, 0, 0);

        try {

            PAINT.setStrokeWidth(a.getInteger(R.styleable.FramedViewFinderView_scanFrameThickness, 10));

            PAINT.setColor(a.getColor(R.styleable.FramedViewFinderView_scanFrameColor, 0xFFFFFF));

            PAINT.setAlpha((int) (0xFF * a.getFloat(R.styleable.FramedViewFinderView_scanFrameAlpha, 1f)));

            FRAME_CORNER_SIZE = a.getDimensionPixelSize(R.styleable.FramedViewFinderView_scanFrameCornerSize, 48);

            FRAME_OFFSET = a.getDimensionPixelSize(R.styleable.FramedViewFinderView_scanFrameOffset, 10);

            FRAME_STYLE = a.getInteger(R.styleable.FramedViewFinderView_scanFrameStyle, 2);

        } finally {

            PAINT.setStyle(Paint.Style.STROKE);

            a.recycle();

        }

    }


    ////////////// Other Event Methods Start  //////////////

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (FRAME_STYLE != 0 && framingRect != null) {

            // Add the actual frame offset
            final int X1 = framingRect.left - FRAME_OFFSET;
            final int Y1 = framingRect.top - FRAME_OFFSET;
            final int X2 = framingRect.right + FRAME_OFFSET;
            final int Y2 = framingRect.bottom + FRAME_OFFSET;

            if (FRAME_STYLE == 2) {

                // Offset of stroke thickness
                int OFFSET = (int) PAINT.getStrokeWidth() / 2;

                // Add offset for the stroke thickness
                final int X1_OFFSET = X1 - OFFSET;
                final int Y1_OFFSET = Y1 - OFFSET;
                final int X2_OFFSET = X2 + OFFSET;
                final int Y2_OFFSET = Y2 + OFFSET;

                // Top-Left Corner
                canvas.drawLine(X1_OFFSET, Y1, X1_OFFSET + FRAME_CORNER_SIZE, Y1, PAINT);
                canvas.drawLine(X1, Y1_OFFSET, X1, Y1_OFFSET + FRAME_CORNER_SIZE, PAINT);

                // Top-Right Corner
                canvas.drawLine(X2_OFFSET - FRAME_CORNER_SIZE, Y1, X2_OFFSET, Y1, PAINT);
                canvas.drawLine(X2, Y1_OFFSET, X2, Y1_OFFSET + FRAME_CORNER_SIZE, PAINT);

                // Bottom-Right Corner
                canvas.drawLine(X2_OFFSET, Y2, X2_OFFSET - FRAME_CORNER_SIZE, Y2, PAINT);
                canvas.drawLine(X2, Y2_OFFSET - FRAME_CORNER_SIZE, X2, Y2_OFFSET, PAINT);

                // Bottom-Left Corner
                canvas.drawLine(X1_OFFSET, Y2, X1_OFFSET + FRAME_CORNER_SIZE, Y2, PAINT);
                canvas.drawLine(X1, Y2_OFFSET - FRAME_CORNER_SIZE, X1, Y2_OFFSET, PAINT);

            } else

                // Draw the frame as a rectangle
                canvas.drawRect(X1, Y1, X2, Y2, PAINT);

        }


    }

}
