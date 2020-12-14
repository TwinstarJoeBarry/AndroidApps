package edu.ncc.nest.nestapp.FragmentScanner;

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
import android.util.Log;

import com.journeyapps.barcodescanner.ViewfinderView;

import edu.ncc.nest.nestapp.R;

/**
 * FramedViewFinderView --
 * Adds a frame around a ViewFinderView
 * @author Tyler Sizse
 */
public class FramedViewFinderView extends ViewfinderView {

    private final Paint PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int frameCornerSize;
    private int frameOffset;
    private int frameStyle;


    ////////////// Constructor //////////////

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
