package edu.csulb.bacindicator.games.colors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.games.Game;

/**
 * Created by Johan on 25/04/2015.
 */
public class ColorsGameView extends Game implements View.OnTouchListener {
    private int nbrOfLaps = 25;
    private int lapTotalDuration = 1800; // In milliseconds
    private int possibleFailed = 1;

    private final String[] colorsName;
    private final int[] colors;

    private int width;
    private int height;

    private boolean gameStarted = false;

    private long timeLapStart;
    private int currentColorIndex;
    private int currentColorNameIndex;
    private long lapDuration;

    private int circleTop;
    private int circleBottom;
    private Bitmap ok, no;
    private Rect okRect, noRect;

    public ColorsGameView(Activity context) {
        super(context);

        setOnTouchListener(this);

        initWindow(context);

        ok = BitmapFactory.decodeResource(getResources(), R.drawable.check);
        no = BitmapFactory.decodeResource(getResources(), R.drawable.no);

        colorsName = context.getResources().getStringArray(R.array.colors_name);
        String[] codes = context.getResources().getStringArray(R.array.colors_code);

        colors = new int[codes.length];
        for (int i = 0; i < codes.length; ++i) {
            colors[i] = Color.parseColor(codes[i]);
        }

        pickColors(true);
    }

    @Override
    public void start() {
        gameStarted = true;
        invalidate();
    }

    /**
     *
     * @param retry retry if the color doesn't match to improve probability to get same colors
     */
    private void pickColors(boolean retry) {
        currentColorIndex = (int)(Math.random() * colors.length);
        currentColorNameIndex = (int)(Math.random() * colorsName.length);

        if (retry && currentColorIndex != currentColorNameIndex) {
            pickColors(false);
        }
    }

    private void initWindow(Context context) {
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();

        width = mDisplay.getWidth();
        height = mDisplay.getHeight();
    }

    private void drawPie(Canvas canvas, float angle) {
        int stroke = 50;
        int marginLeftRight = 10 + stroke;
        int mWidth = width - marginLeftRight * 2;
        int mHeight = mWidth;
        int marginTopBottom = (height - mWidth) / 2 - stroke;

        circleTop = marginTopBottom;
        circleBottom =  mHeight + marginTopBottom + stroke;

        Paint paint = new Paint();
        final RectF rect = new RectF();
        rect.set(marginLeftRight, marginTopBottom, mWidth + marginLeftRight, mHeight + marginTopBottom);
        paint.setColor(colors[currentColorIndex]);
        paint.setStrokeWidth(stroke);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rect, -90, angle, false, paint);
    }

    private void drawColorName(Canvas canvas) {
        Paint paint = new Paint();
        String text = colorsName[currentColorNameIndex].toUpperCase();
        paint.setColor(colors[currentColorIndex]);
        paint.setTextSize(100);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Rect boundsTxt = new Rect();
        paint.getTextBounds(text, 0, text.length(), boundsTxt);
        canvas.drawText(text, width / 2 - boundsTxt.width() / 2, height / 2 - boundsTxt.height() / 2, paint);
    }

    private void drawButtons(Canvas canvas) {
        int okLeft = width / 4 - ok.getWidth() / 2;
        int okTop =  height - (height - circleBottom - ok.getHeight() / 2);
        int noLeft = width - width / 4 - no.getWidth() / 2;
        int noTop = height - (height - circleBottom - no.getHeight() / 2);

        canvas.drawBitmap(ok, okLeft, okTop, null);
        canvas.drawBitmap(no, noLeft, noTop, null);

        okRect = new Rect(okLeft, okTop, okLeft + ok.getWidth(), okTop + ok.getHeight());
        noRect = new Rect(noLeft, noTop, noLeft + no.getWidth(), noTop + no.getHeight());
    }

    private void drawLegend(Canvas canvas) {
        Paint paint = new Paint();
        String text = getContext().getString(R.string.game_color_legend);
        paint.setColor(Color.parseColor("#cccccc"));
        paint.setTextSize(70);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        Rect boundsTxt = new Rect();
        paint.getTextBounds(text, 0, text.length(), boundsTxt);
        canvas.drawText(text, width / 2 - boundsTxt.width() / 2, circleTop / 2 - boundsTxt.height() / 2, paint);
    }

    private void checkEndOfLap() {
        long now = System.currentTimeMillis();
        if (timeLapStart == 0) {
            timeLapStart = now;
        }
        lapDuration = now - timeLapStart;

        if (lapDuration > lapTotalDuration) {
            possibleFailed--;
            endOfLap();
        }
    }

    private void endOfLap() {
        nbrOfLaps--;
        pickColors(true);
        timeLapStart = 0;
        lapDuration = 0;
        if (lapTotalDuration > 1000)
            lapTotalDuration -= 50;
    }

    private void checkGameOver() {
        if (nbrOfLaps < 0) {
            gameStarted = false;
            success();
        } else if (possibleFailed < 0) {
            gameStarted = false;
            failure();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!gameStarted) {
            return;
        }

        checkEndOfLap();
        checkGameOver();

        drawLegend(canvas);
        drawPie(canvas, lapDuration * 360 / lapTotalDuration);
        drawColorName(canvas);
        drawButtons(canvas);

        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Point p = new Point((int)motionEvent.getX(), (int)motionEvent.getY());
        Rect r = new Rect(p.x, p.y, p.x + 1, p.y + 1);
        if (okRect.contains(r)) {
            if (currentColorIndex != currentColorNameIndex) {
                possibleFailed--;
            }
            endOfLap();
        } else if (noRect.contains(r)) {
            if (currentColorIndex == currentColorNameIndex) {
                possibleFailed--;
            }
            endOfLap();
        }
        return false;
    }
}
