package edu.csulb.bacindicator.games.pint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.games.GameView;

/**
 * Created by Brian on 30/04/2015.
 */
public class PintGameView extends GameView implements View.OnTouchListener {
    private PintElement target;
    private PintElement pint;

    private Bitmap green_circle;
    private Bitmap red_circle;
    Paint paint;

    private long startTime;
    private long nowTime;
	private CountDownTimer countDown = null;

    private boolean startCount = false;

    private int[]	targetSize;
    private int[] pintSize;
    private int timeLeft;
    
    private boolean start = false;

    private int width;
    private int height;

    public PintGameView(Activity context) {
        super(context);

        targetSize = new int[2];
        pintSize = new int[2];
        
        paint = new Paint(); 
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        targetSize[0] = size.x / 4;
        targetSize[1] = size.y / 5;

        pintSize[0] = size.x / 6;
        pintSize[1] = size.y / 7;
        

        Bitmap b = BitmapFactory.decodeResource(getResources(),
                R.drawable.pinte);
        red_circle = BitmapFactory.decodeResource(getResources(),
                R.drawable.red_circle);
        green_circle = BitmapFactory.decodeResource(getResources(),
                R.drawable.green_circle);
        target = new PintElement(Bitmap.createScaledBitmap(red_circle, targetSize[0], targetSize[1],
                false), 70, 70);
        pint = new PintElement(Bitmap.createScaledBitmap(b, pintSize[0], pintSize[1], false), 70, 70);

        setFocusable(true);

        setOnTouchListener(this);
        countDown();

        initWindow(context);
    }

    @Override
    public void start() {
        start = true;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //canvas.drawColor(Color.BLACK);
        target.draw(canvas);
        pint.draw(canvas);

      

        paint.setColor(Color.BLACK); 
        paint.setTextSize(60);
        canvas.drawText(String.valueOf(timeLeft), width / 2 - paint.measureText(String.valueOf(timeLeft)) / 2, 100, paint);
        
        if (start) {
            update();
            invalidate();
        }
    }

    private void initWindow(Context context) {
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display mDisplay = mWindowManager.getDefaultDisplay();

        width = mDisplay.getWidth();
        height = mDisplay.getHeight();
    }

    public void update() {
        if (target.getSpeed().getxDirection() == PintSpeed.DIRECTION_RIGHT
                && target.getX() + target.getBitmap().getWidth() / 2 >= getWidth()) {
            target.getSpeed().toggleXDirection();
        }
        if (target.getSpeed().getxDirection() == PintSpeed.DIRECTION_LEFT
                && target.getX() - target.getBitmap().getWidth() / 2 <= 0) {
            target.getSpeed().toggleXDirection();
        }
        if (target.getSpeed().getyDirection() == PintSpeed.DIRECTION_DOWN
                && target.getY() + target.getBitmap().getHeight() / 2 >= getHeight()) {
            target.getSpeed().toggleYDirection();
        }
        if (target.getSpeed().getyDirection() == PintSpeed.DIRECTION_UP
                && target.getY() - target.getBitmap().getHeight() / 2 <= 0) {
            target.getSpeed().toggleYDirection();
        }
        target.update();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pint.handleActionDown((int) event.getX(), (int) event.getY());

            if (event.getY() > getHeight() - 50) {
                failure();
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (pint.isTouched()) {
                pint.setX((int) event.getX());
                pint.setY((int) event.getY());

                if (target.checkIfIn(pint.getX(), pint.getY())) {
                    target.setBitmap(Bitmap.createScaledBitmap(green_circle,
                            targetSize[0], targetSize[1], false));

                    if (startCount == false) {
                        startTime = System.currentTimeMillis() / 1000;
                        startCount = true;
                    } else {
                        nowTime = System.currentTimeMillis() / 1000;
                        if (nowTime - startTime >= 3) {
                            success();
                        }
                    }
                } else {
                    target.setBitmap(Bitmap.createScaledBitmap(red_circle, targetSize[0],
                            targetSize[1], false));
                    startCount = false;
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (pint.isTouched()) {
                pint.setTouched(false);
                target.setBitmap(Bitmap.createScaledBitmap(red_circle, targetSize[0], targetSize[1],
                        false));
            }
        }
        return true;
    }
    
    public void countDown() {
		countDown = new CountDownTimer(30000, 500) {

			public void onTick(long millisUntilFinished) {
				timeLeft = (int)millisUntilFinished / 1000;
			}

			public void onFinish() {
	            failure();
			}
		}.start();
	}
}